/*
 * NAME: ALLAN CALDERWOOD
 * MATRICULATION NUMBER : S1628544
 */

package allancalderwood.com.mpdcoursework.utils.mapUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;

import allancalderwood.com.mpdcoursework.utils.httpRequests.AsyncResponse;
import allancalderwood.com.mpdcoursework.utils.httpRequests.GetRequest;

public class MapRoute {
    private final static String baseURL = "https://maps.googleapis.com/maps/api/directions/json?";
    private LatLng origin;
    private LatLng destination;

    public MapRoute() {
    }

    public MapRoute(LatLng origin, LatLng destination) {
        this.origin = origin;
        this.destination = destination;
    }

    private void display(final GoogleMap map, String polyline, final Context context){
        map.setMyLocationEnabled(true);
        ArrayList<LatLng> coords = PolylineDecoder.decode(polyline);
        PolylineOptions options = new PolylineOptions().width(5).color(Color.MAGENTA).geodesic(true);
        options.addAll(coords);
        Polyline line = map.addPolyline(options);
        // set code to dismiss a route upon clicking through the use of an alertDialog
        line.setClickable(true);
        map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            public void onPolylineClick(final Polyline polyline) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setMessage("Do you wish to remove this route?");
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        polyline.setVisible(false);
                        map.clear();
                    }
                });
                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                alertBuilder.show();
            }
        });
        // make call to function to remove markers from the map that wont affect the route
        MapMarkerHelper.removeMarkersPolyline(map, coords);
    }

    public void showOnMap(final Context context, final GoogleMap map, String apiKey){
        // perform a get request by sending appropriate data
        // to google routes API to retrieve route data
        final String URL = baseURL+
                "origin="+origin.latitude+","+origin.longitude+
                "&destination="+destination.latitude+","+destination.longitude+
                "&key="+apiKey;
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    JSONObject jo = new JSONObject( response[1] );
                    // pass the polyline point to the display function
                    display(map, jo.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points"), context);
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        gr.execute(URL); // execute with the roadworks url
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }
}
