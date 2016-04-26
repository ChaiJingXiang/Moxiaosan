package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.RespShop;

/**
 * Created by qiangfeng on 16/3/10.
 * 出售商品列表
 */
public class RespBusinissInfo extends ModelBase {
    private String res;
    private List<RespShop> data;
    private String newsnum;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public List<RespShop> getData() {
        return data;
    }

    public void setData(List<RespShop> data) {
        this.data = data;
    }

    public String getNewsnum() {
        return newsnum;
    }

    public void setNewsnum(String newsnum) {
        this.newsnum = newsnum;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return "RespBusinissInfo{" +
                "res='" + res + '\'' +
                ", data=" + data +
                ", newsnum='" + newsnum + '\'' +
                ", err='" + err + '\'' +
                '}';
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
