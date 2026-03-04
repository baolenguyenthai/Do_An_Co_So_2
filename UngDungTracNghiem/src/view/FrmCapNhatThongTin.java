
package view;

import dao.NguoiDungDAO;
import model.NguoiDung;
import javax.swing.JOptionPane;
/**
 *
 * @author lebao
 */
public class FrmCapNhatThongTin extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmCapNhatThongTin.class.getName());

    private NguoiDung nguoiDung;
    private NguoiDungDAO dao = new NguoiDungDAO();

    // Constructor mặc định, sử dụng ID từ phiên
    public FrmCapNhatThongTin() {
        if (!utils.QuanLyPhien.daDangNhap()) {
            JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        initComponents();
        setLocationRelativeTo(null);
        customUI();
        txtMatKhau.setEchoChar('•'); // đẹp & chuẩn macOS
        txtXacNhanMatKhau.setEchoChar('•'); // đẹp & chuẩn macOS
        bindEnterKeyForUpdate();
        taiThongTin(utils.QuanLyPhien.layIdNguoiDungHienTai());
    }

    // Constructor với ID cụ thể (cho admin hoặc test)
    public FrmCapNhatThongTin(int idNguoiDung) {
        initComponents();
        setLocationRelativeTo(null);
        customUI();
        txtMatKhau.setEchoChar('•');
        txtXacNhanMatKhau.setEchoChar('•');
        bindEnterKeyForUpdate();
        taiThongTin(idNguoiDung);
    }

    private void bindEnterKeyForUpdate() {
        javax.swing.KeyStroke enterKey =
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0);

        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(enterKey, "capNhatThongTin");

        getRootPane().getActionMap().put("capNhatThongTin", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (btnCapNhat.isEnabled()) {
                    btnCapNhat.doClick();
                }
            }
        });
    }

    private void taiThongTin(int id) {
        nguoiDung = dao.getById(id);
        if (nguoiDung != null) {
            txtHoTen.setText(nguoiDung.getHoTen());
            txtEmail.setText(nguoiDung.getEmail());
            txtTenDangNhap.setText(nguoiDung.getTenDangNhap());
            txtTenDangNhap.setEditable(false);
            txtTenDangNhap.setEnabled(false); // ko cho bấm vô
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    
    private void customUI() {
        // ===== Focus + padding ô nhập =====
        addFocusBorder(txtTenDangNhap);
        addFocusBorder(txtHoTen);
        addFocusBorder(txtEmail);
        addFocusBorder(txtMatKhau);
        addFocusBorder(txtXacNhanMatKhau);
        
        // ===== Hover nút cập nhật =====
        addHoverButton(
            btnCapNhat,
            new java.awt.Color(255, 255, 255),   // nền thường
            new java.awt.Color(153, 153, 255),   // chữ thường
            new java.awt.Color(153, 153, 255),   // nền hover
            java.awt.Color.WHITE                // chữ hover
        );

        // ===== Hover nút Rời khỏi =====
        addHoverButton(
            btnHuy,
            new java.awt.Color(255, 255, 255),   // nền thường
            new java.awt.Color(153, 153, 255),   // chữ thường
            new java.awt.Color(153, 153, 255),   // nền hover
            java.awt.Color.WHITE                // chữ hover
        );
    }

    private void addFocusBorder(javax.swing.JComponent txt) {

        // padding cố định
        javax.swing.border.Border padding =
            javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8);

        // viền thường (tím nhạt)
        javax.swing.border.Border normalBorder =
            javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(153, 153, 255), 2, true
            );

        // viền focus (tím đậm)
        javax.swing.border.Border focusBorder =
            javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(98, 0, 238), 2, true
            );

        // border ban đầu
        txt.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(normalBorder, padding)
        );

        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                txt.setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(focusBorder, padding)
                );
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                txt.setBorder(
                    javax.swing.BorderFactory.createCompoundBorder(normalBorder, padding)
                );
            }
        });
    }

    
    private void addHoverButton(
        javax.swing.JButton btn,
        java.awt.Color bgNormal,
        java.awt.Color fgNormal,
        java.awt.Color bgHover,
        java.awt.Color fgHover
    ) {
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setBackground(bgNormal);
        btn.setForeground(fgNormal);
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bgHover);
                btn.setForeground(fgHover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bgNormal);
                btn.setForeground(fgNormal);
            }
        });
    }
    
      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtXacNhanMatKhau = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        chkHienMatKhau = new javax.swing.JCheckBox();
        btnCapNhat = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtTenDangNhap = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JPasswordField();
        btnHuy = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtXacNhanMatKhau.setForeground(new java.awt.Color(153, 153, 255));
        txtXacNhanMatKhau.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 153, 255));
        jLabel6.setText("Mật khẩu mới");

        chkHienMatKhau.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        chkHienMatKhau.setForeground(new java.awt.Color(153, 153, 255));
        chkHienMatKhau.setText("Hiện mật khẩu");
        chkHienMatKhau.addActionListener(this::chkHienMatKhauActionPerformed);

        btnCapNhat.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnCapNhat.setForeground(new java.awt.Color(153, 153, 255));
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));
        btnCapNhat.setContentAreaFilled(false);
        btnCapNhat.addActionListener(this::btnCapNhatActionPerformed);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 255));
        jLabel1.setText("CẬP NHẬT THÔNG TIN TÀI KHOẢN");

        txtTenDangNhap.setForeground(new java.awt.Color(153, 153, 255));
        txtTenDangNhap.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 255));
        jLabel2.setText("Tên tài khoản");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 255));
        jLabel3.setText("Họ tên");

        txtHoTen.setForeground(new java.awt.Color(153, 153, 255));
        txtHoTen.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 255));
        jLabel4.setText("Email");

        txtEmail.setForeground(new java.awt.Color(153, 153, 255));
        txtEmail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 255));
        jLabel5.setText("Xác nhận mật khẩu");

        txtMatKhau.setForeground(new java.awt.Color(153, 153, 255));
        txtMatKhau.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));

        btnHuy.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnHuy.setForeground(new java.awt.Color(153, 153, 255));
        btnHuy.setText("Rời khỏi");
        btnHuy.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));
        btnHuy.setContentAreaFilled(false);
        btnHuy.addActionListener(this::btnHuyActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(txtTenDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkHienMatKhau))
                        .addComponent(txtXacNhanMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(52, 52, 52))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGap(51, 51, 51)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTenDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(chkHienMatKhau))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtXacNhanMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("CẬP NHẬT THÔNG TIN TÀI KHOẢN");

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Cập nhật thông tin 1 cách dễ dàng");

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("© 2026 Lê Nguyễn Thái Bảo – lenguyenthaib@gmail.com ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(53, 53, 53))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(99, 99, 99))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jLabel10)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(215, 215, 215)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkHienMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkHienMatKhauActionPerformed
        if (chkHienMatKhau.isSelected()) {
            txtMatKhau.setEchoChar((char) 0);
            txtXacNhanMatKhau.setEchoChar((char) 0);
        } else {
            txtMatKhau.setEchoChar('•');
            txtXacNhanMatKhau.setEchoChar('•');
        }
    }//GEN-LAST:event_chkHienMatKhauActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        String hoTen = txtHoTen.getText().trim();
        String email = txtEmail.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String xacNhanMatKhau = new String(txtXacNhanMatKhau.getPassword());

        if (hoTen.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ họ tên và email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra email đã tồn tại chưa (trừ người dùng hiện tại)
        if (dao.existsByEmailExceptId(email, nguoiDung.getNguoiDungId())) {
            JOptionPane.showMessageDialog(this, "Email đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!matKhau.isEmpty()) {
            if (!matKhau.equals(xacNhanMatKhau)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (matKhau.length() < 6) {
                JOptionPane.showMessageDialog(this, "Mật khẩu phải ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            nguoiDung.setMatKhau(matKhau);
        }

        nguoiDung.setHoTen(hoTen);
        nguoiDung.setEmail(email);
        dao.update(nguoiDung);

        JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        
        this.dispose();
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnHuyActionPerformed

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

        java.awt.EventQueue.invokeLater(() -> new FrmCapNhatThongTin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnHuy;
    private javax.swing.JCheckBox chkHienMatKhau;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JPasswordField txtMatKhau;
    private javax.swing.JTextField txtTenDangNhap;
    private javax.swing.JPasswordField txtXacNhanMatKhau;
    // End of variables declaration//GEN-END:variables
}
