/*
* NAME: ALLAN CALDERWOOD
* MATRICULATION NUMBER : S1628544
*/
package allancalderwood.com.mpdcoursework;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import allancalderwood.com.mpdcoursework.models.Roadwork;
import allancalderwood.com.mpdcoursework.utils.httpRequests.AsyncResponse;
import allancalderwood.com.mpdcoursework.utils.httpRequests.GetRequest;
import allancalderwood.com.mpdcoursework.utils.LocationHelper;
import allancalderwood.com.mpdcoursework.utils.mapUtils.MapMarkerHelper;
import allancalderwood.com.mpdcoursework.utils.mapUtils.MapRoute;
import allancalderwood.com.mpdcoursework.utils.xmlParsers.IncidentParser;
import allancalderwood.com.mpdcoursework.utils.mapUtils.MapStyle;
import allancalderwood.com.mpdcoursework.utils.xmlParsers.RoadworksParser;
import allancalderwood.com.mpdcoursework.utils.TrafficScotland;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PlacesClient places;
    private LinearLayout searchContainer;
    private ImageButton current;
    private ImageButton roadworks;
    private ImageButton planned;
    private ImageButton openSrch;
    private ImageButton closeSrch;

    private String title="Current Incidents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // set title and icon on action bar to current incidents as default
        ActionBar actionBar = getActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_warning);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
        actionBar.setSubtitle("S1628544"); // add matric number as subtitle

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Initialise the google places autocomplete
        initPlaces();
        // Set up class variables
        current = findViewById(R.id.current);
        roadworks = findViewById(R.id.roadworks);
        planned = findViewById(R.id.planned);
        openSrch = findViewById(R.id.openSrch);
        closeSrch = findViewById(R.id.closeSrch);
        searchContainer = findViewById(R.id.searchContainer);

        // load current incidents as default
        current();

        // Set on click listeners for buttons
        openSrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContainer.setVisibility(View.VISIBLE);
            }
        });
        closeSrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContainer.setVisibility(View.GONE);
                openSrch.setVisibility(View.VISIBLE);
            }
        });
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current();
            }
        });
        roadworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roadworks();
            }
        });

        planned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve date from datepicker
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        planned(myCalendar.getTime());
                    }

                };

                new DatePickerDialog(MapsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Initialise mMap
        mMap = googleMap;
        // Move camera to scotland and lock bounds to UK
        LatLng scotland = new LatLng(53.6, -4.2);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(scotland));
        LatLngBounds Bounds = new LatLngBounds(new LatLng(52.5, -7.78), new LatLng(58, 0.31));
        mMap.setLatLngBoundsForCameraTarget(Bounds);
        // Set zoom to country level
        mMap.setMinZoomPreference(5);

        // Set style of the map using custom MapStyle class
        String water_color = getResources().getString(0+R.color.colorPrimaryDark);
        String land_color = getResources().getString(0+R.color.colorPrimary);
        String road_color = getResources().getString(0+R.color.colorAccent);
        String label_color = getResources().getString(0+R.color.text);
        MapStyle style = new MapStyle(water_color, land_color, road_color, label_color);
        style.applyToMap(getApplicationContext(), mMap);
    }

    // function to intialise google places api and set it up
    private void initPlaces(){
        // Set up google places API
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        }
        places = Places.createClient(this);
        final AutocompleteSupportFragment autoComp = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.search);

        // set the returns format for when a user selects a place
        autoComp.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));

        // Set listener for autocomplete selection
        autoComp.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // zoom to current location
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LocationHelper.getLocation(getApplicationContext()), 12f));
                // Create a custom MapRoute object from the current location and selected location
                MapRoute mr = new MapRoute(
                        LocationHelper.getLocation(getApplicationContext()),
                        place.getLatLng()
                );
                // Show the route on the map
                mr.showOnMap(MapsActivity.this, mMap, getResources().getString(R.string.google_maps_key));
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MapsActivity.this, ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // function called when current button clicked
    private void current(){
        // Update Action bar title
        title = "Current Incidents";
        getActionBar().setTitle(title);
        getActionBar().setHomeAsUpIndicator(R.drawable.ic_warning);
        // perform get request to retrieve the rss feed for current incidents
        // add callback method anonymously
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    // parse the response, using the custom IncidentParses class,
                    // into an array of incidents then pass it to the currentDisplay function
                    MapMarkerHelper.displayCurrent(getApplicationContext(),IncidentParser.parseIncidents(response[1]), mMap);
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        gr.execute(TrafficScotland.urlCurrent); // execute with current incidents url
    }

    // function called when roadworks button clicked
    private void roadworks(){
        // Update Action bar title
        title = "Roadworks";
        getActionBar().setHomeAsUpIndicator(R.drawable.ic_roadworks);
        getActionBar().setTitle(title);
        // perform get request to retrieve the rss feed for current incidents
        // add callback method anonymously
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    // parse the response, using the custom IncidentParses class,
                    // into an array of incidents then pass it to the currentDisplay function
                    MapMarkerHelper.displayRoadworks(getApplicationContext(), RoadworksParser.parseRoadworks(response[1]), mMap);
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        gr.execute(TrafficScotland.urlRoadworks); // execute with the roadworks url
    }

    // function called when planned button clicked
    private void planned(final Date date){
        // Update Action bar title
        title = "Planned Roadworks";
        getActionBar().setTitle(title);
        getActionBar().setHomeAsUpIndicator(R.drawable.ic_planned);
        // perform get request to retrieve the rss feed for current incidents
        // add callback method anonymously

        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                final String[] response = (String[]) output;
                try{
                    GetRequest gr = new GetRequest(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            String[] response2 = (String[]) output;
                            try{
                                // parse the response, using the custom RoadworksParser class,
                                // into an 2 arrays of roadworks now and planned roadworks then pass it to the currentDisplay function
                                // after filtering both for only roadworks that occur during the selected date
                                ArrayList<Roadwork> roadworksCurrent = RoadworksParser.parseRoadworks(response[1]);
                                ArrayList<Roadwork> roadworksPlanned = RoadworksParser.parseRoadworks(response2[1]);
                                // loop and remove roadworks that don't affect selected date
                                ArrayList<Roadwork> newRoadworks = new ArrayList<>();
                                for (Roadwork r : roadworksCurrent){
                                    if( date.after(r.getStart_date()) && date.before(r.getEnd_date()) ){
                                        newRoadworks.add(r);
                                        // if it fits add it to new array list containing
                                        // only affected roadworks
                                    }
                                }
                                for (Roadwork r : roadworksPlanned){
                                    if( date.after(r.getStart_date()) && date.before(r.getEnd_date()) ){
                                        newRoadworks.add(r);
                                        // if it fits add it to new array list containing
                                        // only affected roadworks
                                    }
                                }
                                MapMarkerHelper.displayRoadworks(getApplicationContext(), newRoadworks, mMap);
                            }catch (Exception e){
                                System.err.println(e.getLocalizedMessage());
                            }
                        }
                    });
                    gr.execute(TrafficScotland.urlPlanned); // execute with the planned roadworks url
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        gr.execute(TrafficScotland.urlRoadworks); // execute with the roadworks url
    }
}
