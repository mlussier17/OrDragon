import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

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

    final static private String IP = "149.56.47.97";
    final static private int PORT = 51006;
    private static Socket pSocket;

    class foobar implements Runnable {
        Circle piece;
        foobar(Circle cercle){
            piece = cercle;
        }
        public void run(){
            System.out.println("Poil de poche");
            piece.setFill(Color.YELLOW);
        }
    }
    public void run(){
        try {
            InetSocketAddress adress = new InetSocketAddress(IP, PORT);
            pSocket = new Socket();
            pSocket.connect(adress);
            BufferedReader posReader = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
            PrintWriter write = new PrintWriter(new OutputStreamWriter(pSocket.getOutputStream()));
            ArrayList<Noeud> nodes = Noeud.getList();
            while(true){
                String[] tokens = (line = posReader.readLine()).split(" ");
                for (int i = 0; i < tokens.length; ++i){
                    String[] deplacement = tokens[i].split(":");
                    System.out.println(deplacement[0] + "     " + deplacement[1]);
                    if(deplacement[1].equals("P")) {
                        Circle piece = nodes.get(Integer.parseInt(deplacement[0]));
                        System.out.println(piece);
                        Platform.runLater(() -> piece.setFill(Color.YELLOW));
                        //Platform.runLater(new foobar(piece));
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ie) {}

                        System.out.println(nodes.get(Integer.parseInt(deplacement[0])));
                    }
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
