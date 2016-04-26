package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.ResMynews;

/**
 * Created by qiangfeng on 16/3/8.
 */
public class Mynews extends ModelBase {
    private String res;
    private String nums;
    private List<ResMynews> data;
    private String err;

    public List<ResMynews> getData() {
        return data;
    }

    public void setData(List<ResMynews> data) {
        this.data = data;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
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
