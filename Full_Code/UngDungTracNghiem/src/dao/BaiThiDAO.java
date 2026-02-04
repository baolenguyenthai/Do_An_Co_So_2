package dao;

import database.KetNoiDB;
import model.BaiThi;
import model.ChiTietBaiThi;
import java.sql.*;


public class BaiThiDAO {
    // Thêm bài thi mới và trả về ID
    public int insert(BaiThi bt) {
        String sql = "INSERT INTO bai_thi (nguoi_dung_id, bo_cau_hoi_id, tong_cau, so_cau_dung, diem, thoi_gian_bat_dau, thoi_gian_ket_thuc) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bt.getNguoiDungId());
            ps.setInt(2, bt.getBoCauHoiId());
            ps.setInt(3, bt.getTongCau());
            ps.setInt(4, bt.getSoCauDung());
            ps.setFloat(5, bt.getDiem());
            ps.setTimestamp(6, new Timestamp(bt.getThoiGianBatDau().getTime()));
            ps.setTimestamp(7, new Timestamp(bt.getThoiGianKetThuc().getTime()));
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // Cập nhật bài thi (ví dụ: khi nộp bài)
    public boolean update(BaiThi bt) {
        String sql = "UPDATE bai_thi SET so_cau_dung = ?, diem = ?, thoi_gian_ket_thuc = ? WHERE bai_thi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bt.getSoCauDung());
            ps.setFloat(2, bt.getDiem());
            ps.setTimestamp(3, new Timestamp(bt.getThoiGianKetThuc().getTime()));
            ps.setInt(4, bt.getBaiThiId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Thêm chi tiết bài thi
    public boolean insertChiTiet(ChiTietBaiThi ct) {
        String sql = "INSERT INTO chi_tiet_bai_thi (bai_thi_id, cau_hoi_id, dap_an_da_chon, dung) VALUES (?, ?, ?, ?)";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getBaiThiId());
            ps.setInt(2, ct.getCauHoiId());
            if (ct.getDapAnDaChon() != null) ps.setInt(3, ct.getDapAnDaChon()); else ps.setNull(3, Types.INTEGER);
            if (ct.getDung() != null) ps.setBoolean(4, ct.getDung()); else ps.setNull(4, Types.BOOLEAN);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    
    // Kiểm tra người dùng đã làm bộ câu hỏi này chưa
    public boolean hasAttempt(int nguoiDungId, int boCauHoiId) {
        String sql = "SELECT COUNT(*) FROM bai_thi WHERE nguoi_dung_id = ? AND bo_cau_hoi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nguoiDungId);
            ps.setInt(2, boCauHoiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}