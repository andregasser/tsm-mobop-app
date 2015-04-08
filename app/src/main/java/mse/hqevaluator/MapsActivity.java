package mse.hqevaluator;

import android.app.Fragment;
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
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnCameraChangeListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient googleApiClient;

    private Location location;

    private DbHelper dbHelper = null;

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

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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
            mMap.setOnCameraChangeListener(this);
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

//    void placeNuclearPowerPlants() {
//        NuclearPowerPlantTable table = dbHelper.getNuclearPowerPlantTable();
//        List<NuclearPowerPlant> nuclearPowerPlants = table.getAll();
//
//        Iterator<NuclearPowerPlant> iterator = nuclearPowerPlants.iterator();
//
//        while(iterator.hasNext()) {
//            NuclearPowerPlant plant = iterator.next();
//            mMap.addMarker(
//                    new MarkerOptions()
//                            .position(new LatLng(plant.Latitude, plant.Longitude))
//                            .title(plant.Name + "\nLatitude: " + plant.Latitude + "\nLongitude: " + plant.Longitude));
//            Log.i(this.getLocalClassName(), "setUpMap: Placing marker for " + plant.Name);
//        }
//    }
//
//    void placeMotorwayRamps() {
//        MotorwayRampTable table = dbHelper.getMotorwayRampTable();
//        List<MotorwayRamp> motorwayRamps = table.getAll();
//
//        Iterator<MotorwayRamp> iterator = motorwayRamps.iterator();
//
//        while(iterator.hasNext()) {
//            MotorwayRamp ramp = iterator.next();
//            mMap.addMarker(
//                    new MarkerOptions()
//                            .position(new LatLng(ramp.Latitude, ramp.Longitude))
//                            .title(ramp.Name + "\nMotorway: " + ramp.Motorway + "\nLatitude: " + ramp.Latitude + "\nLongitude: " + ramp.Longitude)
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//            Log.i(this.getLocalClassName(), "setUpMap: Placing marker for " + ramp.Name);
//        }
//    }

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
            float zoom = 10.0f;    // valid values between 2.0 and 21.0
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
        }
        else {
            // Display toast for debugging purposes
            Helpers.showToast("Location was empty", getApplicationContext());
        }
    }

    private HeatmapTileProvider mProvider;

    private TileOverlay mOverlay;

    private static final int[] colors = {
            Color.rgb(243, 156, 18),
            Color.rgb(192, 57, 43)
    };

    private static final float[] startPoints = {
            0.1f, 2f
    };

    int radiusInMeterAKW = 10000;

    private void addHeatMap() {
        ArrayList<WeightedLatLng> weightedLatLngakw = new ArrayList<WeightedLatLng>();

        NuclearPowerPlantTable table = dbHelper.getNuclearPowerPlantTable();
        List<NuclearPowerPlant> nuclearPowerPlants = table.getAll();

        Iterator<NuclearPowerPlant> iterator = nuclearPowerPlants.iterator();

        while(iterator.hasNext()) {
            NuclearPowerPlant plant = iterator.next();
            LatLng actualLocation = new LatLng(plant.Longitude, plant.Latitude);
            WeightedLatLng actualLocationweighted = new WeightedLatLng(actualLocation, 1);
            weightedLatLngakw.add(actualLocationweighted);
        }
        //weightedLatLngakw.add(actualLocationweighted);
        Log.d("","akwlist is "+weightedLatLngakw);



        Gradient gradient = new Gradient(colors, startPoints);

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(weightedLatLngakw)
                .gradient(gradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        mProvider.setOpacity(0.7);

        double meterPerPixel = MeterPerPixel();
        mProvider.setRadius(meterToPixel(radiusInMeterAKW,meterPerPixel));


    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        double meterPerPixel = MeterPerPixel();
        //Log.d("Radius in ","Pixel "+metertoPixel(radiusInMeterAKW,meterPerPixel));
        mProvider.setRadius(meterToPixel(radiusInMeterAKW,meterPerPixel));

        Log.d("CameraChange","radius in pixel"+meterToPixel(radiusInMeterAKW,meterPerPixel));
        mOverlay.clearTileCache();

    }

    public int meterToPixel(int rad, double mPerPixel){
        int x = (int)(rad / mPerPixel);
        if(x <=0) {
            x = 0;
        }
        return x;
    }

    public double MeterPerPixel(){
        float results[] = new float[1];
        Location.distanceBetween(
                mMap.getProjection().getVisibleRegion().nearLeft.latitude,
                mMap.getProjection().getVisibleRegion().nearLeft.longitude,
                mMap.getProjection().getVisibleRegion().farRight.latitude,
                mMap.getProjection().getVisibleRegion().farRight.longitude,
                results); //visible distance on display
        LinearLayout layout = (LinearLayout) this.findViewById(R.id.maps_activity);
        double meterPerPixel = results[0] / Math.hypot(layout.getWidth(),layout.getHeight());
        return meterPerPixel;
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
