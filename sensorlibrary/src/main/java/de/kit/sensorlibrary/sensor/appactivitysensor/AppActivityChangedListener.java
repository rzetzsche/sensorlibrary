package de.kit.sensorlibrary.sensor.appactivitysensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 30.01.2015.
 */
public interface AppActivityChangedListener extends ValueChangedListener<AppActivityEvent> {
    @Override
    void onValueChanged(AppActivityEvent event);
}
