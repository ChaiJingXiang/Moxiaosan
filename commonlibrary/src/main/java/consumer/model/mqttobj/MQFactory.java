package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/17.
 * 恢复出厂设置
 */
public class MQFactory implements Serializable {
    private static final long serialVersionUID = 4468949079505427461L;
    private String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
