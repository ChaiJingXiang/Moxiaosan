package consumer.model;

import com.utils.api.ModelBase;

import consumer.model.obj.Alarmnums;

/**
 * Created by chris on 16/4/25.
 */
public class RespAlarmnums extends ModelBase {

    private String res;
    private Alarmnums data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public Alarmnums getData() {
        return data;
    }

    public void setData(Alarmnums data) {
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
        return "RespAlarmnums{" +
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
