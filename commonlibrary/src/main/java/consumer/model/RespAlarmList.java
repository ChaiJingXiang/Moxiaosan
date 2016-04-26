package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.AlarmObj;

/**
 * Created by chris on 16/4/25.
 */
public class RespAlarmList extends ModelBase {

    private String res;
    private List<AlarmObj> data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public List<AlarmObj> getData() {
        return data;
    }

    public void setData(List<AlarmObj> data) {
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
        return "RespAlarmList{" +
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
