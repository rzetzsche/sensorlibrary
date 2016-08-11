package de.kit.sensorlibrary.sensor.appactivitysensor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.os.Parcelable;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * @deprecated This method was deprecated in API level 21. As of LOLLIPOP, this method is no longer available to third party applications:
 * the introduction of document-centric recents means it can leak person information to the caller.
 * For backwards compatibility, it will still return a small subset of its data:
 * at least the caller's own tasks, and possibly some other tasks such as home that are known to not be sensitive.
 */
public class AppActivitySensor extends AbstractSensorImpl<AppActivityChangedListener> implements AppActivityInterface {
    private Context context;
    private int updateTime;
    private AppActivityHandler handler;
    private Intent appActivityIntent;
    private ComponentName actualActivity;

    public AppActivitySensor(Context context, int updateTime) {
        this.context = context;
        this.updateTime = updateTime;
        handler = new AppActivityHandler(this, AppActivityIntentService.BUNDLE_IDENTIFIER);
        appActivityIntent = new Intent(context, AppActivityIntentService.class);
        appActivityIntent.putExtra(AppActivityIntentService.MESSENGER_IDENTIFIER, new Messenger(handler));
        appActivityIntent.putExtra("UPDATE_TIME", updateTime);

    }

    @Override
    public void openSensor() {
        super.openSensor();
        context.startService(appActivityIntent);
    }

    @Override
    public void closeSensor() {
        super.closeSensor();
        context.stopService(appActivityIntent);
    }

    @Override
    public String[] getLog() {
        if (actualActivity == null) {
            return new String[]{""};
        } else {
            return new String[]{actualActivity.getPackageName()};
        }
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"actualAppName"};
    }

    public ComponentName getActualActivity() {
        super.throwNewExceptionWhenSensorNotOpen();
        return actualActivity;
    }

    @Override
    public void handleAppActivity(Parcelable parcelable) {
        if (parcelable instanceof ComponentName) {
            this.actualActivity = (ComponentName) parcelable;
            AppActivityEvent appActivityEvent = new AppActivityEvent(AppActivitySensor.this);
            for (AppActivityChangedListener listener : listeners) {
                listener.onValueChanged(appActivityEvent);
            }

        }
    }
}
