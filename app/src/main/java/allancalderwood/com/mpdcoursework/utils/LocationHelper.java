/*
 * NAME: ALLAN CALDERWOOD
 * MATRICULATION NUMBER : S1628544
 */

package allancalderwood.com.mpdcoursework.utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

// Function to return current location of the user
// Due to emulators not having the possibility of getting the current location
// a hard coded location for glasgow has been returned instead
// but the code for use on an actual phone has been retained and commented out

public class LocationHelper {
    public static LatLng getLocation(Context context) {
       /* LocationManager locationManager = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
        return currentLocation;*/
       return new LatLng(55.860916, -4.251433);
    }
}
