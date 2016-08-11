
package de.kit.sensorlibrary.sensor.locationsensor;

import de.kit.sensorlibrary.sensor.ValueChangedListener;

public interface LocationChangedListener extends ValueChangedListener<LocationChangedEvent> {

    @Override
    void onValueChanged(LocationChangedEvent event);
}
