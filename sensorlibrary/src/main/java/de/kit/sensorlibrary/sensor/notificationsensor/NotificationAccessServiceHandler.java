package de.kit.sensorlibrary.sensor.notificationsensor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

class NotificationAccessServiceHandler extends Handler {

    private NotificationAccessInterface _notificationAccessInterface;

    public NotificationAccessServiceHandler(
            NotificationAccessInterface callback) {
        super();
        this._notificationAccessInterface = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        Parcelable[] notifications = bundle.getParcelableArray(NotificationAccessService.NOTIFICATION_ALL);
        Parcelable removedNotification = bundle.getParcelable(NotificationAccessService.NOTIFICATION_REMOVED);
        Parcelable postedNotification = bundle.getParcelable(NotificationAccessService.NOTIFICATION_POSTED);
        if (notifications != null) {
            this._notificationAccessInterface.handleNotifications(notifications);
        }
        if (removedNotification != null) {
            this._notificationAccessInterface.handleRemovedNotification(removedNotification);
        }
        if (postedNotification != null) {
            this._notificationAccessInterface.handlePostedNotification(postedNotification);
        }

    }
}
