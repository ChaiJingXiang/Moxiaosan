package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/14.
 */
public class MQPickup implements Serializable {
    private static final long serialVersionUID = -3608533976073073721L;
    private String orderid;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
