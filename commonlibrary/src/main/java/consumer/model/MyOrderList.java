package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.MyOrderObj;

/**
 * Created by chris on 16/3/23.
 */
public class MyOrderList extends ModelBase {

    private String res;
    private List<MyOrderObj> data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public List<MyOrderObj> getData() {
        return data;
    }

    public void setData(List<MyOrderObj> data) {
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
        return "MyOrderList{" +
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
