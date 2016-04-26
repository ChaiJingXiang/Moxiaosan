package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/17.
 * 电子围栏
 */
public class MQCircle implements Serializable {
    private static final long serialVersionUID = 7279316390875316804L;
    private String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
