package mse.hqevaluator.webserviceproxy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mse.hqevaluator.Helpers;
import mse.hqevaluator.entities.MotorwayRamp;
import mse.hqevaluator.entities.NuclearPowerPlant;

/**
 * Class for JSON/REST web service connectivity.
 */
public class WebServiceProxy {

    /*
     * Web service URL for power plants.
     */
    private String baseUrlPowerPlant = "http://tsm-mobop-service.azurewebsites.net/api/nuclearpowerplant";

    /*
     * Web Service URL for motorways.
     */
    private String baseUrlMotorway = "http://tsm-mobop-service.azurewebsites.net/api/motorwayramp";

    /**
     * Returns all nuclear power plants.
     *
     * @return a list of nuclear power plants
     * @throws IOException
     */
    public List<NuclearPowerPlant> getAllNuclearPowerPlants() throws WebServiceException {
        List<NuclearPowerPlant> list = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        String response = null;

        try {
            URL url = new URL(baseUrlPowerPlant);
            urlConnection = (HttpURLConnection) url.openConnection();
            response = Helpers.inputStreamToString(urlConnection.getInputStream());
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

        } catch (MalformedURLException e) {
            throw new WebServiceException("Malformed web service url provided.", e);
        } catch (JSONException e) {
            throw new WebServiceException("Invalid JSON data received.", e);
        } catch (IOException e) {
            throw new WebServiceException("An error occurred during web service request.", e);
        } finally {
            urlConnection.disconnect();
        }

        return list;
    }

    /**
     * Returns all nuclear power plants.
     *
     * @return a list of nuclear power plants
     * @throws IOException
     */
    public List<MotorwayRamp> getAllMotorwayRamps() throws WebServiceException {
        List<MotorwayRamp> list = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        String response = null;

        try {
            URL url = new URL(baseUrlMotorway);
            urlConnection = (HttpURLConnection) url.openConnection();
            response = Helpers.inputStreamToString(urlConnection.getInputStream());
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

        } catch (MalformedURLException e) {
            throw new WebServiceException("Malformed web service url provided.", e);
        } catch (JSONException e) {
            throw new WebServiceException("Invalid JSON data received.", e);
        } catch (IOException e) {
            throw new WebServiceException("An error occurred during web service request.", e);
        } finally {
            urlConnection.disconnect();
        }

        return list;
    }
}
