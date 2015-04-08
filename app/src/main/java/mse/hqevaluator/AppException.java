package mse.hqevaluator;

/**
 * Created by aga on 3/22/15.
 */
public class AppException extends Exception
{
    public AppException() {}

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable e) {
        super(message, e);
    }
}