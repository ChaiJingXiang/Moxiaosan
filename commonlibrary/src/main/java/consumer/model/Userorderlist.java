package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.RespUserOrder;

/**
 * Created by qiangfeng on 16/3/23.
 */
public class Userorderlist extends ModelBase {
    private String err;
    private List<RespUserOrder> data;
    private String res;

    public List<RespUserOrder> getData() {
        return data;
    }

    public void setData(List<RespUserOrder> data) {
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
