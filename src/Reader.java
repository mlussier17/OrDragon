import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Math on 2016-12-04.
 */
public class Reader {
    private static BufferedReader reader;
    private static String line = null;
    private static ArrayList<Noeud> nodeList;

    public static void node(Socket socket){
        try {
            nodeList = new ArrayList();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((line = reader.readLine()) != null){
                if (line.trim().length() < 1) break;
                String[] tokens = line.split(" ");
                System.out.println(line);
                Noeud node = new Noeud(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]));
                nodeList.add(node);
            }
            reader.close();
        }
        catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }
    public ArrayList getList(){return nodeList;}
}
