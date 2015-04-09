package mse.hqevaluator;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import android.graphics.Color;

import java.util.Iterator;
import java.util.List;

import mse.hqevaluator.entities.MotorwayRamp;
import mse.hqevaluator.entities.NuclearPowerPlant;
import mse.hqevaluator.persistence.DbHelper;
import mse.hqevaluator.persistence.MotorwayRampTable;
import mse.hqevaluator.persistence.NuclearPowerPlantTable;

public class MapsActivity extends ActionBarActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient googleApiClient;

    private Location location;

    private DbHelper dbHelper = null;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dbHelper = new DbHelper(this);
        setUpMapIfNeeded();
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();
    }

    /***
     * Setup the map control if it was not the case yet.
     */
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Log.i(this.getLocalClassName(), "setUpMap: Setting up map.");
        Log.i(this.getLocalClassName(), "setUpMap: Map set up.");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        setCurrentLocation();
        addHeatMap();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Helpers.showToast("Disconnected", getApplicationContext());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Helpers.showToast("Connection failed", getApplicationContext());
    }

    private void setCurrentLocation() {
        if (location != null) {
            Log.d("MapsActivity", "Current Pos: Lat: " + location.getLatitude() + "\nLng: " + location.getLongitude());
            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            float zoom = 7.0f;    // valid values between 2.0 and 21.0
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
        }
        else {
            // Display toast for debugging purposes
            Log.d("MapsActivity", "Current Pos: Location could not be determined");
        }
    }

    private void addHeatMap() {

        prefs = getSharedPreferences(null, MODE_PRIVATE);

        Log.d("MapsActivity","AKW INT is "+prefs.getInt("nuclear_power_plant",0));

        // Process nuclear power plants
        NuclearPowerPlantTable nuclearPowerPlantTable = dbHelper.getNuclearPowerPlantTable();
        List<NuclearPowerPlant> nuclearPowerPlants = nuclearPowerPlantTable.getAll();

        Iterator<NuclearPowerPlant> nuclearPowerPlantIterator = nuclearPowerPlants.iterator();

        while(nuclearPowerPlantIterator.hasNext()) {
            NuclearPowerPlant plant = nuclearPowerPlantIterator.next();
            Log.d("MapsActivity", "Adding circle at Long: " + plant.Longitude + ", Lat:" + plant.Latitude);

            //get dropdown value menu from settings
            if (prefs.getInt("spinner_nearfar1", 0) == 1) {
                //add red heat spot
                int radius = prefs.getInt("nuclear_power_plant", 0) * 1000;
                LatLng pos = new LatLng(plant.Latitude, plant.Longitude);
                int r = 192;
                int g = 57;
                int b = 43;
                addCircle(radius, pos, r, g, b);
            }
            else {
                //add green heat spot
                int radius = prefs.getInt("nuclear_power_plant", 0) * 1000;
                LatLng pos = new LatLng(plant.Latitude, plant.Longitude);
                int r = 46;
                int g = 204;
                int b = 113;
                addCircle(radius, pos, r, g, b);
            }
        }

        // Process motorway ramps
        MotorwayRampTable motorwayRampsTable = dbHelper.getMotorwayRampTable();
        List<MotorwayRamp> motorwayRampsPlants = motorwayRampsTable.getAll();

        Iterator<MotorwayRamp> motorwayRampsIterator = motorwayRampsPlants.iterator();

        while(motorwayRampsIterator.hasNext()) {
            MotorwayRamp plant = motorwayRampsIterator.next();
            Log.d("add Circle", ":" + plant.Longitude + plant.Latitude);

            //get dropdown value menu from settings
            if(prefs.getInt("spinner_nearfar2" ,0) == 1){
                // add red heat spot
                int radius = prefs.getInt("motorway_ramp", 0) * 1000;
                LatLng pos = new LatLng(plant.Latitude, plant.Longitude);
                int r = 192;
                int g = 57;
                int b = 43;
                addCircle(radius, pos, r, g, b);
            }
            else{
                // add green heat spot
                int radius = prefs.getInt("motorway_ramp", 0) * 1000;
                LatLng pos = new LatLng(plant.Latitude, plant.Longitude);
                int r = 46;
                int g = 204;
                int b = 113;
                addCircle(radius, pos, r, g, b);
            }
        }
    }

    /***
     * Adds a new heat spot in a circular shape.
     *
     * @param radius
     * @param pos
     * @param r
     * @param g
     * @param b
     */
    public void addCircle(int radius, LatLng pos,int r, int g, int b){
        int alpha = 170;

        // draw 5 circles per heat spot
        for(int i = radius / 5;i <= radius; i += radius / 5)
        {
            CircleOptions circleOptions = new CircleOptions()
                    .center(pos)                           //set center
                    .radius(i)                             //set radius in meters
                    .fillColor(Color.argb(alpha, r, g, b)) //default
                    .strokeWidth(0);

            Circle circle = mMap.addCircle(circleOptions);
            alpha = alpha - 35; //reduce the alpha in outer circles
        }
    }
}
