package model;

public class ChiTietBaiThi {
    private int baiThiId;
    private int cauHoiId;
    private Integer dapAnDaChon; // Có thể null nếu chưa chọn
    private Boolean dung; // null nếu chưa chấm

    // Getters và Setters
    public int getBaiThiId() { return baiThiId; }
    public void setBaiThiId(int baiThiId) { this.baiThiId = baiThiId; }

    public int getCauHoiId() { return cauHoiId; }
    public void setCauHoiId(int cauHoiId) { this.cauHoiId = cauHoiId; }

    public Integer getDapAnDaChon() { return dapAnDaChon; }
    public void setDapAnDaChon(Integer dapAnDaChon) { this.dapAnDaChon = dapAnDaChon; }

    public Boolean getDung() { return dung; }
    public void setDung(Boolean dung) { this.dung = dung; }
}