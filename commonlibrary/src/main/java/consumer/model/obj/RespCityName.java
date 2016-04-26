package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/3/16.
 */
public class RespCityName implements Serializable {
    private static final long serialVersionUID = -7905173167162136955L;
    private String Id;
    private String cityname;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
