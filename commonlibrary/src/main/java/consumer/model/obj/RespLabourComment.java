package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/3/10.
 * 找劳力列表 评论对象
 */
public class RespLabourComment implements Serializable {
    private static final long serialVersionUID = 3901995981021102709L;
    private String Id;
    private String username; //评论人账号
    private String labourid; //找劳力ID
    private String datetime; //评论时间
    private String content; //评论内容

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getLabourid() {
        return labourid;
    }

    public void setLabourid(String labourid) {
        this.labourid = labourid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
