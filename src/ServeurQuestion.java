import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 201078339 on 2016-05-13.
 */
public class ServeurQuestion implements Runnable{
    private ServerSocket server;
    final private int PORT = 1666;

    private boolean exist = false;

    public ServeurQuestion(){
        exist = true;
    }

    public void connect(){
        try{
            server = new ServerSocket(PORT);
        }
        catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    public void disconnect(){
        try{
            server.close();
            exist = false;
        }
        catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }


    @Override
    public void run() {
        connect();
        while(exist){
            try {
                Socket s = server.accept();
                ServiceQuestion c = new ServiceQuestion(s);
                Thread t = new Thread(c);
                t.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
