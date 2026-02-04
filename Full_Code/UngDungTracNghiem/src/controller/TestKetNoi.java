package controller;

import database.KetNoiDB;
import java.sql.Connection;

public class TestKetNoi {
    public static void main(String[] args) {
        Connection conn = KetNoiDB.ketNoi();
        if (conn != null) {
            System.out.println("Ket noi MySQL thanh cong!");
        } else {
            System.out.println("Ket noi that bai!");
        }
    }
}
