package controller;

import view.FrmDangNhap;

public class MainApp {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new FrmDangNhap().setVisible(true);
        });
    }
}
// hihi