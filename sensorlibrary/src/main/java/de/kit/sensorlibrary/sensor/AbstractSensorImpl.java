package de.kit.sensorlibrary.sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 03.02.2015.
 */
public abstract class AbstractSensorImpl<T extends ValueChangedListener> implements SensorInterface<T> {
    private boolean isOpen = false;
    protected List<T> listeners = new ArrayList<T>();

    @Override
    public void openSensor() {
        isOpen = true;
    }

    @Override
    public void closeSensor() {
        isOpen = false;
    }

    @Override
    public void addValueChangedListener(T listener) {
        listeners.add(listener);
    }

    @Override
    public void removeValueChangedListener(T listener) {
        listeners.remove(listener);
    }

    public void throwNewExceptionWhenSensorNotOpen() {
        if (isOpen == false) {
            throw new IllegalStateException("Sensor isn't open");
        }
    }


    public boolean isOpen() {
        return isOpen;
    }


}
