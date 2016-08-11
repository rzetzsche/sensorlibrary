package de.kit.sensorlibrary.sensor;

import java.util.EventListener;

/**
 * Created by Robert on 11.05.2015.
 */
 public interface ValueChangedListener<T extends ValueChangedEvent> extends EventListener{
    void onValueChanged(T event);
}
