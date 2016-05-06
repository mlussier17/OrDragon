import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * Created by 196128636 on 2016-05-05.
 */
public class Noeud extends Circle{
    private int number;
    private Boolean construisible;
    private static ArrayList<Noeud> nodeList = new ArrayList();

    Noeud(int num, int x, int y, int constr){
        super(x, y, 5);
        number=num;
        if (constr == 1) construisible = true;
        else construisible = false;
    }

    public static void add(Noeud node){
        nodeList.add(node);
    }

    public int getNumber(){return number;}
    public Boolean getConstruisible(){return construisible;}
    public static ArrayList getList(){return nodeList;}

}
