package de.kit.sensorlibrary.sensor.ambientlightsensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 03.02.2015.
 */
public interface LightSensorChangedListener extends ValueChangedListener<LightChangedEvent> {
    @Override
    void onValueChanged(LightChangedEvent e);
}
