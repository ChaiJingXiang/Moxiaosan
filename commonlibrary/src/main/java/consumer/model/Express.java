package consumer.model;

import com.utils.api.ModelBase;

import consumer.model.obj.RespExpress;

/**
 * Created by qiangfeng on 16/3/9.
 */
public class Express extends ModelBase {
    private String res;
    private RespExpress data;
    private String err;

    public RespExpress getData() {
        return data;
    }

    public void setData(RespExpress data) {
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
