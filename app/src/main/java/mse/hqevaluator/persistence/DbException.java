package mse.hqevaluator.persistence;

import mse.hqevaluator.AppException;

/**
 * Created by aga on 3/22/15.
 */
public class DbException extends AppException
{
    public DbException() {}

    public DbException(String message) {
        super(message);
    }

    public DbException(String message, Throwable e) {
        super(message, e);
    }
}