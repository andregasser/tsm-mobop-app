package mse.hqevaluator.persistence;

/**
 * Created by aga on 3/22/15.
 */
public class DbException extends Exception
{
    public DbException() {}

    public DbException(String message) {
        super(message);
    }

    public DbException(String message, Throwable e) {
        super(message, e);
    }
}