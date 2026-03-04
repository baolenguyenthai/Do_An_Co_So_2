package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class KetNoiDB {

    public static Connection ketNoi() {
        try {
            String url = "jdbc:mysql://localhost:3307/trac_nghiem"
                       + "?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh";
            String user = "root";
            String pass = ""; // macOS thường để trống
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

