package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/3/23.
 */
public class RespUserOrder implements Serializable {
    private static final long serialVersionUID = -218432805002177198L;
    private String orderid;
    private String title;
    private String datetime;
    private String destination;
    private String beginningplace;
    private String cost;
    private String servicestatus;
    private String pickuptime;
    private String commentsid;

    public String getCommentsid() {
        return commentsid;
    }

    public void setCommentsid(String commentsid) {
        this.commentsid = commentsid;
    }

    public String getBeginningplace() {
        return beginningplace;
    }

    public void setBeginningplace(String beginningplace) {
        this.beginningplace = beginningplace;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPickuptime() {
        return pickuptime;
    }

    public void setPickuptime(String pickuptime) {
        this.pickuptime = pickuptime;
    }

    public String getServicestatus() {
        return servicestatus;
    }

    public void setServicestatus(String servicestatus) {
        this.servicestatus = servicestatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
