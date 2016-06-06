package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/4/9.
 */
public class LocationObj implements Serializable {

    private double lat;
    private double lng;
    private String speed;
    private String datetime;
    private String name;
    private String direction;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "LocationObj{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", speed='" + speed + '\'' +
                ", datetime='" + datetime + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
