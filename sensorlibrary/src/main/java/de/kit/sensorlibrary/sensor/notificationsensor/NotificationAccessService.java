package de.kit.sensorlibrary.sensor.notificationsensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationAccessService extends NotificationListenerService {
    public static final String NOTIFICATION_ALL = "NOTIFICATION_ALL";
    public static final String NOTIFICATION_POSTED = "NOTIFICATION_POSTED";
    public static final String NOTIFICATION_REMOVED = "NOTIFICATION_REMOVED";
    public static final String MESSENGER = "NOTIFICATION_MESSENGER";
    private static final String TAG = NotificationAccessService.class.getSimpleName();

    private Messenger messenger;
    private NotificationServiceReceiver receiver;
    private boolean isUnregistered = false;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationAccessService.class.getName());
        receiver = new NotificationServiceReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        if (!isUnregistered) {
            unregisterReceiver(receiver);
            isUnregistered = true;
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putParcelable(NotificationAccessService.NOTIFICATION_POSTED, sbn);
        bundle.putParcelableArray(NotificationAccessService.NOTIFICATION_ALL, getActiveNotifications());
        message.setData(bundle);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putParcelable(NotificationAccessService.NOTIFICATION_REMOVED, sbn);
        bundle.putParcelableArray(NotificationAccessService.NOTIFICATION_ALL, getActiveNotifications());
        message.setData(bundle);

        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    class NotificationServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Bundle extras = intent.getExtras();
                NotificationAccessService.this.messenger = (Messenger) extras
                        .get(NotificationAccessService.MESSENGER);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
            StatusBarNotification[] allNotifications = NotificationAccessService.this
                    .getActiveNotifications();
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putParcelableArray(NotificationAccessService.NOTIFICATION_ALL, allNotifications);
            message.setData(bundle);
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (!isUnregistered) {
                unregisterReceiver(this);
                isUnregistered = true;
            }

        }
    }
}
