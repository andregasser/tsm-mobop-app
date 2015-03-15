package mse.hqevaluator.asynctasks;

/**
 * A generic class to handle AsyncTask responses.
 */
public class AsyncTaskResult<T> {

    /**
     * The result.
     */
    private T result;

    /**
     * In case an exception occurred, this member is set.
     */
    private Exception exception;

    /**
     * The status code.
     */
    private AsyncTaskResultStatus status;

    /**
     * Returns the result.
     *
     * @return a result object of the specified type
     */
    public T getResult() {
        return result;
    }

    /**
     * Returns the exception.
     *
     * @return the exception that occurred
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Return the status code.
     *
     * @return the status code.
     */
    public AsyncTaskResultStatus getStatus() {
        return status;
    }

    /**
     * Constructor to be used in the success case.
     *
     * @param result The result to be returned
     */
    public AsyncTaskResult(T result) {
        this.result = result;
        this.exception = null;
        this.status = AsyncTaskResultStatus.SUCCESS;
    }

    /**
     * The constructor to be used in case of error.
     *
     * @param result        The result to be returned
     * @param exception     The exception which was thrown (if any)
     */
    public AsyncTaskResult(T result, Exception exception) {
        this.result = result;
        this.exception = exception;
        this.status = AsyncTaskResultStatus.ERROR;
    }
}