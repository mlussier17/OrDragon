/**
 * Created by Math on 2016-12-04.
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Gameboard extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Connexion connexion = new Connexion();
        Thread tServ = new Thread(connexion);
        tServ.start();
        Group group = new Group();
        ArrayList<Noeud> nodes = Noeud.getList();
        for (int i = 0; i < nodes.size(); ++i){
            Circle cercle = new Circle(nodes.get(i).getCenterX(),nodes.get(i).getCenterY(),nodes.get(i).getRadius());
            cercle.setStroke(Color.BLACK);
            cercle.setFill(null);
            cercle.setStrokeWidth(1);
            group.getChildren().add(cercle);

        }
        Scene scene = new Scene(group);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1600);
        primaryStage.setHeight(900);
        primaryStage.show();

    }
}
