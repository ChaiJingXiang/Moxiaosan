package consumer.model;

import com.ta.utdid2.device.Device;
import com.utils.api.ModelBase;

import consumer.model.obj.DeviceStatus;

/**
 * Created by chris on 16/4/17.
 */
public class RespDeviceStatus extends ModelBase {

    private String res;
    private DeviceStatus data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public DeviceStatus getData() {
        return data;
    }

    public void setData(DeviceStatus data) {
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
        return "RespDeviceStatus{" +
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
