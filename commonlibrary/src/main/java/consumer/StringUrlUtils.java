package consumer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by chris on 16/3/4.
 */
public class StringUrlUtils {

    public static String geturl(String nameUrl, LinkedHashMap<String, Object> map) {
        String key = null;
        StringBuffer buffer =new StringBuffer();
        StringBuffer subBuffer = null;
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {

            key = iterator.next();
            subBuffer = new StringBuffer(key + "=" + map.get(key)+"&");
            buffer.append(subBuffer);
        }

        return nameUrl+buffer.toString();
    }

    /**
     * @author  fengyongqiang
     * @param map
     * @return
     */
    public static String geturl(LinkedHashMap<String, Object> map) {
        String key = null;
        StringBuffer buffer =new StringBuffer();
        StringBuffer subBuffer = null;
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {

            key = iterator.next();
            try {
                if (map.get(key) instanceof String){
                    subBuffer = new StringBuffer(key + "=" + URLEncoder.encode((String) map.get(key),"utf-8")+"&");
                }else {
                    subBuffer = new StringBuffer(key + "=" +  map.get(key)+"&");
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            buffer.append(subBuffer);
        }

        return buffer.toString();
    }
}
