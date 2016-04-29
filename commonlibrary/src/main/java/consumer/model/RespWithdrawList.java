package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.WithdrawObj;

/**
 * Created by chris on 16/4/29.
 */
public class RespWithdrawList extends ModelBase {
    private String res;
    private List<WithdrawObj> data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public List<WithdrawObj> getData() {
        return data;
    }

    public void setData(List<WithdrawObj> data) {
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
        return "RespWithdrawList{" +
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
