package picture.view;

/**
 * Created by peizhihui on 15/10/2.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 方形ImageView
 */
public class MySquareImageView extends ImageView {
    public MySquareImageView(Context context) {
        super(context);
    }

    public MySquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}

