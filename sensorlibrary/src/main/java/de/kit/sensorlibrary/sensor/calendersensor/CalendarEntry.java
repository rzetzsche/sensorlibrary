package de.kit.sensorlibrary.sensor.calendersensor;

/**
 * Created by Robert on 05.02.2015.
 */
public class CalendarEntry {

    long instanceID;
    long instanceBegin;
    long instanceEnd;
    String eventName;
    String calenderName;

    public CalendarEntry(long instanceID, long instanceBegin, long instanceEnd, String eventName, String calenderName) {
        this.instanceID = instanceID;
        this.instanceBegin = instanceBegin;
        this.instanceEnd = instanceEnd;
        this.eventName = eventName;
        this.calenderName = calenderName;
    }

    public long getInstanceID() {
        return instanceID;
    }

    public long getInstanceBegin() {
        return instanceBegin;
    }

    public long getInstanceEnd() {
        return instanceEnd;
    }

    public String getEventName() {
        return eventName;
    }

    public String getCalenderName() {
        return calenderName;
    }
}
