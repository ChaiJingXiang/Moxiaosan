package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.OrderObj;

/**
 * Created by qiangfeng on 16/3/23.
 */
public class RespOrderList extends ModelBase {
    private int res;
    private List<OrderObj> data;
    private String err;

    public List<OrderObj> getData() {
        return data;
    }

    public void setData(List<OrderObj> data) {
        this.data = data;
    }

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

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
