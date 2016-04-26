package consumer.model.obj;

/**
 * Created by chris on 16/4/25.
 */
public class Alarmnums  {

    private String nums;
    private String alarminfo;

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getAlarminfo() {
        return alarminfo;
    }

    public void setAlarminfo(String alarminfo) {
        this.alarminfo = alarminfo;
    }

    @Override
    public String toString() {
        return "Alarmnums{" +
                "nums='" + nums + '\'' +
                ", alarminfo='" + alarminfo + '\'' +
                '}';
    }
}
