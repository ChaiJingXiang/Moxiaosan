package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/14.
 */
public class MQComment implements Serializable {
    private static final long serialVersionUID = 1857101176963049987L;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
