package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/3/8.
 */
public class ResPictrue implements Serializable {
    private static final long serialVersionUID = -1941321224662811152L;
    private int Id;
    private String picurl;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}
