// ALLAN CALDERWOOD - S1628544

package allancalderwood.com.mpdcoursework.utils.xmlParsers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import allancalderwood.com.mpdcoursework.models.Incident;

public class IncidentParser {

    public static ArrayList<Incident> parseIncidents(String xml){
        ArrayList<Incident> incidents = new ArrayList<>();
        // Use try catch to catch any XMLPullParserExceptions that might be thrown
        try{
            // initialise a new XmlPullParser
            XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
            // set the input to a new string reader containing the xml
            xmlParser.setInput(new StringReader(xml));
            int eventType = xmlParser.getEventType();
            boolean done = false;
            Incident incident = null;

            // while loop to go through each 'Item' i.e. incident, and construct an incident object and then add that object to the arraylist
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item")) {
                            incident = new Incident();
                        } else if (incident != null) {
                            if (name.equalsIgnoreCase("Title")) {
                                incident.setTitle(xmlParser.nextText());
                            } else if (name.equalsIgnoreCase("Description")) {
                                incident.setDescription(xmlParser.nextText());
                            } else if (name.equalsIgnoreCase("Link")) {
                                incident.setLink(xmlParser.nextText());
                            } else if (name.equalsIgnoreCase("Georss:point")) {
                                incident.setLatLong(xmlParser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item") && incident != null) {
                            incidents.add(incident);
                        } else if (name.equalsIgnoreCase("Channel")) {
                            done = true;
                        }
                        break;
                }
                eventType = xmlParser.next();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return incidents;
    }
}
