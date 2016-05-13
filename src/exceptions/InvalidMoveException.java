package exceptions;


/*
 * Created by 196128636 on 2016-05-13.
 */
public class InvalidMoveException extends Exception{
    public InvalidMoveException() {
        super();
    }

    public InvalidMoveException(String message) {
        super(message);
    }
}
