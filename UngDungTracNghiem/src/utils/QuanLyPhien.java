package utils;

/**
 * Lớp quản lý phiên người dùng hiện tại.
 * @author lebao
 */
public class QuanLyPhien {
    private static int idNguoiDungHienTai = -1; // ID người dùng hiện tại
    private static String tenDangNhapHienTai = ""; // Tên đăng nhập
    private static String vaiTroHienTai = ""; // Vai trò người dùng hiện tại

    public static void datNguoiDungHienTai(int idNguoiDung, String tenDangNhap, String vaiTro) {
        idNguoiDungHienTai = idNguoiDung;
        tenDangNhapHienTai = tenDangNhap;
        vaiTroHienTai = vaiTro;
    }

    public static int layIdNguoiDungHienTai() {
        return idNguoiDungHienTai;
    }

    public static String layTenDangNhapHienTai() {
        return tenDangNhapHienTai;
    }

    public static String layVaiTro() {
        return vaiTroHienTai;
    }

    public static void dangXuat() {
        idNguoiDungHienTai = -1;
        tenDangNhapHienTai = "";
        vaiTroHienTai = "";
    }

    public static boolean daDangNhap() {
        return idNguoiDungHienTai != -1;
    }
}