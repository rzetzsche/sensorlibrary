package de.kit.sensorlibrary.sensor.appactivitysensor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

class AppActivityHandler extends Handler {

    private final String bundleLocationIdentifier;
    private AppActivityInterface appActivityInterface;

    public AppActivityHandler(AppActivityInterface callback,
                              String _bundleIdentifier) {
        super();
        this.bundleLocationIdentifier = _bundleIdentifier;
        this.appActivityInterface = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        this.appActivityInterface.handleAppActivity((bundle
                .getParcelable(this.bundleLocationIdentifier)));

    }
}
