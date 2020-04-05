/*
 * NAME: ALLAN CALDERWOOD
 * MATRICULATION NUMBER : S1628544
 */

package allancalderwood.com.mpdcoursework.utils.mapUtils;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import allancalderwood.com.mpdcoursework.R;
import allancalderwood.com.mpdcoursework.models.Incident;
import allancalderwood.com.mpdcoursework.models.Roadwork;

public class MapMarkerHelper {
    public static void displayCurrent(Context context, ArrayList<Incident> incidents, GoogleMap map){
        // Clear map markers
        map.clear();
        // If there is no incidents display a toast
        if(incidents.isEmpty()){
            Toast toast = Toast.makeText(context, "No Current Incidents", Toast.LENGTH_SHORT);
            toast.show();
        }
        // Else add map markers for each incident
        else{
            for (Incident incident : incidents){
                map.addMarker( new MarkerOptions()
                        .position(new LatLng(incident.getLatitude(),incident.getLongitude()))
                        .title(incident.getTitle())
                        .snippet(incident.getDescription()+"\n"+incident.getLink())
                        .alpha(0.8f)
                );
            }
        }
    }

    public static void displayRoadworks(Context context, ArrayList<Roadwork> roadworks, GoogleMap map){
        // Clear map markers
        map.clear();
        // If there is no incidents display a toast
        if(roadworks.isEmpty()){
            Toast toast = Toast.makeText(context, "No Roadworks", Toast.LENGTH_SHORT);
            toast.show();
        }
        // Else add map markers for each Roadwork
        else{
            // for each Roadwork add a marker to the map
            for (Roadwork roadwork : roadworks){
                System.out.println("DESCR: "+roadwork.getDescription());
                map.addMarker( new MarkerOptions()
                        .position(new LatLng(roadwork.getLatitude(),roadwork.getLongitude()))
                        .title(roadwork.getTitle())
                        // handle possible 0 days left and display discription
                        .snippet(
                                ((roadwork.getNumDays()==0) ? "Finishing Today " : "Days left: "+roadwork.getNumDays()) +"\n"+
                                        roadwork.getDescription()
                        )
                        .alpha(0.8f)
                        // Add custom map icon depending on amount of time works will be in place
                        .icon(BitmapDescriptorFactory.fromResource(
                                (roadwork.getNumDays()<=7) ? R.mipmap.ic_map_icon_yellow :
                                        (roadwork.getNumDays()<=30) ? R.mipmap.ic_map_icon_orange :
                                                (roadwork.getNumDays()<=90) ? R.mipmap.ic_map_icon_red :
                                                        R.mipmap.ic_map_icon_dark
                                )
                        ));
            }
        }
    }

    public static void removeMarkersPolyline(GoogleMap map, ArrayList<LatLng> coords){
        // TODO code to remove markers that don't affect the route
    }
}

