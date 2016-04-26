package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.CostObj;
import consumer.model.obj.RespCityName;

/**
 * Created by chris on 16/4/6.
 */
public class ExpressCost extends ModelBase {

    private String res;
    private CostObj data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public CostObj getData() {
        return data;
    }

    public void setData(CostObj data) {
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
        return "ExpressCost{" +
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
