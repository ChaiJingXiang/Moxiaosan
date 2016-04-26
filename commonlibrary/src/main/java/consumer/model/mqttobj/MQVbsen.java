package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/17.
 * 设置灵敏度
 */
public class MQVbsen implements Serializable {
    private static final long serialVersionUID = -6174570443660420989L;
    private String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
