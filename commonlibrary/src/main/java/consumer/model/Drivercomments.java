package consumer.model;

import com.utils.api.ModelBase;

/**
 * Created by feng on 16/3/23.
 * 评价顺风车  速递
 */
public class Drivercomments extends ModelBase {
    private String res;
    private Object data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

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
