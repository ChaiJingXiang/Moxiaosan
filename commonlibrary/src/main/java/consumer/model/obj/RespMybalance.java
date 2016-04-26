package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/3/25.
 */
public class RespMybalance implements Serializable {
    private static final long serialVersionUID = 1457441164808866377L;
    private String Id;

    private String money;

    private String datetime;

    private String title;

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
