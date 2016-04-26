package consumer.model.obj;

/**
 * Created by chris on 16/4/25.
 */
public class AlarmObj  {

    private double lat;
    private double lng;
    private String speed;
    private String datetime;
    private String name;
    private String alarminfo;
    private String address;

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

    public String getAlarminfo() {
        return alarminfo;
    }

    public void setAlarminfo(String alarminfo) {
        this.alarminfo = alarminfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "AlarmObj{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", speed='" + speed + '\'' +
                ", datetime='" + datetime + '\'' +
                ", name='" + name + '\'' +
                ", alarminfo='" + alarminfo + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
