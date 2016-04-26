package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.RespMycomment;

/**
 * Created by fengyongqiang on 16/3/14.
 * 我的评价
 */
public class Mycomments extends ModelBase {

    private String res;
    private List<RespMycomment> data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public List<RespMycomment> getData() {
        return data;
    }

    public void setData(List<RespMycomment> data) {
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
        return "Mycomments{" +
                "res=" + res +
                ", data=" + data +
                ", err='" + err + '\'' +
                '}';
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
