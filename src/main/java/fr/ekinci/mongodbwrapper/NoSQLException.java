package fr.ekinci.mongodbwrapper;


/**
 * An Exception class for wrapping errors with NoSQL database manipulation
 * 
 * @author Gokan EKINCI
 */
public class NoSQLException extends Exception {
    public NoSQLException(Throwable t){
        super(t);
    }
}
