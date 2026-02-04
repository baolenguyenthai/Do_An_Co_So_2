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
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
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
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Phương thức mới: Xóa câu hỏi theo ID
    public boolean delete(int id) {
        String sql = "DELETE FROM cau_hoi WHERE cau_hoi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}