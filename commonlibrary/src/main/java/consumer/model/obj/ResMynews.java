package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/3/8.
 */
public class ResMynews implements Serializable {
    private static final long serialVersionUID = -6901028208508449999L;
    private int Id;
    private String content;
    private String datetime;

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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
