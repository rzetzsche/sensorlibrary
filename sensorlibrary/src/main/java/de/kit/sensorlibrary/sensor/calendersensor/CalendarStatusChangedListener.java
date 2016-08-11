package de.kit.sensorlibrary.sensor.calendersensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 05.02.2015.
 */
public interface CalendarStatusChangedListener extends ValueChangedListener<CalenderStatusChangedEvent> {
    @Override
    void onValueChanged(CalenderStatusChangedEvent event);
}
