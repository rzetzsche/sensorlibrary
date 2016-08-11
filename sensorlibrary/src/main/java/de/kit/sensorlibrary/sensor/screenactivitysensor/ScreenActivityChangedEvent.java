package de.kit.sensorlibrary.sensor.screenactivitysensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class ScreenActivityChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public ScreenActivityChangedEvent(ScreenActivitySensor source) {
        super(source);
    }

    public boolean isActive() {
        return ((ScreenActivitySensor) source).isActive();
    }
}
