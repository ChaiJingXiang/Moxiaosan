package consumer.model.obj;


/**
 * Created by chris on 16/4/17.
 */
public class DeviceStatus  {

    private String guard;
    private String cut;
    private String vbsen; //灵敏度
    private String sos1;
    private String sos2;
    private String sos3;
    private String type;
    private String circle; //电子围栏
    private String hour;
    private String mlieage; //保养里程

    public String getGuard() {
        return guard;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    public String getVbsen() {
        return vbsen;
    }

    public void setVbsen(String vbsen) {
        this.vbsen = vbsen;
    }

    public String getSos1() {
        return sos1;
    }

    public void setSos1(String sos1) {
        this.sos1 = sos1;
    }

    public String getSos2() {
        return sos2;
    }

    public void setSos2(String sos2) {
        this.sos2 = sos2;
    }

    public String getSos3() {
        return sos3;
    }

    public void setSos3(String sos3) {
        this.sos3 = sos3;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMlieage() {
        return mlieage;
    }

    public void setMlieage(String mlieage) {
        this.mlieage = mlieage;
    }

    @Override
    public String toString() {
        return "DeviceStatus{" +
                "circle='" + circle + '\'' +
                ", guard='" + guard + '\'' +
                ", cut='" + cut + '\'' +
                ", vbsen='" + vbsen + '\'' +
                ", sos1='" + sos1 + '\'' +
                ", sos2='" + sos2 + '\'' +
                ", sos3='" + sos3 + '\'' +
                ", type='" + type + '\'' +
                ", hour='" + hour + '\'' +
                ", mlieage='" + mlieage + '\'' +
                '}';
    }
}
