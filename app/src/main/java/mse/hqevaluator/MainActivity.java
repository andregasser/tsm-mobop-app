package mse.hqevaluator;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import mse.hqevaluator.asynctasks.AsyncTaskResult;
import mse.hqevaluator.asynctasks.AsyncTaskResultStatus;
import mse.hqevaluator.asynctasks.GetAllMotorwayRampsTask;
import mse.hqevaluator.asynctasks.GetAllNuclearPowerPlantsTask;
import mse.hqevaluator.asynctasks.OnAllMotorwayRampsReceivedListener;
import mse.hqevaluator.asynctasks.OnAllNuclearPowerPlantsReceivedListener;
import mse.hqevaluator.entities.MotorwayRamp;
import mse.hqevaluator.entities.NuclearPowerPlant;
import mse.hqevaluator.persistence.DbException;
import mse.hqevaluator.persistence.DbHelper;

public class MainActivity extends ActionBarActivity
    implements OnAllNuclearPowerPlantsReceivedListener, OnAllMotorwayRampsReceivedListener {

    private DbHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateData();
    }

    public void openSettingsActivity(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openMapsActivity(View view)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void updateData() {
        boolean mustUpdateNuclearPowerPlants = false;
        boolean mustUpdateMotorwayRamps = false;

        try {
            mustUpdateNuclearPowerPlants = dbHelper.mustUpdateNuclearPowerPlants();
            mustUpdateMotorwayRamps = dbHelper.mustUpdateMotorwayRamps();

            if (mustUpdateNuclearPowerPlants) {
                new GetAllNuclearPowerPlantsTask(this).execute();
            }

            if (mustUpdateMotorwayRamps) {
                new GetAllMotorwayRampsTask(this).execute();
            }
        } catch (DbException e) {
            Helpers.showToast("Error during update of local data.", this);
        }
    }

    @Override
    public void onAllNuclearPowerPlantsReceived(AsyncTaskResult<List<NuclearPowerPlant>> result) {
        List<NuclearPowerPlant> list = result.getResult();
        Exception ex = result.getException();
        AsyncTaskResultStatus status = result.getStatus();
        boolean errorOccurred = false;

        if (status.equals(AsyncTaskResultStatus.SUCCESS)) {
            // Everything was fine.
            try {
                dbHelper.updateNuclearPowerPlants(list);
            } catch (DbException e) {
                errorOccurred = true;
            }
        } else {
            // There was an error. We should display an error to the user.
            errorOccurred = true;
        }

        if (errorOccurred) {
            Helpers.showToast("An error occurred during update of local data.", this);
        }
    }

    @Override
    public void onAllMotorwayRampsReceived(AsyncTaskResult<List<MotorwayRamp>> result) {
        List<MotorwayRamp> list = result.getResult();
        Exception ex = result.getException();
        AsyncTaskResultStatus status = result.getStatus();
        boolean errorOccurred = false;

        if (status.equals(AsyncTaskResultStatus.SUCCESS)) {
            // Everything was fine.
            try {
                dbHelper.updateMotorwayRamps(list);
            } catch (DbException e) {
                errorOccurred = true;
            }
        } else {
            // There was an error. We should display an error to the user.
            errorOccurred = true;
        }

        if (errorOccurred) {
            Helpers.showToast("An error occurred during update of local data.", this);
        }
    }
}
