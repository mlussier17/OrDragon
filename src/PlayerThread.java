import exceptions.InvalidMoveException;
import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by 196128636 on 2016-05-13.
 */

public class PlayerThread implements Runnable{
    Socket pSocket;
    Timer timer = new Timer(true);
    public static boolean run = true;
    public ArrayBlockingQueue<Job> jobs = new ArrayBlockingQueue<Job>( 10 );

    private ChoiceDialog mDialogue;
    private String question;
    private  ArrayList<String> mReponse = new ArrayList<>();
    private static final String mTitre = "Sélectionné la bonne réponse ???";
    private Connection conn = null;
    private BufferedReader posReader;
    private PrintWriter write;

    public synchronized void run() {
         try {
             InetSocketAddress adress = new InetSocketAddress(Reader.IP, 51007);
             pSocket = new Socket();
             pSocket.connect(adress);

             posReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
             write = new PrintWriter(new OutputStreamWriter(pSocket.getOutputStream()));

             // Send HELLO, it's me
             write.println("HELLO " + Gameboard.TEAM + " " + Gameboard.LOCALIP);
             write.flush();
             String tmprep = posReader.readLine();
             System.out.println("TCP Response -> " + tmprep);

             //Send NODE to get wich node we are on
             write.println("NODE");
             write.flush();
             String tmprep2 = posReader.readLine();
             System.out.println("TCP Response -> " + tmprep2);

//             if(run) {
//                 timer.scheduleAtFixedRate(new TimerTask() {
//                     @Override
//                     public void run() {
//                         Job job = new Job("NOOP");
//                         JobThread jt = new JobThread(job, Gameboard.pobj);
//                         Thread jThread = new Thread(jt);
//                         jThread.setDaemon(true);
//                         jThread.start();
//                     }
//                 }, 25000, 25000);
//             }

             while (run) {
                 Job currentJob = jobs.take();

                 System.out.println("TCP Command -> " + currentJob.getCommand());

                 write.println(currentJob.getCommand());
                 write.flush();

                 currentJob.setResponse(posReader.readLine());

                 System.out.println("TCP Response -> " + currentJob.getResponse());

                 if(currentJob.getResponse().startsWith("P")){
                     conn = Database.getConnection();
                     CallableStatement stm = conn.prepareCall("{call PLAYERSPKG.AUGMENTERCAPITAL}");
                     stm.execute();
                 }

                 if(currentJob.getResponse().startsWith("D")){
                     conn = Database.getConnection();
                     CallableStatement stm = conn.prepareCall("{call PLAYERSPKG.AUGMENTERDORITOS}");
                     stm.execute();
                 }

                 if(currentJob.getResponse().startsWith("M")){
                     conn = Database.getConnection();
                     CallableStatement stm = conn.prepareCall("{call PLAYERSPKG.AUGMENTERDEW}");
                     stm.execute();
                 }

                 if(currentJob.getResponse().startsWith("IP"))
                    Platform.runLater(new Runnable() {
                                          public void run() {
                                              question(currentJob.getResponse());
                                          }
                                      });

                 Platform.runLater(() -> {
                         Gameboard.UpdateStats();
                 });

                 currentJob.done();
             }


         } catch(NullPointerException npe){
             System.err.println(npe.getMessage());
         }
         catch (SQLException sqle){
             System.out.println(sqle.getMessage());

         }
         catch (InterruptedException ie) {

         } catch (IOException ex) {

         }
    }

    private void question(String ip){
        try {
            String[] tokens = ip.split(" ");
            InetSocketAddress clientAdress = new InetSocketAddress(tokens[1], 1666);
            Socket client = new Socket();
            client.connect(clientAdress);

            /*
            write.println("NODE");
            write.flush();
            String tmprep2 = posReader.readLine();
            */
            Job job = new Job("NODE");
            try {
                JobThread jt = new JobThread(job, Gameboard.pobj);
                Thread jThread = new Thread(jt);
                jThread.setDaemon(true);
                jThread.start();
                jThread.join();
            } catch (InterruptedException ie) {
                // TODO
            }

            BufferedReader posReaderClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writeClient = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

            writeClient.println(Gameboard.TEAM + " " + job.getResponse());
            System.out.println(Gameboard.TEAM + " " +  job.getResponse());
            writeClient.flush();

            question = posReaderClient.readLine();
            mReponse.add(posReaderClient.readLine());
            mReponse.add(posReaderClient.readLine());
            mReponse.add(posReaderClient.readLine());
            mReponse.add(posReaderClient.readLine());

            //TODO GET QUESTION ET REPONSE

            mDialogue = new ChoiceDialog(mReponse.get(0),mReponse);
            mDialogue.setTitle(mTitre);
            mDialogue.setContentText(question);
            mDialogue.setHeaderText(question);

            Optional<String> reponse = mDialogue.showAndWait();
            if(reponse.isPresent()){
                for (int i = 0; i < mReponse.size(); ++i) {
                    if (mDialogue.getSelectedItem().equals(mReponse.get(i))){
                        System.out.println(i +1);
                        writeClient.println(i +1);
                        writeClient.flush();
                    }
                }
            }
            mReponse.clear();
            question = null;
            System.out.println( "Answer from question : " + posReaderClient.readLine());

        }
            catch(IOException ioe){
            ioe.printStackTrace();
        }


    }

}
