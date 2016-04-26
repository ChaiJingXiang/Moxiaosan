package consumer.model;

import com.utils.api.ModelBase;
import com.utils.api.ModelBaseCache;

import java.util.List;

import consumer.model.obj.RespLabour;

/**
 * Created by qiangfeng on 16/3/10.
 * 找劳力列表
 */
public class LabourList extends ModelBaseCache {
    private String res;
    private List<RespLabour> data;
    private String err;

    public List<RespLabour> getData() {
        return data;
    }

    public void setData(List<RespLabour> data) {
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
    public String getKeyValue() {
        return "LabourList";
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
