package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/17.
 * 设置保养里程
 */
public class MQMmlieage implements Serializable {
    private static final long serialVersionUID = -5107928207768129835L;
    private String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
