package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/4/29.
 */
public class WithdrawObj {

    private String Id;
    private String datetime;
    private String money;
    private String type;
    private String account;
    private String status;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WithdrawObj{" +
                "Id='" + Id + '\'' +
                ", datetime='" + datetime + '\'' +
                ", money='" + money + '\'' +
                ", type='" + type + '\'' +
                ", account='" + account + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
