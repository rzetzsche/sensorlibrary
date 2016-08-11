package de.kit.sensorlibrary.sensor.ambientlightsensor;

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
public class LightSensor extends AbstractSensorImpl<LightSensorChangedListener> {
    public static final String IDENTIFIER = "ambientlight";
    private LightListener lightListener;
    private SensorManager sensorManager;
    private final long updateTime;
    private long lastUpdate = 0;
    //Time between updates 1s
    private static int SENSOR_DELAY = 1000;

    private float sensorValue;

    public LightSensor(Context context, long updateTime) {
        lightListener = new LightListener();
        sensorManager = (SensorManager) context.getSystemService(
                Context.SENSOR_SERVICE);
        this.updateTime = updateTime;
    }

    public float getSensorValue() {
        throwNewExceptionWhenSensorNotOpen();
        return sensorValue;
    }


    @Override
    public void openSensor() {
        super.openSensor();
        sensorManager.registerListener(lightListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SENSOR_DELAY);
    }

    @Override
    public void closeSensor() {
        super.closeSensor();
        sensorManager.unregisterListener(lightListener);
    }

    @Override
    public String[] getLog() {
        return new String[]{String.valueOf(getSensorValue())};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"lumen"};
    }

    public class LightListener implements
            SensorEventListener {
        private float sensorValue;


        @Override
        public void onSensorChanged(SensorEvent event) {
            long actualTime = System.currentTimeMillis();
            sensorValue = event.values[0];
            if (LightSensor.this.sensorValue != sensorValue && (actualTime - lastUpdate) > updateTime) {
                LightSensor.this.sensorValue = sensorValue;
                LightChangedEvent accelerometerChangedEvent = new LightChangedEvent(LightSensor.this);
                for (LightSensorChangedListener listener : listeners) {
                    listener.onValueChanged(accelerometerChangedEvent);
                }
                lastUpdate = actualTime;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not Used

        }
    }

}
