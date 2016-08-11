package de.kit.sensorlibrary.sensor.ambientnoisesensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class SpeechAndNoiseEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public SpeechAndNoiseEvent(SpeechAndNoiseSensor source) {
        super(source);
    }

    public boolean isSpeech() {
        return ((SpeechAndNoiseSensor) source).isSpeech();
    }


    public AmbientNoiseMeasurement getNoiseMeasurement() {
        return ((SpeechAndNoiseSensor) source).getMeasurement();
    }

}
