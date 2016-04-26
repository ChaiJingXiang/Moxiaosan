package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.ResPictrue;

/**
 * Created by qiangfeng on 16/3/8.
 */
public class Pictrues extends ModelBase {
    private String res;
    private List<ResPictrue> data;
    private String err;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public List<ResPictrue> getData() {
        return data;
    }

    public void setData(List<ResPictrue> data) {
        this.data = data;
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
