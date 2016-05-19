import oracle.jdbc.OracleTypes;
import java.sql.*;

/**
 * Created by 201127412 on 2016-05-16.
 */
public class Database {
    public static String url = "jdbc:myDriver:myDatabase";
    private static String username = "LESDIEUXGREC";
    private static String password = "SoccerSuck";
    static private Database db = null;

    public static Question getQuestion(short difficulty){
        CallableStatement stm;
        PreparedStatement pstm;
        Question question = new Question();

        Connection con = getConnection();
        try {
            stm = con.prepareCall("{? = call PKGCLIENTS.LISTER}");
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.execute();
        } catch (SQLException sqlex) {
            // TODO
        }

        return question;
    }

    private static Connection getConnection() {
        Connection con = null;
        try {
             con = DriverManager.getConnection(url, username, password);
        } catch(SQLException ex) {
            // TODO
        }
        return con;
    }
    public static String getUsername(){ return username;}
    public static String getPassword(){ return password;}
}
