package de.kit.sensorlibrary.sensor.locationsensor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

class LocationHandler extends Handler {

    private LocationAccessInterface locationAccessInterface;
    private final String bundleLocationIdentifier;

    public LocationHandler(LocationAccessInterface callback,
                           String bundleIdentifier) {
        super();
        this.bundleLocationIdentifier = bundleIdentifier;
        this.locationAccessInterface = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        if (this.bundleLocationIdentifier == LocationIntentServiceReceiver.BUNDLE_IDENTIFIER) {
            this.locationAccessInterface.onLocationChanged((bundle
                    .getDoubleArray(this.bundleLocationIdentifier)));
        }
    }
}
