package consumer.model;

import com.utils.api.ModelBase;

import consumer.model.obj.CostObj;

/**
 * Created by qiangfeng on 16/3/27.
 * 顺风车预估费用
 */
public class Ranging extends ModelBase {
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
        return "Ranging{" +
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
