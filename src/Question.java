import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by 201127412 on 2016-05-16.
 */
public class Question {
    private String text;
    private int id;
    private ArrayList<Response> responses;

    public ArrayList<Response> getResponses() {
        return responses;
    }

    public String getText() {
        return text;
    }

    public void setText(String text_) {
        text = text_;
    }

    public int getId() {
        return id;
    }

    public void setId(int id_) {
        id = id_;
    }
}
