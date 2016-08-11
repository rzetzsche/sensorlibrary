package de.kit.sensorlibrary.sensor.geofencingsensor;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 05.02.2015.
 */
public class GeofenceEventParcelable implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new GeofenceEventParcelable(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new GeofenceEventParcelable[0];
        }
    };
    private int geofenceTransition;
    private List<String> geofenceNames;
    private Location location;

    public GeofenceEventParcelable(int geofenceTransition, List<String> geofenceNames, Location location) {
        this.geofenceTransition = geofenceTransition;
        this.geofenceNames = geofenceNames;
        this.location = location;
    }

    private GeofenceEventParcelable(Parcel in) {
        this.geofenceNames = new ArrayList<String>();
        this.geofenceTransition = in.readInt();
        in.readStringList(this.getGeofenceNames());
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(geofenceTransition);
        dest.writeStringList(geofenceNames);
        dest.writeParcelable(location, 0);
    }

    public int getGeofenceTransition() {
        return geofenceTransition;
    }

    public List<String> getGeofenceNames() {
        return geofenceNames;
    }

    public Location getLocation() {
        return location;
    }
}
