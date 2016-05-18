/**
 * Created by Math on 2016-12-04.
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.event.EventHandler;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gameboard extends Application {

    final static short NOMBRE_BOUTON=4;

    public static void main(String[] args) {
        // LOAD SQL DRIVERS
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("Driver charger");
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        launch(args);
    }
    public final static String TEAM = "LesDieuxGrec";
    public static String LOCALIP;
    public static PlayerThread pobj;
    private Thread pThread;
    public static Group group;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Get local address
            LOCALIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("L'adresse du client -> " + LOCALIP);

            //Connexion au serveur de position
            Connexion connexion = new Connexion();
            Thread tServ = new Thread(connexion);
            tServ.start();

            //Start du serveur de question qui ecoute
            ServeurQuestion question = new ServeurQuestion();
            Thread tquestion = new Thread(question);
            tquestion.setDaemon(true);
            tquestion.start();

            // Load Images
            Entity.LoadImages();

            group = new Group();
            ArrayList<Noeud> nodes = Noeud.getList();
            ArrayList<Chemin> lines = Chemin.getList();
            tServ.join();
            group.getChildren().add(new ImageView(new Image("http://prog101.com/travaux/dragon/images/nowhereland.png")));

            //Dessin des lignes sur la map
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

            //Dessin des noeuds sur la map
            for (int i = 0; i < nodes.size(); ++i) {
                Noeud node = nodes.get(i);
                node.setStroke(Color.BLACK);
                if (node.getConstruisible()) nodes.get(i).setFill(Color.GREEN);
                else node.setFill(Color.RED);
                node.setStrokeWidth(1);
                group.getChildren().addAll(node,node.getPane());
            }

            HBox box = new HBox();

            Bouton btn1 = new Bouton(1500,850,"Jouer",1);
            btn1.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t){
                    btn1Click(btn1);
                }
            });
            Bouton btn2 = new Bouton(1300, 850,"Construire",2);
            btn2.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t){
                    btn2Click();
                }
            });
            Bouton btn3 = new Bouton(1100, 850,"Payer",3);
            btn3.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t){
                    btn3Click();
                }
            });
            Bouton btn4 = new Bouton(900, 850,"Quitter",4);
            btn4.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t){
                    btn4Click(btn1);
                }
            });

            ArrayList<Bouton> arrayBouton =  new ArrayList<>(Arrays.asList(btn1,btn2,btn3,btn4));
            for(int i=0;i < NOMBRE_BOUTON;++i)
            {
                HBox.setHgrow(arrayBouton.get(i), Priority.ALWAYS);
                arrayBouton.get(i).setMaxWidth(150);

            }

            box.getChildren().addAll(btn1, btn2, btn3, btn4);
            box.setLayoutX(1000);
            box.setLayoutY(874);
            box.setPrefWidth(600);
            group.getChildren().add(box);

            Scene scene = new Scene(group);

            primaryStage.setScene(scene);
            primaryStage.setWidth(1607);
            primaryStage.setHeight(929);
            primaryStage.setResizable(false);
            primaryStage.show();

            Reader reader = new Reader();
            Thread tReader = new Thread(reader);
            tReader.setDaemon(true);
            tReader.start();


        }
        catch(UnknownHostException bitch) {
            System.out.println("Poil de poche.");
        }
        catch(InterruptedException ie) {
            System.err.println("Reading nodes from server has been interrupted");

        }
    }
    // TODO implements functions
    //Bouton Jouer
    private void btn1Click(Bouton source){
        if(source.getText().equalsIgnoreCase("Jouer")) {
            pobj = new PlayerThread();
            pThread = new Thread(pobj);
            pThread.setDaemon(true);
            pThread.start();
            source.setText("Se déconnecter");
        }
        else{
            pobj.jobs.add(new Job("QUIT"));
            pThread.interrupt();
            pThread = null;
            pobj = null;
            source.setText("Jouer");
        }
    }

    //Bouton Construire
    private void btn2Click(){
        pobj.jobs.add(new Job("BUILD CHA"));
    }

    //Bouton Payer
    private void btn3Click(){
        System.out.println("Work");
    }

    //Bouton Quitter
    private void btn4Click(Bouton source){
        btn1Click(source);
        System.exit(0);
    }
}
