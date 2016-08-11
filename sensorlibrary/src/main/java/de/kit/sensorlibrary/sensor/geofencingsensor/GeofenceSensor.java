package de.kit.sensorlibrary.sensor.geofencingsensor;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * Created by Robert on 04.02.2015.
 */

public class GeofenceSensor extends AbstractSensorImpl<GeofenceChangedListener> implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    public static final String IDENTIFIER = "geofence";
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private List<Geofence> geofenceList;
    private PendingIntent pendingIntent;
    private GeofenceEventParcelable geofenceEventParcelable;
    private IntentFilter intentFilter;
    private GeofenceBroadcastReceiver geofenceBroadcastReceiver;

    public GeofenceSensor(Context context) {
        geofenceList = new ArrayList<Geofence>();
        this.context = context;
        intentFilter = new IntentFilter("de.kit.sensorlibrary.sensor.geofence");
        geofenceBroadcastReceiver = new GeofenceBroadcastReceiver();
    }

    public GeofenceEventParcelable getGeofenceEventParcelable() {
        return geofenceEventParcelable;
    }

    public void addGeofence(String name, double latitude, double longitude, float radius) {
        Geofence geofence = new Geofence.Builder()
                .setRequestId(name)
                .setCircularRegion(latitude, longitude, radius)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        geofenceList.add(geofence);
        updateGeofences();
    }

    private void updateGeofences() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                GeofencingRequest request = new GeofencingRequest.Builder().addGeofences(geofenceList).build();
                LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, pendingIntent);
                LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, request, pendingIntent);
            }
        }
    }

    public void removeGeofence(String name) {
        for (Geofence geofence : geofenceList) {
            if (geofence.getRequestId().equals(name)) {
                geofenceList.remove(geofence);
                break;
            }
        }
        updateGeofences();
    }

    @Override
    public void openSensor() {
        super.openSensor();
        createPendingIntent();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        context.registerReceiver(geofenceBroadcastReceiver, intentFilter);
    }

    @Override
    public void closeSensor() {
        super.closeSensor();
        LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, pendingIntent);
        mGoogleApiClient.disconnect();
        context.unregisterReceiver(geofenceBroadcastReceiver);
    }

    @Override
    public String[] getLog() {
        if (geofenceEventParcelable == null) {
            return new String[5];
        } else {
            return new String[]{
                    geofenceEventParcelable.getLocation().toString(),
                    getGeofenceEventParcelable().getGeofenceNames().toString(),
                    String.valueOf(getGeofenceEventParcelable().getGeofenceTransition()),
                    String.valueOf(geofenceEventParcelable.getLocation().getLatitude()),
                    String.valueOf(geofenceEventParcelable.getLocation().getLongitude())};
        }

    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"geofenceLocation", "geofenceNames", "geofenceTransition", "geofenceLatitude", "geofenceLongitude"};
    }


    @Override
    public void onConnected(Bundle bundle) {
        GeofencingRequest request = new GeofencingRequest.Builder().addGeofences(geofenceList).build();
        LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, request, pendingIntent);
    }

    private void createPendingIntent() {
        if (this.pendingIntent == null) {
            Intent intent = new Intent(context, GeofenceIntentService.class);
            this.pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    class GeofenceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Parcelable parcelable = intent.getParcelableExtra(GeofenceIntentService.BUNDLE_IDENTIFIER);
            if (parcelable instanceof GeofenceEventParcelable) {
                geofenceEventParcelable = (GeofenceEventParcelable) parcelable;
                GeofenceSensor.this.geofenceEventParcelable = geofenceEventParcelable;
                GeofenceChangedEvent geofenceChangedEvent = new GeofenceChangedEvent(GeofenceSensor.this);
                for (GeofenceChangedListener listener : listeners) {
                    listener.onValueChanged(geofenceChangedEvent);
                }
            }
        }
    }
}
