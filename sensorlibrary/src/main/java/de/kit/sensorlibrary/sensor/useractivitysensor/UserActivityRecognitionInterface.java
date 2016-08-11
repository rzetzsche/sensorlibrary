package de.kit.sensorlibrary.sensor.useractivitysensor;

import android.os.Parcelable;

import java.util.List;

interface UserActivityRecognitionInterface {

    void handleActivity(Parcelable detectedActivity);

    void handleActivities(List<Parcelable> detectedActivities);


}
