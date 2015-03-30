package mse.hqevaluator.persistence;

/**
 * Created by aga on 3/22/15.
 */
class DbRecordNotFoundException extends DbException
{
    public DbRecordNotFoundException() {}

    public DbRecordNotFoundException(String message) {
        super(message);
    }

    public DbRecordNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}