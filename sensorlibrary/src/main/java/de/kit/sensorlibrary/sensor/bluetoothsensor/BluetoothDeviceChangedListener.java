package de.kit.sensorlibrary.sensor.bluetoothsensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 29.01.2015.
 */
public interface BluetoothDeviceChangedListener extends ValueChangedListener<BluetoothDeviceChangedEvent> {
    @Override
    void onValueChanged(BluetoothDeviceChangedEvent event);
}
