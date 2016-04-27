package consumer.model.obj;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qiangfeng on 16/3/10.
 * 出售商品列表  对象
 */
public class RespShop implements Serializable {
    private static final long serialVersionUID = 2268243742088734665L;
    private String Id;
    private String title;
    private String fb_datetime;
    private String describes;
    private String nums;
    private String price;
    private String address;
    private String pictures;
    private List<RespShopComment> comments;
    private boolean isOpenComments = false;
    private String username;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<RespShopComment> getComments() {
        return comments;
    }

    public void setComments(List<RespShopComment> comments) {
        this.comments = comments;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getFb_datetime() {
        return fb_datetime;
    }

    public void setFb_datetime(String fb_datetime) {
        this.fb_datetime = fb_datetime;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOpenComments() {
        return isOpenComments;
    }

    public void setOpenComments(boolean openComments) {
        isOpenComments = openComments;
    }
}
