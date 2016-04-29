package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/3/8.
 */
public class ResMynews implements Serializable {
    private static final long serialVersionUID = -6901028208508449999L;
    private String Id;
    private String content;
    private String datetime;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
