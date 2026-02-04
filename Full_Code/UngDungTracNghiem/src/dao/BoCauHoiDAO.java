package dao;

import database.KetNoiDB;
import model.BoCauHoi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoCauHoiDAO {

    // lấy ds bộ câu 
    public List<BoCauHoi> getAll() {
        List<BoCauHoi> list = new ArrayList<>();
        String sql = "SELECT * FROM bo_cau_hoi";

        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BoCauHoi b = new BoCauHoi();
                b.setBoCauHoiId(rs.getInt("bo_cau_hoi_id"));
                b.setTenBoCauHoi(rs.getString("ten_bo_cau_hoi"));
                b.setMoTa(rs.getString("mo_ta"));
                b.setCapHocId(rs.getInt("cap_hoc_id"));
                b.setMonHocId(rs.getInt("mon_hoc_id"));
                b.setNguoiTao(rs.getInt("nguoi_tao"));
                b.setTrangThai(rs.getString("trang_thai"));
                b.setCongKhai(rs.getBoolean("cong_khai"));
                b.setNgayTao(rs.getTimestamp("ngay_tao"));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // thêm 
    public boolean insert(BoCauHoi b) {
        String sql = "INSERT INTO bo_cau_hoi(ten_bo_cau_hoi, mo_ta, cap_hoc_id, mon_hoc_id, nguoi_tao, trang_thai, cong_khai) VALUES (?, ?, ?, ?, ?, 'CHO_DUYET', 0)";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getTenBoCauHoi());
            ps.setString(2, b.getMoTa());
            ps.setInt(3, b.getCapHocId());
            ps.setInt(4, b.getMonHocId());
            ps.setInt(5, b.getNguoiTao());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Cập nhật
    public boolean update(BoCauHoi b) {
        String sql = "UPDATE bo_cau_hoi SET ten_bo_cau_hoi = ?, mo_ta = ?, cap_hoc_id = ?, mon_hoc_id = ?, trang_thai = ?, cong_khai = ? WHERE bo_cau_hoi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getTenBoCauHoi());
            ps.setString(2, b.getMoTa());
            ps.setInt(3, b.getCapHocId());
            ps.setInt(4, b.getMonHocId());
            ps.setString(5, b.getTrangThai());
            ps.setBoolean(6, b.isCongKhai());
            ps.setInt(7, b.getBoCauHoiId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy theo ID
    public BoCauHoi getById(int id) {
        String sql = "SELECT * FROM bo_cau_hoi WHERE bo_cau_hoi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BoCauHoi b = new BoCauHoi();
                b.setBoCauHoiId(rs.getInt("bo_cau_hoi_id"));
                b.setTenBoCauHoi(rs.getString("ten_bo_cau_hoi"));
                b.setMoTa(rs.getString("mo_ta"));
                b.setCapHocId(rs.getInt("cap_hoc_id"));
                b.setMonHocId(rs.getInt("mon_hoc_id"));
                b.setNguoiTao(rs.getInt("nguoi_tao"));
                b.setTrangThai(rs.getString("trang_thai"));
                b.setCongKhai(rs.getBoolean("cong_khai"));
                b.setNgayTao(rs.getTimestamp("ngay_tao"));
                return b;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // xoá
    public boolean delete(int id) {
        String sql = "DELETE FROM bo_cau_hoi WHERE bo_cau_hoi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    
    // Thêm và trả về ID (dùng cho tải lên)
    public int themBoCauHoi(BoCauHoi b) {
        String sql = "INSERT INTO bo_cau_hoi(ten_bo_cau_hoi, mo_ta, cap_hoc_id, mon_hoc_id, nguoi_tao, trang_thai, cong_khai) VALUES (?, ?, ?, ?, ?, 'CHO_DUYET', 0)";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {  // Thêm RETURN_GENERATED_KEYS

            ps.setString(1, b.getTenBoCauHoi());
            ps.setString(2, b.getMoTa());
            ps.setInt(3, b.getCapHocId());
            ps.setInt(4, b.getMonHocId());
            ps.setInt(5, b.getNguoiTao());

            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);  // Trả về ID tự động
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;  // Trả về -1 nếu lỗi
    }

    // Phương thức mới: Đếm số bộ câu hỏi dùng môn học này
    public int countByMonHocId(int monHocId) {
        String sql = "SELECT COUNT(*) FROM bo_cau_hoi WHERE mon_hoc_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, monHocId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
    
    // BoCauHoiDAO
    public List<BoCauHoi> getByMonHocAndCapHoc(int monHocId, int capHocId) {
        List<BoCauHoi> list = new ArrayList<>();
        String sql = "SELECT * FROM bo_cau_hoi WHERE mon_hoc_id = ? AND cap_hoc_id = ? AND trang_thai = 'DA_DUYET'";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, monHocId);
            ps.setInt(2, capHocId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BoCauHoi b = new BoCauHoi();
                b.setBoCauHoiId(rs.getInt("bo_cau_hoi_id"));
                b.setTenBoCauHoi(rs.getString("ten_bo_cau_hoi"));
                b.setMoTa(rs.getString("mo_ta"));
                b.setCapHocId(rs.getInt("cap_hoc_id"));
                b.setMonHocId(rs.getInt("mon_hoc_id"));
                b.setNguoiTao(rs.getInt("nguoi_tao"));
                b.setTrangThai(rs.getString("trang_thai"));
                b.setCongKhai(rs.getBoolean("cong_khai"));
                b.setNgayTao(rs.getTimestamp("ngay_tao"));
                list.add(b);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // Phần của FrmLambaiThi
    // Lấy danh sách bộ câu hỏi theo môn học (trạng thái DA_DUYET)
    public List<BoCauHoi> getByMonHoc(int monHocId) {
        List<BoCauHoi> list = new ArrayList<>();
        String sql = "SELECT * FROM bo_cau_hoi WHERE mon_hoc_id = ? AND trang_thai = 'DA_DUYET'";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, monHocId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BoCauHoi b = new BoCauHoi();
                b.setBoCauHoiId(rs.getInt("bo_cau_hoi_id"));
                b.setTenBoCauHoi(rs.getString("ten_bo_cau_hoi"));
                b.setMoTa(rs.getString("mo_ta"));
                b.setCapHocId(rs.getInt("cap_hoc_id"));
                b.setMonHocId(rs.getInt("mon_hoc_id"));
                b.setNguoiTao(rs.getInt("nguoi_tao"));
                b.setTrangThai(rs.getString("trang_thai"));
                b.setCongKhai(rs.getBoolean("cong_khai"));
                b.setNgayTao(rs.getTimestamp("ngay_tao"));
                list.add(b);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // Lấy danh sách bộ câu hỏi theo cấp học (trạng thái DA_DUYET)
    public List<BoCauHoi> getByCapHoc(int capHocId) {
        List<BoCauHoi> list = new ArrayList<>();
        String sql = "SELECT * FROM bo_cau_hoi WHERE cap_hoc_id = ? AND trang_thai = 'DA_DUYET'";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, capHocId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BoCauHoi b = new BoCauHoi();
                b.setBoCauHoiId(rs.getInt("bo_cau_hoi_id"));
                b.setTenBoCauHoi(rs.getString("ten_bo_cau_hoi"));
                b.setMoTa(rs.getString("mo_ta"));
                b.setCapHocId(rs.getInt("cap_hoc_id"));
                b.setMonHocId(rs.getInt("mon_hoc_id"));
                b.setNguoiTao(rs.getInt("nguoi_tao"));
                b.setTrangThai(rs.getString("trang_thai"));
                b.setCongKhai(rs.getBoolean("cong_khai"));
                b.setNgayTao(rs.getTimestamp("ngay_tao"));
                list.add(b);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
