package de.kit.sensorlibrary.sensor.ambientnoisesensor;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

/**
 * Created by Robert on 05.02.2015.
 */
public class AmbientNoiseSensor extends AbstractSensorImpl<AmbientNoiseChangedListener> {
    private MediaRecorder recorder;
    private Context context;
    private int updateTime;
    private int measurementCount;
    private AmbientNoiseMeasurement measurement;

    public AmbientNoiseSensor(Context context, int updateTime, int measurementCount) {
        this.context = context;
        this.updateTime = updateTime;
        this.measurementCount = measurementCount;
    }


    public AmbientNoiseMeasurement getMeasurement() {
        return measurement;
    }

    @Override
    public void openSensor() {
        super.openSensor();
        initializeRecorder();
        startAmbientNoiseMeasurement(updateTime, measurementCount);
    }


    @Override
    public void closeSensor() {
        recorder.reset();
        super.closeSensor();
    }

    @Override
    public String[] getLog() {
        return new String[]{String.valueOf(measurement.getMaxNoise())
                , String.valueOf(measurement.getMinNoise()), String.valueOf(measurement.getAverageNoise())};
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

    private void startAmbientNoiseMeasurement(int updateTime, int measurementCount) {
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
            } catch (IllegalStateException | IOException e1) {
                e1.printStackTrace();
            }
            measurement = new AmbientNoiseMeasurement(records);
            AmbientNoiseEvent ambientNoiseEvent = new AmbientNoiseEvent(AmbientNoiseSensor.this);
            for (AmbientNoiseChangedListener listener : listeners) {
                listener.onValueChanged(ambientNoiseEvent);
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
}
