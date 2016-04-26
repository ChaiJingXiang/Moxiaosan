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
}
