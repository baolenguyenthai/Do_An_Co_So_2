package view;

import dao.BaiThiDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.GeminiService;

/**
 * Giao diện xem chi tiết kết quả một bài thi.
 */
public class FrmXemChiTietBaiThi extends JFrame {

    private static final float AI_FONT_SIZE_DEFAULT = 16f;
    private static final float AI_FONT_SIZE_MIN = 12f;
    private static final float AI_FONT_SIZE_MAX = 30f;
    private static final float AI_FONT_SIZE_STEP = 2f;
    private static final String EXPLAIN_BUTTON_TEXT = "Giải thích câu chọn (AI)";

    private final BaiThiDAO baiThiDAO = new BaiThiDAO();
    private JLabel lblTitle;
    private JLabel lblAiStatus;
    private JTable tblChiTiet;
    private JButton btnGiaiThich;
    private JButton btnDong;
    private JPanel pnlMain;

    public FrmXemChiTietBaiThi(int baiThiId, String tenBoCauHoi) {
        initComponentsManually();
        setLocationRelativeTo(null);
        lblTitle.setText("CHI TIẾT: " + tenBoCauHoi.toUpperCase());
        taiDuLieu(baiThiId);
    }

    private void initComponentsManually() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Chi tiết bài thi");
        setBackground(Color.WHITE);

