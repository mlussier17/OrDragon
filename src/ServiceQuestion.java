import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import oracle.jdbc.OracleTypes;

import java.io.*;
import java.net.Socket;
import java.sql.*;
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

    private ChoiceDialog mDialogue;
    private final String mReponse[] = {};
    private List mListe;
    private static final String mTitre = "Sélectionné la bonne réponse ???";




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

            try {
                CONN = DriverManager.getConnection(url, user, pwd);
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
        catch (IOException IOE){

               // ResultSet Allo = CalSup.executeQuery();

                mDialogue = new ChoiceDialog(mListe.get(0),mListe);
                mDialogue.setTitle(mTitre);
                //mDialogue.setHeaderText(Allo.getString(1));

                Optional reponse = mDialogue.showAndWait();
                String choix = "Aucune réponse";


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
