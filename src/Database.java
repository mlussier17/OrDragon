import oracle.jdbc.OracleTypes;
import java.sql.*;

/**
 * Created by 201127412 on 2016-05-16.
 */
public class Database {
    public static String url = "jdbc:oracle:thin:@mercure.clg.qc.ca:1521:orcl";
    private static String username = "LESDIEUXGREC";
    private static String password = "SoccerSuck";
    static private Database db = null;
    private static CallableStatement stm;
    private static PreparedStatement pstm;
    private static Connection con;

    public static Question getQuestion(short difficulty){

        Question question = new Question();

         con = getConnection();
        try {
            stm = con.prepareCall("{? = call PKGCLIENTS.LISTER}");
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.execute();
        } catch (SQLException sqlex) {
            // TODO
        }

        return question;
    }

    public static Connection getConnection() {
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

    public static void resetStats(){
        try {
            stm = getConnection().prepareCall("{call PLAYERSPKG.RESETAVOIRJOUEURS}");
            stm.execute();
        }
        catch (SQLException sqle){

        }
    }

    public static Boolean payDoritos(){
        try {
            stm = getConnection().prepareCall("{? = call PLAYERSPKG.GETAVOIRTEAM}");
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.execute();
            ResultSet rst = (ResultSet) stm.getObject(1);
            rst.next();
            int doritos = rst.getInt("nbdoritos");
            int capital = rst.getInt("capital");
            if (doritos >= 1){
                stm = getConnection().prepareCall("{call PLAYERSPKG.PAYERDORITOS}");
                stm.execute();
                return true;
            } else if(capital >= 2){
                stm = getConnection().prepareCall("{call PLAYERSPKG.PAYERCAPITAL(2)}");
                stm.execute();
                return true;
            }
        }
        catch (SQLException sqle){

        }
        return false;
    }

    public static Boolean payDew(){
        try {
            stm = getConnection().prepareCall("{? = call PLAYERSPKG.GETAVOIRTEAM}");
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.execute();
            ResultSet rst = (ResultSet) stm.getObject(1);
            rst.next();
            int dew = rst.getInt("nbmountaindew");
            int capital = rst.getInt("capital");
            if (dew >= 1){
                stm = getConnection().prepareCall("{call PLAYERSPKG.PAYERDEW}");
                stm.execute();
                return true;
            } else if(capital >= 2){
                stm = getConnection().prepareCall("{call PLAYERSPKG.PAYERCAPITAL(2)");
                stm.execute();
                return true;
            }
        }
        catch (SQLException sqle){

        }
        return false;
    }

    public static Boolean payAuberge() {
        try {
            stm = getConnection().prepareCall("{? = call PLAYERSPKG.GETAVOIRTEAM}");
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.execute();
            ResultSet rst = (ResultSet) stm.getObject(1);
            rst.next();
            int capital = rst.getInt("capital");
            if (capital >= 3)return true;
        }
        catch (SQLException sqle) {}
        return false;
    }

    public static void pay() {
        try {
            stm = getConnection().prepareCall("{call PLAYERSPKG.PAYERCAPITAL(3)}");
            stm.execute();
        }
        catch(SQLException sqle){}
    }
}