        pnlMain = new JPanel(new BorderLayout(18, 18));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblTitle = new JLabel("CHI TIẾT BÀI THI");
        lblTitle.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        tblChiTiet = new JTable();
        tblChiTiet.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        tblChiTiet.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "STT", "Câu hỏi", "Đáp án đã chọn", "Đáp án đúng", "Kết quả" }) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tblChiTiet.setRowHeight(30);
        tblChiTiet.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tblChiTiet);
        pnlMain.add(scrollPane, BorderLayout.CENTER);

        // Nút dưới
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setOpaque(false);

        lblAiStatus = new JLabel();
        lblAiStatus.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        lblAiStatus.setForeground(new Color(0, 102, 204));
        lblAiStatus.setVisible(false);
        pnlBottom.add(lblAiStatus, BorderLayout.WEST);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActions.setOpaque(false);

        btnGiaiThich = new JButton(EXPLAIN_BUTTON_TEXT);
        btnGiaiThich.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        btnGiaiThich.setBackground(new Color(102, 102, 255));
        btnGiaiThich.addActionListener(e -> handleGiaiThichAI());
        pnlActions.add(btnGiaiThich);

        btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        btnDong.addActionListener(e -> this.dispose());
        pnlActions.add(btnDong);

        pnlBottom.add(pnlActions, BorderLayout.EAST);
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);

        add(pnlMain);
        pack();
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void taiDuLieu(int baiThiId) {
        DefaultTableModel model = (DefaultTableModel) tblChiTiet.getModel();
        model.setRowCount(0);

        List<Object[]> rows = baiThiDAO.getChiTietKetQua(baiThiId);
        int stt = 1;
        for (Object[] row : rows) {
            String ketQua = (row[3] != null && (boolean) row[3]) ? "Đúng" : "Sai";
            model.addRow(new Object[] {
                    stt++,
                    row[0], // Câu hỏi
                    row[1] == null ? "(Không chọn)" : row[1], // Đáp án đã chọn
                    row[2], // Đáp án đúng
                    ketQua
            });
        }

        // Custom renderer để tô màu cột kết quả
        tblChiTiet.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                if ("Đúng".equals(value)) {
                    c.setForeground(new Color(0, 153, 51));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(Color.RED);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }
                return c;
            }
        });

        // Chỉnh độ rộng cột
        if (tblChiTiet.getColumnModel().getColumnCount() > 0) {
            tblChiTiet.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblChiTiet.getColumnModel().getColumn(0).setMaxWidth(60);
            tblChiTiet.getColumnModel().getColumn(4).setPreferredWidth(80);
            tblChiTiet.getColumnModel().getColumn(4).setMaxWidth(100);
        }
    }

    private void handleGiaiThichAI() {
        int row = tblChiTiet.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một câu hỏi trong bảng để được giải thích!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cauHoi = tblChiTiet.getValueAt(row, 1).toString();
        String daChon = tblChiTiet.getValueAt(row, 2).toString();
        String daDung = tblChiTiet.getValueAt(row, 3).toString();

        String prompt = String.format(
                "Bạn là giáo viên hướng dẫn.\n" +
                        "Hãy giải thích ngắn gọn, dễ hiểu tại sao đáp án đúng là '%s'.\n" +
                        "Câu hỏi: %s\n" +
                        "Thí sinh đã chọn: %s\n" +
                        "Yêu cầu định dạng trả lời:\n" +
                        "1) Chỉ dùng văn bản thuần tiếng Việt có dấu chuẩn Unicode.\n" +
                        "2) Không dùng markdown hay ký tự đặc biệt như #, *, `, $, [ ].\n" +
                        "3) Trình bày 3 mục rõ ràng: vì sao đáp án đúng, lỗi sai của thí sinh, kiến thức cần nhớ.",
                daDung, cauHoi, daChon);

        setAiLoading(true);
        new Thread(() -> {
            String result = fetchAiExplanation(prompt);
            SwingUtilities.invokeLater(() -> {
                setAiLoading(false);
                showAiExplanationDialog(result, cauHoi, daChon, daDung);
            });
        }, "ai-explain-thread").start();
    }

    private String fetchAiExplanation(String prompt) {
        try {
            return normalizeAiText(GeminiService.callGemini(prompt));
        } catch (Exception ex) {
            return "Lỗi khi gọi AI: " + ex.getMessage();
        }
    }

    private void setAiLoading(boolean loading) {
        btnDong.setEnabled(!loading);
        btnGiaiThich.setEnabled(!loading);
        btnGiaiThich.setText(loading ? "Đang xử lý..." : EXPLAIN_BUTTON_TEXT);
        lblAiStatus.setText(loading ? "⏳ AI đang trả lời, vui lòng chờ..." : "");
        lblAiStatus.setVisible(loading);
    }

    private String normalizeAiText(String rawText) {
        if (rawText == null || rawText.isBlank()) {
            return "AI không trả về nội dung.";
        }

        String text = rawText
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replace("```json", "")
                .replace("```", "")
                .replace("**", "")
                .replace("__", "")
                .replace("`", "")
                .replace("$$", "")
                .replace("$", "")
                .replace("\\times", "x")
                .replace("\\(", "(")
                .replace("\\)", ")");

        text = text.replaceAll("(?m)^\\s*#{1,6}\\s*", "");
        text = text.replaceAll("[ \\t]+\\n", "\n");
        text = text.replaceAll("\\n{3,}", "\n\n");
        text = text.trim();

        return text.isBlank() ? "AI không trả về nội dung." : text;
    }

    private void showAiExplanationDialog(String firstMessage, String cauHoi, String daChon, String daDung) {
        JDialog dialog = new JDialog(this, "Giải thích từ AI", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("Helvetica Neue", Font.PLAIN, (int) AI_FONT_SIZE_DEFAULT));
        appendChatMessage(textArea, "AI", firstMessage);

        StringBuilder conversationHistory = new StringBuilder();
        conversationHistory.append("AI: ").append(firstMessage).append("\n");

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(760, 430));

        JPanel zoomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel lblZoom = new JLabel("Cỡ chữ:");
        JButton btnZoomOut = new JButton("A-");
        JButton btnZoomIn = new JButton("A+");
        JButton btnDefault = new JButton("Mặc định");
        zoomPanel.add(lblZoom);
        zoomPanel.add(btnZoomOut);
        zoomPanel.add(btnZoomIn);
        zoomPanel.add(btnDefault);

        final float[] fontSize = { AI_FONT_SIZE_DEFAULT };
        Runnable applyFont = () -> textArea.setFont(textArea.getFont().deriveFont(fontSize[0]));

        btnZoomOut.addActionListener(e -> {
            fontSize[0] = Math.max(AI_FONT_SIZE_MIN, fontSize[0] - AI_FONT_SIZE_STEP);
            applyFont.run();
        });
        btnZoomIn.addActionListener(e -> {
            fontSize[0] = Math.min(AI_FONT_SIZE_MAX, fontSize[0] + AI_FONT_SIZE_STEP);
            applyFont.run();
        });
        btnDefault.addActionListener(e -> {
            fontSize[0] = AI_FONT_SIZE_DEFAULT;
            applyFont.run();
        });

        JLabel lblChatStatus = new JLabel(" ");
        lblChatStatus.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        lblChatStatus.setForeground(new Color(0, 102, 204));

        JTextField txtChatInput = new JTextField();
        txtChatInput.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        txtChatInput.setToolTipText("Nhập câu hỏi tiếp theo để trò chuyện với AI");

        JButton btnSend = new JButton("Gửi");
        btnSend.setFont(new Font("Helvetica Neue", Font.BOLD, 13));

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dialog.dispose());

        Runnable sendChatAction = () -> {
            String userMessage = txtChatInput.getText().trim();
            if (userMessage.isEmpty()) {
                return;
            }

            appendChatMessage(textArea, "Bạn", userMessage);
            conversationHistory.append("Bạn: ").append(userMessage).append("\n");
            txtChatInput.setText("");

            txtChatInput.setEnabled(false);
            btnSend.setEnabled(false);
            lblChatStatus.setText("⏳ AI đang trả lời...");

            new Thread(() -> {
                String followUpPrompt = buildFollowUpPrompt(cauHoi, daChon, daDung, conversationHistory.toString(),
                        userMessage);
                String aiReply = fetchAiExplanation(followUpPrompt);
                SwingUtilities.invokeLater(() -> {
                    appendChatMessage(textArea, "AI", aiReply);
                    conversationHistory.append("AI: ").append(aiReply).append("\n");
                    lblChatStatus.setText(" ");
                    txtChatInput.setEnabled(true);
                    btnSend.setEnabled(true);
                    txtChatInput.requestFocusInWindow();
                });
            }, "ai-chat-thread").start();
        };

        btnSend.addActionListener(e -> sendChatAction.run());
        txtChatInput.addActionListener(e -> sendChatAction.run());

        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.add(txtChatInput, BorderLayout.CENTER);
        inputPanel.add(btnSend, BorderLayout.EAST);

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        closePanel.add(btnClose);

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.add(lblChatStatus, BorderLayout.NORTH);
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(closePanel, BorderLayout.SOUTH);

        panel.add(zoomPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(680, 420));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String buildFollowUpPrompt(String cauHoi, String daChon, String daDung, String conversationHistory,
            String userMessage) {
        String history = conversationHistory == null ? "" : conversationHistory;
        if (history.length() > 4000) {
            history = history.substring(history.length() - 4000);
        }
        return String.format(
                "Bạn là giáo viên đang tiếp tục giải thích một câu trắc nghiệm.\n" +
                        "Ngữ cảnh cố định:\n" +
                        "- Câu hỏi: %s\n" +
                        "- Thí sinh đã chọn: %s\n" +
                        "- Đáp án đúng: %s\n\n" +
                        "Lịch sử hội thoại gần đây:\n%s\n" +
                        "Yêu cầu:\n" +
                        "1) Chỉ dùng tiếng Việt có dấu chuẩn Unicode.\n" +
                        "2) Trả lời ngắn gọn, dễ hiểu, bám sát câu hỏi.\n" +
                        "3) Không dùng markdown và ký tự đặc biệt.\n\n" +
                        "Câu hỏi tiếp theo của thí sinh: %s",
                cauHoi, daChon, daDung, history, userMessage);
    }

    private void appendChatMessage(JTextArea textArea, String role, String message) {
        String safeMessage = message == null ? "" : message.trim();
        if (!textArea.getText().isBlank()) {
            textArea.append("\n\n");
        }
        textArea.append(role + ":\n" + safeMessage);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
