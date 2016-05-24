/**
 * Created by 196128636 on 2016-05-13.
 */
public class Job {
    private boolean done_ = false;
    private String command_ = null;
    private String response_ = null;

    Job(String command) {
        command_ = command;
    }

    public String getCommand() {
        return command_;
    }

    public void done() {
        done_ = true;
    }

    public boolean isDone() {
        return done_;
    }

    public String getResponse() {
        return response_;
    }

    public void setResponse(String response) {
        response_ = response;
    }
}
