package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/14.
 */
public class MQNewsNotify implements Serializable {
    private static final long serialVersionUID = 4447342584191089016L;
    private String orderid;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
