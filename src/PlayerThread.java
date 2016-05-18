import exceptions.InvalidMoveException;
import javafx.scene.control.ChoiceDialog;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by 196128636 on 2016-05-13.
 */

public class PlayerThread implements Runnable{
    Socket pSocket;
    Timer timer = new Timer(true);
    boolean run = true;
    public ArrayBlockingQueue<Job> jobs = new ArrayBlockingQueue<Job>( 10 );

    private ChoiceDialog mDialogue;
    private final ArrayList<String> mReponse = new ArrayList<>();
    private static final String mTitre = "Sélectionné la bonne réponse ???";

    public synchronized void run() {
         try {
             InetSocketAddress adress = new InetSocketAddress(Reader.IP, 51007);
             pSocket = new Socket();
             pSocket.connect(adress);

             BufferedReader posReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
             PrintWriter write = new PrintWriter(new OutputStreamWriter(pSocket.getOutputStream()));

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

             timer.scheduleAtFixedRate(new TimerTask() {
                 @Override
                 public void run() {
                     Job job = new Job("NOOP");
                     JobThread jt = new JobThread(job, Gameboard.pobj);
                     Thread jThread = new Thread(jt);
                     jThread.setDaemon(true);
                     jThread.start();
                 }
             }, 25000, 25000);

             while (run) {
                 Job currentJob = jobs.take();

                 System.out.println("TCP Command -> " + currentJob.getCommand());

                 write.println(currentJob.getCommand());
                 write.flush();

                 currentJob.setResponse(posReader.readLine());

                 System.out.println("TCP Response -> " + currentJob.getResponse());

                    if(currentJob.getResponse().startsWith("IP"))
                        question(currentJob.getResponse());

                 currentJob.done();
             }

         } catch (InterruptedException ie) {

         } catch (IOException ex) {

         }
    }

    private void question(String ip){
        try {
            String[] tokens = ip.split(" ");
            InetSocketAddress clientAdress = new InetSocketAddress(tokens[1], 1666);
            Socket client = new Socket();
            client.connect(clientAdress);

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter write = new PrintWriter(new OutputStreamWriter(pSocket.getOutputStream()));

            write.println(Gameboard.TEAM);
            write.flush();

            String line = null;
                while ((line = reader.readLine()) != null) {
                    mReponse.add(line);
                }
            //TODO GET QUESTION ET REPONSE

            mDialogue = new ChoiceDialog(mReponse.get(0),mReponse);
            mDialogue.setTitle(mTitre);
            mDialogue.setHeaderText(mReponse.get(0));

            Optional reponse = mDialogue.showAndWait();
            String choix = reponse.get().toString();
            System.out.println(choix);
            write.println(choix);
            //ICI QUI VA LE DIALOGUE
        }
        catch(IOException ioe){
        ioe.printStackTrace();
    }


    }

}
