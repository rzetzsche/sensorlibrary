package de.kit.sensorlibrary.sensor.timesensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;
import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 12.02.2015.
 */
public interface TimeChangedListener extends ValueChangedListener<ValueChangedEvent> {
    @Override
    void onValueChanged(ValueChangedEvent event);
}
