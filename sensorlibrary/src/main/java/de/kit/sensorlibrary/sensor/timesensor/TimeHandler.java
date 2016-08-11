package de.kit.sensorlibrary.sensor.timesensor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

class TimeHandler extends Handler {

    private final String bundleIdentifier;
    private TimeAccessInterface timeAccessInterface;

    public TimeHandler(TimeAccessInterface callback,
                       String bundleIdentifier) {
        super();
        this.bundleIdentifier = bundleIdentifier;
        this.timeAccessInterface = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        if (TimeAlarmIntentService.BUNDLE_IDENTIFIER.equals(bundleIdentifier)) {
            this.timeAccessInterface.handleTime(
                    bundle.getLong(TimeAlarmIntentService.ALERT_IDENTIFIER),
                    bundle.getLong(TimeAlarmIntentService.TRIGGER_IDENTIFIER),
                    AlarmMode.valueOf(bundle.getString(TimeAlarmIntentService.MODE_IDENTIFIER)));
        } else if (TimeAlarmDailyIntentService.BUNDLE_IDENTIFIER.equals(bundleIdentifier)) {
            this.timeAccessInterface.generateNewAlarms((bundle
                    .getLong(this.bundleIdentifier)));
        }

    }
}
