package de.kit.sensorlibrary.sensor.telephonesensor;

import de.kit.sensorlibrary.sensor.ValueChangedEvent;

/**
 * Created by Robert on 11.05.2015.
 */
public class TelephoneCallStateChangedEvent extends ValueChangedEvent {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public TelephoneCallStateChangedEvent(TelephoneSensor source) {
        super(source);
    }

    public String getCallStateAsString() {
        return ((TelephoneSensor) source).getCallStateAsString();
    }

    public int getCallState() {
        return ((TelephoneSensor) source).getCallState();
    }

    public String getNumber() {
        return ((TelephoneSensor) source).getNumber();
    }
}
