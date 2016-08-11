package de.kit.sensorlibrary.sensor.ambientnoisesensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 03.02.2015.
 */
public interface SpeechAndNoiseSensorChangedListener extends ValueChangedListener<SpeechAndNoiseEvent> {
    @Override
    void onValueChanged(SpeechAndNoiseEvent e);
}
