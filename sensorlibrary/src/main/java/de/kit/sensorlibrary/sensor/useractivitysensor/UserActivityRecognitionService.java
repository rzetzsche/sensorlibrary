package de.kit.sensorlibrary.sensor.useractivitysensor;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by Robert on 30.01.2015.
 */
public class UserActivityRecognitionService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UserActivityRecognitionService() {
        super(UserActivityRecognitionService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult recognitionResult = ActivityRecognitionResult.extractResult(intent);
            Intent i = new Intent("de.kit.sensorlibrary.sensors.useractivitysensor.callback");
            i.putExtra("DETECTED_ACTIVITY", recognitionResult.getMostProbableActivity());
            i.putExtra("DETECTED_ACTIVITIES", new ArrayList<DetectedActivity>(recognitionResult.getProbableActivities()));
            sendBroadcast(i);
        }
    }


}