package de.kit.sensorlibrary.sensor.locationsensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class LocationChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public LocationChangedEvent(LocationSensor source) {
        super(source);
    }

    public double getLongitude() {
        return ((LocationSensor) source).getLongitude();
    }

    public double getLatitude() {
        return ((LocationSensor) source).getLatitude();
    }
}
