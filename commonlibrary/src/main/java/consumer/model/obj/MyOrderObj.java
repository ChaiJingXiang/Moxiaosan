package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/3/23.
 */
public class MyOrderObj implements Serializable {

    private static final long serialVersionUID = -4370851566417023491L;

    private String orderid;
    private String title;
    private String datetime;
    private String beginningplace;
    private String destination;
    private String cost;
    private String status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MyOrderObj{" +
                "orderid='" + orderid + '\'' +
                ", title='" + title + '\'' +
                ", datetime='" + datetime + '\'' +
                ", beginningplace='" + beginningplace + '\'' +
                ", destination='" + destination + '\'' +
                ", cost='" + cost + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
