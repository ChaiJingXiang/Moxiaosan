package consumer.model;

import com.utils.api.ModelBase;

import consumer.model.obj.OrderedInfo;

/**
 * Created by chris on 16/4/7.
 */
public class RespOrderedInfo extends ModelBase {

    private String res;
    private OrderedInfo data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public OrderedInfo getData() {
        return data;
    }

    public void setData(OrderedInfo data) {
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
        return "RespOrderedInfo{" +
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
