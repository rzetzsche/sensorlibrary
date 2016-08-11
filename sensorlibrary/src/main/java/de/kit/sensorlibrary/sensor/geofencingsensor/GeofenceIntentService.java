package de.kit.sensorlibrary.sensor.geofencingsensor;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 04.02.2015.
 */
public class GeofenceIntentService extends IntentService {

    public static final String MESSENGER_IDENTIFIER = "GEOFENCE_MESSENGER";

    public static final String BUNDLE_IDENTIFIER = "GEOFENCE";

    public GeofenceIntentService() {
        super(GeofenceIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event != null) {
            if (!event.hasError()) {
                GeofenceEventParcelable geofenceEventParcelable = parseGeofenceEventToGeofenceParceble(event);
                Intent i = new Intent("de.kit.sensorlibrary.sensor.geofence");
                i.putExtra(BUNDLE_IDENTIFIER, geofenceEventParcelable);
                this.sendBroadcast(i);
            }
        }

    }

    private GeofenceEventParcelable parseGeofenceEventToGeofenceParceble(GeofencingEvent event) {
        int geofenceTransition = event.getGeofenceTransition();
        List<Geofence> geofenceList = event.getTriggeringGeofences();
        List<String> geofenceIdentifier = new ArrayList<String>();
        for (Geofence geofence : geofenceList) {
            geofenceIdentifier.add(geofence.getRequestId());
        }
        Location location = event.getTriggeringLocation();
        return new GeofenceEventParcelable(geofenceTransition, geofenceIdentifier, location);
    }
}
