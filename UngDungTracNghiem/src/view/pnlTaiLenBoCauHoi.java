
package view;

import java.io.*;
import java.util.*;
import javax.swing.*;
import com.opencsv.CSVReader;
import dao.BoCauHoiDAO;
import model.BoCauHoi;
import model.CauHoi;
import model.DapAn;
import dao.CauHoiDAO;
import dao.DapAnDAO;
import dao.CapHocDAO;
import dao.MonHocDAO;
import model.CapHoc;
import utils.QuanLyPhien;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;
//import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import utils.GeminiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author lebao
 */
public class pnlTaiLenBoCauHoi extends javax.swing.JPanel {
    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(pnlTaiLenBoCauHoi.class.getName());
    private static final char[] OPTION_LABELS = { 'A', 'B', 'C', 'D' };
    private int globalQuestionCount = 0;
    private int globalAnswerCount = 0;

    /**
     * Creates new form pnlTaiLenBoCauHoi
     */
    public pnlTaiLenBoCauHoi() {
        initComponents();
        // Populate combo box Cấp học
        CapHocDAO capHocDAO = new CapHocDAO();
        List<CapHoc> capHocList = capHocDAO.getAll();
        cbCapHoc.setModel(new DefaultComboBoxModel<>(capHocList.toArray(new CapHoc[0])));
        configurePreviewArea();
        configureAiInputArea();
        lockTxtDuongDanSize();
    }

    private void configurePreviewArea() {
        txtPreview.setLineWrap(true);
        txtPreview.setWrapStyleWord(true);
        txtPreview.setEditable(false);
    }

    private void configureAiInputArea() {
        txtNoiDungAI.setLineWrap(true);
        txtNoiDungAI.setWrapStyleWord(true);
    }

    private void lockTxtDuongDanSize() {
        SwingUtilities.invokeLater(() -> {
            java.awt.Dimension initialSize = txtDuongDan.getSize();
            if (initialSize.width <= 0 || initialSize.height <= 0) {
                initialSize = txtDuongDan.getPreferredSize();
            }
            java.awt.Dimension fixedSize = new java.awt.Dimension(initialSize);
            txtDuongDan.setMinimumSize(fixedSize);
            txtDuongDan.setPreferredSize(fixedSize);
            txtDuongDan.setMaximumSize(fixedSize);
            txtDuongDan.revalidate();
        });
    }

