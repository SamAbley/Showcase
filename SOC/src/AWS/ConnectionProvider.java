package AWS;

import java.sql.Connection;
import java.sql.DriverManager;

import static AWS.AWSConstants.*;

public class ConnectionProvider {
    private static Connection con = null;

    static {
        try {
            con = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
        } catch (Exception e) {
        }
    }

    public static Connection getCon() {
        return con;
    }
}
