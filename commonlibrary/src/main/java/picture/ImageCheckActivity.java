package picture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.moxiaosan.commonlibrary.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.common.EUtil;
import com.utils.common.KeelApplication;
import com.utils.ui.base.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import picture.adapter.ImageCheckAdatper;
import picture.view.MyHackyViewPager;

/**
 * @author fengyongqiang
 * @data 15-9-23
 * @des 图片查看activity
 */
public class ImageCheckActivity extends BaseActivity implements View.OnClickListener {
    private int index;
    private boolean isLocal;
    private static String selectUrl;
    private ArrayList<String> imageUrl;
    private ArrayList<String> thmbnailImageUrl;
    private MyThread myThread;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Looper.prepare();
            if (msg.what == 1) {
                EUtil.showToast("图片保存成功");
            } else {
                EUtil.showToast("图片保存失败");
            }
            Looper.loop();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_check);
        initView();

    }

    private void initView() {

        final TextView selsectNum = (TextView) findViewById(R.id.image_num);
        index = getIntent().getIntExtra("index", 0);
        imageUrl = getIntent().getStringArrayListExtra("imageUrl");
        thmbnailImageUrl = getIntent().getStringArrayListExtra("thmbnailImageUrl");
        isLocal = getIntent().getBooleanExtra("isLocal", false);
        selsectNum.setText((index + 1) + "/" + getIntent().getStringArrayListExtra("imageUrl").size());

        //viewPager设置
        MyHackyViewPager viewPager = (MyHackyViewPager) findViewById(R.id.image_check_view_pager);
        ImageCheckAdatper adapter = new ImageCheckAdatper(this, imageUrl, thmbnailImageUrl, isLocal);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                index = i;
                selsectNum.setText((index + 1) + "/" + getIntent().getStringArrayListExtra("imageUrl").size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //下载按钮设置
        TextView downLoad = (TextView) findViewById(R.id.down_load);
        downLoad.setOnClickListener(this);
        if (isLocal) {
            downLoad.setVisibility(View.GONE);
        } else {
            downLoad.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param context
     * @param imageUrl
     * @param index
     */
    public static void invokeStartActivity(Context context, ArrayList<String> imageUrl, int index) {
        Intent intent = new Intent(context, ImageCheckActivity.class);
        intent.putStringArrayListExtra("imageUrl", imageUrl);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }

    /**
     * @param context
     */
    public static void invokeLocalImagelStartActivity(Context context, ArrayList<String> imageUrl, int index) {
        Intent intent = new Intent(context, ImageCheckActivity.class);
        intent.putStringArrayListExtra("imageUrl", imageUrl);
        intent.putExtra("index", index);
        intent.putExtra("isLocal", true);
        context.startActivity(intent);
    }

    public static void invokeStartActivity(Context context, ArrayList<String> imageUrl, ArrayList<String> thmbnailImageUrl, int index) {
        Intent intent = new Intent(context, ImageCheckActivity.class);
        intent.putStringArrayListExtra("imageUrl", imageUrl);
        intent.putStringArrayListExtra("thmbnailImageUrl", thmbnailImageUrl);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.down_load) {
            selectUrl = imageUrl.get(index);
            myThread = new MyThread();
            myThread.start();
        }
    }


    private static class MyThread extends Thread implements Runnable {
        private boolean mRunning = false;

        public void run() {
            mRunning = true;
            while (mRunning) {
                Message message = new Message();
                if (saveImageToGallery()) {
                    message.what = 1;
                } else {
                    message.what = 0;
                }
                handler.handleMessage(message);
            }
        }

        public void close() {
            mRunning = false;
        }
    }

    /**
     * @return
     * @des 保存图片
     */
    public static boolean saveImageToGallery() {
        Bitmap bmp = ImageLoader.getInstance().loadImageSync(selectUrl);
        if (bmp == null) {
            return false;
        }

        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "MoXiaoSan");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (FileNotFoundException e) {
            return false;
        } finally {

            try {
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            bmp.recycle();
        }

        // 其次把文件插入到系统图库

//            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    bmp, fileName, "");

        // 最后通知图库更新
//        if(path != null && !"".equals(path)) {
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)));
        KeelApplication.getApplicationConxt().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
        return true;
//        }else {
//            return  false;
//        }
    }

    @Override
    protected void onDestroy() {
        if (thmbnailImageUrl != null) {
            for (int i = 0; i < thmbnailImageUrl.size(); i++) {
                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(thmbnailImageUrl.get(i));
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        }
        if (myThread != null) {
            myThread.close();
        }
        if (selectUrl != null) {
            selectUrl = "";
        }
        super.onDestroy();
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }
}

