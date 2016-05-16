import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 201127412 on 2016-05-16.
 */
public class Database {
    final public static String url = "jdbc:myDriver:myDatabase";
    final public static String username = "LESDIEUXGREC";
    final public static String password = "SoccerSuck";
    static private Database db = null;

    public static Question getQuestion(short difficulty){

    }

    private static Connection getConnection() {
        try {

            Connection con = DriverManager.getConnection(url, username, password);
        } catch(SQLException ex) {

        }
    }
}
