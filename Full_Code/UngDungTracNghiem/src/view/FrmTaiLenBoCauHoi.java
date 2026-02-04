
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


/**
 *
 * @author lebao
 */
public class FrmTaiLenBoCauHoi extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmTaiLenBoCauHoi.class.getName());
    private int globalQuestionCount = 0;
    private int globalAnswerCount = 0;
    /**
     * Creates new form FrmTaiBoCauHoi
     */
    public FrmTaiLenBoCauHoi() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Tải Bộ Câu Hỏi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Populate combo box Cấp học
        CapHocDAO capHocDAO = new CapHocDAO();
        List<CapHoc> capHocList = capHocDAO.getAll();
        cbCapHoc.setModel(new DefaultComboBoxModel<>(capHocList.toArray(new CapHoc[0])));
        
       
    }
    
    private String docVaPreviewFile(File file) throws Exception {
        StringBuilder preview = new StringBuilder();
        if (file.getName().endsWith(".xlsx")) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = Math.min(sheet.getLastRowNum(), 5);
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
        } else if (file.getName().endsWith(".csv")) {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] line;
            int count = 0;
            while ((line = reader.readNext()) != null && count < 5) {
                for (String cell : line) {
                    preview.append(cell).append("\t");
                }
                preview.append("\n");
                count++;
            }
            reader.close();
        } else if (file.getName().endsWith(".docx")) {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(file));
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            int count = 0;
            for (XWPFParagraph para : paragraphs) {
                if (count >= 5) break;
                preview.append(para.getText()).append("\n");
                count++;
            }
            doc.close();
        }
        return preview.toString();
    }
    
        private static final Pattern ANSWER_PATTERN =
        Pattern.compile("(?is)(?:^|\\s)([A-D])\\s*[\\.|\\)|,|_]\\s*(.+?)(?=(?:\\s+[A-D]\\s*[\\.|\\)|,|_]\\s+)|$)");

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
            if (runText == null) runText = "";
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
            if (monHocId == -1) throw new Exception("Lỗi tạo môn học mới");
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
        if (boId == -1) throw new Exception("Lỗi tạo bộ câu hỏi");
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
        System.out.println("Hoàn thành parse Word. Tổng câu hỏi: " + questionCount + ", Tổng đáp án: " + answerCount);

        globalQuestionCount = questionCount;
        globalAnswerCount = answerCount;
    }
    }  


    private void saveQuestionAndAnswers(BoCauHoiDAO dao, int boId, String question, List<String> answers, List<Boolean> isCorrect) throws Exception {
        CauHoiDAO cauHoiDAO = new CauHoiDAO();
        DapAnDAO dapAnDAO = new DapAnDAO();

        CauHoi cauHoi = new CauHoi();
        cauHoi.setBoCauHoiId(boId);
        cauHoi.setNoiDung(question);
        cauHoi.setMucDo("TB");
        int cauHoiId = cauHoiDAO.themCauHoi(cauHoi);
        if (cauHoiId == -1) throw new Exception("Lỗi tạo câu hỏi");

        for (int i = 0; i < answers.size(); i++) {
            DapAn dapAn = new DapAn();
            dapAn.setCauHoiId(cauHoiId);
            dapAn.setNoiDung(answers.get(i));
            dapAn.setDung(isCorrect.get(i) ? 1 : 0);
            dapAnDAO.themDapAn(dapAn);
        }
    }

    // Overload cho Excel/CSV (dùng String correct thay vì List<Boolean>)
    private void saveQuestionAndAnswers(BoCauHoiDAO dao, int boId, String question, List<String> answers, String correct) throws Exception {
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        btnTaiLen = new javax.swing.JButton();
        lblThongBao = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtMonHoc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbCapHoc = new javax.swing.JComboBox<>();
        btnChonFile = new javax.swing.JButton();
        txtDuongDan = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPreview = new javax.swing.JTextArea();
        btnThoat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel2.setText("Đường dẫn:");

        btnTaiLen.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnTaiLen.setText("Tải lên");
        btnTaiLen.addActionListener(this::btnTaiLenActionPerformed);

        lblThongBao.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel3.setText("Chọn cấp học");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel4.setText("Nhập tên môn học");

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel1.setText("Chọn file tải lên (Excel, CSV hoặc Word):");

        btnChonFile.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnChonFile.setText("Chọn File");
        btnChonFile.addActionListener(this::btnChonFileActionPerformed);

        txtPreview.setColumns(20);
        txtPreview.setRows(5);
        jScrollPane1.setViewportView(txtPreview);

        btnThoat.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnThoat.setText("Về trang chủ");
        btnThoat.addActionListener(this::btnThoatActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblThongBao, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(186, 186, 186))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDuongDan, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnChonFile)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnTaiLen))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 835, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnThoat)))
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(26, 26, 26)
                        .addComponent(cbCapHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(106, 106, 106))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblThongBao, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cbCapHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDuongDan, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(btnChonFile)
                        .addComponent(btnTaiLen)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaiLenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaiLenActionPerformed
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Cấp học và nhập Môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
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
                    JOptionPane.showMessageDialog(FrmTaiLenBoCauHoi.this, "Tải lên thành công! Đã lưu " + globalQuestionCount + " câu hỏi và " + globalAnswerCount + " đáp án. Đã gửi admin duyệt.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    // Thay thế lblThongBao bằng dialog lỗi
                    JOptionPane.showMessageDialog(FrmTaiLenBoCauHoi.this, "Lỗi tải lên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute(); // Bắt đầu background task
    }//GEN-LAST:event_btnTaiLenActionPerformed

    private void btnChonFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonFileActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel, CSV or Word", "xlsx", "csv", "docx"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtDuongDan.setText(selectedFile.getAbsolutePath());
            try {
                String preview = docVaPreviewFile(selectedFile);
                txtPreview.setText(preview);
                // Thay thế lblThongBao bằng dialog thành công
                JOptionPane.showMessageDialog(this, "Đã tải preview.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                // Thay thế lblThongBao bằng dialog lỗi
                JOptionPane.showMessageDialog(this, "Lỗi đọc file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } 
    }//GEN-LAST:event_btnChonFileActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        new UserForm().setVisible(true);
        setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnThoatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmTaiLenBoCauHoi().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChonFile;
    private javax.swing.JButton btnTaiLen;
    private javax.swing.JButton btnThoat;
    private javax.swing.JComboBox<CapHoc> cbCapHoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblThongBao;
    private javax.swing.JTextField txtDuongDan;
    private javax.swing.JTextField txtMonHoc;
    private javax.swing.JTextArea txtPreview;
    // End of variables declaration//GEN-END:variables
}
