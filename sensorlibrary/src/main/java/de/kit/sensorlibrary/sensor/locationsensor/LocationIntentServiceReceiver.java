package de.kit.sensorlibrary.sensor.locationsensor;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationIntentServiceReceiver extends IntentService implements
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    public static final String BUNDLE_IDENTIFIER = "LOCATION_RECEIVER";

    public static final String MESSENGER_IDENTIFIER = "LOCATION_RECEIVER_MESSENGER";

    private GoogleApiClient mGoogleApiClient;

    private Messenger messenger;

    private long refreshTime;

    public LocationIntentServiceReceiver() {
        super(LocationIntentServiceReceiver.class.getSimpleName());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            Message message = Message.obtain();
            Bundle bundleCallback = new Bundle();
            bundleCallback.putDoubleArray(BUNDLE_IDENTIFIER, new double[]{
                    lastLocation.getLatitude(), lastLocation.getLongitude()
            });
            message.setData(bundleCallback);
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(600000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("LOCATION", "changed" + location.getLatitude() + " " + location.getLongitude());
        Message message = Message.obtain();
        Bundle bundleCallback = new Bundle();
        bundleCallback.putDoubleArray(BUNDLE_IDENTIFIER, new double[]{
                location.getLatitude(), location.getLongitude()
        });
        message.setData(bundleCallback);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        this.messenger = (Messenger) extras
                .get(MESSENGER_IDENTIFIER);
        this.refreshTime = extras.getLong("refreshTime");
        mGoogleApiClient.connect();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(LocationIntentServiceReceiver.this)
                .build();
    }

}
