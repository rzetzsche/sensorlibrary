package de.kit.sensorlibrary.sensor.speechsensor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import de.kit.sensorlibrary.sensor.AbstractSensorPermissionImpl;

/**
 * Created by Robert on 05.02.2015.
 */
public class SpeechSensor extends AbstractSensorPermissionImpl<SpeechChangedListener> {
    private int listeningTime;
    private int updateTime;
    private boolean isSpeech;

    public SpeechSensor(Context context, int listeningTime, int updateTime) {
        super(context);
        this.listeningTime = listeningTime;
        this.updateTime = updateTime;
    }

    public boolean isSpeech() {
        return isSpeech;
    }

    @Override
    public void openSensor() {
        super.openSensor();
    }

    @Override
    public void permissionGranted(boolean granted) {
        super.permissionGranted(granted);
        if (granted) {
            startSpeechRecorder(listeningTime, updateTime);
        }
    }

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.RECORD_AUDIO};
    }


    @Override
    public void closeSensor() {
        super.closeSensor();
    }

    @Override
    public String[] getLog() {
        return new String[]{String.valueOf(isSpeech), "", "", ""};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"isSpeech"};
    }

//    public void startSpeechRecorder() {
//        startSpeechRecorder(30000, 1000);
//    }

    public void startSpeechRecorder(int listeningTime, int updateTime) {
        throwNewExceptionWhenSensorNotOpen();
        final SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        SpeechRecognitionListener speechRecognitionListener = new SpeechRecognitionListener(speechRecognizer, intent, updateTime, listeningTime);
        speechRecognizer.setRecognitionListener(speechRecognitionListener);
        speechRecognizer.startListening(intent);
    }

    private class SpeechTimer extends CountDownTimer {
        private boolean started = false;
        private SpeechRecognizer speechRecognizer;
        private Intent intent;


        private SpeechTimer(SpeechRecognizer speechRecognizer, Intent intent, int updateTime, int listeningTime) {
            super(listeningTime, updateTime);
            this.speechRecognizer = speechRecognizer;
            this.intent = intent;
        }

        private boolean isStarted() {
            return started;
        }

        private void setStarted(boolean started) {
            this.started = started;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            speechRecognizer.startListening(intent);
        }

        @Override
        public void onFinish() {
            SpeechSensor.this.isSpeech = false;
            SpeechEvent speechEvent = new SpeechEvent(SpeechSensor.this);
            for (SpeechChangedListener listener : listeners) {
                listener.onValueChanged(speechEvent);
            }

        }
    }

    private class SpeechRecognitionListener implements RecognitionListener {
        SpeechTimer timer;

        SpeechRecognitionListener(SpeechRecognizer speechRecognizer, Intent intent, int updateTime, int listeningTime) {
            timer = new SpeechTimer(speechRecognizer, intent, updateTime, listeningTime);
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            if (!timer.isStarted()) {
                timer.start();
                timer.setStarted(true);
            }

        }

        @Override
        public void onBeginningOfSpeech() {
            SpeechSensor.this.isSpeech = true;
            SpeechEvent speechEvent = new SpeechEvent(SpeechSensor.this);
            for (SpeechChangedListener listener : listeners) {
                listener.onValueChanged(speechEvent);
            }
            timer.cancel();
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results) {
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }
}
