package mse.hqevaluator.asynctasks;

import java.util.List;

import mse.hqevaluator.entities.NuclearPowerPlant;

/**
 * This interface must be implemented by activities which call the
 * GetAllNuclearPowerPlantsTask..
 */
public interface OnAllNuclearPowerPlantsReceivedListener {
    void onAllNuclearPowerPlantsReceived(AsyncTaskResult<List<NuclearPowerPlant>> result);
}