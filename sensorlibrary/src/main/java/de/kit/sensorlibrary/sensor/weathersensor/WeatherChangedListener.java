package de.kit.sensorlibrary.sensor.weathersensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 27.03.2015.
 */
public interface WeatherChangedListener extends ValueChangedListener<WeatherChangedEvent> {
    @Override
    void onValueChanged(WeatherChangedEvent event);
}
