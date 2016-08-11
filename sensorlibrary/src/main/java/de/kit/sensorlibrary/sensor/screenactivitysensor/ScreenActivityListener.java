
package de.kit.sensorlibrary.sensor.screenactivitysensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

public interface ScreenActivityListener extends ValueChangedListener<ScreenActivityChangedEvent> {

    @Override
    void onValueChanged(ScreenActivityChangedEvent event);
}
