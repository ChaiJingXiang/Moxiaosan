package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.OrderDetailObj;
import consumer.model.obj.RelayInfo;
import consumer.model.obj.SDOrderDetailObj;

/**
 * Created by chris on 16/3/23.
 */
public class RespSDOrderDetail extends ModelBase {

    private String res;
    private SDOrderDetailObj data;
    private String err;
    private List<RelayInfo> relayinfo;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public SDOrderDetailObj getData() {
        return data;
    }

    public void setData(SDOrderDetailObj data) {
        this.data = data;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public List<RelayInfo> getRelayinfo() {
        return relayinfo;
    }

    public void setRelayinfo(List<RelayInfo> relayinfo) {
        this.relayinfo = relayinfo;
    }

    @Override
    public String toString() {
        return "RespSDOrderDetail{" +
                "res='" + res + '\'' +
                ", data=" + data +
                ", err='" + err + '\'' +
                ", relayinfo=" + relayinfo +
                '}';
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
