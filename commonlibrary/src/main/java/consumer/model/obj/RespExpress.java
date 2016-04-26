package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by qiangfeng on 16/4/15.
 */
public class RespExpress implements Serializable {
    private static final long serialVersionUID = 4589245025986792400L;
    private String username;
    private String orderid;
    private String beginningplace;
    private String b_x;
    private String b_y;
    private String destination;
    private String d_x;
    private String d_y;

    public String getB_x() {
        return b_x;
    }

    public void setB_x(String b_x) {
        this.b_x = b_x;
    }

    public String getB_y() {
        return b_y;
    }

    public void setB_y(String b_y) {
        this.b_y = b_y;
    }

    public String getBeginningplace() {
        return beginningplace;
    }

    public void setBeginningplace(String beginningplace) {
        this.beginningplace = beginningplace;
    }

    public String getD_x() {
        return d_x;
    }

    public void setD_x(String d_x) {
        this.d_x = d_x;
    }

    public String getD_y() {
        return d_y;
    }

    public void setD_y(String d_y) {
        this.d_y = d_y;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
