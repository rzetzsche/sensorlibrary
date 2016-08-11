package de.kit.sensorlibrary.sensor.appactivitysensor;

/**
 * Created by Robert on 30.01.2015.
 */

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class AppActivityIntentService extends IntentService {

    public static final String BUNDLE_IDENTIFIER = "APP_ACTIVITY";
    public static final String MESSENGER_IDENTIFIER = "APP_ACTIVITY_MESSENGER";
    private static final String TAG = AppActivityIntentService.class.getSimpleName();
    private ActivityManager activityManager;
    private String packageName;
    private Messenger messenger;
    private int updateTime;
    private Thread thread;

    public AppActivityIntentService() {
        super(AppActivityIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        activityManager = (ActivityManager) AppActivityIntentService.this
                .getSystemService(ACTIVITY_SERVICE);
        packageName = null;
        messenger = (Messenger) intent.getExtras().get(MESSENGER_IDENTIFIER);
        updateTime = intent.getIntExtra("UPDATE_TIME", 0);
        thread = new Thread(new AppActivityTask());
        thread.start();
    }

    @Override
    public void onDestroy() {
        thread.interrupt();
        super.onDestroy();
    }

    private class AppActivityTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                // The first in the list of RunningTasks is always the foreground task.
                ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
                if (!foregroundTaskInfo.topActivity.getPackageName().equals(packageName)) {
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BUNDLE_IDENTIFIER, foregroundTaskInfo.topActivity);
                    message.setData(bundle);
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                packageName = foregroundTaskInfo.topActivity.getPackageName();
                synchronized (this) {
                    try {
                        this.wait(updateTime);
                    } catch (InterruptedException e) {
                        stopSelf();
                        return;
                    }
                }

            }
        }
    }
}

