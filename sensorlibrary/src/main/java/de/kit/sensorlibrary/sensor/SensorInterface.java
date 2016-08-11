package de.kit.sensorlibrary.sensor;

/**
 * Created by Robert on 29.01.2015.
 */
public interface SensorInterface<T extends ValueChangedListener> {

    void openSensor();

    void closeSensor();

    String[] getLog();

    String[] getLogColumns();

    void addValueChangedListener(T listener);

    void removeValueChangedListener(T listener);
}
