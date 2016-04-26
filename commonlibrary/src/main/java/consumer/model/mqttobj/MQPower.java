package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/17.
 * 取电
 */
public class MQPower implements Serializable {
    private static final long serialVersionUID = 6278679339164339408L;
    private String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
