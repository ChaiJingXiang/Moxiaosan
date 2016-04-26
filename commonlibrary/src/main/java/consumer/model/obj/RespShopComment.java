package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/3/10.
 * 商品列表  评论对象
 */
public class RespShopComment implements Serializable {
    private static final long serialVersionUID = -1084139527239974246L;
    private String Id;
    private String username;
    private String shopid;
    private String commentstext;
    private String datetime;

    public String getCommentstext() {
        return commentstext;
    }

    public void setCommentstext(String commentstext) {
        this.commentstext = commentstext;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
