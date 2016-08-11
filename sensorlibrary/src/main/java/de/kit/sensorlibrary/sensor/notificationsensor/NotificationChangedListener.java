package de.kit.sensorlibrary.sensor.notificationsensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 28.01.2015.
 */
public interface NotificationChangedListener extends ValueChangedListener<NotificationChangedEvent> {
    @Override
    void onValueChanged(NotificationChangedEvent event);
}
