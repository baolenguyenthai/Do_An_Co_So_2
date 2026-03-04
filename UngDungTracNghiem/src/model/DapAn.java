package model;

public class DapAn {
    private int dapAnId;
    private int cauHoiId;
    private String noiDung;
    private int dung;  // 0: Sai, 1: Đúng

    // Constructor mặc định
    public DapAn() {}

    // Constructor có tham số
    public DapAn(int dapAnId, int cauHoiId, String noiDung, int dung) {
        this.dapAnId = dapAnId;
        this.cauHoiId = cauHoiId;
        this.noiDung = noiDung;
        this.dung = dung;
    }

    // Getter & Setter
    public int getDapAnId() {
        return dapAnId;
    }

    public void setDapAnId(int dapAnId) {
        this.dapAnId = dapAnId;
    }

    public int getCauHoiId() {
        return cauHoiId;
    }

    public void setCauHoiId(int cauHoiId) {
        this.cauHoiId = cauHoiId;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public int getDung() {
        return dung;
    }

    public void setDung(int dung) {
        this.dung = dung;
    }

    // toString (tùy chọn)
    @Override
    public String toString() {
        return "DapAn{" +
                "dapAnId=" + dapAnId +
                ", cauHoiId=" + cauHoiId +
                ", noiDung='" + noiDung + '\'' +
                ", dung=" + dung +
                '}';
    }
}