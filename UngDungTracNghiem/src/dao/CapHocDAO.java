package dao;

import database.KetNoiDB;
import model.CapHoc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CapHocDAO {
    public List<CapHoc> getAll() {
        List<CapHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM cap_hoc";
        try (Connection conn = KetNoiDB.ketNoi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CapHoc c = new CapHoc();
                c.setCapHocId(rs.getInt("cap_hoc_id"));
                c.setTenCapHoc(rs.getString("ten_cap_hoc"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}