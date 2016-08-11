package de.kit.sensorlibrary.sensor.bluetoothsensor;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class BluetoothDeviceChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public BluetoothDeviceChangedEvent(BluetoothSensor source) {
        super(source);
    }

    public List<BluetoothDevice> getDeviceList() {
        return ((BluetoothSensor) source).getDeviceList();
    }
}
