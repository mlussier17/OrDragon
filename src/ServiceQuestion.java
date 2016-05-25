import oracle.jdbc.internal.OracleTypes;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;


import java.io.*;
import java.io.Reader;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
            String[] id = reader.readLine().split(" ");
            System.out.println(id[0]);

            String url = "jdbc:oracle:thin:@mercure.clg.qc.ca:1521:orcl";
            Connection CONN = null;

            try {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                System.out.println("Driver charger");
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }
            CONN = Database.getConnection();

            CallableStatement statementQuestion =CONN.prepareCall("{ ? = call QUESTIONSPKG.getQuestion(?)}");
            statementQuestion.registerOutParameter(1, oracle.jdbc.internal.OracleTypes.CURSOR);

            statementQuestion.setInt(2,1);
            statementQuestion.execute();

            ResultSet rest=(ResultSet) statementQuestion.getObject(1);

            String Question = null;
            boolean enonce=true;
            ArrayList<String> arrayReponse = new ArrayList<String>();
            int goodAnswer = 0;
            int j = 0;
            while(rest.next())
            {
                if(enonce) {
                    Question = rest.getObject(2).toString();
                    enonce = false;
                }
                if(rest.getInt(4)==1){
                    arrayReponse.add(rest.getObject(3).toString());
                    goodAnswer = j;
                }
                else
                    arrayReponse.add(rest.getObject(3).toString());
                ++j;
            }

            writer.println(Question);

            for(int i=0;i<arrayReponse.size();++i)
                writer.println(arrayReponse.get(i));
            writer.flush();

            int rep = Integer.parseInt(reader.readLine());
            System.out.println("Answer gotten : " + rep);

            if(!(rep == goodAnswer)){
                writer.println("ERR");
                CallableStatement stm = Database.getConnection().prepareCall("{call PLAYERSPKG.AUGMENTERCAPITAL}");
                stm.execute();
                stm.execute();
            }
            else {
                writer.println("OK");
            }
            writer.flush();

            Job job = new Job("UNLOCK " + id[0]);
            JobThread jt = new JobThread(job, Gameboard.pobj);
            Thread jThread = new Thread(jt);
            jThread.setDaemon(true);
            jThread.start();
            jThread.join();

            System.out.println("TCP Response : " + job.getResponse());
            reader.close();
            writer.close();
            s.close();

        }
        catch (IOException IOE) {

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
