package de.kit.sensorlibrary.sensor.notificationsensor;

import android.service.notification.StatusBarNotification;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class NotificationChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public NotificationChangedEvent(NotificationSensor source) {
        super(source);
    }

    public StatusBarNotification getNotification() {
        return ((NotificationSensor) source).getNotification();
    }

    public boolean isRemoved() {
        return ((NotificationSensor) source).isRemoved();
    }

    public boolean isPosted() {
        return ((NotificationSensor) source).isRemoved();
    }


}
