/*
 * NAME: ALLAN CALDERWOOD
 * MATRICULATION NUMBER : S1628544
*/

package allancalderwood.com.mpdcoursework.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// Roadwork is a subclass of Incident with added information including start and end dates
public class Roadwork extends Incident{
    private String title;
    private Date start_date;
    private Date end_date;
    private int numDays;
    private String description;
    private String link;
    private float latitude;
    private float longitude;

    public Roadwork() {
        super();
    }


    public int getNumDays() {
        return (int) TimeUnit.DAYS.convert(end_date.getTime()-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public void setNumDays(int numDays) {
        this.numDays = numDays;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    // Override setDescription from SuperClass Incident to
    @Override
    public void setDescription(String description) {
        // finally remove <br> tags before setting
        this.description = description
                .replace("<br />", " ");

        // obtain start and end dates from description
        String start = description.substring(
            12,
            description.indexOf("<br />")
        );
        String end;
        // Check for what format the description is in, then create substring accordingly
        if(description.endsWith("00:00")){
            end = description.substring(
                    description.indexOf("<br />")+16);
        }else{
            end = description.substring(
                    description.indexOf("<br />")+16,
                    description.lastIndexOf("<br />"));
        }

        // Format into Dates and catch any exception
        try {
            this.start_date = new SimpleDateFormat("EEE, dd MMM yyyy - HH:mm").parse(start);
            this.end_date = new SimpleDateFormat("EEE, dd MMM yyyy - HH:mm").parse(end);
        } catch (ParseException e){
            e.printStackTrace();
        }

    }
}
