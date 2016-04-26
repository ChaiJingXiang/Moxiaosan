package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/3/24.
 */
public class AchievementObj implements Serializable {


    private static final long serialVersionUID = -6880628661496157896L;

    private String income;
    private String orders;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "AchievementObj{" +
                "income='" + income + '\'' +
                ", orders='" + orders + '\'' +
                '}';
    }
}
