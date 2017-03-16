package de.kit.sensorlibrary.sensor.speechsensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class SpeechEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    SpeechEvent(SpeechSensor source) {
        super(source);
    }

    public boolean isSpeech() {
        return ((SpeechSensor) source).isSpeech();
    }

    @Override
    public String toString() {
        return " isSpeech: " + isSpeech();
    }

}
