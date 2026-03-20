package model;

import java.util.Date;

public class BoCauHoi {

    private int boCauHoiId;
    private String tenBoCauHoi;
    private String moTa;
    private int capHocId;
    private int monHocId;
    private int nguoiTao;
    private String trangThai;
    private boolean congKhai;
    private Date ngayTao;

    // ===== GETTER & SETTER =====

    public int getBoCauHoiId() {
        return boCauHoiId;
    }

    public void setBoCauHoiId(int boCauHoiId) {
        this.boCauHoiId = boCauHoiId;
    }

    public String getTenBoCauHoi() {
        return tenBoCauHoi;
    }

    public void setTenBoCauHoi(String tenBoCauHoi) {
        this.tenBoCauHoi = tenBoCauHoi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getCapHocId() {
        return capHocId;
    }

    public void setCapHocId(int capHocId) {
        this.capHocId = capHocId;
    }

    public int getMonHocId() {
        return monHocId;
    }

    public void setMonHocId(int monHocId) {
        this.monHocId = monHocId;
    }

    public int getNguoiTao() {
        return nguoiTao;
    }

    public void setNguoiTao(int nguoiTao) {
        this.nguoiTao = nguoiTao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public boolean isCongKhai() {
        return congKhai;
    }

    public void setCongKhai(boolean congKhai) {
        this.congKhai = congKhai;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }
}
