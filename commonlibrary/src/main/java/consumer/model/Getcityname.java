package consumer.model;

import com.utils.api.ModelBase;
import com.utils.api.ModelBaseCache;

import java.util.List;

import consumer.model.obj.RespCityName;

/**
 * Created by qiangfeng on 16/3/16.
 */
public class Getcityname extends ModelBaseCache {
    private String res;
    private List<RespCityName> data;
    private String err;

    public List<RespCityName> getData() {
        return data;
    }

    public void setData(List<RespCityName> data) {
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

    @Override
    public String getKeyValue() {
        return "Getcityname";
    }
}
