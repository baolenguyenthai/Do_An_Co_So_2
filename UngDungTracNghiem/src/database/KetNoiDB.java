package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class KetNoiDB {

    public static Connection ketNoi() {
        try {
            String url = "jdbc:mysql://mysql-22383302-lenguyenthaibao.g.aivencloud.com:19341/tracnghiem"
                    + "?sslMode=REQUIRED&serverTimezone=Asia/Ho_Chi_Minh";

            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASS");
            if (user == null || user.isBlank() || pass == null || pass.isBlank()) {
                throw new IllegalStateException("Thiếu DB_USER/DB_PASS trong biến môi trường.");
            }

            return DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
