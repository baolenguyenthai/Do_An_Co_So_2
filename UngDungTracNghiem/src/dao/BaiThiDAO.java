package dao;

import database.KetNoiDB;
import model.BaiThi;
import model.ChiTietBaiThi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm chi tiết bài thi
    public boolean insertChiTiet(ChiTietBaiThi ct) {
        String sql = "INSERT INTO chi_tiet_bai_thi (bai_thi_id, cau_hoi_id, dap_an_da_chon, dung) VALUES (?, ?, ?, ?)";
        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getBaiThiId());
            ps.setInt(2, ct.getCauHoiId());
            if (ct.getDapAnDaChon() != null)
                ps.setInt(3, ct.getDapAnDaChon());
            else
                ps.setNull(3, Types.INTEGER);
            if (ct.getDung() != null)
                ps.setBoolean(4, ct.getDung());
            else
                ps.setNull(4, Types.BOOLEAN);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (rs.next())
                return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy lịch sử làm bài của một người dùng
    public List<Object[]> getLichSuTheoNguoiDung(int nguoiDungId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT bt.bai_thi_id, bch.ten_bo_cau_hoi, m.ten_mon_hoc, ch.ten_cap_hoc, " +
                "bt.tong_cau, bt.so_cau_dung, bt.diem, bt.thoi_gian_bat_dau, bt.thoi_gian_ket_thuc, " +
                "TIMESTAMPDIFF(SECOND, bt.thoi_gian_bat_dau, bt.thoi_gian_ket_thuc) AS thoi_luong_giay, " +
                "bch.cong_khai " +
                "FROM bai_thi bt " +
                "JOIN bo_cau_hoi bch ON bt.bo_cau_hoi_id = bch.bo_cau_hoi_id " +
                "JOIN mon_hoc m ON bch.mon_hoc_id = m.mon_hoc_id " +
                "JOIN cap_hoc ch ON bch.cap_hoc_id = ch.cap_hoc_id " +
                "WHERE bt.nguoi_dung_id = ? " +
                "ORDER BY bt.thoi_gian_ket_thuc DESC, bt.bai_thi_id DESC";
        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nguoiDungId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[] {
                        rs.getInt("bai_thi_id"),
                        rs.getString("ten_bo_cau_hoi"),
                        rs.getString("ten_mon_hoc"),
                        rs.getString("ten_cap_hoc"),
                        rs.getInt("tong_cau"),
                        rs.getInt("so_cau_dung"),
                        rs.getFloat("diem"),
                        rs.getTimestamp("thoi_gian_bat_dau"),
                        rs.getTimestamp("thoi_gian_ket_thuc"),
                        rs.getInt("thoi_luong_giay"),
                        rs.getBoolean("cong_khai")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy chi tiết câu hỏi và đáp án cho một bài thi
    public List<Object[]> getChiTietKetQua(int baiThiId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT ch.noi_dung AS cau_hoi, " +
                "da_chon.noi_dung AS dap_an_da_chon, " +
                "da_dung.noi_dung AS dap_an_dung, " +
                "ct.dung " +
                "FROM chi_tiet_bai_thi ct " +
                "JOIN cau_hoi ch ON ct.cau_hoi_id = ch.cau_hoi_id " +
                "LEFT JOIN dap_an da_chon ON ct.dap_an_da_chon = da_chon.dap_an_id " +
                "LEFT JOIN dap_an da_dung ON ch.cau_hoi_id = da_dung.cau_hoi_id AND da_dung.dung = 1 " +
                "WHERE ct.bai_thi_id = ? " +
                "ORDER BY ct.cau_hoi_id ASC";
        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, baiThiId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[] {
                        rs.getString("cau_hoi"),
                        rs.getString("dap_an_da_chon"),
                        rs.getString("dap_an_dung"),
                        rs.getBoolean("dung")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy bảng xếp hạng
    public List<Object[]> getBangXepHang(String tenNguoiDung, String tenMonHoc, String tenCapHoc) {
        List<Object[]> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT nd.ho_ten, mh.ten_mon_hoc, ch.ten_cap_hoc, bt.diem, bt.so_cau_dung, bt.tong_cau, bt.thoi_gian_ket_thuc "
                        +
                        "FROM bai_thi bt " +
                        "JOIN nguoi_dung nd ON bt.nguoi_dung_id = nd.nguoi_dung_id " +
                        "JOIN bo_cau_hoi bch ON bt.bo_cau_hoi_id = bch.bo_cau_hoi_id " +
                        "JOIN mon_hoc mh ON bch.mon_hoc_id = mh.mon_hoc_id " +
                        "JOIN cap_hoc ch ON bch.cap_hoc_id = ch.cap_hoc_id " +
                        "WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (tenNguoiDung != null && !tenNguoiDung.trim().isEmpty()) {
            sql.append(" AND nd.ho_ten LIKE ?");
            params.add("%" + tenNguoiDung + "%");
        }
        if (tenMonHoc != null && !tenMonHoc.equals("Tất cả")) {
            sql.append(" AND mh.ten_mon_hoc = ?");
            params.add(tenMonHoc);
        }
        if (tenCapHoc != null && !tenCapHoc.equals("Tất cả")) {
            sql.append(" AND ch.ten_cap_hoc = ?");
            params.add(tenCapHoc);
        }

        sql.append(" ORDER BY bt.diem DESC, bt.thoi_gian_ket_thuc ASC");

        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                list.add(new Object[] {
                        rank++,
                        rs.getString("ho_ten"),
                        rs.getString("ten_mon_hoc"),
                        rs.getString("ten_cap_hoc"),
                        rs.getFloat("diem"),
                        rs.getInt("so_cau_dung") + "/" + rs.getInt("tong_cau"),
                        rs.getTimestamp("thoi_gian_ket_thuc")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy thống kê tổng quan (cho Admin)
    public Object[] getStatistics() {
        String sql = "SELECT COUNT(*) as total_exams, " +
                "AVG(diem) as avg_score, " +
                "(SELECT ho_ten FROM nguoi_dung nd JOIN bai_thi bt ON nd.nguoi_dung_id = bt.nguoi_dung_id GROUP BY nd.nguoi_dung_id ORDER BY COUNT(*) DESC LIMIT 1) as top_user, "
                +
                "(SELECT m.ten_mon_hoc FROM mon_hoc m JOIN bo_cau_hoi bch ON m.mon_hoc_id = bch.mon_hoc_id JOIN bai_thi bt ON bch.bo_cau_hoi_id = bt.bo_cau_hoi_id GROUP BY m.mon_hoc_id ORDER BY COUNT(*) DESC LIMIT 1) as top_subject "
                +
                "FROM bai_thi";
        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Object[] {
                        rs.getInt("total_exams"),
                        rs.getFloat("avg_score"),
                        rs.getString("top_user"),
                        rs.getString("top_subject")
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[] { 0, 0.0f, "N/A", "N/A" };
    }

    // Lấy thống kê chi tiết theo môn học
    public List<Object[]> getStatisticsBySubject() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT m.ten_mon_hoc, COUNT(*) as so_luot_thi, AVG(bt.diem) as diem_tb " +
                "FROM mon_hoc m " +
                "JOIN bo_cau_hoi bch ON m.mon_hoc_id = bch.mon_hoc_id " +
                "JOIN bai_thi bt ON bch.bo_cau_hoi_id = bt.bo_cau_hoi_id " +
                "GROUP BY m.mon_hoc_id " +
                "ORDER BY so_luot_thi DESC";
        try (Connection conn = KetNoiDB.ketNoi();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[] {
                        rs.getString("ten_mon_hoc"),
                        rs.getInt("so_luot_thi"),
                        rs.getFloat("diem_tb")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
