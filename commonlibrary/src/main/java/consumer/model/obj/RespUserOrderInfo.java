package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/3/24.
 * 我的订单  查看详情
 */
public class RespUserOrderInfo implements Serializable {
    private static final long serialVersionUID = -727802049464963401L;
    private String orderid;
    private String beginningplace;
    private String destination;
    private String addressee;
    private String rec_tel;
    private String goodsname;
    private String declared;
    private String weight;
    private String length;
    private String width;
    private String height;
    private String reward;
    private String commentsid;
    private String com_tel;
    private String servicestatus;
    private String pickuptime;
    private String deliverytime;

    private String b_lat;
    private String b_lng;
    private String d_lat;
    private String d_lng;
    private String car_lat;
    private String car_lng;

    public String getDeliverytime() {
        return deliverytime;
    }

    public void setDeliverytime(String deliverytime) {
        this.deliverytime = deliverytime;
    }

    public String getPickuptime() {
        return pickuptime;
    }

    public void setPickuptime(String pickuptime) {
        this.pickuptime = pickuptime;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getBeginningplace() {
        return beginningplace;
    }

    public void setBeginningplace(String beginningplace) {
        this.beginningplace = beginningplace;
    }

    public String getCom_tel() {
        return com_tel;
    }

    public void setCom_tel(String com_tel) {
        this.com_tel = com_tel;
    }

    public String getCommentsid() {
        return commentsid;
    }

    public void setCommentsid(String commentsid) {
        this.commentsid = commentsid;
    }

    public String getDeclared() {
        return declared;
    }

    public void setDeclared(String declared) {
        this.declared = declared;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getRec_tel() {
        return rec_tel;
    }

    public void setRec_tel(String rec_tel) {
        this.rec_tel = rec_tel;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getServicestatus() {
        return servicestatus;
    }

    public void setServicestatus(String servicestatus) {
        this.servicestatus = servicestatus;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getB_lat() {
        return b_lat;
    }

    public void setB_lat(String b_lat) {
        this.b_lat = b_lat;
    }

    public String getB_lng() {
        return b_lng;
    }

    public void setB_lng(String b_lng) {
        this.b_lng = b_lng;
    }

    public String getCar_lat() {
        return car_lat;
    }

    public void setCar_lat(String car_lat) {
        this.car_lat = car_lat;
    }

    public String getCar_lng() {
        return car_lng;
    }

    public void setCar_lng(String car_lng) {
        this.car_lng = car_lng;
    }

    public String getD_lat() {
        return d_lat;
    }

    public void setD_lat(String d_lat) {
        this.d_lat = d_lat;
    }

    public String getD_lng() {
        return d_lng;
    }

    public void setD_lng(String d_lng) {
        this.d_lng = d_lng;
    }
}
