package de.kit.sensorlibrary.permissions;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import de.kit.sensorlibrary.sensor.PermissionInterface;

/**
 * Created by Robert Zetzsche on 16.03.2017.
 */

public class PermissionHandler extends Handler {
    PermissionInterface permissionInterface;

    public PermissionHandler(PermissionInterface permissionInterface) {
        this.permissionInterface = permissionInterface;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        permissionInterface.permissionGranted(bundle.getBoolean("isGranted"));
    }
}
