package consumer.model;

import com.utils.api.ModelBase;

/**
 * Created by fengyongqiang on 16/3/14.
 * 取消订单
 */
public class Cancelexpress extends ModelBase {

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
    public String toString() {
        return "Cancelexpress{" +
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
