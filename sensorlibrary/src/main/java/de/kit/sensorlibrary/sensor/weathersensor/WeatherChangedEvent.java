package de.kit.sensorlibrary.sensor.weathersensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class WeatherChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public WeatherChangedEvent(WeatherSensor source) {
        super(source);
    }

    public double getTemperature() {
        return ((WeatherSensor) source).getTemperature();
    }

    public String getCondition() {
        return ((WeatherSensor) source).getCondition();
    }
}
