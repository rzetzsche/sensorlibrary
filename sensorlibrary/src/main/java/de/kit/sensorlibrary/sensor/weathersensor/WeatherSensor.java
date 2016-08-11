package de.kit.sensorlibrary.sensor.weathersensor;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;
import de.kit.sensorlibrary.sensor.locationsensor.LocationChangedEvent;
import de.kit.sensorlibrary.sensor.locationsensor.LocationChangedListener;
import de.kit.sensorlibrary.sensor.locationsensor.LocationSensor;

/**
 * Created by Robert on 28.01.2015.
 */


public class WeatherSensor extends AbstractSensorImpl<WeatherChangedListener> {

    public final static String IDENTIFIER = "weather";
    //request API-Key from openweathermap.org
    private static String API_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static String URL = "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metrics&lang=de&APPID=%s";
    private static String TAG = WeatherSensor.class.getSimpleName();
    private double temperature;
    private String condition;
    private double lastLatitude = Integer.MAX_VALUE;
    private double lastLongitude = Integer.MAX_VALUE;
    private LocationSensor locationSensor;

    /**
     * Initialize WeatherSensor with LocationSensor.
     *
     * @param locationSensor the needed location source
     * @param isAutoRefresh  if true, onLocationChange causes automatic refresh of weather (be careful for network traffic and battery use)
     */
    public WeatherSensor(LocationSensor locationSensor, boolean isAutoRefresh) {
        temperature = 0;
        condition = "";
        this.locationSensor = locationSensor;
        if (isAutoRefresh) {
            locationSensor.addValueChangedListener(new LocationChangedListener() {
                @Override
                public void onValueChanged(LocationChangedEvent event) {
                    getWeather(event.getLongitude(), event.getLatitude());
                }
            });
        }
    }


    public double getTemperature() {
        super.throwNewExceptionWhenSensorNotOpen();
        return temperature;
    }

    public String getCondition() {
        super.throwNewExceptionWhenSensorNotOpen();
        return condition;
    }


    public String[] getWeather() {
        super.throwNewExceptionWhenSensorNotOpen();
        if (locationSensor != null && locationSensor.getLatitude() != 0 && locationSensor.getLatitude() != 0) {
            if (lastLatitude != locationSensor.getLatitude() || lastLongitude != locationSensor.getLongitude()) {
                lastLongitude = locationSensor.getLongitude();
                lastLatitude = locationSensor.getLatitude();
                return requestWeather(locationSensor.getLongitude(), locationSensor.getLatitude());
            }


        }
        return new String[]{
                String.valueOf(temperature), condition};
    }


    public String[] getWeather(final double longitude, final double latitude) {
        super.throwNewExceptionWhenSensorNotOpen();
        return requestWeather(longitude, latitude);

    }

    private String[] requestWeather(double longitude, double latitude) {
        WeatherTask weatherTask = new WeatherTask();
        try {
            String[] weatherValues = weatherTask.execute(longitude, latitude).get();
            String tempCondition = weatherValues[0];
            String tempDegree = weatherValues[1];
            if (tempCondition != null && tempDegree != null) {
                double tempTemperature = ((double) Math.round(Double.parseDouble(tempDegree) * 10)) / 10;
                if (temperature != tempTemperature || !condition.equals(tempCondition)) {
                    this.temperature = tempTemperature;
                    this.condition = tempCondition;
                    WeatherChangedEvent weatherChangedEvent = new WeatherChangedEvent(this);
                    for (WeatherChangedListener listener : listeners) {
                        listener.onValueChanged(weatherChangedEvent);
                    }
                }
            }
            return weatherValues;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String[] getLog() {
        return new String[]{getCondition(), String.valueOf(getTemperature())};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"condition", "temperature"};
    }

    private class WeatherTask extends AsyncTask<Double, Void, String[]> {

        @Override
        protected String[] doInBackground(Double... params) {
            if (params.length != 2) {
                Log.e(TAG, "Missing longitude and latitude arguments");
            }

            return requestWeather(params[0], params[1]);
        }

        private String[] requestWeather(double longitude, double latitude) {
            String[] weather = new String[2];
            JsonReader reader = null;
            try {
                HttpClient
                        httpClient = new DefaultHttpClient();
                String formattedURL = String.format(URL, latitude, longitude, API_KEY);
                System.out.println(formattedURL);
                HttpGet httpGet = new HttpGet(formattedURL);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                reader = new JsonReader(new InputStreamReader(
                        entity.getContent()));
                weather = getWeather(reader);
            } catch (ClientProtocolException e) {
                // Log.v(TAG, "It occured a Problem with the Internet-Connection");
            } catch (IOException e) {
                // Log.v(TAG, "It occured a Problem with the Internet-Connection");
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return weather;
        }

        private String[] getWeather(JsonReader reader)
                throws IOException {
            String[] weather = new String[2];
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("weather")) {
                    reader.beginArray();
                    reader.beginObject();
                    while (reader.hasNext()) {
                        if (reader.nextName().equals("icon")) {
                            weather[0] = parseIconToWeatherCondition(reader.nextString());
                        } else
                            reader.skipValue();
                    }
                    reader.endObject();
                    reader.endArray();
                } else if (name.equals("main")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        if (reader.nextName().equals("temp")) {
                            weather[1] = String.valueOf((reader.nextDouble() - 273.15));
                        } else
                            reader.skipValue();
                    }
                    reader.endObject();
                } else
                    reader.skipValue();
            }
            return weather;
        }

        private String parseIconToWeatherCondition(String iconId) {
            if (iconId.contains("01")) {
                return "clear sky";
            } else if (iconId.contains("02")) {
                return "few clouds";
            } else if (iconId.contains("03")) {
                return "scattered clouds";
            } else if (iconId.contains("04")) {
                return "broken clouds";
            } else if (iconId.contains("09")) {
                return "shower rain";
            } else if (iconId.contains("10")) {
                return "rain";
            } else if (iconId.contains("11")) {
                return "thunderstorm";
            } else if (iconId.contains("13")) {
                return "snow";
            } else if (iconId.contains("50")) {
                return "mist";
            } else {
                return "other";
            }

        }
    }

}
