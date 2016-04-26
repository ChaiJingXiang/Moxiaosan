package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/3/17.
 */
public class OrderObj implements Serializable {

    private static final long serialVersionUID = 28201936265203099L;
    private String Id;
    private String title;
    private String datetime;
    private String orderid;
    private String beginningplace;
    private String destination;
    private String reward;
    private boolean isZhidai;
    private boolean isShunfeng;
    private boolean isQingQiuJieLi;
    private boolean isJieLi;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getBeginningplace() {
        return beginningplace;
    }

    public void setBeginningplace(String beginningplace) {
        this.beginningplace = beginningplace;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public boolean isZhidai() {
        return isZhidai;
    }

    public void setZhidai(boolean zhidai) {
        isZhidai = zhidai;
    }

    public boolean isShunfeng() {
        return isShunfeng;
    }

    public void setShunfeng(boolean shunfeng) {
        isShunfeng = shunfeng;
    }

    public boolean isQingQiuJieLi() {
        return isQingQiuJieLi;
    }

    public void setQingQiuJieLi(boolean qingQiuJieLi) {
        isQingQiuJieLi = qingQiuJieLi;
    }

    public boolean isJieLi() {
        return isJieLi;
    }

    public void setJieLi(boolean jieLi) {
        isJieLi = jieLi;
    }

    @Override
    public String toString() {
        return "OrderObj{" +
                "Id='" + Id + '\'' +
                ", title='" + title + '\'' +
                ", datetime='" + datetime + '\'' +
                ", orderid='" + orderid + '\'' +
                ", beginningplace='" + beginningplace + '\'' +
                ", destination='" + destination + '\'' +
                ", reward='" + reward + '\'' +
                ", isZhidai=" + isZhidai +
                ", isShunfeng=" + isShunfeng +
                ", isQingQiuJieLi=" + isQingQiuJieLi +
                ", isJieLi=" + isJieLi +
                '}';
    }
}
