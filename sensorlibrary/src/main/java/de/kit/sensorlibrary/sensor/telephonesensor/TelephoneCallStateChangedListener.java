package de.kit.sensorlibrary.sensor.telephonesensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 03.02.2015.
 */
public interface TelephoneCallStateChangedListener extends ValueChangedListener<TelephoneCallStateChangedEvent> {
    @Override
    void onValueChanged(TelephoneCallStateChangedEvent event);
}
