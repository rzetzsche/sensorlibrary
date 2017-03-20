package main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.kit.sensorlibrary.sensor.speechsensor.SpeechChangedListener;
import de.kit.sensorlibrary.sensor.speechsensor.SpeechEvent;
import de.kit.sensorlibrary.sensor.speechsensor.SpeechSensor;
import de.kit.sensorlibrary.sensor.useractivitysensor.UserActivityChangedEvent;
import de.kit.sensorlibrary.sensor.useractivitysensor.UserActivityChangedListener;
import de.kit.sensorlibrary.sensor.useractivitysensor.UserActivitySensor;
import de.kit.sensorlibrarytestapp.R;

/**
 * Created by Robert Zetzsche on 16.03.2017.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layou);
        SpeechSensor speechSensor = new SpeechSensor(getApplicationContext(), 30000, 1000);
        speechSensor.addValueChangedListener(new SpeechChangedListener() {
            @Override
            public void onValueChanged(SpeechEvent event) {
                Log.e("TEST", String.valueOf(event.isSpeech()));
            }
        });
        speechSensor.openSensor();

        UserActivitySensor userActivitySensor = new UserActivitySensor(getApplicationContext(), 1000);
        userActivitySensor.addValueChangedListener(new UserActivityChangedListener() {
            @Override
            public void onValueChanged(UserActivityChangedEvent event) {
                Log.e("TEST", String.valueOf(event.getDetectedActivityAsString()));
            }
        });
        userActivitySensor.openSensor();
    }
}
