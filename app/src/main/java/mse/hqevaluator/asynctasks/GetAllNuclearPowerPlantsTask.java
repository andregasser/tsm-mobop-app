package mse.hqevaluator.asynctasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import mse.hqevaluator.NuclearPowerPlant;
import mse.hqevaluator.WebServiceProxy;

/**
 * This class fetches all nuclear power plants in the background. As soon as the data
 * has been successfully fetched, the onPostExecute method is invoked.
 */
public class GetAllNuclearPowerPlantsTask extends AsyncTask<Void, Void, AsyncTaskResult<List<NuclearPowerPlant>>> {

    /*
     * A reference to the activity which has invoked this task. This reference
     * is required in order to notify the activity upon completion of this task.
     */
    private OnAllNuclearPowerPlantsReceivedListener listener;

    /*
     * Constructor of this task.
     *
     * @param   listener    A reference to the listening object which has invoked this task
     */
    public GetAllNuclearPowerPlantsTask(OnAllNuclearPowerPlantsReceivedListener listener) {
        this.listener = listener;
    }

    /**
     * The background task to be performed by this class.
     *
     * @param params
     * @return a list of nuclear power plants
     */
    @Override
    protected AsyncTaskResult<List<NuclearPowerPlant>> doInBackground(Void... params) {
        WebServiceProxy wsProxy = new WebServiceProxy();
        List<NuclearPowerPlant> list = null;

        try {
            list = wsProxy.getAllNuclearPowerPlants();
        } catch (IOException e) {
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
    protected void onPostExecute(AsyncTaskResult<List<NuclearPowerPlant>> result) {
        listener.onAllNuclearPowerPlantsReceived(result);
    }
}
