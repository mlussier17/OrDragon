import javafx.scene.image.Image;
import sun.tools.jar.Main;

import java.util.HashMap;

/**
 * Created by 196128636 on 2016-05-12.
 */
public class Entity {
    private String Name;
    private Noeud Noeud;

    private static HashMap<String,Image> images = new HashMap<>();

    public Entity(String name, Noeud noeud) {
        Name = name;
        Noeud = noeud;
        addToNoeud();
    }

    private void addToNoeud() {
        Noeud.addEntity(this);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Noeud getNoeud() {
        return Noeud;
    }

    public void setNoeud(Noeud noeud) {
        this.Noeud = noeud;
    }

    public Image getImage() {
        return images.get(Name);
    }

    public static void LoadImages() {
        Image im;

        // PLAYER
        im = new Image(Entity.class.getResource("resources/Joueur.png").toString());
        images.put("J", im);

        // TROLL
        im = new Image(Entity.class.getResource("resources/Troll.png").toString());
        images.put("T", im);

        // GOBELIN
        im = new Image(Entity.class.getResource("resources/Gobelin.png").toString());
        images.put("G", im);

        // MOUNTAINDEW
        im = new Image(Entity.class.getResource("resources/MountainDew.png").toString());
        images.put("M", im);

        // DORITOS
        im = new Image(Entity.class.getResource("resources/Doritos.png").toString());
        images.put("D", im);

        // MANOIR
        im = new Image(Entity.class.getResource("resources/Manoir.png").toString());
        images.put("N", im);

        // AUBERGE
        im = new Image(Entity.class.getResource("resources/Auberge.png").toString());
        images.put("A", im);

        // CHATEAU
        im = new Image(Entity.class.getResource("resources/Chateau.png").toString());
        images.put("C", im);

        // COIN
        im = new Image(Entity.class.getResource("resources/Coin.png").toString());
        images.put("P", im);
    }
}
