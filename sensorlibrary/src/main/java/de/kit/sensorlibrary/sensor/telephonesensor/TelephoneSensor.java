package de.kit.sensorlibrary.sensor.telephonesensor;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * Created by Robert on 03.02.2015.
 */
public class TelephoneSensor extends AbstractSensorImpl<TelephoneCallStateChangedListener> {
    public static final String IDENTIFIER = "telephone";
    private final TelephonyManager telephonyMgr;
    private TelephoneListener listener;
    private int callState;
    private String number;

    public TelephoneSensor(Context context) {
        telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        listener = new TelephoneListener();
    }

    public int getCallState() {
        super.throwNewExceptionWhenSensorNotOpen();
        return callState;
    }

    public String getCallStateAsString() {
        super.throwNewExceptionWhenSensorNotOpen();
        switch (callState) {
            case TelephonyManager.CALL_STATE_IDLE:
                return "CALL_STATE_IDLE";
            case TelephonyManager.CALL_STATE_OFFHOOK:
                return "CALL_STATE_OFFHOOK";
            case TelephonyManager.CALL_STATE_RINGING:
                return "CALL_STATE_RINGING";
            default:
                return null;
        }
    }

    public String getNumber() {
        super.throwNewExceptionWhenSensorNotOpen();
        return number;
    }

    @Override
    public void openSensor() {

        super.openSensor();
        telephonyMgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void closeSensor() {
        super.closeSensor();
        telephonyMgr.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public String[] getLog() {
        return new String[]{getCallStateAsString()};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"telephoneState"};
    }

    public class TelephoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TelephoneSensor.this.callState = state;
            TelephoneSensor.this.number = incomingNumber;
            TelephoneCallStateChangedEvent telephoneCallStateChangedEvent = new TelephoneCallStateChangedEvent(TelephoneSensor.this);
            for (TelephoneCallStateChangedListener listener : listeners) {
                listener.onValueChanged(telephoneCallStateChangedEvent);
            }
        }
    }
}
