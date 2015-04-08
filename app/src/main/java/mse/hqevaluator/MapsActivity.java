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

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
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
        //placeNuclearPowerPlants();
        //placeMotorwayRamps();
        Log.i(this.getLocalClassName(), "setUpMap: Map set up.");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        setlocationtocurent();
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

    private void setlocationtocurent() {
        if (location != null) {
            // Display toast for debugging purposes
            //Helpers.showToast("Lat: " + location.getLatitude() + "\nLng: " + location.getLongitude(), getApplicationContext());

            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            float zoom = 7.0f;    // valid values between 2.0 and 21.0
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
        }
        else {
            // Display toast for debugging purposes
            Helpers.showToast("Location was empty", getApplicationContext());
        }
    }

    private void addHeatMap() {

        prefs = getSharedPreferences(null, MODE_PRIVATE);

        Log.d(" ","AKW INT IST"+prefs.getInt("nuclear_power_plant",0));

        //*********************************************************************************NuclearPowerPlant
        NuclearPowerPlantTable nuclearPowerPlantTable = dbHelper.getNuclearPowerPlantTable();       //init db support
        List<NuclearPowerPlant> nuclearPowerPlants = nuclearPowerPlantTable.getAll();

        Iterator<NuclearPowerPlant> nuclearPowerPlantIterator = nuclearPowerPlants.iterator();

       while(nuclearPowerPlantIterator.hasNext()) {     //iterate to all nuclearPowerplants
            NuclearPowerPlant plant = nuclearPowerPlantIterator.next();
            Log.d("add Circle",":"+plant.Longitude+plant.Latitude);
            if(prefs.getInt("spinner_nearfar1",0)==1){  //get dropdown value menu from settings
                addCircle(prefs.getInt("nuclear_power_plant",0)*1000,new LatLng(plant.Latitude, plant.Longitude),192,57,43);    //add red heatspot
            }
            else{
                addCircle(prefs.getInt("nuclear_power_plant",0)*1000,new LatLng(plant.Latitude, plant.Longitude),46,204,113);   //add green heatspot
            }
            }
        //*********************************************************************************MotorwayRamps
        MotorwayRampTable motorwayRampsTable = dbHelper.getMotorwayRampTable();         //init db support
        List<MotorwayRamp> motorwayRampsPlants = motorwayRampsTable.getAll();

        Iterator<MotorwayRamp> motorwayRampsIterator = motorwayRampsPlants.iterator();

        while(motorwayRampsIterator.hasNext()) {        //iterate to all motorwayramps
            MotorwayRamp plant = motorwayRampsIterator.next();
            Log.d("add Circle",":"+plant.Longitude+plant.Latitude);
            if(prefs.getInt("spinner_nearfar2",0)==1){  //get dropdown value menu from settings
                addCircle(prefs.getInt("motorway_ramp",0)*1000,new LatLng(plant.Latitude, plant.Longitude),192,57,43);      //add red heatspot
            }
            else{
                addCircle(prefs.getInt("motorway_ramp",0)*1000,new LatLng(plant.Latitude, plant.Longitude),46,204,113);     //add green heatspot
            }
        }

    }

    public void addCircle(int radius,LatLng pos,int r,int g, int b){        //add Heatspot
        int alpha = 170;
        for(int i=radius/5;i<=radius;i+=radius/5)   //draw 5 circles per heatspot
        {

            CircleOptions circleOptions = new CircleOptions()
                    .center(pos)   //set center
                    .radius(i)   //set radius in meters
                    .fillColor(Color.argb(alpha,r,g,b))  //default
                    .strokeWidth(0);

            Circle circle= mMap.addCircle(circleOptions);
            alpha = alpha -35; //reduce the alpha in outer circles
        }
    }
}
