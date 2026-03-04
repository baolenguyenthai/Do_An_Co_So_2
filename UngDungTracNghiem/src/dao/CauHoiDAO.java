package dao;

import database.KetNoiDB;
import model.CauHoi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CauHoiDAO {
    public int themCauHoi(CauHoi ch) {
        String sql = "INSERT INTO cau_hoi(bo_cau_hoi_id, noi_dung, muc_do) VALUES (?, ?, ?)";
        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ch.getBoCauHoiId());
            ps.setString(2, ch.getNoiDung());
            ps.setString(3, ch.getMucDo());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Phương thức mới: Lấy danh sách câu hỏi theo ID bộ câu hỏi
    public List<CauHoi> getByBoCauHoiId(int boId) {
        List<CauHoi> list = new ArrayList<>();
        String sql = "SELECT * FROM cau_hoi WHERE bo_cau_hoi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, boId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CauHoi ch = new CauHoi();
                ch.setCauHoiId(rs.getInt("cau_hoi_id"));
                ch.setBoCauHoiId(rs.getInt("bo_cau_hoi_id"));
                ch.setNoiDung(rs.getString("noi_dung"));
                ch.setMucDo(rs.getString("muc_do"));
                list.add(ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Phương thức mới: Xóa câu hỏi theo ID
    public boolean delete(int id) {
        String sql = "DELETE FROM cau_hoi WHERE cau_hoi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tạo bảng yêu thích nếu chưa có
    public void initFavoriteTable() {
        String sql = "CREATE TABLE IF NOT EXISTS cau_hoi_yeu_thich (" +
                "nguoi_dung_id INT, " +
                "cau_hoi_id INT, " +
                "PRIMARY KEY (nguoi_dung_id, cau_hoi_id), " +
                "FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(nguoi_dung_id), " +
                "FOREIGN KEY (cau_hoi_id) REFERENCES cau_hoi(cau_hoi_id) ON DELETE CASCADE)";
        try (Connection conn = KetNoiDB.ketNoi();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFavorite(int userId, int cauHoiId) {
        String sql = "SELECT 1 FROM cau_hoi_yeu_thich WHERE nguoi_dung_id = ? AND cau_hoi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, cauHoiId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean toggleFavorite(int userId, int cauHoiId) {
        if (isFavorite(userId, cauHoiId)) {
            String sql = "DELETE FROM cau_hoi_yeu_thich WHERE nguoi_dung_id = ? AND cau_hoi_id = ?";
            try (Connection conn = KetNoiDB.ketNoi();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setInt(2, cauHoiId);
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String sql = "INSERT INTO cau_hoi_yeu_thich (nguoi_dung_id, cau_hoi_id) VALUES (?, ?)";
            try (Connection conn = KetNoiDB.ketNoi();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setInt(2, cauHoiId);
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<CauHoi> getFavoriteQuestions(int userId, String subjectName, String gradeName) {
        List<CauHoi> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT ch.* FROM cau_hoi ch " +
                        "JOIN cau_hoi_yeu_thich yt ON ch.cau_hoi_id = yt.cau_hoi_id " +
                        "JOIN bo_cau_hoi bch ON ch.bo_cau_hoi_id = bch.bo_cau_hoi_id " +
                        "JOIN mon_hoc mh ON bch.mon_hoc_id = mh.mon_hoc_id " +
                        "JOIN cap_hoc choc ON bch.cap_hoc_id = choc.cap_hoc_id " +
                        "WHERE yt.nguoi_dung_id = ?");

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (subjectName != null && !subjectName.equals("Tất cả")) {
            sql.append(" AND mh.ten_mon_hoc = ?");
            params.add(subjectName);
        }
        if (gradeName != null && !gradeName.equals("Tất cả")) {
            sql.append(" AND choc.ten_cap_hoc = ?");
            params.add(gradeName);
        }

        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CauHoi ch = new CauHoi();
                ch.setCauHoiId(rs.getInt("cau_hoi_id"));
                ch.setBoCauHoiId(rs.getInt("bo_cau_hoi_id"));
                ch.setNoiDung(rs.getString("noi_dung"));
                ch.setMucDo(rs.getString("muc_do"));
                list.add(ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}