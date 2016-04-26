package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by fengyongqiang on 2016/2/25.
 */
public class RespUserEntity implements Serializable {
    private static final long serialVersionUID = -9211841034805076703L;
    public final static int USER_TYPE_LOGIN = 1;
    public final static int USER_UNKNOW=0;
    public final static int USER_MALE=1;
    public final static int USER_FEMALE=2;
    private int id;
    private String nick;
    private String name;
    private int gender;  //1-男，2-女
    private String mobile;
    private long birthday;
    private int accumulate;
    private int activity;
    private int fans;
    private int favorite;
    private int badge;
//    private Image image;
    private int userType;
    private String securityString;
    private int loginType;
    private int isMaster;
    private int contactsNum;
    private String address;
    private int waitJoinActivity; //待参加
    private int waitAccessActivity; //待评价


    public int getWaitJoinActivity() {
        return waitJoinActivity;
    }

    public void setWaitJoinActivity(int waitJoinActivity) {
        this.waitJoinActivity = waitJoinActivity;
    }

    public int getWaitAccessActivity() {
        return waitAccessActivity;
    }

    public void setWaitAccessActivity(int waitAccessActivity) {
        this.waitAccessActivity = waitAccessActivity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

//    public Image getImage() {
//        return image;
//    }
//
//    public void setImage(Image image) {
//        this.image = image;
//    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getAccumulate() {
        return accumulate;
    }

    public void setAccumulate(int accumulate) {
        this.accumulate = accumulate;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getSecurityString() {
        return securityString;
    }

    public void setSecurityString(String securityString) {
        this.securityString = securityString;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public int getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(int isMaster) {
        this.isMaster = isMaster;
    }

    public int getContactsNum() {
        return contactsNum;
    }

    public void setContactsNum(int contactsNum) {
        this.contactsNum = contactsNum;
    }

    @Override
    public String toString() {
        return "RespUserEntity{" +
                "id=" + id +
                ", nick='" + nick + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", mobile='" + mobile + '\'' +
                ", birthday=" + birthday +
                ", accumulate=" + accumulate +
                ", activity=" + activity +
                ", fans=" + fans +
                ", favorite=" + favorite +
                ", badge=" + badge +
                ", userType=" + userType +
                ", securityString='" + securityString + '\'' +
                ", loginType=" + loginType +
                ", isMaster=" + isMaster +
                ", contactsNum=" + contactsNum +
                ", address='" + address + '\'' +
                ", waitJoinActivity=" + waitJoinActivity +
                ", waitAccessActivity=" + waitAccessActivity +
                '}';
    }
}
