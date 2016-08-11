package de.kit.sensorlibrary.sensor.proximitysensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * Created by Robert on 03.02.2015.
 */
public class ProximitySensor extends AbstractSensorImpl<ProximityChangedListener> {
    private ProximityValue value = ProximityValue.NEAR;
    private ProximityListener proximityListener;
    private SensorManager sensorManager;
    //Time between updates 1s
    private static int SENSOR_DELAY = 1000;

    public ProximitySensor(Context context) {
        proximityListener = new ProximityListener();
        sensorManager = (SensorManager) context.getSystemService(
                Context.SENSOR_SERVICE);
    }

    public ProximityValue getValue() {
        super.throwNewExceptionWhenSensorNotOpen();
        return value;
    }

    @Override
    public void openSensor() {
        super.openSensor();
        sensorManager.registerListener(proximityListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SENSOR_DELAY);

    }

    @Override
    public void closeSensor() {
        super.closeSensor();
        sensorManager.unregisterListener(proximityListener);
    }

    @Override
    public String[] getLog() {
        return new String[]{value.toString()};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"proximityValue"};
    }

    public enum ProximityValue {NEAR, FAR}

    private class ProximityListener implements
            SensorEventListener {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not Used
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float sensorValue = event.values[0];
            ProximityValue value;
            if (sensorValue == 0.0) {
                value = ProximityValue.NEAR;
            } else {
                value = ProximityValue.FAR;
            }

            if (ProximitySensor.this.value != value) {
                ProximitySensor.this.value = value;
                ProximityChangedEvent proximityChangedEvent = new ProximityChangedEvent(ProximitySensor.this);
                for (ProximityChangedListener listener : listeners) {
                    listener.onValueChanged(proximityChangedEvent);
                }
            }
        }
    }
}
