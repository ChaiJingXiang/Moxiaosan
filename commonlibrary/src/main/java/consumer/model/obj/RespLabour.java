package consumer.model.obj;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qiangfeng on 16/3/10.
 * 找劳力列表  对象
 */
public class RespLabour implements Serializable {
    private static final long serialVersionUID = 1044969461332246173L;
    private String Id;
    private String title;
    private String fb_datetime;
    private String technique;  //技能要求
    private String nums;
    private String salary;
    private String address;
    private List<RespLabourComment> comments;
    private String username;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<RespLabourComment> getComments() {
        return comments;
    }

    public void setComments(List<RespLabourComment> comments) {
        this.comments = comments;
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

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getTechnique() {
        return technique;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
