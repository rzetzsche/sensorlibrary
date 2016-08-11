package de.kit.sensorlibrary.sensor.locationsensor;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Messenger;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.kit.sensorlibrary.sensor.AbstractSensorImpl;

public class LocationSensor extends AbstractSensorImpl<LocationChangedListener> implements LocationAccessInterface {

    public static final String IDENTIFIER = "location";
    private static String TAG = LocationSensor.class.getSimpleName();
    private Context context;
    private double longitude = 0;
    private double latitude = 0;
    private double lastLongitude = 0;
    private double lastLatitude = 0;
    private String address = "";

    public LocationSensor(Context context, long refreshTime) {
        this.context = context;
        LocationHandler handler = new LocationHandler(this, LocationIntentServiceReceiver.BUNDLE_IDENTIFIER);
        Intent i = new Intent(context, LocationIntentServiceReceiver.class);
        i.putExtra(LocationIntentServiceReceiver.MESSENGER_IDENTIFIER, new Messenger(
                handler));
        i.putExtra("refreshTime", refreshTime);
        context.startService(i);
    }

    @Override
    public void onLocationChanged(double[] coordinates) {
        if (!super.isOpen()) {
            Log.e(TAG, "Sensor isn't open");
        } else {
            latitude = coordinates[0];
            longitude = coordinates[1];
            LocationChangedEvent event = new LocationChangedEvent(this);
            for (LocationChangedListener listener : listeners) {
                listener.onValueChanged(event);
            }
        }

    }

    public String getAddress() {
        super.throwNewExceptionWhenSensorNotOpen();
        if (lastLatitude != latitude || lastLongitude != longitude) {
            lastLatitude = latitude;
            lastLongitude = longitude;
            Geocoder geocoder = new Geocoder(context,
                    Locale.getDefault());
            String addressString = "";
            try {
                List<Address> addressList = geocoder.getFromLocation(
                        latitude, longitude, 1);
                if (addressList.size() > 0) {
                    Address address = addressList.get(0);
                    if (address.getMaxAddressLineIndex() != -1) {
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            addressString += address.getAddressLine(i) + " ";
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            address = addressString;
            return addressString;
        }
        return address;

    }

    public double getLongitude() {
        super.throwNewExceptionWhenSensorNotOpen();
        return longitude;
    }

    public double getLatitude() {
        super.throwNewExceptionWhenSensorNotOpen();
        return latitude;
    }

    @Override
    public String[] getLog() {
        return new String[]{String.valueOf(getLatitude()), String.valueOf(getLongitude()), getAddress()};
    }

    @Override
    public String[] getLogColumns() {
        return new String[]{"latitude", "longitude", "address"};
    }

}
