package de.kit.sensorlibrary.sensor.geofencingsensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class GeofenceChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public GeofenceChangedEvent(GeofenceSensor source) {
        super(source);
    }

    public GeofenceEventParcelable getGeofenceEvent() {
        return ((GeofenceSensor) source).getGeofenceEventParcelable();
    }
}
