package de.kit.sensorlibrary.sensor;

import java.util.EventObject;

/**
 * Created by Robert on 11.05.2015.
 */
public abstract class ValueChangedEvent extends EventObject{
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public ValueChangedEvent(Object source) {
        super(source);
    }
}
