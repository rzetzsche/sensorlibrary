package de.kit.sensorlibrary.sensor.timesensor;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Robert on 12.02.2015.
 */
public class TimeAlarmIntentService extends IntentService {
    public static final String BUNDLE_IDENTIFIER = "TIME";
    public static final String MESSENGER_IDENTIFIER = "TIME_MESSENGER";
    public static final String ALERT_IDENTIFIER = "ALERT";
    public static final String TRIGGER_IDENTIFIER = "TRIGGER";
    public static final String MODE_IDENTIFIER = "MODE";
    private static final String TAG = TimeAlarmIntentService.class.getSimpleName();


    public TimeAlarmIntentService() {
        super(TimeAlarmIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "TRIGGER");
        Bundle extras = intent.getExtras();
        long alertTime = extras.getLong("alertTime");
        AlarmMode alarmMode = AlarmMode.valueOf(extras.getString("mode"));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(alertTime);
        Log.d(TAG, "TRIGGER " + cal.getTime().toString());
        Messenger messenger = (Messenger) extras
                .get(MESSENGER_IDENTIFIER);
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putLong(ALERT_IDENTIFIER, alertTime);
        bundle.putLong(TRIGGER_IDENTIFIER, System.currentTimeMillis());
        bundle.putString(MODE_IDENTIFIER, alarmMode.toString());
        message.setData(bundle);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}