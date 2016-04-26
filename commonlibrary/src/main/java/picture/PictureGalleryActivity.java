package picture;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.moxiaosan.commonlibrary.R;
import com.utils.ui.base.BaseFragmentActivity;

/**
 * Created by chaijingxiang on 15/10/1.
 */
public class PictureGalleryActivity extends BaseFragmentActivity {

    private int maxNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);
        maxNum = getIntent().getIntExtra("maxNum", 0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, new PictureGalleryFragment())
                .commit();
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    public int getMaxNum() {
        return maxNum;
    }

}