import javafx.scene.shape.Circle;

/**
 * Created by 196128636 on 2016-05-05.
 */
public class Noeud extends Circle{
    private int number;
    private Boolean construisible;

    Noeud(int num, int x, int y, int constr){
        super(x, y, 5);
        number=num;
        if (constr == 1) construisible = true;
        else construisible = false;
    }

    public int getNumber(){return number;}
    public Boolean getConstruisible(){return construisible;}

}
