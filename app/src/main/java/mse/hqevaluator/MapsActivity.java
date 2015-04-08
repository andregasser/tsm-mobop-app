package mse.hqevaluator;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.google.maps.android.heatmaps.Gradient;
import android.graphics.Color;
import java.lang.Math;
import java.util.Collection;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;



import java.util.ArrayList;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mse.hqevaluator.asynctasks.GetAllMotorwayRampsTask;
import mse.hqevaluator.asynctasks.OnAllMotorwayRampsReceivedListener;
import mse.hqevaluator.asynctasks.OnAllNuclearPowerPlantsReceivedListener;
import mse.hqevaluator.asynctasks.AsyncTaskResult;
import mse.hqevaluator.asynctasks.AsyncTaskResultStatus;
import mse.hqevaluator.asynctasks.GetAllNuclearPowerPlantsTask;
import mse.hqevaluator.entities.MotorwayRamp;
import mse.hqevaluator.entities.NuclearPowerPlant;
import mse.hqevaluator.persistence.DbHelper;
import mse.hqevaluator.persistence.MotorwayRampTable;
import mse.hqevaluator.persistence.NuclearPowerPlantTable;

public class MapsActivity extends ActionBarActivity
    implements OnAllNuclearPowerPlantsReceivedListener, OnAllMotorwayRampsReceivedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

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

        SharedPreferences.Editor editor;
        String slider = null;
        prefs = getSharedPreferences(slider, MODE_PRIVATE);
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

        Log.d(" ","AKW INT IST"+prefs.getInt("akw",0));

        //*********************************************************************************NuclearPowerPlant
        ArrayList<WeightedLatLng> weightedLatLngNuclearPowerPlant = new ArrayList<WeightedLatLng>();

        NuclearPowerPlantTable nuclearPowerPlantTable = dbHelper.getNuclearPowerPlantTable();
        List<NuclearPowerPlant> nuclearPowerPlants = nuclearPowerPlantTable.getAll();

        Iterator<NuclearPowerPlant> nuclearPowerPlantIterator = nuclearPowerPlants.iterator();

       while(nuclearPowerPlantIterator.hasNext()) {
            NuclearPowerPlant plant = nuclearPowerPlantIterator.next();
            Log.d("add Circle",":"+plant.Longitude+plant.Latitude);
            addCircle(25000,new LatLng(plant.Longitude, plant.Latitude));
        }
        //*********************************************************************************MotorwayRamps
        ArrayList<WeightedLatLng> weightedLatLngMotorwayRamps = new ArrayList<WeightedLatLng>();

        MotorwayRampTable motorwayRampsTable = dbHelper.getMotorwayRampTable();
        List<MotorwayRamp> motorwayRampsPlants = motorwayRampsTable.getAll();

        Iterator<MotorwayRamp> motorwayRampsIterator = motorwayRampsPlants.iterator();

        while(motorwayRampsIterator.hasNext()) {
            MotorwayRamp plant = motorwayRampsIterator.next();
            Log.d("add Circle",":"+plant.Longitude+plant.Latitude);
            addCircle(5000,new LatLng(plant.Longitude, plant.Latitude));
        }

    }

    public void addCircle(int radius,LatLng pos){
        int alpha = 170;
        for(int i=radius/5;i<=radius;i+=radius/5)
        {

            CircleOptions circleOptions = new CircleOptions()
                    .center(pos)   //set center
                    .radius(i)   //set radius in meters
                    .fillColor(Color.argb(alpha,192,57,43))  //default
                    .strokeWidth(0);

            Circle circle= mMap.addCircle(circleOptions);
            alpha = alpha -35;
        }
    }

    @Override
    public void onAllNuclearPowerPlantsReceived(AsyncTaskResult<List<NuclearPowerPlant>> result) {
        List<NuclearPowerPlant> list = result.getResult();
        Exception e = result.getException();
        AsyncTaskResultStatus status = result.getStatus();

        if (status.equals(AsyncTaskResultStatus.SUCCESS)) {
            // Everything was fine.
            Iterator<NuclearPowerPlant> iterator = list.iterator();

            while(iterator.hasNext()){
                NuclearPowerPlant plant = iterator.next();
                mMap.addMarker(
                    new MarkerOptions()
                        .position(new LatLng(plant.Latitude, plant.Longitude))
                        .title(plant.Name + "\nLatitude: " + plant.Latitude + "\nLongitude: " + plant.Longitude));
            }
        } else {
            // There was an error. We should display an error to the user.
            // TODO: Display error message
        }
    }

    @Override
    public void onAllMotorwayRampsReceived(AsyncTaskResult<List<MotorwayRamp>> result) {
        List<MotorwayRamp> list = result.getResult();
        Exception e = result.getException();
        AsyncTaskResultStatus status = result.getStatus();

        if (status.equals(AsyncTaskResultStatus.SUCCESS)) {
            // Everything was fine.
            Iterator<MotorwayRamp> iterator = list.iterator();

            while(iterator.hasNext()){
                MotorwayRamp ramp = iterator.next();
                mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(ramp.Latitude, ramp.Longitude))
                                .title(ramp.Name + "\nLatitude: " + ramp.Latitude + "\nLongitude: " + ramp.Longitude)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        } else {
            // There was an error. We should display an error to the user.
            // TODO: Display error message
        }
    }

}
