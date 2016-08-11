package de.kit.sensorlibrary.sensor.bluetoothsensor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * Created by Robert on 29.01.2015.
 */
public class BluetoothSensor extends AbstractSensorImpl<BluetoothDeviceChangedListener> implements BluetoothTimeInterface {
    public static final String IDENTIFIER = "bluetooth";
    private final IntentFilter filter;
    private final BluetoothAdapter mBluetoothAdapter;
    private final BluetoothDiscoveredDevicesReceiver bluetoothDiscoveredDevicesReceiver;
    private List<BluetoothDevice> deviceList;
    private Context context;
    /**
     * Initialize Sensor and start one time a Discovery
     *
     * @param context Context to initialize the Sensor
     */
    public BluetoothSensor(Context context) {
        this.context = context;
        deviceList = new ArrayList<BluetoothDevice>();
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDiscoveredDevicesReceiver = new BluetoothDiscoveredDevicesReceiver();
    }

    /**
     * starts a BluetoothDiscovery for one Time
     */
    public void startDiscovery() {
        super.throwNewExceptionWhenSensorNotOpen();
        if (!mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.startDiscovery();
        }
    }


    /**
     * closes Sensor
     */
    public void closeSensor() {
        super.closeSensor();
        context.unregisterReceiver(bluetoothDiscoveredDevicesReceiver);
    }

    @Override
    public String[] getLog() {
        if (deviceList.size() == 0) {
            return new String[]{"0", "[]", "[]"};
        } else {
            String macs = "[";
            String names = "[";
            for (BluetoothDevice bluetoothDevice : deviceList) {
                macs += bluetoothDevice.getAddress() + ",";
                names += bluetoothDevice.getName() + ",";
            }
            return new String[]{String.valueOf(getDeviceCount())
                    , names.substring(0, names.length() - 1) + "]"
                    , macs.substring(0, macs.length() - 1) + "]"};
        }
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"bluetoothCount", "bluetoothNames", "bluetoothMacs"};
    }


    /**
     * openSensor
     */
    public void openSensor() {
        super.openSensor();
        context.registerReceiver(bluetoothDiscoveredDevicesReceiver, filter);
    }

    public List<BluetoothDevice> getDeviceList() {
        super.throwNewExceptionWhenSensorNotOpen();
        return deviceList;
    }

    public int getDeviceCount() {
        super.throwNewExceptionWhenSensorNotOpen();
        return deviceList.size();
    }

    @Override
    public void handleNewRequest() {
        startDiscovery();
    }

    private class BluetoothDiscoveredDevicesReceiver extends BroadcastReceiver {

        private final String TAG = BluetoothDiscoveredDevicesReceiver.class.getSimpleName();
        private List<BluetoothDevice> newDeviceList;

        public BluetoothDiscoveredDevicesReceiver() {
            super();
            newDeviceList = new ArrayList<>();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!newDeviceList.contains(device)) {
                    newDeviceList.add(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (!(deviceList.containsAll(newDeviceList) && deviceList.size() == newDeviceList.size())) {
                    deviceList = new ArrayList<>(newDeviceList);
                    BluetoothDeviceChangedEvent bluetoothDeviceChangedEvent = new BluetoothDeviceChangedEvent(BluetoothSensor.this);
                    for (BluetoothDeviceChangedListener listener : listeners) {
                        listener.onValueChanged(bluetoothDeviceChangedEvent);
                    }
                }
                newDeviceList.clear();
            }

        }
    }
}
