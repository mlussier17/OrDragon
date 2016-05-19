import java.io.*;
import java.net.Socket;
import java.sql.*;

/**
 * Created by 201078339 on 2016-05-13.
 */
public class ServiceQuestion implements Runnable{
    private Socket s;
    private BufferedReader reader;
    private PrintWriter writer;

    ServiceQuestion(Socket soc){
        s = soc;
    }

    @Override
    public void run() {
        try {

            String url = "jdbc:oracle:thin:@mercure.clg.qc.ca:1521:orcl";
            Connection CONN = null;

            try {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                System.out.println("Driver charger");
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }
            try {

                try {
                    CONN = DriverManager.getConnection(url, Database.getUsername(), Database.getPassword());
                    System.out.println("Connecte");

                    PreparedStatement pst = null;
                } catch (SQLException se) {
                    System.out.println(se.getMessage());
                }

                reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

                //CallableStatement CalSup = CONN.prepareCall();
                writer.print("hello");
                writer.flush();
            }
            catch (IOException IOE) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
