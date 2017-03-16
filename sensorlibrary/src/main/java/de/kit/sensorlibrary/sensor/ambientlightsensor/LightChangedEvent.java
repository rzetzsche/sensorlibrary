package de.kit.sensorlibrary.sensor.ambientlightsensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class LightChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    LightChangedEvent(LightSensor source) {
        super(source);
    }

    public float getLumen() {
        return ((LightSensor) source).getSensorValue();
    }

    @Override
    public String toString() {
        return "Lumen: " + getLumen();
    }
}