    private String docVaPreviewFile(File file) throws Exception {
        StringBuilder preview = new StringBuilder();
        String fileName = file.getName().toLowerCase(Locale.ROOT);
        if (fileName.endsWith(".xlsx")) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();
            for (int i = 0; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (Cell cell : row) {
                        preview.append(cell.toString()).append("\t");
                    }
                    preview.append("\n");
                }
            }
            workbook.close();
        } else if (fileName.endsWith(".csv")) {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] line;
            while ((line = reader.readNext()) != null) {
                for (String cell : line) {
                    preview.append(cell).append("\t");
                }
                preview.append("\n");
            }
            reader.close();
        } else if (fileName.endsWith(".docx")) {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(file));
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                preview.append(para.getText()).append("\n");
            }
            doc.close();
        }
        return preview.toString();
    }

    private static final Pattern ANSWER_PATTERN = Pattern
            .compile("(?is)(?:^|\\s)([A-D])\\s*[\\.|\\)|,|_]\\s*(.+?)(?=(?:\\s+[A-D]\\s*[\\.|\\)|,|_]\\s+)|$)");

    private void extractAnswersFromParagraph(XWPFParagraph para, String text, int baseOffset,
            List<String> answers, List<Boolean> isCorrect) {
        List<RunSpan> spans = buildRunSpans(para);
        Matcher m = ANSWER_PATTERN.matcher(text);
        while (m.find()) {
            String answerText = m.group(2).trim();
            int start = baseOffset + m.start(2);
            int end = baseOffset + m.end(2);
            boolean bold = isBoldInRange(spans, start, end);
            answers.add(answerText);
            isCorrect.add(bold);
        }
    }

    private static class RunSpan {
        final int start;
        final int end;
        final boolean bold;

        RunSpan(int start, int end, boolean bold) {
            this.start = start;
            this.end = end;
            this.bold = bold;
        }
    }

    private List<RunSpan> buildRunSpans(XWPFParagraph para) {
        List<RunSpan> spans = new ArrayList<>();
        int cursor = 0;
        for (var run : para.getRuns()) {
            String runText = run.toString();
            if (runText == null)
                runText = "";
            int len = runText.length();
            if (len > 0) {
                spans.add(new RunSpan(cursor, cursor + len, run.isBold()));
                cursor += len;
            }
        }
        return spans;
    }

    private boolean isBoldInRange(List<RunSpan> spans, int start, int end) {
        for (RunSpan s : spans) {
            if (s.end > start && s.start < end && s.bold) {
                return true;
            }
        }
        return false;
    }

    private int indexOfIgnoreCase(String text, String needle) {
        return text.toLowerCase(Locale.ROOT).indexOf(needle.toLowerCase(Locale.ROOT));
    }

    private void taiLenBoCauHoi(File file, int nguoiDungId, String tenMonHoc, int capHocId) throws Exception {
        // Xử lý Môn học: Tìm ID, nếu không có thì thêm mới
        MonHocDAO monHocDAO = new MonHocDAO();
        int monHocId = monHocDAO.getIdByTen(tenMonHoc);
        if (monHocId == -1) {
            monHocId = monHocDAO.themMonHoc(tenMonHoc);
            if (monHocId == -1)
                throw new Exception("Lỗi tạo môn học mới");
        }

        BoCauHoi bo = new BoCauHoi();
        bo.setTenBoCauHoi("Bộ câu hỏi từ " + file.getName());
        bo.setMoTa("Tự động tạo");
        bo.setCapHocId(capHocId);
        bo.setMonHocId(monHocId);
        bo.setNguoiTao(nguoiDungId);
        bo.setTrangThai("CHO_DUYET");
        bo.setCongKhai(false);
        BoCauHoiDAO dao = new BoCauHoiDAO();
        int boId = dao.themBoCauHoi(bo);
        if (boId == -1)
            throw new Exception("Lỗi tạo bộ câu hỏi");
        System.out.println("Đã tạo bộ câu hỏi ID: " + boId);

        if (file.getName().endsWith(".xlsx")) {
            System.out.println("Đang parse Excel...");
        } else if (file.getName().endsWith(".csv")) {
            System.out.println("Đang parse CSV...");
        } else if (file.getName().endsWith(".docx")) {
            System.out.println("Đang parse Word...");
            XWPFDocument doc = new XWPFDocument(new FileInputStream(file));
            List<XWPFParagraph> paragraphs = doc.getParagraphs();

            String currentQuestion = null;
            List<String> answers = new ArrayList<>();
            List<Boolean> isCorrect = new ArrayList<>();
            int questionCount = 0;
            int answerCount = 0;

            for (XWPFParagraph para : paragraphs) {
                String paraText = para.getText();
                if (paraText == null || paraText.trim().isEmpty()) {
                    continue;
                }

                int questionIdx = indexOfIgnoreCase(paraText, "Câu hỏi:");
                boolean isQuestionParagraph = questionIdx >= 0;

                if (isQuestionParagraph) {
                    // Lưu câu hỏi trước nếu có
                    if (currentQuestion != null) {
                        try {
                            saveQuestionAndAnswers(dao, boId, currentQuestion, answers, isCorrect);
                            questionCount++;
                            answerCount += answers.size();

                            System.out.println("---- CÂU HỎI ĐÃ LƯU ----");
                            System.out.println("Câu hỏi: " + currentQuestion);
                            for (int i = 0; i < answers.size(); i++) {
                                System.out.println("  " + (i + 1) + ". " + answers.get(i)
                                        + (isCorrect.get(i) ? " [ĐÚNG]" : " [SAI]"));
                            }
                            System.out.println("------------------------");

                        } catch (Exception e) {
                            System.err.println("Lỗi lưu câu hỏi: " + e.getMessage());
                            e.printStackTrace();
                        }
                        answers.clear();
                        isCorrect.clear();
                    }

                    int contentStart = questionIdx + "Câu hỏi:".length();
                    String afterQuestion = paraText.substring(contentStart).trim();

                    Matcher ansMatcher = ANSWER_PATTERN.matcher(afterQuestion);
                    if (ansMatcher.find()) {
                        currentQuestion = afterQuestion.substring(0, ansMatcher.start()).trim();
                    } else {
                        currentQuestion = afterQuestion.trim();
                    }

                    extractAnswersFromParagraph(para, afterQuestion, contentStart, answers, isCorrect);
                } else if (currentQuestion != null) {
                    extractAnswersFromParagraph(para, paraText, 0, answers, isCorrect);
                }
            }

            // Lưu câu hỏi cuối
            if (currentQuestion != null) {
                try {
                    saveQuestionAndAnswers(dao, boId, currentQuestion, answers, isCorrect);
                    questionCount++;
                    answerCount += answers.size();

                    System.out.println("---- CÂU HỎI ĐÃ LƯU ----");
                    System.out.println("Câu hỏi: " + currentQuestion);
                    for (int i = 0; i < answers.size(); i++) {
                        System.out.println("  " + (i + 1) + ". " + answers.get(i)
                                + (isCorrect.get(i) ? " [ĐÚNG]" : " [SAI]"));
                    }
                    System.out.println("------------------------");

                } catch (Exception e) {
                    System.err.println("Lỗi lưu câu hỏi cuối: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            doc.close();
            System.out
                    .println("Hoàn thành parse Word. Tổng câu hỏi: " + questionCount + ", Tổng đáp án: " + answerCount);

            globalQuestionCount = questionCount;
            globalAnswerCount = answerCount;
        }
    }

    private void saveQuestionAndAnswers(BoCauHoiDAO dao, int boId, String question, List<String> answers,
            List<Boolean> isCorrect) throws Exception {
        CauHoiDAO cauHoiDAO = new CauHoiDAO();
        DapAnDAO dapAnDAO = new DapAnDAO();

        CauHoi cauHoi = new CauHoi();
        cauHoi.setBoCauHoiId(boId);
        cauHoi.setNoiDung(question);
        cauHoi.setMucDo("TB");
        int cauHoiId = cauHoiDAO.themCauHoi(cauHoi);
        if (cauHoiId == -1)
            throw new Exception("Lỗi tạo câu hỏi");

        for (int i = 0; i < answers.size(); i++) {
            DapAn dapAn = new DapAn();
            dapAn.setCauHoiId(cauHoiId);
            dapAn.setNoiDung(answers.get(i));
            dapAn.setDung(isCorrect.get(i) ? 1 : 0);
            dapAnDAO.themDapAn(dapAn);
        }
    }

    // Overload cho Excel/CSV (dùng String correct thay vì List<Boolean>)
    private void saveQuestionAndAnswers(BoCauHoiDAO dao, int boId, String question, List<String> answers,
            String correct) throws Exception {
        List<Boolean> isCorrect = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            isCorrect.add(("ABCD".charAt(i) + "").equals(correct));
        }
        saveQuestionAndAnswers(dao, boId, question, answers, isCorrect);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblThongBao = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbCapHoc = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtMonHoc = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        spnSoLuongCauHoi = new javax.swing.JSpinner();
        btnTaoDeAI = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNoiDungAI = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        btnChonFile = new javax.swing.JButton();
        txtDuongDan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnTaiLen = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPreview = new javax.swing.JTextArea();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel5.setText("TẢI LÊN / TẠO BỘ CÂU HỎI");

        lblThongBao.setFont(new java.awt.Font("Helvetica Neue", 0, 16)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        jLabel3.setText("Chọn cấp học");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel4.setText("Nhập tên môn học");

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        jLabel6.setText("Số lượng câu hỏi");

        spnSoLuongCauHoi.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        spnSoLuongCauHoi.setModel(new javax.swing.SpinnerNumberModel(5, 1, 100, 1));

        btnTaoDeAI.setFont(new java.awt.Font("Helvetica Neue", 1, 22)); // NOI18N
        btnTaoDeAI.setText("Tạo đề bằng AI");
        btnTaoDeAI.addActionListener(this::btnTaoDeAIActionPerformed);

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        jLabel7.setText("Nội dung cho AI tạo câu hỏi");

        txtNoiDungAI.setColumns(20);
        txtNoiDungAI.setRows(4);
        jScrollPane2.setViewportView(txtNoiDungAI);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel1.setText("Chọn file tải lên (Excel, CSV hoặc Word):");

        btnChonFile.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnChonFile.setText("Chọn File");
        btnChonFile.addActionListener(this::btnChonFileActionPerformed);

        txtPreview.setColumns(20);
        txtPreview.setRows(5);
        jScrollPane1.setViewportView(txtPreview);

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel2.setText("Đường dẫn:");

        btnTaiLen.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        btnTaiLen.setText("Tải lên");
        btnTaiLen.addActionListener(this::btnTaiLenActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setAutoCreateGaps(true);
        jPanel1Layout.setAutoCreateContainerGaps(true);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(lblThongBao, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbCapHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 240,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 230,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(spnSoLuongCauHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTaoDeAI))
                        .addComponent(jLabel7)
                        .addComponent(jScrollPane2)
                        .addComponent(jLabel1)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDuongDan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnChonFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTaiLen))
                        .addComponent(jScrollPane1));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblThongBao, javax.swing.GroupLayout.PREFERRED_SIZE, 30,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(cbCapHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(txtMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)
                                        .addComponent(spnSoLuongCauHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnTaoDeAI, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel1)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(8, 8, 8)
                                                .addComponent(jLabel2))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnTaiLen, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDuongDan,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnChonFile,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(12, 12, 12)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void btnChonFileActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChonFileActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter("Excel, CSV or Word", "xlsx", "csv", "docx"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtDuongDan.setText(selectedFile.getAbsolutePath());
            try {
                String preview = docVaPreviewFile(selectedFile);
                txtPreview.setText(preview);
                txtPreview.setCaretPosition(0);
                // Thay thế lblThongBao bằng dialog thành công
                JOptionPane.showMessageDialog(this, "Đã tải preview.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                // Thay thế lblThongBao bằng dialog lỗi
                JOptionPane.showMessageDialog(this, "Lỗi đọc file: " + e.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }// GEN-LAST:event_btnChonFileActionPerformed

    private void btnTaiLenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTaiLenActionPerformed
        String duongDan = txtDuongDan.getText();
        if (duongDan.isEmpty()) {
            // Thay thế lblThongBao bằng dialog lỗi
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CapHoc selectedCapHoc = (CapHoc) cbCapHoc.getSelectedItem();
        String tenMonHoc = txtMonHoc.getText().trim();
        if (selectedCapHoc == null || tenMonHoc.isEmpty()) {
            // Thay thế lblThongBao bằng dialog lỗi
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Cấp học và nhập Môn học!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra xem môn học đã tồn tại chưa
        MonHocDAO monHocDAO = new MonHocDAO();
        if (monHocDAO.getIdByTen(tenMonHoc) != -1) {
            JOptionPane.showMessageDialog(this, "Môn học này đã tồn tại! Vui lòng chọn môn học khác hoặc đổi tên.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return; // Dừng việc tải lên
        }

        File file = new File(duongDan);

        if (!QuanLyPhien.daDangNhap()) {
            // Thay thế lblThongBao bằng dialog lỗi
            JOptionPane.showMessageDialog(this, "Bạn chưa đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int nguoiDungId = QuanLyPhien.layIdNguoiDungHienTai();

        // Chạy tải lên trong background
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                taiLenBoCauHoi(file, nguoiDungId, tenMonHoc, selectedCapHoc.getCapHocId());
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Kiểm tra exception
                    // Thay thế lblThongBao bằng dialog thành công
                    JOptionPane.showMessageDialog(
                            pnlTaiLenBoCauHoi.this, "Tải lên thành công! Đã lưu " + globalQuestionCount + " câu hỏi và "
                                    + globalAnswerCount + " đáp án. Đã gửi admin duyệt.",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    // Thay thế lblThongBao bằng dialog lỗi
                    JOptionPane.showMessageDialog(pnlTaiLenBoCauHoi.this, "Lỗi tải lên: " + e.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute(); // Bắt đầu background task
    }// GEN-LAST:event_btnTaiLenActionPerformed

    private void btnTaoDeAIActionPerformed(java.awt.event.ActionEvent evt) {
        CapHoc selectedCapHoc = (CapHoc) cbCapHoc.getSelectedItem();
        String tenMonHoc = txtMonHoc.getText().trim();
        String noiDungAI = txtNoiDungAI.getText().trim();
        int soLuongCauHoi = ((Number) spnSoLuongCauHoi.getValue()).intValue();

        if (selectedCapHoc == null || tenMonHoc.isEmpty() || noiDungAI.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Cấp học, nhập Môn học, Nội dung AI trước!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (soLuongCauHoi <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng câu hỏi phải lớn hơn 0.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!QuanLyPhien.daDangNhap()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String prompt = String.format(
                "Hãy tạo %d câu hỏi trắc nghiệm dựa trên nội dung: '%s'. Cho trình độ: '%s'.\n" +
                        "Toàn bộ nội dung câu hỏi và đáp án phải là tiếng Việt có dấu chuẩn Unicode.\n" +
                        "Mỗi câu phải có đúng 4 đáp án A, B, C, D (nội dung khác nhau).\n" +
                        "Trường correct chỉ nhận 1 ký tự: A hoặc B hoặc C hoặc D.\n" +
                        "Yêu cầu trả về DUY NHẤT định dạng JSON như sau (không có text nào khác):\n" +
                        "[\n" +
                        "  {\n" +
                        "    \"question\": \"Nội dung câu hỏi\",\n" +
                        "    \"answers\": {\n" +
                        "      \"A\": \"...\",\n" +
                        "      \"B\": \"...\",\n" +
                        "      \"C\": \"...\",\n" +
                        "      \"D\": \"...\"\n" +
                        "    },\n" +
                        "    \"correct\": \"A\"\n" +
                        "  }\n" +
                        "]",
                soLuongCauHoi, noiDungAI, selectedCapHoc.getTenCapHoc());

        new Thread(() -> {
            SwingUtilities.invokeLater(() -> btnTaoDeAI.setEnabled(false));
            String response = GeminiService.callGemini(prompt);
            try {
                String jsonResult = extractJsonArrayFromResponse(response);
                if (!jsonResult.startsWith("[")) {
                    throw new Exception("AI không trả về đúng JSON: \n" + jsonResult);
                }
                JsonArray jsonArr = JsonParser.parseString(jsonResult).getAsJsonArray();
                if (jsonArr.isEmpty()) {
                    throw new Exception("AI không tạo được câu hỏi nào.");
                }

                int nguoiDungId = QuanLyPhien.layIdNguoiDungHienTai();
                MonHocDAO monHocDAO = new MonHocDAO();
                int monHocId = monHocDAO.getIdByTen(tenMonHoc);
                if (monHocId == -1) {
                    monHocId = monHocDAO.themMonHoc(tenMonHoc);
                }

                BoCauHoiDAO boDao = new BoCauHoiDAO();
                BoCauHoi bo = new BoCauHoi();
                String tenBo = noiDungAI.length() > 40 ? noiDungAI.substring(0, 40) + "..." : noiDungAI;
                bo.setTenBoCauHoi("AI Tạo: " + tenBo);
                bo.setMoTa("Bộ câu hỏi tạo tự động bởi AI");
                bo.setCapHocId(selectedCapHoc.getCapHocId());
                bo.setMonHocId(monHocId);
                bo.setNguoiTao(nguoiDungId);
                bo.setTrangThai("CHO_DUYET");
                bo.setCongKhai(false);
                int boId = boDao.themBoCauHoi(bo);
                if (boId == -1) {
                    throw new Exception("Không thể tạo bộ câu hỏi mới.");
                }

                int savedCount = 0;
                StringBuilder previewBuilder = new StringBuilder();
                for (JsonElement el : jsonArr) {
                    if (!el.isJsonObject()) {
                        continue;
                    }

                    JsonObject obj = el.getAsJsonObject();
                    String qText = obj.has("question") ? obj.get("question").getAsString().trim() : "";
                    if (qText.isEmpty()) {
                        throw new Exception("Thiếu nội dung câu hỏi tại vị trí " + (savedCount + 1) + ".");
                    }

                    List<String> answers = extractAiAnswers(obj);
                    String correct = obj.has("correct") ? obj.get("correct").getAsString().trim().toUpperCase(Locale.ROOT)
                            : "";
                    if (!correct.matches("^[ABCD]$")) {
                        throw new Exception("Đáp án đúng của câu " + (savedCount + 1) + " phải là A/B/C/D.");
                    }

                    saveQuestionAndAnswers(boDao, boId, qText, answers, correct);
                    savedCount++;

                    previewBuilder.append("Câu ").append(savedCount).append(": ").append(qText).append("\n");
                    for (int i = 0; i < OPTION_LABELS.length; i++) {
                        previewBuilder.append(OPTION_LABELS[i]).append(". ").append(answers.get(i)).append("\n");
                    }
                    previewBuilder.append("Đáp án đúng: ").append(correct).append("\n\n");
                }

                if (savedCount == 0) {
                    throw new Exception("AI trả về dữ liệu không hợp lệ.");
                }

                String previewText = "=== PREVIEW BỘ CÂU HỎI AI ===\n"
                        + "Cấp học: " + selectedCapHoc.getTenCapHoc() + "\n"
                        + "Môn học: " + tenMonHoc + "\n"
                        + "Số câu đã tạo: " + savedCount + "\n"
                        + "Nội dung yêu cầu: " + noiDungAI + "\n\n"
                        + previewBuilder;
                final int finalSavedCount = savedCount;

                SwingUtilities.invokeLater(() -> {
                    btnTaoDeAI.setEnabled(true);
                    txtPreview.setText(previewText);
                    txtPreview.setCaretPosition(0);
                    JOptionPane.showMessageDialog(this, "Đã tạo thành công " + finalSavedCount
                            + " câu hỏi bằng AI và hiển thị ở khung Preview.", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    btnTaoDeAI.setEnabled(true);
                    JOptionPane.showMessageDialog(this, "Lỗi khi xử lý dữ liệu AI: " + e.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private String extractJsonArrayFromResponse(String response) {
        String tempResponse = response == null ? "" : response.trim();
        int start = tempResponse.indexOf("[");
        int end = tempResponse.lastIndexOf("]");
        if (start != -1 && end != -1 && end > start) {
            return tempResponse.substring(start, end + 1);
        }
        return tempResponse;
    }

    private List<String> extractAiAnswers(JsonObject obj) throws Exception {
        List<String> answers = new ArrayList<>(OPTION_LABELS.length);

        if (!obj.has("answers")) {
            throw new Exception("Thiếu trường answers.");
        }

        JsonElement answersElement = obj.get("answers");
        if (answersElement.isJsonObject()) {
            JsonObject ansObj = answersElement.getAsJsonObject();
            for (char label : OPTION_LABELS) {
                String key = String.valueOf(label);
                if (!ansObj.has(key)) {
                    throw new Exception("Thiếu đáp án " + key + ".");
                }
                answers.add(normalizeAnswerOption(ansObj.get(key).getAsString()));
            }
        } else if (answersElement.isJsonArray()) {
            JsonArray ansArr = answersElement.getAsJsonArray();
            if (ansArr.size() != OPTION_LABELS.length) {
                throw new Exception("Mỗi câu hỏi phải có đúng 4 đáp án.");
            }
            for (JsonElement ans : ansArr) {
                answers.add(normalizeAnswerOption(ans.getAsString()));
            }
        } else {
            throw new Exception("Trường answers phải là object hoặc mảng.");
        }

        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).isBlank()) {
                throw new Exception("Nội dung đáp án " + OPTION_LABELS[i] + " đang trống.");
            }
        }
        return answers;
    }

    private String normalizeAnswerOption(String raw) {
        String normalized = raw == null ? "" : raw.trim();
        normalized = normalized.replaceAll("^[A-Da-d]\\s*[\\.|\\)|:|-]\\s*", "");
        normalized = normalized.replaceAll("\\s+", " ").trim();
        return normalized;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChonFile;
    private javax.swing.JButton btnTaiLen;
    private javax.swing.JButton btnTaoDeAI;
    private javax.swing.JComboBox<CapHoc> cbCapHoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblThongBao;
    private javax.swing.JSpinner spnSoLuongCauHoi;
    private javax.swing.JTextField txtDuongDan;
    private javax.swing.JTextField txtMonHoc;
    private javax.swing.JTextArea txtNoiDungAI;
    private javax.swing.JTextArea txtPreview;
    // End of variables declaration//GEN-END:variables
}
