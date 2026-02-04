package dao;

import database.KetNoiDB;
import model.NguoiDung;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;


public class NguoiDungDAO {

    // ========== CRUD NGƯỜI DÙNG ==========

    public List<NguoiDung> getAll() {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoi_dung";

        try (
            Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setNguoiDungId(rs.getInt("nguoi_dung_id"));
                nd.setTenDangNhap(rs.getString("ten_dang_nhap"));
                nd.setHoTen(rs.getString("ho_ten"));
                nd.setEmail(rs.getString("email"));
                nd.setVaiTroId(rs.getInt("vai_tro_id"));
                nd.setTrangThai(rs.getInt("trang_thai"));
                list.add(nd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public NguoiDung getById(int id) {
        String sql = "SELECT nguoi_dung_id, ten_dang_nhap, ho_ten, email, vai_tro_id, trang_thai FROM nguoi_dung WHERE nguoi_dung_id = ?";
        try (
            Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setNguoiDungId(rs.getInt("nguoi_dung_id"));
                nd.setTenDangNhap(rs.getString("ten_dang_nhap"));
                nd.setHoTen(rs.getString("ho_ten"));
                nd.setEmail(rs.getString("email"));
                nd.setVaiTroId(rs.getInt("vai_tro_id"));
                nd.setTrangThai(rs.getInt("trang_thai"));
                // Không set mật khẩu để tránh giữ giá trị cũ
                return nd;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public NguoiDung layTheoTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM nguoi_dung WHERE ten_dang_nhap = ?";
        try (
            Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, tenDangNhap);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setNguoiDungId(rs.getInt("nguoi_dung_id"));
                nd.setTenDangNhap(rs.getString("ten_dang_nhap"));
                nd.setMatKhau(rs.getString("mat_khau"));
                nd.setHoTen(rs.getString("ho_ten"));
                nd.setEmail(rs.getString("email"));
                nd.setVaiTroId(rs.getInt("vai_tro_id"));
                nd.setTrangThai(rs.getInt("trang_thai"));
                return nd;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // kiểm tra coi có trùng email vs ng khác ko
    public boolean existsByEmailExceptId(String email, int id) {
    String sql = "SELECT 1 FROM nguoi_dung WHERE email = ? AND nguoi_dung_id != ?";
    try (
        Connection c = KetNoiDB.ketNoi();
        PreparedStatement ps = c.prepareStatement(sql)
    ) {
        ps.setString(1, email);
        ps.setInt(2, id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
    }
    
    public boolean existsByTenDangNhap(String tenDangNhap) {
        String sql = "SELECT 1 FROM nguoi_dung WHERE ten_dang_nhap = ?";
        try (
            Connection c = KetNoiDB.ketNoi();
            PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, tenDangNhap);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insert(NguoiDung nd) {
        String sql = """
            INSERT INTO nguoi_dung
            (ten_dang_nhap, mat_khau, ho_ten, email, vai_tro_id, trang_thai)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (
            Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nd.getTenDangNhap());
            ps.setString(2, nd.getMatKhau());
            ps.setString(3, nd.getHoTen());
            ps.setString(4, nd.getEmail());
            ps.setInt(5, nd.getVaiTroId());
            ps.setInt(6, nd.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void update(NguoiDung nd) {
        String sql = "UPDATE nguoi_dung SET ho_ten=?, email=?, vai_tro_id=?, trang_thai=?";
        boolean updatePassword = nd.getMatKhau() != null && !nd.getMatKhau().isEmpty();
        if (updatePassword) {
            sql += ", mat_khau=?";
        }
        sql += " WHERE nguoi_dung_id=?";

        try (
            Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nd.getHoTen());
            ps.setString(2, nd.getEmail());
            ps.setInt(3, nd.getVaiTroId());
            ps.setInt(4, nd.getTrangThai());
            int paramIndex = 5;
            if (updatePassword) {
                ps.setString(paramIndex++, nd.getMatKhau());
            }
            ps.setInt(paramIndex, nd.getNguoiDungId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM nguoi_dung WHERE nguoi_dung_id=?";
        try (
            Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== QUÊN MẬT KHẨU ==========

    public static boolean kiemTraEmailTonTai(String email) {
        try {
            Connection conn = KetNoiDB.ketNoi();
            String sql = "SELECT 1 FROM nguoi_dung WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void guiOTP(String email, String otp) {

        final String emailGui = "lenguyenthaib@gmail.com";
        final String matKhau = "nmap nafc qdsl wrbo";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailGui, matKhau);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailGui));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(email)
            );
            // ===== TIÊU ĐỀ EMAIL =====
            message.setSubject("OTP đặt lại mật khẩu App trắc nghiệm");

            // ===== NỘI DUNG EMAIL (SỬA Ở ĐÂY) =====
            String noiDung = 
                    "Mã OTP của bạn là: " + otp + "\n\n"
                  + "Xin đừng chia sẻ mã OTP này với người khác.";

            message.setText(noiDung);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean capNhatMatKhau(String email, String matKhauMoi) {
        try {
            Connection conn = KetNoiDB.ketNoi();
            String sql = "UPDATE nguoi_dung SET mat_khau = ? WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, matKhauMoi);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
