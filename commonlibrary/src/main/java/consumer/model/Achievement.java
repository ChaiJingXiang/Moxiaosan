package consumer.model;

import com.utils.api.ModelBase;

import consumer.model.obj.AchievementObj;

/**
 * Created by chris on 16/3/23.
 */
public class Achievement extends ModelBase {

    private String res;
    private AchievementObj data;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public AchievementObj getData() {
        return data;
    }

    public void setData(AchievementObj data) {
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
        return "Achievement{" +
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
