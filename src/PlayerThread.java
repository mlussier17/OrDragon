import exceptions.InvalidMoveException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by 196128636 on 2016-05-13.
 */

public class PlayerThread implements Runnable{
    Socket pSocket;
    Timer timer = new Timer();
    boolean run = true;
    public ArrayBlockingQueue<Job> jobs = new ArrayBlockingQueue<Job>( 10 );

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
             // Error hadling LATER PLZ
             String tmprep = posReader.readLine();
             System.out.println("TCP Response -> " + tmprep);

             /*timer.scheduleAtFixedRate(new TimerTask() {
                 @Override
                 public void run() {
                     Job job = new Job("NOOP");
                     JobThread jt = new JobThread(job, Gameboard.pobj);
                     Thread jThread = new Thread(jt);
                     jThread.setDaemon(true);
                     jThread.start();
                 }
             }, 25000, 25000);*/

             while (run) {
                 Job currentJob = jobs.take();

                 System.out.println("TCP Command -> " + currentJob.getCommand());

                 write.println(currentJob.getCommand());
                 write.flush();

                 currentJob.setResponse(posReader.readLine());

                 System.out.println("TCP Response -> " + currentJob.getResponse());

                 currentJob.done();
             }

         } catch (InterruptedException ie) {

         } catch (IOException ex) {

         }
    }

}
