package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.RespMybalance;

/**
 * Created by qiangfeng on 16/3/23.
 */
public class Mybalance extends ModelBase {
    private String res;
    private List<RespMybalance> data;
    private String balance;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public List<RespMybalance> getData() {
        return data;
    }

    public void setData(List<RespMybalance> data) {
        this.data = data;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return "Mybalance{" +
                "res='" + res + '\'' +
                ", data=" + data +
                ", balance='" + balance + '\'' +
                ", err='" + err + '\'' +
                '}';
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
