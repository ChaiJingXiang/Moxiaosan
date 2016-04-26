package consumer.model.mqttobj;

import com.utils.api.ModelBase;

/**
 * Created by qiangfeng on 16/4/16.
 */
public class MQArm extends ModelBase{
    private static final long serialVersionUID = 6642725466252700128L;
    private String err;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
