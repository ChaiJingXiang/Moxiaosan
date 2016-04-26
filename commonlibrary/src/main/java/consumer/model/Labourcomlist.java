package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.RespLabourComment;

/**
 * Created by qiangfeng on 16/3/18.
 */
public class Labourcomlist extends ModelBase {

    private int res;
    private List<RespLabourComment> data;
    private String err;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public List<RespLabourComment> getData() {
        return data;
    }

    public void setData(List<RespLabourComment> data) {
        this.data = data;
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
