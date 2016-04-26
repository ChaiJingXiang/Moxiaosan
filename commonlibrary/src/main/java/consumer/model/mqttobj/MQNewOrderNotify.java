package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/14.
 */
public class MQNewOrderNotify implements Serializable{
    private static final long serialVersionUID = 8993936227736269069L;
    private String orderid;
    private String type;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MQNewOrderNotify{" +
                "orderid='" + orderid + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
