/*
 * NAME: ALLAN CALDERWOOD
 * MATRICULATION NUMBER : S1628544
 */

package allancalderwood.com.mpdcoursework.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Incident {
    private String title;
    private String description;
    private String link;
    private float latitude;
    private float longitude;

    public Incident() {
    }

    public Incident(String title, String description, String link, float latitude, float longitude) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatLong(String LatLong){
        // split the string into latitude and longitude with the space seperator
        String[] points = LatLong.split(" ");
        // assign latitude and longitude by parsing each string to their respective float
        this.latitude = Float.parseFloat(points[0]);
        this.longitude = Float.parseFloat(points[1]);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
