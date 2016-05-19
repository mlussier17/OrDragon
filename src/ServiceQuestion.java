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
            System.out.println("Client connecte");
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

            //Get the id of the player
            String id = reader.readLine();
            System.out.println(id);

//            String url = "jdbc:oracle:thin:@mercure.clg.qc.ca:1521:orcl";
//            Connection CONN = null;

//            try {
//                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
//                System.out.println("Driver charger");
//            } catch (SQLException se) {
//                System.out.println(se.getMessage());
//            }
//            CONN = Database.getConnection();


            for (int i = 0; i < 5; ++i) {
                writer.println("hello");
                writer.flush();
            }
        }
        catch (IOException IOE) {

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
