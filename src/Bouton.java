import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class Bouton extends Rectangle{

    private int num;

    public Bouton(int x1, int y1, int x2, int y2, int num) {
        super(x1, y1, x2, y2);
        this.num = num;
        this.setStroke(Color.BLUE);
        this.setFill(Color.LIGHTBLUE);
        this.setStrokeWidth(2);
    }

    public int getNum() {
        return num;

    }
    }
