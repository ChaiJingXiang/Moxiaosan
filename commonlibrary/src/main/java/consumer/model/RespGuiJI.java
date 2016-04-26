package consumer.model;

import com.utils.api.ModelBase;

import java.util.List;

import consumer.model.obj.GuiJiObj;

/**
 * Created by chris on 16/4/9.
 */
public class RespGuiJI extends ModelBase {

    private String res;
    private List<GuiJiObj> data;
    private String maxspeed;
    private String averagespeed;
    private String journey;
    private String err;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public List<GuiJiObj> getData() {
        return data;
    }

    public void setData(List<GuiJiObj> data) {
        this.data = data;
    }

    public String getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(String maxspeed) {
        this.maxspeed = maxspeed;
    }

    public String getAveragespeed() {
        return averagespeed;
    }

    public void setAveragespeed(String averagespeed) {
        this.averagespeed = averagespeed;
    }

    public String getJourney() {
        return journey;
    }

    public void setJourney(String journey) {
        this.journey = journey;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return "RespGuiJI{" +
                "res='" + res + '\'' +
                ", data=" + data +
                ", maxspeed='" + maxspeed + '\'' +
                ", averagespeed='" + averagespeed + '\'' +
                ", journey='" + journey + '\'' +
                ", err='" + err + '\'' +
                '}';
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
