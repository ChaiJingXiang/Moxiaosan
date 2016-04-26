package consumer.model;

import com.utils.api.ModelBase;

import consumer.model.obj.OrderDetailObj;

/**
 * Created by chris on 16/3/23.
 */
public class RespOrderDetail extends ModelBase {

    private String res;
    private OrderDetailObj data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public OrderDetailObj getData() {
        return data;
    }

    public void setData(OrderDetailObj data) {
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
        return "RespOrderDetail{" +
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
