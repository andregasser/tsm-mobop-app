package mse.hqevaluator;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mse.hqevaluator.asynctasks.RestTask;
import mse.hqevaluator.asynctasks.RestUtil;
import mse.hqevaluator.entities.MotorwayRamp;
import mse.hqevaluator.entities.NuclearPowerPlant;
import mse.hqevaluator.persistence.DbException;
import mse.hqevaluator.persistence.DbHelper;

public class MainActivity extends ActionBarActivity
    implements RestTask.ResponseCallback {

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

    public void openAboutActivity(View view)
    {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void updateData() {
        try {
            boolean mustUpdateNuclearPowerPlants = dbHelper.mustUpdateNuclearPowerPlants();
            boolean mustUpdateMotorwayRamps = dbHelper.mustUpdateMotorwayRamps();
            boolean mustUpdate = mustUpdateNuclearPowerPlants || mustUpdateMotorwayRamps;
            List<URL> urls = new ArrayList<URL>();

            if (mustUpdateNuclearPowerPlants) {
                String url = "http://tsm-mobop-service.azurewebsites.net/api/nuclearpowerplant";
                urls.add(new URL(url));
            }

            if (mustUpdateMotorwayRamps) {
                String url = "http://tsm-mobop-service.azurewebsites.net/api/motorwayramp";
                urls.add(new URL(url));
            }

            if (mustUpdate) {
                RestTask getTask = RestUtil.obtainGetTask("updateData");
                getTask.setResponseCallback(this);
                getTask.execute(urls.toArray(new URL[urls.size()]));
            }
        } catch (DbException e) {
            Helpers.showToast("Error during update of local data.", this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestSuccess(String response, String taskId, String identifier) {
        if (taskId.equals("updateData")) {
            if (identifier.equals("nuclearPowerPlants")) {
                try {
                    handleNuclearPowerPlantResponse(response);
                } catch (AppException e) {
                    Log.e("MainActivity", "Error while processing update request for nuclear power plants", e);
                }
            }

            if (identifier.equals("motorwayRamps")) {
                try {
                    handleMotorwayRampResponse(response);
                } catch (AppException e) {
                    Log.e("MainActivity", "Error while processing update request for motorway ramps", e);
                }
            }
        }
    }

    @Override
    public void onRequestError(Exception error, String taskId) {
        Log.e("MainActivity", "Current request ended in an error", error);
    }

    private void handleNuclearPowerPlantResponse(String response) throws AppException {
        try {
            List<NuclearPowerPlant> list = getNuclearPowerPlantListFromJSON(response);
            dbHelper.updateNuclearPowerPlants(list);
        } catch (JSONException e) {
            throw new AppException("handleNuclearPowerPlantResponse: An error occurred while processing JSON response.", e);
        } catch (DbException e) {
            throw new AppException("handleNuclearPowerPlantResponse: An error occurred while updating database.", e);
        }
    }

    private void handleMotorwayRampResponse(String response) throws AppException {
        try {
            List<MotorwayRamp> list = getMotorwayRampListFromJSON(response);
            dbHelper.updateMotorwayRamps(list);
        } catch (JSONException e) {
            throw new AppException("handleMotorwayRampResponse: An error occurred while processing JSON response.", e);
        } catch (DbException e) {
            throw new AppException("handleMotorwayRampResponse: An error occurred while updating database.", e);
        }
    }

    private List<NuclearPowerPlant> getNuclearPowerPlantListFromJSON(String response) throws JSONException {
        List<NuclearPowerPlant> list = new ArrayList<>();
        JSONArray jArray = new JSONArray(response);

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject oneObject = jArray.getJSONObject(i);
            NuclearPowerPlant plant = new NuclearPowerPlant();
            plant.Id = oneObject.getInt("Id");
            plant.Name = oneObject.getString("Name");
            plant.Description = oneObject.getString("Description");
            plant.Longitude = oneObject.getDouble("Longitude");
            plant.Latitude = oneObject.getDouble("Latitude");
            list.add(plant);
        }

        return list;
    }

    private List<MotorwayRamp> getMotorwayRampListFromJSON(String response) throws JSONException {
        List<MotorwayRamp> list = new ArrayList<>();
        JSONArray jArray = new JSONArray(response);

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject oneObject = jArray.getJSONObject(i);
            MotorwayRamp ramp = new MotorwayRamp();
            ramp.Id = oneObject.getInt("Id");
            ramp.Name = oneObject.getString("Name");
            ramp.Motorway = oneObject.getString("Motorway");
            ramp.Longitude = oneObject.getDouble("Longitude");
            ramp.Latitude = oneObject.getDouble("Latitude");
            list.add(ramp);
        }

        return list;
    }
}
