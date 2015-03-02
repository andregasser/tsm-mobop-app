package mse.hqevaluator;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aga on 3/1/15.
 */
public class WebServiceProxy {

    public List<NuclearPowerPlant> getAllNuclearPowerPlants()
    {
        List<NuclearPowerPlant> list = new ArrayList<NuclearPowerPlant>();

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("http://tsm-mobop-service.azurewebsites.net/api/nuclearpowerplant"));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();

                JSONArray jArray = new JSONArray(responseString);

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    int id = oneObject.getInt("Id");
                    String name = oneObject.getString("Name");
                    String description = oneObject.getString("Description");
                    double longitude = oneObject.getDouble("Longitude");
                    double latitude = oneObject.getDouble("Latitude");

                    NuclearPowerPlant plant = new NuclearPowerPlant();
                    plant.Id = id;
                    plant.Name = name;
                    plant.Description = description;
                    plant.Longitude = longitude;
                    plant.Latitude = latitude;
                    list.add(plant);
                }
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }
        catch (Exception e)
        {
            // More about HTTP exception handling in another tutorial.
            // For now we just print the stack trace.
            e.printStackTrace();
        }

        return list;
    }
}
