package de.kit.sensorlibrary.sensor.appactivitysensor;

import android.content.ComponentName;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class AppActivityEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public AppActivityEvent(AppActivitySensor source) {
        super(source);
    }

    public ComponentName getNoiseMeasurement() {
        return ((AppActivitySensor) source).getActualActivity();
    }

}
