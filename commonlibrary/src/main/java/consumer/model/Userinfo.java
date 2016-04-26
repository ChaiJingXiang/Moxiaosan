package consumer.model;

import com.utils.api.ModelBase;

import consumer.model.obj.RespUserInfo;

/**
 * Created by chris on 16/3/4.
 */
public class Userinfo extends ModelBase {

    private int res;
    private RespUserInfo data;
    private String err;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public RespUserInfo getData() {
        return data;
    }

    public void setData(RespUserInfo data) {
        this.data = data;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return "Login{" +
                "res=" + res +
                ", data=" + data +
                ", err='" + err + '\'' +
                '}';
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
