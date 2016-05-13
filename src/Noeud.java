import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * Created by 196128636 on 2016-05-05.
 */
public class Noeud extends Circle{
    final static int RADIUS = 10;
    final static int PXIMAGE = 32;
    final static int IVMARGIN = 2;
    private int number;
    private Boolean construisible;
    private static ArrayList<Noeud> nodeList = new ArrayList<>();
    private ArrayList<ImageView> ivPlayer = new ArrayList<>();
    private Pane pane = new Pane();

    Noeud(int num, int x, int y, int constr){
        super(x, y, RADIUS);
        number=num;
        if (constr == 1) construisible = true;
        else construisible = false;

        setCursor(Cursor.HAND); // set cursor to hand when hover on noeud
        pane.setMouseTransparent(true); // Set panel so it doesn't catch mouse events

        // On button click
        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            System.out.println("J'ai clicker -> " + this.getNumber());

            Job job = new Job("GOTO " + getNumber());
            JobThread jt = new JobThread(job, Gameboard.pobj);
            Thread jThread = new Thread(jt);
            jThread.setDaemon(true);
            jThread.start();
        });

        addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            if (this.getConstruisible()) Platform.runLater(() -> this.setFill(Color.color(0,0.8,0)));
            else Platform.runLater(() -> this.setFill(Color.color(0.8, 0, 0)));
        });

        addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            if (this.getConstruisible()) Platform.runLater(() -> this.setFill(Color.GREEN));
            else Platform.runLater(() -> this.setFill(Color.RED));
        });

        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (this.getConstruisible()) Platform.runLater(() -> this.setFill(Color.color(0,0.8,0,0.8)));
            else Platform.runLater(() -> this.setFill(Color.color(0.8, 0, 0, 0.8)));
        });

        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (this.getConstruisible()) Platform.runLater(() -> this.setFill(Color.color(0,0.8,0,1)));
            else Platform.runLater(() -> this.setFill(Color.color(0.8, 0, 0, 1)));
        });
    }

    public static void add(Noeud node){
        nodeList.add(node);
    }

    // Add entity to pane
    public void addEntity(Entity entity) {
        ImageView iv = new ImageView();
        iv.setImage(entity.getImage());

        // ITEMS
        if (entity.getName().equals("M") || entity.getName().equals("D") || entity.getName().equals("P")) {
            iv.setX(getCenterX() + RADIUS + IVMARGIN);
            iv.setY(getCenterY() - RADIUS);
        }
        // BATIMENTS
        else if (entity.getName().equals("A") || entity.getName().equals("N") || entity.getName().equals("C")) {
            iv.setX(getCenterX() - iv.getImage().getWidth() / 2);
            iv.setY(getCenterY() - iv.getImage().getHeight() / 2);
        }
        // MOBS
        else if (entity.getName().equals("T") || entity.getName().equals("G")) {
            iv.setX(getCenterX() - RADIUS - IVMARGIN - iv.getImage().getWidth());
            iv.setY(getCenterY() - RADIUS);
        }
        // PLAYERS
        else {
            ivPlayer.add(iv);
            if (ivPlayer.size() == 1) {
                iv.setX(getCenterX() - iv.getImage().getWidth() / 2);
                iv.setY(getCenterY() - RADIUS - IVMARGIN - iv.getImage().getHeight());
            }
            else {
                int width = (int)iv.getImage().getWidth() * ivPlayer.size() + ivPlayer.size() + 1;

                for (int i = 0; i < ivPlayer.size(); i++){
                    ivPlayer.get(i).setX((getCenterX() - (width / 2)) + ((i + 1)* IVMARGIN) + (i * iv.getImage().getHeight()));
                    ivPlayer.get(i).setY(getCenterY() - RADIUS - IVMARGIN - iv.getImage().getHeight());
                }
            }
        }

        pane.getChildren().add(iv);
    }

    public void resetEntities() {
        ivPlayer.clear();
        pane.getChildren().clear();
    }

    public int getNumber(){return number;}
    public Boolean getConstruisible(){return construisible;}
    public static ArrayList getList(){return nodeList;}
    public Pane getPane() {return pane;}
}
