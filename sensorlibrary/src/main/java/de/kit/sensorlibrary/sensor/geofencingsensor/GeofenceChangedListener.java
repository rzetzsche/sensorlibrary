package de.kit.sensorlibrary.sensor.geofencingsensor;


import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 05.02.2015.
 */
public interface GeofenceChangedListener extends ValueChangedListener<GeofenceChangedEvent> {
    @Override
    void onValueChanged(GeofenceChangedEvent event);
}
