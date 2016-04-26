package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.RespShop;

/**
 * Created by qiangfeng on 16/3/10.
 * 出售商品列表
 */
public class ShopList extends ModelBase {
    private String res;
    private List<RespShop> data;
    private String err;

    public List<RespShop> getData() {
        return data;
    }

    public void setData(List<RespShop> data) {
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
