package mse.hqevaluator.asynctasks;

import android.os.AsyncTask;

import java.util.List;

import mse.hqevaluator.entities.MotorwayRamp;
import mse.hqevaluator.webserviceproxy.WebServiceException;
import mse.hqevaluator.webserviceproxy.WebServiceProxy;

/**
 * This class fetches all nuclear power plants in the background. As soon as the data
 * has been successfully fetched, the onPostExecute method is invoked.
 */
public class GetAllMotorwayRampsTask extends AsyncTask<Void, Void, AsyncTaskResult<List<MotorwayRamp>>> {

    /*
     * A reference to the activity which has invoked this task. This reference
     * is required in order to notify the activity upon completion of this task.
     */
    private OnAllMotorwayRampsReceivedListener listener;

    /*
     * Constructor of this task.
     *
     * @param   listener    A reference to the listening object which has invoked this task
     */
    public GetAllMotorwayRampsTask(OnAllMotorwayRampsReceivedListener listener) {
        this.listener = listener;
    }

    /**
     * The background task to be performed by this class.
     *
     * @param params
     * @return a list of nuclear power plants
     */
    @Override
    protected AsyncTaskResult<List<MotorwayRamp>> doInBackground(Void... params) {
        WebServiceProxy wsProxy = new WebServiceProxy();
        List<MotorwayRamp> list = null;

        try {
            list = wsProxy.getAllMotorwayRamps();
        } catch (WebServiceException e) {
            return new AsyncTaskResult<>(list, e);
        }

        return new AsyncTaskResult<>(list);
    }

    /*
     * This method is executed as soon as doInBackground has finished executing.
     *
     * @param   result  The result of the background task
     */
    @Override
    protected void onPostExecute(AsyncTaskResult<List<MotorwayRamp>> result) {
        listener.onAllMotorwayRampsReceived(result);
    }
}
