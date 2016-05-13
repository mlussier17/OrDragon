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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Gameboard extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public final static String TEAM = "LesDieuxGrec";
    public static String LOCALIP;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Get local address
            LOCALIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("L'adresse du client -> " + LOCALIP);

            Connexion connexion = new Connexion();
            Thread tServ = new Thread(connexion);
            tServ.start();

            // Load Images
            Entity.LoadImages();

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
                Noeud node = nodes.get(i);
                node.setStroke(Color.BLACK);
                if (node.getConstruisible()) nodes.get(i).setFill(Color.GREEN);
                else node.setFill(Color.RED);
                node.setStrokeWidth(1);
                group.getChildren().addAll(node,node.getPane());
            }

            Scene scene = new Scene(group);

            primaryStage.setScene(scene);
            primaryStage.setWidth(1600);
            primaryStage.setHeight(900);
            primaryStage.show();

            // TEST
            PlayerThread pobj = new PlayerThread();
            Thread pThread = new Thread(pobj);
            pThread.setDaemon(true);
            pThread.start();

            Thread.sleep(500);
            Job job = new Job("NOOP");
            JobThread jt = new JobThread(job, pobj);
            Thread jThread = new Thread(jt);
            jThread.setDaemon(true);
            jThread.start();
            jThread.join();

            System.out.println(job.getResponse());

            Reader reader = new Reader();
            Thread tReader = new Thread(reader);
            tReader.setDaemon(true);
            tReader.start();
        }
        catch(UnknownHostException bitch) {
            System.out.println("Poil de poche.");
        }
        catch(InterruptedException ie){
            System.err.println("Reading nodes from server has been interrupted");

        }
    }
}
