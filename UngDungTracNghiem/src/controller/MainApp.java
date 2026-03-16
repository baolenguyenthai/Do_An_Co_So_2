package controller;

import utils.UiEnhancer;
import view.FrmDangNhap;

public class MainApp {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            UiEnhancer.install();
            new FrmDangNhap().setVisible(true);
        });
    }
}
// hihi
