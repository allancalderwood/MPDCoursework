/*
 * NAME: ALLAN CALDERWOOD
 * MATRICULATION NUMBER : S1628544
 */

package allancalderwood.com.mpdcoursework.utils.mapUtils;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PolylineDecoder {

    // Method to decode a String polyline into an ArrayList of LatLng
    public static ArrayList<LatLng> decode(String polyline){
        ArrayList<LatLng> polys = new ArrayList<LatLng>();
        int i = 0, len = polyline.length();
        int latitude = 0, longitude = 0;

        while (i < len) {
            int b, shift = 0, result = 0;
            do {
                b = polyline.charAt(i++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            latitude += dlat;

            shift = 0;
            result = 0;
            do {
                b = polyline.charAt(i++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            longitude += dlng;

            LatLng p = new LatLng((((double) latitude / 1E5)),
                    (((double) longitude / 1E5)));
            polys.add(p);
        }

        return polys;
    }
}
