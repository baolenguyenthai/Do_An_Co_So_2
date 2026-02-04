package dao;

import database.KetNoiDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeDAO {

    // Tổng số người dùng
    public int getTongSoNguoiDung() {
        String sql = "SELECT COUNT(*) FROM nguoi_dung";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Tổng số bài thi cho từng môn học (trả về Map<ten_mon_hoc, tong_bai_thi>)
    public Map<String, Integer> getTongBaiThiTheoMonHoc() {
        Map<String, Integer> result = new HashMap<>();
        String sql = "SELECT m.ten_mon_hoc, COUNT(bt.bai_thi_id) AS tong_bai_thi " +
                     "FROM bai_thi bt " +
                     "JOIN bo_cau_hoi bch ON bt.bo_cau_hoi_id = bch.bo_cau_hoi_id " +
                     "JOIN mon_hoc m ON bch.mon_hoc_id = m.mon_hoc_id " +
                     "GROUP BY m.ten_mon_hoc";  // Sửa: Chỉ GROUP BY tên
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("ten_mon_hoc"), rs.getInt("tong_bai_thi"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Điểm trung bình theo môn học (trả về Map<ten_mon_hoc, diem_trung_binh>)
    public Map<String, Double> getDiemTrungBinhTheoMonHoc() {
        Map<String, Double> result = new HashMap<>();
        String sql = "SELECT m.ten_mon_hoc, AVG(bt.diem) AS diem_tb " +
                     "FROM bai_thi bt " +
                     "JOIN bo_cau_hoi bch ON bt.bo_cau_hoi_id = bch.bo_cau_hoi_id " +
                     "JOIN mon_hoc m ON bch.mon_hoc_id = m.mon_hoc_id " +
                     "GROUP BY m.ten_mon_hoc";  // Sửa: Chỉ GROUP BY tên
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("ten_mon_hoc"), rs.getDouble("diem_tb"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Điểm trung bình theo cấp học (trả về Map<ten_cap_hoc, diem_trung_binh>)
    public Map<String, Double> getDiemTrungBinhTheoCapHoc() {
        Map<String, Double> result = new HashMap<>();
        String sql = "SELECT ch.ten_cap_hoc, AVG(bt.diem) AS diem_tb " +
                     "FROM bai_thi bt " +
                     "JOIN bo_cau_hoi bch ON bt.bo_cau_hoi_id = bch.bo_cau_hoi_id " +
                     "JOIN cap_hoc ch ON bch.cap_hoc_id = ch.cap_hoc_id " +
                     "GROUP BY ch.ten_cap_hoc";  // Sửa: Chỉ GROUP BY tên
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("ten_cap_hoc"), rs.getDouble("diem_tb"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Top người dùng theo từng môn học (trả về List<Object[]> với [ten_mon_hoc, ho_ten, diem_tb])
    public List<Object[]> getTopNguoiDungTheoMonHoc() {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT m.ten_mon_hoc, nd.ho_ten, AVG(bt.diem) AS diem_tb " +
                     "FROM bai_thi bt " +
                     "JOIN bo_cau_hoi bch ON bt.bo_cau_hoi_id = bch.bo_cau_hoi_id " +
                     "JOIN mon_hoc m ON bch.mon_hoc_id = m.mon_hoc_id " +
                     "JOIN nguoi_dung nd ON bt.nguoi_dung_id = nd.nguoi_dung_id " +
                     "GROUP BY m.mon_hoc_id, m.ten_mon_hoc, nd.nguoi_dung_id, nd.ho_ten " +
                     "ORDER BY m.ten_mon_hoc, diem_tb DESC";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Object[]{rs.getString("ten_mon_hoc"), rs.getString("ho_ten"), rs.getDouble("diem_tb")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    
    
    
    
    
    
}