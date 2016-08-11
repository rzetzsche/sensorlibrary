package de.kit.sensorlibrary.sensor.timesensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class TimeChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public TimeChangedEvent(TimeSensor source) {
        super(source);
    }
}
