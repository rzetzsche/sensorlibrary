package de.kit.sensorlibrary.sensor.timesensor;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Messenger;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * Created by Robert on 29.01.2015.
 */
public class TimeSensor extends AbstractSensorImpl<TimeChangedListener> implements TimeAccessInterface {
    private static final String TAG = TimeSensor.class.getSimpleName();
    public static final String IDENTIFIER = "time";
    private Context context;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private Calendar minBoundInterval;
    private Calendar maxBoundInterval;
    private Calendar minBoundRandom;
    private Calendar maxBoundRandom;
    private int count;
    private int interval;
    private boolean isRandom;
    private boolean isInterval;
    private boolean wasRandom;
    private boolean wasInterval;
    private SharedPreferences sharedPreferences;

    public TimeSensor(Context context) {
        this.calendar = Calendar.getInstance();
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.sharedPreferences = context.getSharedPreferences("de.kit.esmdummy", Context.MODE_PRIVATE);
    }

    public String getActualTime() {
        super.throwNewExceptionWhenSensorNotOpen();
        this.calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND);
    }

    public String getActualDate() {
        super.throwNewExceptionWhenSensorNotOpen();
        this.calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "."
                + calendar.get(Calendar.YEAR);
    }

    public String getActualDayTime() {
        super.throwNewExceptionWhenSensorNotOpen();
        this.calendar = Calendar.getInstance();
        int actualHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (actualHour >= 6 && actualHour < 12) {
            return "Morning";
        } else if (actualHour >= 12 && actualHour < 15) {
            return "Noon";
        } else if (actualHour >= 15 && actualHour < 18) {
            return "Afternoon";
        } else if (actualHour >= 18 && actualHour < 22) {
            return "Evening";
        } else {
            return "Night";
        }

    }

    public String getActualDay() {
        super.throwNewExceptionWhenSensorNotOpen();
        this.calendar = Calendar.getInstance();
        String day = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
        }
        return day;
    }

    /**
     * Find out if the actual Day is a Weekday or
     *
     * @return
     */
    public boolean isDayWeekend() {
        super.throwNewExceptionWhenSensorNotOpen();
        this.calendar = Calendar.getInstance();
        switch (getActualDay()) {
            case "SUNDAY":
                return true;
            case "SATURDAY":
                return true;
            default:
                return false;
        }
    }

    public String getMonthString() {
        super.throwNewExceptionWhenSensorNotOpen();
        this.calendar = Calendar.getInstance();
        String month = "";
        switch (calendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                month = "JANUARY";
                break;
            case Calendar.FEBRUARY:
                month = "FEBRUARY";
                break;
            case Calendar.MARCH:
                month = "MARCH";
                break;
            case Calendar.APRIL:
                month = "APRIL";
                break;
            case Calendar.MAY:
                month = "MAY";
                break;
            case Calendar.JUNE:
                month = "JUNE";
                break;
            case Calendar.JULY:
                month = "JULY";
                break;
            case Calendar.AUGUST:
                month = "AUGUST";
                break;
            case Calendar.SEPTEMBER:
                month = "SEPTEMBER";
                break;
            case Calendar.OCTOBER:
                month = "OCTOBER";
                break;
            case Calendar.NOVEMBER:
                month = "NOVEMBER";
                break;
            case Calendar.DECEMBER:
                month = "DECEMBER";
                break;
        }
        return month;
    }

    public String getTimeInMillis() {
        super.throwNewExceptionWhenSensorNotOpen();
        this.calendar = Calendar.getInstance();
        return calendar.getTimeInMillis() + "";
    }

    public void addRepeatingToAlarmManager(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        addRepeatingToAlarmManager(calendar, TimeAlarmIntentService.class, AlarmMode.NORMAL);
    }

    private void addRepeatingToAlarmManager(int hour, int minute, int second, Class serviceClass, AlarmMode alarmMode) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        addRepeatingToAlarmManager(calendar, serviceClass, alarmMode);
    }

    private void addRepeatingToAlarmManager(Calendar calendar, Class serviceClass, AlarmMode alarmMode) {
        PendingIntent pendingIntent = getPendingIntent(serviceClass, calendar.getTimeInMillis(), alarmMode);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void addTimeToAlarmManager(int hour, int minute, int second, Class serviceClass, AlarmMode alarmMode) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        addTimeToAlarmManager(calendar, serviceClass, alarmMode);
    }

    public void addTimeToAlarmManager(Calendar calendar, Class serviceClass, AlarmMode alarmMode) {
        addTimeToAlarmManager(calendar.getTimeInMillis(), serviceClass, alarmMode);
    }

    private void addTimeToAlarmManager(long millis, Class serviceClass, AlarmMode alarmMode) {
        PendingIntent pendingIntent = getPendingIntent(serviceClass, millis, alarmMode);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
    }

    private PendingIntent getPendingIntent(Class<? extends IntentService> service, long millis, AlarmMode alarmMode) {
        Intent intent = new Intent(context, service);
        intent.putExtra("alertTime", millis);
        intent.putExtra("mode", alarmMode.toString());
        if (service.equals(TimeAlarmIntentService.class)) {
            intent.putExtra(TimeAlarmIntentService.MESSENGER_IDENTIFIER
                    , new Messenger(new TimeHandler(this, TimeAlarmIntentService.BUNDLE_IDENTIFIER)));
        } else {
            intent.putExtra(TimeAlarmDailyIntentService.MESSENGER_IDENTIFIER
                    , new Messenger(new TimeHandler(this, TimeAlarmDailyIntentService.BUNDLE_IDENTIFIER)));
        }
        return PendingIntent.getService(context, (int) millis, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public boolean wasInterval() {
        Boolean bool = new Boolean(wasInterval);
        wasInterval = false;
        return bool;
    }


    public boolean wasRandom() {
        Boolean bool = new Boolean(wasRandom);
        wasRandom = false;
        return bool;
    }


    @Override
    public void handleTime(long alertMillis, long triggeredMillis, AlarmMode alarmMode) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alertMillis);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(triggeredMillis);
        if (!(calendar.get(Calendar.HOUR_OF_DAY) < calendar1.get(Calendar.HOUR_OF_DAY)
                || (calendar.get(Calendar.HOUR_OF_DAY) == calendar1.get(Calendar.HOUR_OF_DAY)
                && calendar.get(Calendar.MINUTE) < calendar1.get(Calendar.MINUTE)))) {
            if (alarmMode.equals(AlarmMode.RANDOM)) {
                wasRandom = true;
            } else if (alarmMode.equals(AlarmMode.INTERVAL)) {
                wasInterval = true;
            }
            TimeChangedEvent timeChangedEvent = new TimeChangedEvent(this);
            Log.d("TIME", listeners.toString());
            for (TimeChangedListener listener : listeners) {
                listener.onValueChanged(timeChangedEvent);
            }
        }

    }


    @Override
    public void generateNewAlarms(long millis) {
        if (isRandom) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("randomAlarms", new HashSet<String>());
            editor.commit();
            minBoundRandom = getCalenderForNextDay(minBoundRandom);
            maxBoundRandom = getCalenderForNextDay(maxBoundRandom);
            setRandomTimesToAlarmClock(count, minBoundRandom, maxBoundRandom);
        }
        if (isInterval) {
            minBoundInterval = getCalenderForNextDay(minBoundInterval);
            maxBoundInterval = getCalenderForNextDay(maxBoundInterval);
            setIntervalTimesToAlarmClock(interval, minBoundInterval, maxBoundInterval);
        }
    }

    private Calendar getCalenderForNextDay(Calendar calendar) {
        Calendar calendarNextDay = Calendar.getInstance();
        calendarNextDay.setTimeInMillis(System.currentTimeMillis() + 10000);
        calendarNextDay.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendarNextDay.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        calendarNextDay.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
        return calendarNextDay;
    }

    public void setRandomTimesToAlarmClock(int count, int minHour, int minMinute, int maxHour, int maxMinute) {
        Calendar minBound = getCalendar(minHour, minMinute);
        Calendar maxBound = getCalendar(maxHour, maxMinute);
        setRandomTimesToAlarmClock(count, minBound, maxBound);
    }

    public void setIntervalTimesToAlarmClock(int interval, int minHour, int minMinute, int maxHour, int maxMinute) {
        Calendar minBound = getCalendar(minHour, minMinute);
        Calendar maxBound = getCalendar(maxHour, maxMinute);
        setIntervalTimesToAlarmClock(interval, minBound, maxBound);
    }

    public void setIntervalTimesToAlarmClock(int interval, Calendar minBound, Calendar maxBound) {
        minBoundInterval = minBound;
        maxBoundInterval = maxBound;
        this.interval = interval;
        long window = maxBound.getTimeInMillis() - minBound.getTimeInMillis();
        int intervalCount = Math.round(window / interval);
        for (int i = 0; i <= intervalCount; i++) {
            long millis = minBound.getTimeInMillis() + i * interval;
            Log.d(TAG, getCalendar(millis).getTime().toString());
            addTimeToAlarmManager(millis, TimeAlarmIntentService.class, AlarmMode.INTERVAL);
        }
        if (!isInterval) {
            isInterval = true;
            addRepeatingToAlarmManager(23, 59, 59, TimeAlarmDailyIntentService.class, AlarmMode.NORMAL);
        }

    }

    private Calendar getCalendar(int minHour, int minMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, minHour);
        calendar.set(Calendar.MINUTE, minMinute);
        return calendar;
    }

    private Calendar getCalendar(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
    }

    /**
     * Sets random alarms to AlarmManager
     * <p/>
     * CAUTION: Can used only one time, else the backup mechanism won't function properly
     *
     * @param count    count of random alarms
     * @param minBound minimal time for alarm
     * @param maxBound maximal time for alarm
     */
    public void setRandomTimesToAlarmClock(int count, Calendar minBound, Calendar maxBound) {
        minBoundRandom = minBound;
        maxBoundRandom = maxBound;
        this.count = count;
        Set<String> savedAlarms = sharedPreferences.getStringSet("randomAlarms", new HashSet<String>());
        if (savedAlarms.size() == 0) {
            Set<String> randomAlarms = new HashSet<>();
            for (int i = 0; i < count; i++) {
                Calendar randomTime = getRandomTime(minBound, maxBound);
                addTimeToAlarmManager(randomTime, TimeAlarmIntentService.class, AlarmMode.RANDOM);
                Log.d(TAG, randomTime.getTime().toString());
                randomAlarms.add(String.valueOf(randomTime.getTimeInMillis()));
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("randomAlarms", randomAlarms);
            editor.commit();
            try {
                writeAlarmsToLog(randomAlarms);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            for (String alarm : savedAlarms) {
                long alarmInMillis = Long.parseLong(alarm);
                Calendar alarmCalender = Calendar.getInstance();
                alarmCalender.setTimeInMillis(alarmInMillis);
                addTimeToAlarmManager(alarmInMillis, TimeAlarmIntentService.class, AlarmMode.RANDOM);
                Log.d(TAG, alarmCalender.getTime().toString());
            }
        }
        if (!isRandom) {
            isRandom = true;
            addRepeatingToAlarmManager(23, 59, 59, TimeAlarmDailyIntentService.class, AlarmMode.NORMAL);
        }

    }

    private void writeAlarmsToLog(Set<String> randomAlarms) throws IOException {
        File alarmLog = new File("sdcard/de.kit.esmdummy/alarms.log");
        if (!alarmLog.exists()) {
            alarmLog.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(alarmLog, true));
        for (String alarm : randomAlarms) {
            Calendar alarmCalender = Calendar.getInstance();
            alarmCalender.setTimeInMillis(Long.parseLong(alarm));
            writer.write(alarmCalender.getTime().toString());
            writer.newLine();
        }
        writer.flush();
        writer.close();

    }

    private Calendar getRandomTime(Calendar minBound, Calendar maxBound) {
        long timeWindow = maxBound.getTimeInMillis() - minBound.getTimeInMillis();
        Random random = new Random();
        long randomAlert = Math.round(random.nextDouble() * timeWindow);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(minBound.getTimeInMillis() + randomAlert);
        return calendar;
    }

    @Override
    public String[] getLog() {
        return new String[]{getActualDate(), getActualDay(), getActualDayTime(), getActualTime()};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"date", "weekDay", "dayTime", "time"};
    }

}
