package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/17.
 * 设置报警电话
 */
public class MQSos implements Serializable {
    private static final long serialVersionUID = 4383038981152383648L;
    private String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
