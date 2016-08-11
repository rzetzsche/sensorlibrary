package de.kit.sensorlibrary.sensor.timesensor;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by Robert on 25.03.2015.
 */
public class TimeAlarmDailyIntentService extends IntentService {
    public static final String BUNDLE_IDENTIFIER = "TIME_DAY";
    public static final String MESSENGER_IDENTIFIER = "TIME_DAY_MESSENGER";

    public TimeAlarmDailyIntentService() {
        super(TimeAlarmDailyIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Messenger messenger = (Messenger) extras
                .get(MESSENGER_IDENTIFIER);
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_IDENTIFIER,System.currentTimeMillis());
        message.setData(bundle);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
