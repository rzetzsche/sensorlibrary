package de.kit.sensorlibrary.sensor.screenactivitysensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

public class ScreenActivitySensor extends AbstractSensorImpl<ScreenActivityListener> {
    public static final String IDENTIFIER = "screenactivity";
    private Context context;
    private ScreenReceiver screenReceiver;
    private boolean isActive = true;
    private IntentFilter filter;

    public ScreenActivitySensor(Context context) {
        this.context = context;
        screenReceiver = new ScreenReceiver();
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
    }

    @Override
    public void openSensor() {
        super.openSensor();
        context.registerReceiver(screenReceiver, filter);
    }

    @Override
    public void closeSensor() {
        super.closeSensor();
        context.unregisterReceiver(screenReceiver);
    }

    @Override
    public String[] getLog() {
        return new String[]{String.valueOf(isActive)};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"screenActive"};
    }

    public boolean isActive() {
        super.throwNewExceptionWhenSensorNotOpen();
        return isActive;
    }


    private class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean activated = true;
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                activated = false;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                activated = true;
            }

            ScreenActivitySensor.this.isActive = activated;
            ScreenActivityChangedEvent event = new ScreenActivityChangedEvent(ScreenActivitySensor.this);
            for (ScreenActivityListener listener : listeners) {
                listener.onValueChanged(event);
            }


        }

    }

}
