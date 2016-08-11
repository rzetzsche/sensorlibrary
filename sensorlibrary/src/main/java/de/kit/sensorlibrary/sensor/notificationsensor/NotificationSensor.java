package de.kit.sensorlibrary.sensor.notificationsensor;

import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * Created by Robert on 28.01.2015.
 */
public class NotificationSensor extends AbstractSensorImpl<NotificationChangedListener> implements NotificationAccessInterface {
    public static final String IDENTIFIER = "notifications";
    private static final String TAG = NotificationSensor.class.getSimpleName();
    private List<StatusBarNotification> notifications;
    private Context context;
    private Intent intent;
    private NotificationAccessServiceHandler handler;
    private StatusBarNotification notification;
    private boolean isRemoved;
    private boolean isPosted;

    public NotificationSensor(Context context) {
        this.notifications = new ArrayList<>();
        this.context = context;
        handler = new NotificationAccessServiceHandler(this);
        intent = new Intent(NotificationAccessService.class.getName());
        intent.putExtra(NotificationAccessService.MESSENGER, new Messenger(handler));
        context.sendBroadcast(intent);
    }

    public List<StatusBarNotification> getNotifications() {
        super.throwNewExceptionWhenSensorNotOpen();
        return notifications;
    }

    @Override
    public void handleNotifications(Parcelable[] notifications) {
        this.notifications.clear();
        for (Parcelable parcelable : notifications) {
            if (parcelable instanceof StatusBarNotification) {
                this.notifications.add((StatusBarNotification) parcelable);
            }
        }
    }

    public boolean isPosted() {
        return isPosted;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    @Override
    public void handlePostedNotification(Parcelable notification) {
        throwNewExceptionWhenSensorNotOpen();
        if (notification instanceof StatusBarNotification) {
            this.notification = (StatusBarNotification) notification;
            this.isRemoved = false;
            this.isPosted = true;
            NotificationChangedEvent event = new NotificationChangedEvent(this);
            for (NotificationChangedListener listener : listeners) {
                listener.onValueChanged(event);
            }
        } else if (!super.isOpen()) {
            Log.e(TAG, "Sensor isn't open");
        }

    }

    public StatusBarNotification getNotification() {
        return notification;
    }

    @Override
    public void handleRemovedNotification(Parcelable notification) {
        throwNewExceptionWhenSensorNotOpen();
        if (notification instanceof StatusBarNotification) {
            this.notification = (StatusBarNotification) notification;
            this.isRemoved = true;
            this.isPosted = false;
            NotificationChangedEvent event = new NotificationChangedEvent(this);
            for (NotificationChangedListener listener : listeners) {
                listener.onValueChanged(event);
            }
        } else if (!super.isOpen()) {
            Log.e(TAG, "Sensor isn't open");
        }

    }


    @Override
    public String[] getLog() {
        if (notifications == null) {
            return new String[2];
        } else if (notifications.size() == 0) {
            return new String[]{"0", "[]"};
        } else {
            String notificationNames = "[";
            for (StatusBarNotification statusBarNotification : notifications) {
                notificationNames += statusBarNotification.getPackageName() + ",";
            }
            return new String[]{String.valueOf(notifications.size()),
                    notificationNames.substring(0, notificationNames.length() - 1) + "]"};
        }
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"count", "notifications"};
    }

}
