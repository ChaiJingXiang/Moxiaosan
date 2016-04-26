package consumer.model;

import com.utils.api.ModelBase;

/**
 * Created by qiangfeng on 16/3/16.
 */
public class Dellabour extends ModelBase {
    private String res;
    private Object data;
    private String err;

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
