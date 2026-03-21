
package model;

/**
 *
 * @author lebao
 */

public class NguoiDung {
    private int nguoiDungId;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String email;
    private int vaiTroId;
    private int trangThai;

    // getter & setter

    public int getNguoiDungId() {
        return nguoiDungId;
    }

    public void setNguoiDungId(int nguoiDungId) {
        this.nguoiDungId = nguoiDungId;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVaiTroId() {
        return vaiTroId;
    }

    public void setVaiTroId(int vaiTroId) {
        this.vaiTroId = vaiTroId;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}

