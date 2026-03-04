package model;

public class CapHoc {
    private int capHocId;
    private String tenCapHoc;

    // Getters và Setters
    public int getCapHocId() {
        return capHocId;
    }

    public void setCapHocId(int capHocId) {
        this.capHocId = capHocId;
    }

    public String getTenCapHoc() {
        return tenCapHoc;
    }

    public void setTenCapHoc(String tenCapHoc) {
        this.tenCapHoc = tenCapHoc;
    }

    @Override
    public String toString() {
        return tenCapHoc; // Để hiển thị trong JComboBox
    }
}