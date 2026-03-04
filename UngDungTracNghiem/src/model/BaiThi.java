package model;

import java.util.Date;

public class BaiThi {
    private int baiThiId;
    private int nguoiDungId;
    private int boCauHoiId;
    private int tongCau;
    private int soCauDung;
    private float diem;
    private Date thoiGianBatDau;
    private Date thoiGianKetThuc;

    // Getters và Setters
    public int getBaiThiId() { return baiThiId; }
    public void setBaiThiId(int baiThiId) { this.baiThiId = baiThiId; }

    public int getNguoiDungId() { return nguoiDungId; }
    public void setNguoiDungId(int nguoiDungId) { this.nguoiDungId = nguoiDungId; }

    public int getBoCauHoiId() { return boCauHoiId; }
    public void setBoCauHoiId(int boCauHoiId) { this.boCauHoiId = boCauHoiId; }

    public int getTongCau() { return tongCau; }
    public void setTongCau(int tongCau) { this.tongCau = tongCau; }

    public int getSoCauDung() { return soCauDung; }
    public void setSoCauDung(int soCauDung) { this.soCauDung = soCauDung; }

    public float getDiem() { return diem; }
    public void setDiem(float diem) { this.diem = diem; }

    public Date getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(Date thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public Date getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(Date thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }
}