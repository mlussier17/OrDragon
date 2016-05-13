import exceptions.InvalidMoveException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by 196128636 on 2016-05-13.
 */

public class PlayerThread implements Runnable{
    Socket pSocket;
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

             while (run) {
                 Job currentJob = jobs.take();

                 write.println(currentJob.getCommand());
                 write.flush();

                 currentJob.setResponse(posReader.readLine());
                 currentJob.done();
             }

         } catch (InterruptedException ie) {

         } catch (IOException ex) {

         }
    }

}
