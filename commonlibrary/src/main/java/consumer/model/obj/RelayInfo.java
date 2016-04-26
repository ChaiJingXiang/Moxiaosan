package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/3/23.
 */
public class RelayInfo implements Serializable{


    private static final long serialVersionUID = 7217651568401234337L;

    private String handover;//接力时间
    private String handoveraddress;//接力地址
    private String re_contact;//接力人电话

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHandover() {
        return handover;
    }

    public void setHandover(String handover) {
        this.handover = handover;
    }

    public String getHandoveraddress() {
        return handoveraddress;
    }

    public void setHandoveraddress(String handoveraddress) {
        this.handoveraddress = handoveraddress;
    }

    public String getRe_contact() {
        return re_contact;
    }

    public void setRe_contact(String re_contact) {
        this.re_contact = re_contact;
    }

    @Override
    public String toString() {
        return "RelayInfo{" +
                "handover='" + handover + '\'' +
                ", handoveraddress='" + handoveraddress + '\'' +
                ", re_contact='" + re_contact + '\'' +
                '}';
    }
}
