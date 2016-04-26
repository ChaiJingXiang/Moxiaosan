package consumer.model;

import com.utils.api.ModelBase;

import consumer.model.obj.Guard;

/**
 * Created by chris on 16/4/16.
 */
public class RespGuard extends ModelBase {

    private String res;
    private Guard data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public Guard getData() {
        return data;
    }

    public void setData(Guard data) {
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
        return "RespGuard{" +
                "res='" + res + '\'' +
                ", data=" + data +
                ", err='" + err + '\'' +
                '}';
    }


    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
