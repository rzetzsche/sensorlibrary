package de.kit.sensorlibrary.sensor.ambientnoisesensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class AmbientNoiseEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public AmbientNoiseEvent(AmbientNoiseSensor source) {
        super(source);
    }

    public AmbientNoiseMeasurement getNoiseMeasurement() {
        return ((AmbientNoiseSensor) source).getMeasurement();
    }

    @Override
    public String toString() {
        return "Min: " + getNoiseMeasurement().getMinNoise() +
                " Max: " + getNoiseMeasurement().getMaxNoise() +
                " Avg: " + getNoiseMeasurement().getAverageNoise();
    }

}
