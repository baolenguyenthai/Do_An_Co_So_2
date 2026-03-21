package model;

public class MonHoc {
    private int monHocId;
    private String tenMonHoc;

    // Getters và Setters
    public int getMonHocId() {
        return monHocId;
    }

    public void setMonHocId(int monHocId) {
        this.monHocId = monHocId;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    @Override
    public String toString() {
        return tenMonHoc; // Để hiển thị trong JComboBox
    }
}