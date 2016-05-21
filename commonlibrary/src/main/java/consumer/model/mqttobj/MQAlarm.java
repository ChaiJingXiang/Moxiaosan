package consumer.model.mqttobj;

import com.utils.api.ModelBase;

/**
 * Created by qiangfeng on 16/5/21.
 */
public class MQAlarm extends ModelBase {
    private String res;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
