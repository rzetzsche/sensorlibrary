package de.kit.sensorlibrary.sensor.useractivitysensor;

import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class UserActivityChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public UserActivityChangedEvent(UserActivitySensor source) {
        super(source);
    }

    public DetectedActivity getDetectedActivity() {
        return ((UserActivitySensor) source).getDetectedActivity();
    }

    public String getDetectedActivityAsString() {
        return ((UserActivitySensor) source).getActivityAsString();
    }

    public List<DetectedActivity> getDetectedActivities() {
        return ((UserActivitySensor) source).getDetectedActivities();
    }
}
