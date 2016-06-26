package consumer.model;

import com.utils.api.ModelBase;

/**
 * Created by qiangfeng on 16/6/12.
 * 车主资料修改申请
 */
public class Modiyapply extends ModelBase {
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
        return "AddEquipment{" +
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
