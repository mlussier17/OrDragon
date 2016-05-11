/**
 * Created by Math on 2016-12-04.
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Gameboard extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Connexion connexion = new Connexion();
            Thread tServ = new Thread(connexion);
            tServ.start();
            Group group = new Group();
            ArrayList<Noeud> nodes = Noeud.getList();
            ArrayList<Chemin> lines = Chemin.getList();
            tServ.join();
            group.getChildren().add(new ImageView(new Image("http://prog101.com/travaux/dragon/images/carte03.png")));
            for(int i = 0; i < lines.size(); ++i){
                double sourceX = nodes.get(lines.get(i).getStart()).getCenterX();
                double destX = nodes.get(lines.get(i).getDest()).getCenterX();
                double sourceY = nodes.get(lines.get(i).getStart()).getCenterY();
                double destY = nodes.get(lines.get(i).getDest()).getCenterY();
                Line line = new Line(sourceX, sourceY, destX, destY);
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(1);
                group.getChildren().add(line);
            }

            for (int i = 0; i < nodes.size(); ++i) {
                nodes.get(i).setStroke(Color.BLACK);
                if (nodes.get(i).getConstruisible()) nodes.get(i).setFill(Color.GREEN);
                else nodes.get(i).setFill(Color.RED);
                nodes.get(i).setStrokeWidth(1);
                group.getChildren().add(nodes.get(i));
            }

            Scene scene = new Scene(group);
            primaryStage.setScene(scene);
            primaryStage.setWidth(1600);
            primaryStage.setHeight(900);
            primaryStage.show();

            Reader reader = new Reader();
            Thread tReader = new Thread(reader);
            tReader.setDaemon(true);
            tReader.start();
        }
        catch(InterruptedException ie){
            System.err.println("Reading nodes from server has been interrupted");

        }
    }
}
