package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class KetNoiDB {
    private static final String DB_URL = "jdbc:mysql://mysql-22383302-lenguyenthaibao.g.aivencloud.com:19341/tracnghiem"
            + "?sslMode=REQUIRED&serverTimezone=Asia/Ho_Chi_Minh";
    private static final String HARDCODED_DB_USER = "avnadmin";
    private static final String HARDCODED_DB_PASS = "dien_password_aiven_o_day";

    public static Connection ketNoi() {
        try {
            String user = HARDCODED_DB_USER;
            String pass = HARDCODED_DB_PASS;
            if (user == null || user.isBlank() || pass == null || pass.isBlank()
                    || user.contains("dien_user_aiven_o_day")
                    || pass.contains("dien_password_aiven_o_day")) {
                throw new IllegalStateException("Hãy điền HARDCODED_DB_USER/HARDCODED_DB_PASS trong KetNoiDB.java");
            }

            return DriverManager.getConnection(DB_URL, user, pass);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
