
package view;
import dao.ThongKeDAO;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;  // Thêm import
import java.util.stream.Collectors;

/**
 *
 * @author lebao
 */
public class FrmThongKeBaoCao extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmThongKeBaoCao.class.getName());
    private ThongKeDAO dao = new ThongKeDAO();
    private List<Object[]> listTopNguoiDung;  // Lưu danh sách gốc để lọc
    /**
     * Creates new form FrmThongKeBaoCao
     */
    public FrmThongKeBaoCao() {
        initComponents();
        setLocationRelativeTo(null);
        loadThongKe();
        tblTongBaiThiMonHoc.setDefaultEditor(Object.class, null); // ko cho chỉnh sửa ô trong bảng trực tiếp
        tblDiemMonHoc.setDefaultEditor(Object.class, null); // ko cho chỉnh sửa ô trong bảng trực tiếp
        tblDiemCapHoc.setDefaultEditor(Object.class, null); // ko cho chỉnh sửa ô trong bảng trực tiếp
        tblTopNguoiDung.setDefaultEditor(Object.class, null); // ko cho chỉnh sửa ô trong bảng trực tiếp
        listTopNguoiDung = dao.getTopNguoiDungTheoMonHoc();  // Lưu danh sách gốc
        updateTableTopNguoiDung(listTopNguoiDung);  // Hiển thị toàn bộ
    }

        // Phương thức cập nhật bảng từ danh sách đã lọc
    private void updateTableTopNguoiDung(List<Object[]> data) {
        DefaultTableModel modelTop = (DefaultTableModel) tblTopNguoiDung.getModel();
        modelTop.setRowCount(0);
        for (Object[] row : data) {
            modelTop.addRow(new Object[]{row[0], row[1], String.format("%.2f", row[2])});
        }
    }
    
    // Phương thức tính khoảng cách Levenshtein (độ tương tự chuỗi)
    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                                               dp[i - 1][j] + 1),
                                       dp[i][j - 1] + 1);
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    // Phương thức tìm kiếm
    private void timKiem() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            updateTableTopNguoiDung(listTopNguoiDung);
            return;
        }
        List<Object[]> filtered = listTopNguoiDung.stream()
            .filter(row -> {
                String monHoc = row[0].toString().toLowerCase();
                String hoTen = row[1].toString().toLowerCase();
                // Fuzzy match: khoảng cách ≤ 2 hoặc substring match
                return levenshteinDistance(monHoc, keyword) <= 2 ||
                       levenshteinDistance(hoTen, keyword) <= 2 ||
                       monHoc.contains(keyword) ||
                       hoTen.contains(keyword);
            })
            .collect(Collectors.toList());
        updateTableTopNguoiDung(filtered);
    }
    
    private void loadThongKe() {
        // Tổng số người dùng
        lblTongNguoiDung.setText(String.valueOf(dao.getTongSoNguoiDung()));

        // Tổng bài thi theo môn học
        DefaultTableModel modelTongBaiThi = (DefaultTableModel) tblTongBaiThiMonHoc.getModel();
        modelTongBaiThi.setRowCount(0);
        Map<String, Integer> tongBaiThi = dao.getTongBaiThiTheoMonHoc();
        for (Map.Entry<String, Integer> entry : tongBaiThi.entrySet()) {
            modelTongBaiThi.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        // Điểm trung bình theo môn học
        DefaultTableModel modelDiemMonHoc = (DefaultTableModel) tblDiemMonHoc.getModel();
        modelDiemMonHoc.setRowCount(0);
        Map<String, Double> diemMonHoc = dao.getDiemTrungBinhTheoMonHoc();
        for (Map.Entry<String, Double> entry : diemMonHoc.entrySet()) {
            modelDiemMonHoc.addRow(new Object[]{entry.getKey(), String.format("%.2f", entry.getValue())});
        }

        // Điểm trung bình theo cấp học
        DefaultTableModel modelDiemCapHoc = (DefaultTableModel) tblDiemCapHoc.getModel();
        modelDiemCapHoc.setRowCount(0);
        Map<String, Double> diemCapHoc = dao.getDiemTrungBinhTheoCapHoc();
        for (Map.Entry<String, Double> entry : diemCapHoc.entrySet()) {
            modelDiemCapHoc.addRow(new Object[]{entry.getKey(), String.format("%.2f", entry.getValue())});
        }

        // Top người dùng theo môn học
        DefaultTableModel modelTop = (DefaultTableModel) tblTopNguoiDung.getModel();
        modelTop.setRowCount(0);
        List<Object[]> topUsers = dao.getTopNguoiDungTheoMonHoc();
        for (Object[] row : topUsers) {
            modelTop.addRow(new Object[]{row[0], row[1], String.format("%.2f", row[2])});
        }
    }    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDiemMonHoc = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblDiemCapHoc = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblTongBaiThiMonHoc = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTopNguoiDung = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        lblTongNguoiDung = new javax.swing.JLabel();
        btnTaiLai = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Tổng số người dùng:");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel2.setText("Điểm trung bình môn học");

        tblDiemMonHoc.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        tblDiemMonHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Môn học", "Điểm trung bình"
            }
        ));
        jScrollPane3.setViewportView(tblDiemMonHoc);

        jScrollPane4.setViewportView(jScrollPane3);

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel3.setText("Top người dùng có môn học điểm cao nhất:");

        tblDiemCapHoc.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        tblDiemCapHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Cấp học", "Điểm trung bình"
            }
        ));
        jScrollPane6.setViewportView(tblDiemCapHoc);

        jScrollPane5.setViewportView(jScrollPane6);

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel4.setText("Điểm cấp học");

        tblTongBaiThiMonHoc.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        tblTongBaiThiMonHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Môn học", "Tổng số bài thi"
            }
        ));
        jScrollPane8.setViewportView(tblTongBaiThiMonHoc);

        jScrollPane9.setViewportView(jScrollPane8);

        tblTopNguoiDung.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        tblTopNguoiDung.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Môn học", "Họ tên sinh viên", "Điểm trung bình"
            }
        ));
        jScrollPane1.setViewportView(tblTopNguoiDung);

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel5.setText("Tổng số bài thi môn học:");

        lblTongNguoiDung.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblTongNguoiDung.setForeground(new java.awt.Color(255, 51, 51));
        lblTongNguoiDung.setText("0");

        btnTaiLai.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnTaiLai.setText("Tải lại ");
        btnTaiLai.addActionListener(this::btnTaiLaiActionPerformed);

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 36)); // NOI18N
        jLabel6.setText("Thống kê & Báo cáo");

        jButton1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jButton1.setText("Tìm kiếm");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        btnThoat.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnThoat.setText("Về trang chủ");
        btnThoat.addActionListener(this::btnThoatActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2)
                                    .addComponent(jScrollPane4)
                                    .addComponent(jLabel5)
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnTaiLai, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel4)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnThoat)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(30, 30, 30)
                                        .addComponent(lblTongNguoiDung)))))
                        .addGap(60, 60, 60))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblTongNguoiDung))
                .addGap(61, 61, 61)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)
                        .addComponent(btnTaiLai)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaiLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaiLaiActionPerformed
        loadThongKe();
    }//GEN-LAST:event_btnTaiLaiActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        timKiem(); 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        new AdminForm().setVisible(true);
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
        java.awt.EventQueue.invokeLater(() -> new FrmThongKeBaoCao().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTaiLai;
    private javax.swing.JButton btnThoat;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lblTongNguoiDung;
    private javax.swing.JTable tblDiemCapHoc;
    private javax.swing.JTable tblDiemMonHoc;
    private javax.swing.JTable tblTongBaiThiMonHoc;
    private javax.swing.JTable tblTopNguoiDung;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
