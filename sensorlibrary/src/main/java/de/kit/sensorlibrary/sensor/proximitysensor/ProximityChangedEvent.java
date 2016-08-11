package de.kit.sensorlibrary.sensor.proximitysensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class ProximityChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public ProximityChangedEvent(ProximitySensor source) {
        super(source);
    }

    public ProximitySensor.ProximityValue getProximityValue() {
        return ((ProximitySensor) source).getValue();
    }
}
