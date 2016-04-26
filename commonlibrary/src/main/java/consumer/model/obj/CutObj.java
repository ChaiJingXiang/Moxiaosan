package consumer.model.obj;

/**
 * Created by chris on 16/4/16.
 */
public class CutObj {

    private String cut;

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    @Override
    public String toString() {
        return "CutObj{" +
                "cut='" + cut + '\'' +
                '}';
    }
}
