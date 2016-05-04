import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Math on 2016-12-04.
 */
public class NodeReader {
    private static BufferedReader reader;
    private static PrintWriter writer;
    private static String line = null;
    private static File projectPath;
    private static String currentDirectory = null;
    private static String coordinatesFile = "\\AppData\\Coordinates.txt";

    public static void node(Socket socket){
        try {
            projectPath = new File(".");
            currentDirectory = projectPath.getCanonicalPath() + coordinatesFile;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new BufferedWriter(new FileWriter(currentDirectory)));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                writer.println(line);
            }
            reader.close();
        }
        catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
        finally{
            writer.close();
        }
    }
}
