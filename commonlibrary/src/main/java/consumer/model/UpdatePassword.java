package consumer.model;

import com.utils.api.ModelBase;

/**
 * Created by chris on 16/3/9.
 */
public class UpdatePassword extends ModelBase {

    private int res;
    private Object data;
    private String err;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
        return "UpdateName{" +
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
