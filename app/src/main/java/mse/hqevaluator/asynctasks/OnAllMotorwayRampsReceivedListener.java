package mse.hqevaluator.asynctasks;

import java.util.List;

import mse.hqevaluator.MotorwayRamp;

/**
 * This interface must be implemented by activities which call the
 * GetAllMotorwayRampsTask..
 */
public interface OnAllMotorwayRampsReceivedListener {
    void onAllMotorwayRampsReceived(AsyncTaskResult<List<MotorwayRamp>> result);
}