// ALLAN CALDERWOOD - S1628544
package allancalderwood.com.mpdcoursework.utils.xmlParsers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import allancalderwood.com.mpdcoursework.models.Roadwork;

public class RoadworksParser {

    public static ArrayList<Roadwork> parseRoadworks(String xml){
        ArrayList<Roadwork> roadworks = new ArrayList<>();
        // Use try catch to catch any XMLPullParserExceptions that might be thrown
        try{
            // initialise a new XmlPullParser
            XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
            // set the input to a new string reader containing the xml
            xmlParser.setInput(new StringReader(xml));
            int eventType = xmlParser.getEventType();
            boolean done = false;
            Roadwork roadwork = null;

            // while loop to go through each 'Item' i.e. incident, and construct an incident object and then add that object to the arraylist
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item")) {
                            roadwork = new Roadwork();
                        } else if (roadwork != null) {
                            if (name.equalsIgnoreCase("Title")) {
                                roadwork.setTitle(xmlParser.nextText());
                            } else if (name.equalsIgnoreCase("Description")) {
                                roadwork.setDescription(xmlParser.nextText());
                            } else if (name.equalsIgnoreCase("Link")) {
                                roadwork.setLink(xmlParser.nextText());
                            } else if (name.equalsIgnoreCase("Georss:point")) {
                                roadwork.setLatLong(xmlParser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item") && roadwork != null) {
                            roadworks.add(roadwork);
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

        return roadworks;
    }
}
