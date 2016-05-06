import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * Created by 196128636 on 2016-05-05.
 */
public class Chemin extends Line {
    private int source;
    private int dest;
    private static ArrayList<Chemin> lines = new ArrayList();

    Chemin(int start, int end){
        super();
        source = start;
        dest = end;
    }

    public static void add(Chemin line){
        lines.add(line);
    }

    public static ArrayList getList(){return lines;}
    public int getStart(){return source;}
    public int getDest(){return dest;}

}
