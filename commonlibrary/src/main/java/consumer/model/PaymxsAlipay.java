package consumer.model;

import com.utils.api.ModelBase;

/**
 * Created by qiangfeng on 16/4/19.
 */
public class PaymxsAlipay extends ModelBase {
    private int err;
    private String query;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public ModelBase parseData(String strData) {
        return null;
    }
}
