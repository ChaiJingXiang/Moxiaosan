package picture.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by peizhihui on 15/10/1.
 */
public class MyHackyViewPager extends ViewPager {

    public MyHackyViewPager(Context context) {
        super(context);
    }

    public MyHackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }

}
