package mse.hqevaluator;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by aga on 3/1/15.
 */
public class GetAllNuclearPowerPlantsTask extends AsyncTask<Void, Integer, List<NuclearPowerPlant>> {

    private AsyncResponse activity;

    public GetAllNuclearPowerPlantsTask(Activity activity) {
        this.activity = (AsyncResponse)activity;
    }

    @Override
    protected List<NuclearPowerPlant> doInBackground(Void... params) {
        // Invoke web service
        WebServiceProxy wsProxy = new WebServiceProxy();
        List<NuclearPowerPlant> list = wsProxy.getAllNuclearPowerPlants();

        return list;
    }

    @Override
    protected void onPostExecute(List<NuclearPowerPlant> result) {
        activity.processFinish(result);
    }


}
