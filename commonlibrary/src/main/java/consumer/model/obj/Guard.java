package consumer.model.obj;

/**
 * Created by chris on 16/4/16.
 */
public class Guard {

    private String guard;

    public String getGuard() {
        return guard;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    @Override
    public String toString() {
        return "Guard{" +
                "guard='" + guard + '\'' +
                '}';
    }
}
