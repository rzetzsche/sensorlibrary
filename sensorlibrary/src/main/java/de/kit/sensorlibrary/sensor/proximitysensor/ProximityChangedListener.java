package de.kit.sensorlibrary.sensor.proximitysensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 03.02.2015.
 */
public interface ProximityChangedListener extends ValueChangedListener<ProximityChangedEvent> {
    @Override
    void onValueChanged(ProximityChangedEvent event);
}
