package mse.hqevaluator;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.Iterator;
import java.util.List;

import mse.hqevaluator.entities.MotorwayRamp;
import mse.hqevaluator.entities.NuclearPowerPlant;
import mse.hqevaluator.persistence.DbHelper;
import mse.hqevaluator.persistence.MotorwayRampTable;
import mse.hqevaluator.persistence.NuclearPowerPlantTable;

public class MapsActivity extends ActionBarActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private MapView mapView = null;

    private Location location;

    private GoogleApiClient googleApiClient;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mapView == null) {
            mapView = (MapView)this.findViewById(R.id.mapview);
            if (mapView != null) {
                setUpMap();
            }
        }
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void setUpMap() {
        Log.i(this.getLocalClassName(), "setUpMap: Setting up map.");
        placeNuclearPowerPlants();
        placeMotorwayRamps();
        mapView.setCenter(new LatLng(47.379102, 8.535660));
        Marker marker = new Marker(mapView, "Foo", "Bar", new LatLng(47.379102, 8.535660));
        mapView.addMarker(marker);
        Log.i(this.getLocalClassName(), "setUpMap: Map set up.");
    }

    void placeNuclearPowerPlants() {
        NuclearPowerPlantTable table = dbHelper.getNuclearPowerPlantTable();
        List<NuclearPowerPlant> nuclearPowerPlants = table.getAll();

        Iterator<NuclearPowerPlant> iterator = nuclearPowerPlants.iterator();

        while(iterator.hasNext()) {
            NuclearPowerPlant plant = iterator.next();
            LatLng pos = new LatLng(plant.Latitude, plant.Longitude);
            Marker marker = new Marker(this.mapView, plant.Name, plant.Description, pos);
            this.mapView.addMarker(marker);
            Log.i(this.getLocalClassName(), "setUpMap: Placing marker for " + plant.Name);
        }
    }

    void placeMotorwayRamps() {
        MotorwayRampTable table = dbHelper.getMotorwayRampTable();
        List<MotorwayRamp> motorwayRamps = table.getAll();
        Iterator<MotorwayRamp> iterator = motorwayRamps.iterator();

        while(iterator.hasNext()) {
            MotorwayRamp ramp = iterator.next();
            LatLng pos = new LatLng(ramp.Latitude, ramp.Longitude);
            Marker marker = new Marker(this.mapView, ramp.Name, ramp.Motorway, pos);
            this.mapView.addMarker(marker);
            Log.i(this.getLocalClassName(), "setUpMap: Placing marker for " + ramp.Name);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        Helpers.showToast("Disconnected", getApplicationContext());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Helpers.showToast("Connection failed", getApplicationContext());
    }
}
