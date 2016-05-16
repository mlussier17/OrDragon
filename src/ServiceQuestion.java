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

    }
}
