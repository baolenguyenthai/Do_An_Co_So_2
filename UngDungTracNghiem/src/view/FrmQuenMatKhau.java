
package view;

import dao.NguoiDungDAO;
import database.KetNoiDB;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;




/**
 *
 * @author lebao
 */
public class FrmQuenMatKhau extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmQuenMatKhau.class.getName());
    private String otpDaGui;
    private String emailNhan;
    private long otpThoiGianTao; // thời điểm tạo OTP (milliseconds)
    private static final long OTP_HET_HAN = 60 * 1000; // 60 giây

    /**
     * Creates new form FrmQuenMatKhau
     */
    public FrmQuenMatKhau() {
        initComponents();
        setLocationRelativeTo(null);
        txtOTP.setEnabled(false);
        txtMatKhauMoi.setEnabled(false);
        btnXacNhan.setEnabled(false);
        
        customUI();
        txtMatKhauMoi.setEchoChar('•'); // đẹp & chuẩn macOS
        
        // Bấm ENTER để Đăng nhập
        getRootPane().setDefaultButton(btnXacNhan);        

    }
    
    private void customUI() {

        addFocusBorder(txtEmail);
        addFocusBorder(txtOTP);
        addFocusBorder(txtMatKhauMoi);
        
        // Hover nút
        addHoverButton(
            btnXacNhan,
            new java.awt.Color(255, 255, 255),
            new java.awt.Color(153, 153, 255),
            new java.awt.Color(153, 153, 255),
            java.awt.Color.WHITE
        );

        // Hover nút
        addHoverButton(
            btnLayOTP,
            new java.awt.Color(255, 255, 255),
            new java.awt.Color(153, 153, 255),
            new java.awt.Color(153, 153, 255),
            java.awt.Color.WHITE
        );
        
        addHoverButton(btnDangNhap,
            new java.awt.Color(153, 153, 255),
            java.awt.Color.WHITE,
            java.awt.Color.WHITE,
            new java.awt.Color(153, 153, 255)
        );


    }
    
    // padding và khi bấm vô textfield sẽ hiện màu viền đậm hơn
    private void addFocusBorder(javax.swing.JTextField txt) {
        // padding cố định
        javax.swing.border.Border padding =
            javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8);

        // viền thường
        javax.swing.border.Border normalBorder =
            javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(153, 153, 255), 2, true
            );

        // viền khi focus (tím đậm)
        javax.swing.border.Border focusBorder =
            javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(98, 0, 238), 2, true
            );

        // set border ban đầu (viền thường + padding)
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
    
    private void addHoverLabel(
        javax.swing.JLabel lbl,
        java.awt.Color normalColor,
        java.awt.Color hoverColor
    ) {
        lbl.setForeground(normalColor);
        lbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        String text = lbl.getText();

        lbl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                lbl.setForeground(hoverColor);
                lbl.setText("<html><u>" + text + "</u></html>");
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                lbl.setForeground(normalColor);
                lbl.setText(text);
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

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        chkHienMatKhau = new javax.swing.JCheckBox();
        txtEmail = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnLayOTP = new javax.swing.JButton();
        txtOTP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMatKhauMoi = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        btnXacNhan = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnDangNhap = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 255));

        jPanel2.setBackground(new java.awt.Color(153, 153, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 255));
        jLabel1.setText("QUÊN MẬT KHẨU");

        chkHienMatKhau.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        chkHienMatKhau.setForeground(new java.awt.Color(153, 153, 255));
        chkHienMatKhau.setText("Hiện mật khẩu");
        chkHienMatKhau.addActionListener(this::chkHienMatKhauActionPerformed);

        txtEmail.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(153, 153, 255));
        txtEmail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 255));
        jLabel2.setText("Email");

        btnLayOTP.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        btnLayOTP.setForeground(new java.awt.Color(153, 153, 255));
        btnLayOTP.setText("Lấy OTP");
        btnLayOTP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));
        btnLayOTP.setContentAreaFilled(false);
        btnLayOTP.addActionListener(this::btnLayOTPActionPerformed);

        txtOTP.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        txtOTP.setForeground(new java.awt.Color(153, 153, 255));
        txtOTP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 255));
        jLabel3.setText("Nhập mã OTP");

        txtMatKhauMoi.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        txtMatKhauMoi.setForeground(new java.awt.Color(153, 153, 255));
        txtMatKhauMoi.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 255));
        jLabel4.setText("Mật khẩu mới");

        btnXacNhan.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnXacNhan.setForeground(new java.awt.Color(153, 153, 255));
        btnXacNhan.setText("Xác nhận");
        btnXacNhan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));
        btnXacNhan.setContentAreaFilled(false);
        btnXacNhan.addActionListener(this::btnXacNhanActionPerformed);

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("CHÀO MỪNG ĐẾN VỚI QUÊN MẬT KHẨU ");

        btnDangNhap.setBackground(new java.awt.Color(76, 175, 80));
        btnDangNhap.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        btnDangNhap.setForeground(new java.awt.Color(255, 255, 255));
        btnDangNhap.setText("Về đăng nhập");
        btnDangNhap.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        btnDangNhap.setContentAreaFilled(false);
        btnDangNhap.setName(""); // NOI18N
        btnDangNhap.addActionListener(this::btnDangNhapActionPerformed);

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Đừng lo !! Hãy lấy lại bằng địa chỉ Email của bạn");

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Quên mật khẩu rồi hửm? ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel5))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(jLabel7)))
                .addContainerGap(87, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(179, 179, 179)
                            .addComponent(jLabel9))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(194, 194, 194)
                            .addComponent(btnDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(186, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(jLabel5)
                .addGap(76, 76, 76)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(276, 276, 276)
                    .addComponent(jLabel9)
                    .addGap(68, 68, 68)
                    .addComponent(btnDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(201, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jLabel1)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnLayOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(chkHienMatKhau))
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMatKhauMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2))
                        .addGap(0, 41, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(btnLayOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtOTP, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(chkHienMatKhau))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMatKhauMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String taoOTP() {
        int otp = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
    
    // ===== HÀM KIỂM TRA EMAIL =====
    private boolean kiemTraEmailTonTai(String email) {
        try {
            Connection conn = KetNoiDB.ketNoi();
            String sql = "SELECT * FROM nguoi_dung WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void btnLayOTPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayOTPActionPerformed
        emailNhan = txtEmail.getText().trim();

        if (emailNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email");
            return;
        }

        // Kiểm tra email có tồn tại
        if (!kiemTraEmailTonTai(emailNhan)) {
            JOptionPane.showMessageDialog(this, "Email không tồn tại trong hệ thống");
            return;
        }

        String otpMoi = taoOTP();
        String loiGuiOtp = NguoiDungDAO.guiOTP(emailNhan, otpMoi);
        if (loiGuiOtp != null) {
            JOptionPane.showMessageDialog(this, loiGuiOtp, "Lỗi gửi OTP", JOptionPane.ERROR_MESSAGE);
            return;
        }

        otpDaGui = otpMoi;
        otpThoiGianTao = System.currentTimeMillis(); // LƯU THỜI GIAN TẠO OTP
        JOptionPane.showMessageDialog(this, "OTP đã được gửi về email");

        txtOTP.setEnabled(true);
        txtMatKhauMoi.setEnabled(true);
        btnXacNhan.setEnabled(true);
    }//GEN-LAST:event_btnLayOTPActionPerformed

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        String otpNhap = txtOTP.getText().trim();
        String matKhauMoi = new String(txtMatKhauMoi.getPassword());

        if (otpNhap.isEmpty() || matKhauMoi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ OTP và mật khẩu mới");
            return;
        }
        
        // KIỂM TRA ĐỘ DÀI MẬT KHẨU
        if (matKhauMoi.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 6 ký tự");
            return;
        }
    
        // KIỂM TRA OTP HẾT HẠN
        long thoiGianHienTai = System.currentTimeMillis();
        if (thoiGianHienTai - otpThoiGianTao > OTP_HET_HAN) {
            JOptionPane.showMessageDialog(this, "OTP đã hết hạn (quá 60 giây). Vui lòng lấy OTP mới!");
            return;
        }

        // KIỂM TRA OTP ĐÚNG
        if (!otpNhap.equals(otpDaGui)) {
            JOptionPane.showMessageDialog(this, "OTP không đúng");
            return;
        }

        boolean ok = NguoiDungDAO.capNhatMatKhau(emailNhan, matKhauMoi);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công");
            new FrmDangNhap().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại");
        }
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void chkHienMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkHienMatKhauActionPerformed
        if (chkHienMatKhau.isSelected()) {
            // Hiển thị mật khẩu dưới dạng văn bản thuần túy
            txtMatKhauMoi.setEchoChar((char) 0);
        } else {
            // Ẩn mật khẩu với ký tự '*'
            txtMatKhauMoi.setEchoChar('*');
        }
    }//GEN-LAST:event_chkHienMatKhauActionPerformed

    private void btnDangNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangNhapActionPerformed
        // Quay về form đăng nhập
        new FrmDangNhap().setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_btnDangNhapActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmQuenMatKhau().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangNhap;
    private javax.swing.JButton btnLayOTP;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JCheckBox chkHienMatKhau;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtMatKhauMoi;
    private javax.swing.JTextField txtOTP;
    // End of variables declaration//GEN-END:variables
}
