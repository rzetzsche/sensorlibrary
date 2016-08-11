package de.kit.sensorlibrary.sensor.accelerometersensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 03.02.2015.
 */
public interface AccelerometerSensorChangedListener extends ValueChangedListener<AccelerometerChangedEvent> {
    @Override
    void onValueChanged(AccelerometerChangedEvent e);
}
