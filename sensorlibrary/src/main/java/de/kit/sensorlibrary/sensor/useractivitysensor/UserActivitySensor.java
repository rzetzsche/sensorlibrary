package de.kit.sensorlibrary.sensor.useractivitysensor;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * Created by Robert on 29.01.2015.
 */
public class UserActivitySensor extends AbstractSensorImpl<UserActivityChangedListener> implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, UserActivityRecognitionInterface {
    public static final String IDENTIFIER = "useractivity";
    private Context context;
    private IntentFilter filter;
    private ActivitySensorCallbackReceiver receiver;
    private GoogleApiClient mGoogleApiClient;
    private DetectedActivity detectedActivity;
    private PendingIntent mActivityRecognitionPendingIntent;
    private int updateTime;
    private List<DetectedActivity> detectedActivities;

    public UserActivitySensor(Context context, int updateTime) {
        this.context = context;
        filter = new IntentFilter("de.kit.sensorlibrary.sensors.useractivitysensor.callback");
        receiver = new ActivitySensorCallbackReceiver();
        this.updateTime = updateTime;

    }

    @Override
    public void openSensor() {
        super.openSensor();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void closeSensor() {
        super.closeSensor();
        context.unregisterReceiver(receiver);
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient, mActivityRecognitionPendingIntent);
        mGoogleApiClient.disconnect();
    }

    @Override
    public String[] getLog() {
        if (detectedActivity == null) {
            return new String[2];
        } else {
            return new String[]{getActivityAsString(), String.valueOf(detectedActivity.getConfidence())};
        }
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"detectedActivity", "activityProbability"};
    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent i = new Intent(context, UserActivityRecognitionService.class);
        mActivityRecognitionPendingIntent = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, updateTime, mActivityRecognitionPendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void handleActivity(Parcelable detectedActivity) {
        DetectedActivity activity = (DetectedActivity) detectedActivity;
        if (this.detectedActivity == null) {
            this.detectedActivity = activity;
            UserActivityChangedEvent event = new UserActivityChangedEvent(this);
            for (UserActivityChangedListener listener : listeners) {
                listener.onValueChanged(event);
            }
        } else {
            if (this.detectedActivity.getType() != activity.getType()) {
                this.detectedActivity = activity;
                UserActivityChangedEvent event = new UserActivityChangedEvent(this);
                for (UserActivityChangedListener listener : listeners) {
                    listener.onValueChanged(event);
                }

            }
        }

    }

    @Override
    public void handleActivities(List<Parcelable> detectedActivities) {
        List<DetectedActivity> detectedActivityList = new ArrayList<DetectedActivity>();
        for (Parcelable activity : detectedActivities) {
            if (activity instanceof DetectedActivity) {
                DetectedActivity detectedActivity = (DetectedActivity) activity;
                detectedActivityList.add(detectedActivity);
            }
        }
        this.detectedActivities = detectedActivityList;
    }

    public DetectedActivity getDetectedActivity() {
        super.throwNewExceptionWhenSensorNotOpen();
        return detectedActivity;
    }

    public List<DetectedActivity> getDetectedActivities() {
        super.throwNewExceptionWhenSensorNotOpen();
        return detectedActivities;
    }

    public String getActivityAsString() {
        if (detectedActivity != null) {
            switch (detectedActivity.getType()) {
                case DetectedActivity.STILL:
                    return "STILL";
                case DetectedActivity.IN_VEHICLE:
                    return "IN_VEHICLE";
                case DetectedActivity.ON_FOOT:
                    return "ON_FOOT";
                case DetectedActivity.ON_BICYCLE:
                    return "ON_BICYCLE";
                case DetectedActivity.RUNNING:
                    return "RUNNING";
                case DetectedActivity.TILTING:
                    return "TILTING";
                case DetectedActivity.WALKING:
                    return "WALKING";
                case DetectedActivity.UNKNOWN:
                    return "UNKNOWN";
                default:
                    return "";
            }

        }
        return "";
    }

    private class ActivitySensorCallbackReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handleActivity(intent.getParcelableExtra("DETECTED_ACTIVITY"));
        }
    }

}
