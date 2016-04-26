package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/4/9.
 */
public class GuiJiObj implements Serializable {


    private static final long serialVersionUID = -4467438224105586515L;
    private double lat;
    private double lng;
    private String speed;
    private String journey;

    public String getJourney() {
        return journey;
    }

    public void setJourney(String journey) {
        this.journey = journey;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    @Override
    public String toString() {
        return "GuiJiObj{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", speed='" + speed + '\'' +
                ", journey='" + journey + '\'' +
                '}';
    }
}
