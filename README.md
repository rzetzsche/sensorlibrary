# sensorlibrary
[![Release](https://jitpack.io/v/rzetzsche/sensorlibrary.svg)](https://jitpack.io/#rzetzsche/sensorlibrary)

SensorLibrary which trys to serve sensor data in a energy eficient manner.

# Set Up

Add following to your build.gradle:
```
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    compile 'com.github.rzetzsche:sensorlibrary:0.0.1'
}
```

# Usage

```
        UserActivitySensor userActivitySensor = new UserActivitySensor(getApplicationContext(), 1000);
        userActivitySensor.addValueChangedListener(new UserActivityChangedListener() {
            @Override
            public void onValueChanged(UserActivityChangedEvent event) {
                Log.e("TEST", String.valueOf(event.getDetectedActivityAsString()));
            }
        });
        userActivitySensor.openSensor();
```