package mse.hqevaluator.asynctasks;

public class RestUtil {

    public static final RestTask obtainGetTask(String taskId) {
        RestTask task = new RestTask(taskId);
        return task;
    }

}