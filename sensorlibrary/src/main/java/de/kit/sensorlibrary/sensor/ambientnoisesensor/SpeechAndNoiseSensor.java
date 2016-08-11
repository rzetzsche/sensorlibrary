package de.kit.sensorlibrary.sensor.ambientnoisesensor;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;
import de.kit.sensorlibrary.sensor.ValueChangedListener;

/**
 * Created by Robert on 05.02.2015.
 */
public class SpeechAndNoiseSensor extends AbstractSensorImpl<SpeechAndNoiseSensorChangedListener> {
    private MediaRecorder recorder;
    private Context context;
    private SpeechRecognitionListener speechRecognitionListener;
    private AmbientNoiseMeasurement measurement;

    public boolean isSpeech() {
        return isSpeech;
    }

    public AmbientNoiseMeasurement getMeasurement() {
        return measurement;
    }

    private boolean isSpeech;

    public SpeechAndNoiseSensor(Context context) {
        this.context = context;
    }


    @Override
    public void openSensor() {
        super.openSensor();
        initializeRecorder();
    }


    @Override
    public void closeSensor() {
        recorder.reset();
        super.closeSensor();
    }

    @Override
    public String[] getLog() {
        if (measurement == null) {
            return new String[]{String.valueOf(isSpeech), "", "", ""};
        } else {
            return new String[]{String.valueOf(isSpeech), String.valueOf(measurement.getMaxNoise())
                    , String.valueOf(measurement.getMinNoise()), String.valueOf(measurement.getAverageNoise())};
        }

    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"isSpeech", "MaxNoiseValue", "MinNoiseValue", "AverageNoise"};
    }

    private void initializeRecorder() {

        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(context.getFilesDir() + "/test.ogg");

        } catch (IllegalStateException e) {
            e.printStackTrace();

        }

    }

    public void startSpeechRecorder() {
        startSpeechRecorder(30000, 1000);
    }

    public void startSpeechRecorder(int listeningTime, int updateTime) {
        throwNewExceptionWhenSensorNotOpen();
        final SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognitionListener = new SpeechRecognitionListener(speechRecognizer, intent, updateTime, listeningTime);
        speechRecognizer.setRecognitionListener(speechRecognitionListener);
        speechRecognizer.startListening(intent);
    }

    public void startAmbientNoisMesurement(int updateTime, int measurementCount) {
        throwNewExceptionWhenSensorNotOpen();
        RecordAudioAsyncTask test = new RecordAudioAsyncTask(updateTime, measurementCount);
        test.execute();
    }


    private class RecordAudioAsyncTask extends AsyncTask<Void, Void, Void> {
        private int updateTime;
        private int measurementCount;

        RecordAudioAsyncTask(int updateTime, int measurementCount) {
            this.updateTime = updateTime;
            this.measurementCount = measurementCount;
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<Double> records = new ArrayList<Double>();
            try {
                recorder.prepare();
                recorder.start();
                recorder.getMaxAmplitude();
                records = record(measurementCount, updateTime);
                recorder.stop();
                recorder.release();
            } catch (IllegalStateException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            measurement = new AmbientNoiseMeasurement(records);
            SpeechAndNoiseEvent speechAndNoiseEvent = new SpeechAndNoiseEvent(SpeechAndNoiseSensor.this);
            for (SpeechAndNoiseSensorChangedListener listener : listeners) {
                listener.onValueChanged(speechAndNoiseEvent);
            }
            return null;
        }

        private ArrayList<Double> record(int count, int updateTime) {
            ArrayList<Double> measures = new ArrayList<Double>();
            while (measures.size() < count) {
                double db = 20.0 * Math.log10(recorder.getMaxAmplitude() / 32767.0);
                if (!Double.isInfinite(db) && !Double.isNaN(db)) {
                    measures.add(Math.abs(db));
                }
                synchronized (this) {
                    try {
                        this.wait(updateTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return measures;
        }


    }

    private class SpeechTimer extends CountDownTimer {
        private boolean speech = false;
        private boolean started = false;
        private SpeechRecognizer speechRecognizer;
        private Intent intent;


        public SpeechTimer(SpeechRecognizer speechRecognizer, Intent intent, int updateTime, int listeningTime) {
            super(listeningTime, updateTime);
            this.speechRecognizer = speechRecognizer;
            this.intent = intent;
        }

        public boolean isStarted() {
            return started;
        }

        public void setStarted(boolean started) {
            this.started = started;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            speechRecognizer.startListening(intent);
        }

        @Override
        public void onFinish() {
            SpeechAndNoiseSensor.this.isSpeech = false;
            SpeechAndNoiseEvent speechAndNoiseEvent = new SpeechAndNoiseEvent(SpeechAndNoiseSensor.this);
            for (SpeechAndNoiseSensorChangedListener listener : listeners) {
                listener.onValueChanged(speechAndNoiseEvent);
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
            SpeechAndNoiseSensor.this.isSpeech = true;
            SpeechAndNoiseEvent speechAndNoiseEvent = new SpeechAndNoiseEvent(SpeechAndNoiseSensor.this);
            for (SpeechAndNoiseSensorChangedListener listener : listeners) {
                listener.onValueChanged(speechAndNoiseEvent);
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
