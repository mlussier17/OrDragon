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
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.event.EventHandler;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Gameboard extends Application {

    final static short NOMBRE_BOUTON=4;
    public final static String TEAM = "zLesdieuxGrec";
    public static String LOCALIP;
    public static PlayerThread pobj;
    private Thread pThread;
    public static Group group;
    public static Boolean playing = false;

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

            //Create an array for nodes and lines
            ArrayList<Noeud> nodes = Noeud.getList();
            ArrayList<Chemin> lines = Chemin.getList();
            tServ.join();

            //Get the background map from his website
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

            //Create button bar
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
                    btn3Click(btn1);
                }
            });
            Bouton btn4 = new Bouton(900, 850,"Quitter",4);
            btn4.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t){
                    btn4Click(btn1);
                }
            });

            //Position the button bar
            ArrayList<Bouton> arrayBouton =  new ArrayList<>(Arrays.asList(btn1,btn2,btn3,btn4));
            for(int i=0;i < NOMBRE_BOUTON;++i)
            {
                HBox.setHgrow(arrayBouton.get(i), Priority.ALWAYS);
                arrayBouton.get(i).setMaxWidth(150);

            }
            //Button bar properties
            box.getChildren().addAll(btn1, btn2, btn3, btn4);
            box.setLayoutX(1000);
            box.setLayoutY(874);
            box.setPrefWidth(600);
            group.getChildren().add(box);

            //Creation of the graphic application
            Scene scene = new Scene(group);

            primaryStage.setScene(scene);
            primaryStage.setWidth(1607);
            primaryStage.setHeight(929);
            primaryStage.setResizable(false);
            primaryStage.show();

            //Main thread that reads the position server
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
    public void btn1Click(Bouton source){
        if(source.getText().equalsIgnoreCase("Jouer")) {
            playing = true;
            PlayerThread.run = true;
            //Database.resetStats();
            pobj = new PlayerThread();
            pThread = new Thread(pobj);
            pThread.setDaemon(true);
            pThread.start();
            source.setText("Se déconnecter");
        }
        else{
            PlayerThread.run = false;
            playing = false;
            pobj.jobs.add(new Job("QUIT"));
            pThread.interrupt();
            pThread = null;
            pobj = null;
            source.setText("Jouer");
        }
    }

    //Bouton Construire
    private void btn2Click(){
        if(playing) {
            Boolean paying = Database.payAuberge();
            if (!paying) System.out.println("Not enough gold.");
            else {
                Job job = new Job("BUILD CHA");
                JobThread jt = new JobThread(job, Gameboard.pobj);
                Thread jThread = new Thread(jt);
                try {
                    jThread.setDaemon(true);
                    jThread.start();
                    jThread.join();
                    System.out.println(job.getResponse());
                } catch (InterruptedException ie) {ie.getMessage();}
            }
        }
    }

    //Bouton Payer
    private void btn3Click(Bouton source){
        if(playing) {
            Job job = new Job("NODE");
            JobThread jt = new JobThread(job, Gameboard.pobj);
            Thread jThread = new Thread(jt);
            try {
                jThread.setDaemon(true);
                jThread.start();
                jThread.join();
            } catch (InterruptedException ie) {

            }
            String node = job.getResponse();
            if (Integer.parseInt(node) == 79) {
                Boolean paying = Database.payDoritos();
                if (paying) pobj.jobs.add(new Job("FREE"));
                else {
                    System.out.println("Not enough items quitting game.");
                    btn1Click(source);
                }
            } else if (Integer.parseInt(node) == 53) {
                Boolean paying = Database.payDew();
                if (paying) pobj.jobs.add(new Job("FREE"));
                else {
                    System.out.println("Not enough items quitting game.");
                    btn1Click(source);
                }
            } else {
                System.out.println("Nothing to pay you rich b*tch.");
            }
        }
    }

    //Bouton Quitter
    private void btn4Click(Bouton source){
        btn1Click(source);
        System.exit(0);
    }
}
