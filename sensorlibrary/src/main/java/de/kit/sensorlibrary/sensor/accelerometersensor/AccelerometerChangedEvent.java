package de.kit.sensorlibrary.sensor.accelerometersensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class AccelerometerChangedEvent extends ValueChangedEvent {

    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    AccelerometerChangedEvent(AccelerometerSensor source) {
        super(source);
    }

    public float getX() {
        return ((AccelerometerSensor) source).getX();
    }

    public float getY() {
        return ((AccelerometerSensor) source).getY();
    }

    public float getZ() {
        return ((AccelerometerSensor) source).getZ();
    }

    public float getAcceleration() {
        return ((AccelerometerSensor) source).getSensorValue();
    }

    @Override
    public String toString() {
        return "X: " + getX() + " Y: " + getY() + " Z: " + getZ() + " acc: " + getAcceleration();
    }
}
