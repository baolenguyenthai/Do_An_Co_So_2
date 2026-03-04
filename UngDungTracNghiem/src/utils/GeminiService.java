package utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiService {

    // Có thể override bằng biến môi trường GEMINI_API_KEY hoặc JVM arg -Dgemini.api.key=...
    private static final String HARDCODED_API_KEY = "AIzaSyAQdXJdYpui0xdbpkmu_W23eqWlnCf8NNI";
    private static final String API_BASE = "https://generativelanguage.googleapis.com/v1beta/models/";
    private static final String DEFAULT_MODEL = "gemini-flash-latest";
    private static final String[] FALLBACK_MODELS = {
            "gemini-2.5-flash",
            "gemini-2.0-flash"
    };
    private static final String VIETNAMESE_GUARDRAIL = """
            YÊU CẦU BẮT BUỘC VỀ NGÔN NGỮ:
            - Trả lời hoàn toàn bằng tiếng Việt có dấu chuẩn Unicode (UTF-8).
            - Không dùng tiếng Việt không dấu.
            - Nếu đề bài yêu cầu định dạng cụ thể (ví dụ JSON), phải giữ đúng định dạng đó.
            """;
    private static final int CONNECT_TIMEOUT_MS = 20_000;
    private static final int READ_TIMEOUT_MS = 60_000;
    private static final int MAX_RATE_LIMIT_RETRIES = 2;
    private static final int MAX_HIGH_DEMAND_RETRIES = 3;
    private static final long DEFAULT_RETRY_DELAY_MS = 3_000L;
    private static final long MAX_RETRY_DELAY_MS = 15_000L;
    private static final long BASE_HIGH_DEMAND_DELAY_MS = 2_000L;
    private static final Pattern RETRY_IN_SECONDS_PATTERN = Pattern.compile(
            "(?i)retry\\s+in\\s+([0-9]+(?:\\.[0-9]+)?)s");
    private static final Gson GSON = new Gson();

    public static String callGemini(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return "Lỗi: Prompt rỗng.";
        }

        String apiKey = resolveApiKey();
        if (apiKey.isEmpty()) {
            return "Thiếu API key Gemini. Đặt GEMINI_API_KEY hoặc -Dgemini.api.key.";
        }

        String finalPrompt = VIETNAMESE_GUARDRAIL + "\n" + prompt.trim();

        String lastError = "AI không trả về dữ liệu.";
        for (String model : buildModelCandidates()) {
            ApiResult result = callGeminiWithModel(finalPrompt, apiKey, model);
            if (result.successText != null) {
                return result.successText;
            }

            lastError = result.errorMessage;
            if (!result.shouldRetryWithFallbackModel) {
                break;
            }
        }
        return lastError;
    }

    private static ApiResult callGeminiWithModel(String prompt, String apiKey, String model) {
        int maxRetryAttempts = Math.max(MAX_RATE_LIMIT_RETRIES, MAX_HIGH_DEMAND_RETRIES);
        for (int attempt = 0; attempt <= maxRetryAttempts; attempt++) {
            try {
                String apiUrl = API_BASE + model + ":generateContent?key=" + apiKey;
                URL url = new URI(apiUrl).toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
                conn.setReadTimeout(READ_TIMEOUT_MS);
                conn.setDoOutput(true);

                String jsonInputString = GSON.toJson(buildRequestBody(prompt));
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                String responseBody = readResponseBody(code == 200 ? conn.getInputStream() : conn.getErrorStream());
                if (code == 200) {
                    String text = extractTextFromSuccessResponse(responseBody);
                    if (text != null && !text.isBlank()) {
                        return ApiResult.success(text.trim());
                    }
                    return ApiResult.failure("AI không trả về nội dung hợp lệ.", false);
                }

                String apiError = extractApiErrorMessage(responseBody);
                if (code == 429 && attempt < MAX_RATE_LIMIT_RETRIES) {
                    sleepBeforeRetry(resolveRetryDelayMillis(apiError));
                    continue;
                }
                if (code == 503 && attempt < MAX_HIGH_DEMAND_RETRIES) {
                    sleepBeforeRetry(resolveHighDemandRetryDelayMillis(apiError, attempt));
                    continue;
                }

                boolean modelNotFound = code == 404 && apiError.toLowerCase(Locale.ROOT).contains("not found");
                if (code == 429) {
                    return ApiResult.failure(buildRateLimitMessage(apiError), false);
                }
                if (code == 503) {
                    return ApiResult.failure(buildHighDemandMessage(apiError), true);
                }

                String message = "Lỗi API (" + code + ")"
                        + (apiError.isBlank() ? "." : ": " + apiError);
                return ApiResult.failure(message, modelNotFound);
            } catch (Exception e) {
                return ApiResult.failure("Lỗi kết nối AI: " + e.getMessage(), false);
            }
        }
        return ApiResult.failure("AI tạm thời quá tải. Vui lòng thử lại sau ít phút.", false);
    }

    private static JsonObject buildRequestBody(String prompt) {
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);

        JsonArray partsArray = new JsonArray();
        partsArray.add(part);

        JsonObject content = new JsonObject();
        content.addProperty("role", "user");
        content.add("parts", partsArray);

        JsonArray contentsArray = new JsonArray();
        contentsArray.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contentsArray);
        return requestBody;
    }

    private static String readResponseBody(InputStream stream) throws Exception {
        if (stream == null) {
            return "";
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    private static String extractTextFromSuccessResponse(String body) {
        JsonObject jsonResponse = GSON.fromJson(body, JsonObject.class);
        if (jsonResponse == null || !jsonResponse.has("candidates")) {
            return "";
        }

        JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
        if (candidates == null || candidates.isEmpty()) {
            return "";
        }

        JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
        if (firstCandidate == null || !firstCandidate.has("content")) {
            return "";
        }

        JsonObject content = firstCandidate.getAsJsonObject("content");
        if (content == null || !content.has("parts")) {
            return "";
        }

        JsonArray parts = content.getAsJsonArray("parts");
        if (parts == null || parts.isEmpty()) {
            return "";
        }

        StringBuilder text = new StringBuilder();
        for (JsonElement element : parts) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject part = element.getAsJsonObject();
            if (part.has("text")) {
                text.append(part.get("text").getAsString());
            }
        }
        return text.toString();
    }

    private static String extractApiErrorMessage(String body) {
        if (body == null || body.isBlank()) {
            return "";
        }
        try {
            JsonObject jsonError = GSON.fromJson(body, JsonObject.class);
            if (jsonError != null && jsonError.has("error")) {
                JsonObject error = jsonError.getAsJsonObject("error");
                if (error != null && error.has("message")) {
                    return error.get("message").getAsString();
                }
            }
        } catch (Exception ignored) {
            // Fallback chuỗi thô nếu không parse được JSON
        }
        return body;
    }

    private static Long parseRetryDelayMillis(String apiError) {
        if (apiError == null || apiError.isBlank()) {
            return null;
        }

        Matcher matcher = RETRY_IN_SECONDS_PATTERN.matcher(apiError);
        if (!matcher.find()) {
            return null;
        }

        try {
            double seconds = Double.parseDouble(matcher.group(1));
            long millis = (long) Math.ceil(seconds * 1000);
            if (millis <= 0) {
                return null;
            }
            return millis;
        } catch (Exception ignored) {
            return null;
        }
    }

    private static long resolveRetryDelayMillis(String apiError) {
        Long parsedDelay = parseRetryDelayMillis(apiError);
        if (parsedDelay == null) {
            return DEFAULT_RETRY_DELAY_MS;
        }
        return Math.min(parsedDelay, MAX_RETRY_DELAY_MS);
    }

    private static long resolveHighDemandRetryDelayMillis(String apiError, int attempt) {
        Long parsedDelay = parseRetryDelayMillis(apiError);
        if (parsedDelay != null) {
            return Math.min(parsedDelay, MAX_RETRY_DELAY_MS);
        }
        long backoffDelay = BASE_HIGH_DEMAND_DELAY_MS * (1L << Math.max(0, attempt));
        return Math.min(backoffDelay, MAX_RETRY_DELAY_MS);
    }

    private static void sleepBeforeRetry(long requestedDelayMs) {
        long delayMs = requestedDelayMs <= 0 ? DEFAULT_RETRY_DELAY_MS : requestedDelayMs;
        delayMs = Math.min(delayMs, MAX_RETRY_DELAY_MS);
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static String buildRateLimitMessage(String apiError) {
        long seconds = (long) Math.ceil(resolveRetryDelayMillis(apiError) / 1000.0);
        return "AI đang đạt giới hạn lượt gọi. Hệ thống đã tự thử lại nhưng chưa thành công. "
                + "Vui lòng thử lại sau khoảng " + Math.max(1L, seconds) + " giây.";
    }

    private static String buildHighDemandMessage(String apiError) {
        Long parsedDelay = parseRetryDelayMillis(apiError);
        if (parsedDelay != null) {
            long seconds = (long) Math.ceil(parsedDelay / 1000.0);
            return "AI đang quá tải tạm thời. Hệ thống đã tự thử lại nhưng chưa thành công. "
                    + "Vui lòng thử lại sau khoảng " + Math.max(1L, seconds) + " giây.";
        }
        return "AI đang quá tải tạm thời. Hệ thống đã tự thử lại nhưng chưa thành công. "
                + "Vui lòng thử lại sau ít phút.";
    }

    private static Set<String> buildModelCandidates() {
        Set<String> models = new LinkedHashSet<>();

        String modelFromEnv = System.getenv("GEMINI_MODEL");
        if (modelFromEnv != null && !modelFromEnv.trim().isEmpty()) {
            models.add(modelFromEnv.trim());
        }

        String modelFromProp = System.getProperty("gemini.model");
        if (modelFromProp != null && !modelFromProp.trim().isEmpty()) {
            models.add(modelFromProp.trim());
        }

        models.add(DEFAULT_MODEL);
        for (String fallback : FALLBACK_MODELS) {
            models.add(fallback);
        }
        return models;
    }

    private static String resolveApiKey() {
        String keyFromEnv = System.getenv("GEMINI_API_KEY");
        if (keyFromEnv != null && !keyFromEnv.trim().isEmpty()) {
            return keyFromEnv.trim();
        }

        String keyFromProp = System.getProperty("gemini.api.key");
        if (keyFromProp != null && !keyFromProp.trim().isEmpty()) {
            return keyFromProp.trim();
        }

        return HARDCODED_API_KEY == null ? "" : HARDCODED_API_KEY.trim();
    }

    private static final class ApiResult {

        private final String successText;
        private final String errorMessage;
        private final boolean shouldRetryWithFallbackModel;

        private ApiResult(String successText, String errorMessage, boolean shouldRetryWithFallbackModel) {
            this.successText = successText;
            this.errorMessage = errorMessage;
            this.shouldRetryWithFallbackModel = shouldRetryWithFallbackModel;
        }

        private static ApiResult success(String text) {
            return new ApiResult(text, "", false);
        }

        private static ApiResult failure(String errorMessage, boolean shouldRetryWithFallbackModel) {
            return new ApiResult(null, errorMessage, shouldRetryWithFallbackModel);
        }
    }
}
