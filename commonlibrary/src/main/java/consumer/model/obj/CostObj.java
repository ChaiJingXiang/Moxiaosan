package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/4/12.
 */
public class CostObj implements Serializable {


    private static final long serialVersionUID = 7912369568617566649L;
    private String cost;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "CostObj{" +
                "cost='" + cost + '\'' +
                '}';
    }
}
