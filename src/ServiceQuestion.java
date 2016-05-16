import java.io.*;
import java.net.Socket;

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
        try{

            String url = "jdbc:oracle:thin:@mercure.clg.qc.ca:1521:orcl";
            String user = "L";
            String pwd = "ORACLE1";
            Connection CONN = null;

            try {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                System.out.println("Driver charger");
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }
            try {


                CONN = DriverManager.getConnection(url, user, pwd);
                System.out.println("Connecte");

                PreparedStatement pst = null;
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }

            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

            CallableStatement CalSup = CONN.prepareCall();
            writer.print("hello");
        }
        catch (IOException IOE){

        }
    }
}
