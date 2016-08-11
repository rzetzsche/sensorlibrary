package de.kit.sensorlibrary.sensor.timesensor;

/**
 * Created by Robert on 12.02.2015.
 */
interface TimeAccessInterface {
    void handleTime(long alertMillis, long triggeredMillis, AlarmMode alarmMode);

    void generateNewAlarms(long millis);
}
