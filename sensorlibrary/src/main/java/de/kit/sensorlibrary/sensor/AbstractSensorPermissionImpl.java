package de.kit.sensorlibrary.sensor;

import android.content.Context;
import android.content.Intent;
import android.os.Messenger;

import de.kit.sensorlibrary.permissions.PermissionActivity;
import de.kit.sensorlibrary.permissions.PermissionHandler;


/**
 * Created by Robert Zetzsche on 16.03.2017.
 */

public abstract class AbstractSensorPermissionImpl<T extends ValueChangedListener> extends AbstractSensorImpl<T> implements PermissionInterface {
    public static final int PERMISSIONS_NOT_GRANTED = 0;
    public static final int LOCATION_NOT_ENABLED = 1;
    private final PermissionHandler handler;
    protected Context context;
    private ErrorCallback errorCallback;

    public AbstractSensorPermissionImpl(Context context) {
        this.context = context;
        handler = new PermissionHandler(this);
    }

    private void grantPermissions() {
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra("permissions", getPermissions());
        intent.putExtra("handler", new Messenger(handler));
        context.startActivity(intent);
    }

    @Override
    public void permissionGranted(boolean granted) {
        if (errorCallback != null) {
            if (!granted) {
                setError(PERMISSIONS_NOT_GRANTED);
            }
        }
    }

    protected void setError(int errorCode) {
        if (errorCallback != null) {
            errorCallback.onError(errorCode);
        }
    }

    @Override
    public void openSensor() {
        super.openSensor();
        grantPermissions();
    }

    public abstract String[] getPermissions();

    public void setErrorCallback(ErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    public void removeErrorCallback() {
        this.errorCallback = null;
    }

    public interface ErrorCallback {
        void onError(int errorCode);
    }
}
