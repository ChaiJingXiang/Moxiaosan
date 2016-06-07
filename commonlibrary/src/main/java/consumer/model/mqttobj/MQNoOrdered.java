package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/6/4.
 * 用户端无人接单
 */
public class MQNoOrdered implements Serializable {
    private static final long serialVersionUID = 5050981277336182900L;
    private String orderid;
    private String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
