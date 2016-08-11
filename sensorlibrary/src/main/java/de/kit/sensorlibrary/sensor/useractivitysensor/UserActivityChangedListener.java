package de.kit.sensorlibrary.sensor.useractivitysensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 29.01.2015.
 */
public interface UserActivityChangedListener extends ValueChangedListener<UserActivityChangedEvent> {
    @Override
    void onValueChanged(UserActivityChangedEvent event);
}
