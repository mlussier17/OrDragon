import javafx.application.Platform;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Math on 2016-12-04.
 */
public class Reader implements Runnable{
    private static BufferedReader reader;
    private static String line = null;

    final static public String IP = "149.56.47.97";
    final static private int PORT = 51006;
    private static Socket pSocket;


    public void run(){
        try {
            InetSocketAddress adress = new InetSocketAddress(IP, PORT);
            pSocket = new Socket();
            pSocket.connect(adress);
            BufferedReader posReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
            PrintWriter write = new PrintWriter(new OutputStreamWriter(pSocket.getOutputStream()));
            ArrayList<Noeud> nodes = Noeud.getList();

            while(true) {
                String[] tokens = (line = posReader.readLine()).split(" ");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (Noeud noeud : nodes) {
                            noeud.resetEntities();
                        } // Reset nodes;
                    }
                });
                for (int i = 0; i < tokens.length; ++i) {
                    String[] deplacement = tokens[i].split(":");

                    Noeud piece = nodes.get(Integer.parseInt(deplacement[0]));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            new Entity(deplacement[1], piece);
                        }
                    });
                }


                write.println("");
            }
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }

    }


    public static void node(Socket socket){
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((line = reader.readLine()) != null){
                if (line.trim().length() < 1) break;
                String[] tokens = line.split(" ");
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
