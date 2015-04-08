package mse.hqevaluator.webserviceproxy;

import mse.hqevaluator.AppException;

/**
 * Created by aga on 3/22/15.
 */
public class WebServiceException extends AppException
{
    public WebServiceException() {}

    public WebServiceException(String message)
    {
        super(message);
    }

    public WebServiceException(String message, Throwable e) {
        super(message, e);
    }
}