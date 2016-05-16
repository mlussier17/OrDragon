import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
/**
 * Created by Math on 2016-12-04.
 */
public class Connexion implements Runnable{
    final static private String IP = "149.56.47.97";
    final static private int PORT = 51005;
    private static Socket socket;

    public void run(){
        connect();
        Reader.node(socket);
        disconnect();
    }

    private void connect(){
        try{
            InetSocketAddress adress = new InetSocketAddress(IP,PORT);
            socket = new Socket();
            socket.connect(adress);
        }
        catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    public void disconnect(){
        try{
            socket.close();
        }
        catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
}
