package consumer.model.mqttobj;

import java.io.Serializable;

/**
 * Created by chris on 16/5/17.
 */
public class MQRquestBecomeCarerStatus implements Serializable {


    private static final long serialVersionUID = -2307035097496374501L;

    private String res;
    private String username;
    private String type;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MQRquestBecomeCarerStatus{" +
                "res='" + res + '\'' +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
