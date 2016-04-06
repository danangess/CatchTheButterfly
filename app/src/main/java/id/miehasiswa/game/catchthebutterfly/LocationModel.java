package id.miehasiswa.game.catchthebutterfly;

/**
 * Created by danang on 05/04/16.
 */
public class LocationModel {
    private String id, location, latitude, longitude, range;

    public LocationModel(){}

    public String getId(){return id;}
    public String getLocation(){return location;}
    public String getLatitude(){return latitude;}
    public String getLongitude(){return longitude;}
    public String getRange(){return range;}

    public void setId(String id){this.id = id;}
    public void setLocation(String location){this.location = location;}
    public void setLatitude(String latitude){this.latitude = latitude;}
    public void setLongitude(String longitude){this.longitude = longitude;}
    public void setRange(String range){this.range = range;}
}
