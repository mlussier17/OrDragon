/**
 * Created by Math on 2016-12-04.
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

public class Gameboard extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Connexion connexion = new Connexion();
        Thread tServ = new Thread(connexion);
        tServ.start();
        //Group group = new Group();
    }
}

class drawingCircle implements Runnable{
    public void run(){

    }
}
