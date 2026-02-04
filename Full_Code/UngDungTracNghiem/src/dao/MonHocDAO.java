package dao;

import database.KetNoiDB;
import model.MonHoc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonHocDAO {
    public List<MonHoc> getAll() {
        List<MonHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM mon_hoc";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MonHoc m = new MonHoc();
                m.setMonHocId(rs.getInt("mon_hoc_id"));
                m.setTenMonHoc(rs.getString("ten_mon_hoc"));
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public MonHoc getById(int id) {
        String sql = "SELECT * FROM mon_hoc WHERE mon_hoc_id = ?";
        try (
            Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MonHoc m = new MonHoc();
                m.setMonHocId(rs.getInt("mon_hoc_id"));
                m.setTenMonHoc(rs.getString("ten_mon_hoc"));
                return m;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public boolean insert(MonHoc m) {
        String sql = "INSERT INTO mon_hoc (ten_mon_hoc) VALUES (?)";
        try (
            Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, m.getTenMonHoc());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(MonHoc m) {
        String sql = "UPDATE mon_hoc SET ten_mon_hoc = ? WHERE mon_hoc_id = ?";
        try (
            Connection conn = KetNoiDB.ketNoi();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, m.getTenMonHoc());
            ps.setInt(2, m.getMonHocId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức mới: Xóa môn học theo ID
    public boolean delete(int id) {
        String sql = "DELETE FROM mon_hoc WHERE mon_hoc_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    
    
    // Tìm ID theo tên (trả về ID nếu có, -1 nếu không)
    public int getIdByTen(String tenMonHoc) {
        String sql = "SELECT mon_hoc_id FROM mon_hoc WHERE ten_mon_hoc = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenMonHoc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("mon_hoc_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Thêm môn học mới và trả về ID
    public int themMonHoc(String tenMonHoc) {
        String sql = "INSERT INTO mon_hoc(ten_mon_hoc) VALUES (?)";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tenMonHoc);
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
}