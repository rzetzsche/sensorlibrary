package de.kit.sensorlibrary.sensor.calendersensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class CalenderStatusChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public CalenderStatusChangedEvent(CalendarSensor source) {
        super(source);
    }

    public boolean isCalendarEntry() {
        return ((CalendarSensor) source).isCalendarEntry();
    }
}
