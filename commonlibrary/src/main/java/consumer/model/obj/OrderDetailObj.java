package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/3/23.
 */
public class OrderDetailObj implements Serializable {

    private static final long serialVersionUID = 4309293458683543429L;

    private String orderid;
    private String beginningplace;//始发地
    private String destination;//目的地
    private String name;//下单人
    private String contact;//电话
    private String goodsname;//货物名称
    private String declared;//申报价值
    private String weight;//重量
    private String length;//长
    private String width;//宽
    private String height;//高
    private String addressee;//收件人
    private String rec_tel;//收件人电话
    private String distance;//距离您多远
    private String reward; //费用
    private String lat_o;
    private String lng_o;
    private String origin_region;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getBeginningplace() {
        return beginningplace;
    }

    public void setBeginningplace(String beginningplace) {
        this.beginningplace = beginningplace;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getDeclared() {
        return declared;
    }

    public void setDeclared(String declared) {
        this.declared = declared;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getRec_tel() {
        return rec_tel;
    }

    public void setRec_tel(String rec_tel) {
        this.rec_tel = rec_tel;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getLat_o() {
        return lat_o;
    }

    public void setLat_o(String lat_o) {
        this.lat_o = lat_o;
    }

    public String getLng_o() {
        return lng_o;
    }

    public void setLng_o(String lng_o) {
        this.lng_o = lng_o;
    }

    public String getOrigin_region() {
        return origin_region;
    }

    public void setOrigin_region(String origin_region) {
        this.origin_region = origin_region;
    }

    @Override
    public String toString() {
        return "OrderDetailObj{" +
                "orderid='" + orderid + '\'' +
                ", beginningplace='" + beginningplace + '\'' +
                ", destination='" + destination + '\'' +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", declared='" + declared + '\'' +
                ", weight='" + weight + '\'' +
                ", length='" + length + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", addressee='" + addressee + '\'' +
                ", rec_tel='" + rec_tel + '\'' +
                ", distance='" + distance + '\'' +
                ", reward='" + reward + '\'' +
                ", lat_o='" + lat_o + '\'' +
                ", lng_o='" + lng_o + '\'' +
                ", origin_region='" + origin_region + '\'' +
                '}';
    }
}
