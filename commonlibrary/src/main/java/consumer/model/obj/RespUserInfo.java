package consumer.model.obj;

import java.io.Serializable;

/**
 * Created by chris on 16/3/7.
 */
public class RespUserInfo implements Serializable {

    private static final long serialVersionUID = -1770146043268004779L;

    private String username;  //手机号
    private String headportrait;   //头像
    private String password;
    private String nickname;  //昵称
    private String contact;  //联系方式
    private String balance;   //余额
    private int type; //身份类型   //用户身份类型  1 普通用户 2 普通车主 3 运营车主
    private String address;
    private String carbrand;
    private String carimg;
    private String mileage;
    private String platenum; //车牌号
    private String appstatus;

    private String IMEI;
    private int userType;
    private int bind=3;



    public String getPlatenum() {
        return platenum;
    }

    public void setPlatenum(String platenum) {
        this.platenum = platenum;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadportrait() {
        return headportrait;
    }

    public void setHeadportrait(String headportrait) {
        this.headportrait = headportrait;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarbrand() {
        return carbrand;
    }

    public void setCarbrand(String carbrand) {
        this.carbrand = carbrand;
    }

    public String getCarimg() {
        return carimg;
    }

    public void setCarimg(String carimg) {
        this.carimg = carimg;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public String getAppstatus() {
        return appstatus;
    }

    public void setAppstatus(String appstatus) {
        this.appstatus = appstatus;
    }

    @Override
    public String toString() {
        return "RespUserInfo{" +
                "address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", headportrait='" + headportrait + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", contact='" + contact + '\'' +
                ", balance='" + balance + '\'' +
                ", type=" + type +
                ", carbrand='" + carbrand + '\'' +
                ", carimg='" + carimg + '\'' +
                ", mileage='" + mileage + '\'' +
                ", IMEI='" + IMEI + '\'' +
                ", userType=" + userType +
                ", bind=" + bind +
                ", appstatus='" + appstatus + '\'' +
                '}';
    }
}
