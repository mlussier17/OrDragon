import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class Bouton extends Button{

    private int num;

    public Bouton(int x1, int y1,String text, int num) {

        super(text);
        this.num = num;
        this.setLayoutX(x1);
        this.setLayoutY(y1);
    }

    public int getNum() {
        return num;

    }
}
