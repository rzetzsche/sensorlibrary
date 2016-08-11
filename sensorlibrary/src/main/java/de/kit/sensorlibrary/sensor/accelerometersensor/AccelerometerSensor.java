package de.kit.sensorlibrary.sensor.accelerometersensor;

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
public class AccelerometerSensor extends AbstractSensorImpl<AccelerometerSensorChangedListener> {
    public static final String IDENTIFIER = "accelerometer";
    private AccelerometerListener accelerometerListener;
    private SensorManager sensorManager;

    private float x = Float.NaN;
    private float y = Float.NaN;
    private float z = Float.NaN;
    private float sensorValue = Float.NaN;
    private int updateTime;
    //Time between updates 1s
    private static int SENSOR_DELAY = 1000;


    public AccelerometerSensor(Context context, int updateTime) {
        this.updateTime = updateTime;
        accelerometerListener = new AccelerometerListener();
        sensorManager = (SensorManager) context.getSystemService(
                Context.SENSOR_SERVICE);
    }


    @Override
    public void openSensor() {
        super.openSensor();
        sensorManager.registerListener(accelerometerListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SENSOR_DELAY);
    }

    public float getX() {
        super.throwNewExceptionWhenSensorNotOpen();
        return x;
    }

    public float getY() {
        super.throwNewExceptionWhenSensorNotOpen();
        return y;
    }

    public float getZ() {
        super.throwNewExceptionWhenSensorNotOpen();
        return z;
    }

    public float getSensorValue() {
        super.throwNewExceptionWhenSensorNotOpen();
        return sensorValue;
    }

    public float[] getValuesAsArray() {
        super.throwNewExceptionWhenSensorNotOpen();
        return new float[]{x, y, z, sensorValue};
    }

    @Override
    public void closeSensor() {
        super.throwNewExceptionWhenSensorNotOpen();
        super.closeSensor();

        sensorManager.unregisterListener(accelerometerListener);
    }

    @Override
    public String[] getLog() {
        return new String[]{String.valueOf(getX()), String.valueOf(getY()),
                String.valueOf(getZ()), String.valueOf(getSensorValue())};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"x", "y", "z", "acceleration"};
    }

    private class AccelerometerListener implements
            SensorEventListener {

        private static final String TAG = "AccelerometerService";
        long lastUpdate = 0;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not Used

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            long actualTime = System.currentTimeMillis();
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            float sensorValue = ((x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH));
            if ((actualTime - this.lastUpdate) > updateTime) {
                if (AccelerometerSensor.this.sensorValue != sensorValue) {
                    AccelerometerSensor.this.x = x;
                    AccelerometerSensor.this.y = y;
                    AccelerometerSensor.this.z = z;
                    AccelerometerSensor.this.sensorValue = sensorValue;

                    AccelerometerChangedEvent accelerometerChangedEvent = new AccelerometerChangedEvent(AccelerometerSensor.this);
                    for (AccelerometerSensorChangedListener listener : listeners) {
                        listener.onValueChanged(accelerometerChangedEvent);
                    }

                }
                this.lastUpdate = actualTime;
            }
        }

    }

}
