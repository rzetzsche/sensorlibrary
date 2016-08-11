package de.kit.sensorlibrary.sensor.notificationsensor;

import android.os.Parcelable;

interface NotificationAccessInterface {
    public void handleNotifications(Parcelable[] notifications);

    public void handlePostedNotification(Parcelable notification);

    public void handleRemovedNotification(Parcelable notification);
}
