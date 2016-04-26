package consumer.model;

import com.utils.api.ModelBase;

/**
 * Created by qiangfeng on 16/3/3.
 */
public class Register extends ModelBase {
    private int res;
    private Object data;
    private String err;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }



}
