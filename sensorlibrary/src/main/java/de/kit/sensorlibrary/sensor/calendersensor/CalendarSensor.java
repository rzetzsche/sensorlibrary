package de.kit.sensorlibrary.sensor.calendersensor;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract.Instances;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * Created by Robert on 05.02.2015.
 */
public class CalendarSensor extends AbstractSensorImpl<CalendarStatusChangedListener> {

    private static final String[] INSTANCE_PROJECTION = new String[]{
            Instances.EVENT_ID,
            Instances.BEGIN,
            Instances.END,
            Instances.TITLE,
            Instances.CALENDAR_DISPLAY_NAME,
            Instances.DESCRIPTION
    };
    private static final String CALENDAR_URI = "content://com.android.calendar/instances/when";
    private static final String TAG = CalendarSensor.class.getSimpleName();
    private ContentResolver cr;
    private Uri uriInst;
    private CalenderObserver calendarObserver;
    private boolean isCalendarEntry;

    public boolean isCalendarEntry() {
        return isCalendarEntry;
    }


    public CalendarSensor(Context context) {
        cr = context.getContentResolver();
        uriInst = buildUri();
    }

    public void startCalendarListener(boolean includeAllDayEntries) {
        super.throwNewExceptionWhenSensorNotOpen();
        if (calendarObserver == null) {
            calendarObserver = new CalenderObserver(new Handler(), includeAllDayEntries);
            cr.registerContentObserver(Uri.parse(CALENDAR_URI), true, calendarObserver);
            isCalendarEntry = isActualAnCalendarEntry(includeAllDayEntries);
        } else {
            Log.e(TAG, "startCalendarListener was already called");
        }

    }

    public void stopCalendarChangeListener() {
        super.throwNewExceptionWhenSensorNotOpen();
        if (calendarObserver != null) {
            cr.unregisterContentObserver(calendarObserver);
            calendarObserver = null;
        } else {
            Log.e(TAG, "startCalendarListener was not called");
        }
    }

    public List<CalendarEntry> getCalenderEntries(long time) {
        super.throwNewExceptionWhenSensorNotOpen();
        return getCalenderEntries(System.currentTimeMillis(), (System.currentTimeMillis() + time));
    }


    public boolean isActualAnCalendarEntry(boolean includeAllDayEntries) {
        super.throwNewExceptionWhenSensorNotOpen();
        return getActualCalendarEntries(includeAllDayEntries).size() > 0;
    }

    public List<CalendarEntry> getActualCalendarEntries(boolean includeAllDayEntries) {
        super.throwNewExceptionWhenSensorNotOpen();
        String selectionInst;
        String[] selectionInstArgs;
        if (!includeAllDayEntries) {
            selectionInst = "((" + Instances.BEGIN + " < ?) AND ("
                    + Instances.END + " > ?) AND (" + Instances.ALL_DAY + " = ?))";
            selectionInstArgs = new String[]{
                    String.valueOf(System.currentTimeMillis()),
                    String.valueOf(System.currentTimeMillis()),
                    String.valueOf(String.valueOf(includeAllDayEntries ? 1 : 0))
            };
        } else {
            selectionInst = "((" + Instances.BEGIN + " < ?) AND ("
                    + Instances.END + " > ?))";
            selectionInstArgs = new String[]{
                    String.valueOf(System.currentTimeMillis()),
                    String.valueOf(System.currentTimeMillis()),
            };
        }

        return queryWithArgsAndInst(selectionInst, selectionInstArgs);
    }

    public List<CalendarEntry> getCalenderEntries(long beginTime, long endTime) {
        super.throwNewExceptionWhenSensorNotOpen();
        String selectionInst = "((" + Instances.BEGIN + " > ?) AND ("
                + Instances.END + " < ?))";
        String[] selectionInstArgs = new String[]{
                String.valueOf(beginTime),
                String.valueOf(endTime)
        };
        return queryWithArgsAndInst(selectionInst, selectionInstArgs);
    }

    private Uri buildUri() {
        Uri.Builder builder = Uri.parse(
                CALENDAR_URI).buildUpon();
        ContentUris.appendId(builder, Long.MIN_VALUE);
        ContentUris.appendId(builder, Long.MAX_VALUE);
        return builder.build();

    }

    private List<CalendarEntry> queryWithArgsAndInst(String selectionInst, String[] selectionInstArgs) {
        List<CalendarEntry> calenderEntries = new ArrayList<CalendarEntry>();
        Cursor curInst = cr.query(uriInst, INSTANCE_PROJECTION, selectionInst,
                selectionInstArgs, null);
        while (curInst.moveToNext()) {
            long instanceID = curInst.getLong(0);
            long instanceBegin = curInst.getLong(1);
            long instanceEnd = curInst.getLong(2);
            String eventName = curInst.getString(3);
            String calenderName = curInst.getString(4);
            calenderEntries.add(new CalendarEntry(instanceID, instanceBegin, instanceEnd, eventName, calenderName));
        }
        curInst.close();
        return calenderEntries;
    }

    @Override
    public String[] getLog() {
        return new String[]{String.valueOf(isCalendarEntry())};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"isCalendarEntry"};
    }


    class CalenderObserver extends ContentObserver {

        private final boolean includeAllDayEntries;


        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public CalenderObserver(Handler handler, boolean includeAllDayEntries) {
            super(handler);
            this.includeAllDayEntries = includeAllDayEntries;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            boolean isActualCalendarEntry = CalendarSensor.this.isActualAnCalendarEntry(includeAllDayEntries);
            if (isCalendarEntry != isActualCalendarEntry) {
                isCalendarEntry = isActualCalendarEntry;
                CalenderStatusChangedEvent calenderStatusChangedEvent = new CalenderStatusChangedEvent(CalendarSensor.this);
                for (CalendarStatusChangedListener listener : listeners) {
                    listener.onValueChanged(calenderStatusChangedEvent);
                }
            }
        }
    }
}
