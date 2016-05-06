import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Math on 2016-12-04.
 */
public class Reader {
    private static BufferedReader reader;
    private static String line = null;


    public static void node(Socket socket){
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((line = reader.readLine()) != null){
                if (line.trim().length() < 1) break;
                String[] tokens = line.split(" ");
                System.out.println(line);
                Noeud node = new Noeud(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]));
                Noeud.add(node);
            }
            lines(reader);
        }
        catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }

    public static void lines(BufferedReader reader){
        try {
            System.out.println(line);
            while((line = reader.readLine()) != null){
                String[] tokens = line.split(" ");
                    for (int i = 1; i < tokens.length; ++i) {
                        System.out.println(tokens[i]);
                        Chemin line = new Chemin(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[i]));
                        Chemin.add(line);
                    }
            }
            reader.close();
        }
        catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
    }

}
