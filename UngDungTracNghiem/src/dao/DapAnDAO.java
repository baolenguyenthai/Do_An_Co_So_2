package dao;
import database.KetNoiDB;
import model.DapAn;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DapAnDAO {
    public void themDapAn(DapAn da) {
        String sql = "INSERT INTO dap_an(cau_hoi_id, noi_dung, dung) VALUES (?, ?, ?)";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, da.getCauHoiId());
            ps.setString(2, da.getNoiDung());
            ps.setInt(3, da.getDung());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Phương thức mới: Lấy danh sách đáp án theo ID câu hỏi
    public List<DapAn> getByCauHoiId(int cauHoiId) {
        List<DapAn> list = new ArrayList<>();
        String sql = "SELECT * FROM dap_an WHERE cau_hoi_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cauHoiId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DapAn da = new DapAn();
                da.setDapAnId(rs.getInt("dap_an_id"));
                da.setCauHoiId(rs.getInt("cau_hoi_id"));
                da.setNoiDung(rs.getString("noi_dung"));
                da.setDung(rs.getInt("dung"));
                list.add(da);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Phương thức mới: Xóa đáp án theo ID
    public boolean delete(int id) {
        String sql = "DELETE FROM dap_an WHERE dap_an_id = ?";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}