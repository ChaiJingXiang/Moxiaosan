package picture;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moxiaosan.commonlibrary.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.utils.common.EUtil;
import com.utils.image.BitmapUtils;
import com.utils.ui.base.BaseFragment_v4;

import java.util.ArrayList;
import java.util.LinkedList;

import picture.adapter.PictureGalleryAdapter;
import picture.view.PictureGalleryPopupWindow;

/**
 * Created by peizhihui on 15/10/1.
 */
public class PictureGalleryFragment extends BaseFragment_v4 implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private CursorAdapter adapter;
    private ImageLoader imageLoader;
    private LinkedList<String> imageList = new LinkedList<String>();
    private int maxNum;
    private GridView gridView;
    private TextView next;
    private TextView pictureNumber;
    private Uri imageUri;
    private TextView title;
    private PictureGalleryPopupWindow galleryListPopupWindow;
    private int dirId;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture_gallery, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        gridView = (GridView) view.findViewById(R.id.gv);
        imageLoader = ImageLoader.getInstance();

        maxNum = ((PictureGalleryActivity) getActivity()).getMaxNum();

        view.findViewById(R.id.img_back).setVisibility(View.VISIBLE);
        view.findViewById(R.id.img_back).setOnClickListener(this);
        adapter = new PictureGalleryAdapter(this, null, imageList);
        gridView.setOnScrollListener(new PauseOnScrollListener(imageLoader, false, true));
        gridView.setAdapter(adapter);
        next = (TextView) view.findViewById(R.id.txt_action);
        if (maxNum == 1) {
            next.setVisibility(View.GONE);

            RelativeLayout boomLayout = (RelativeLayout) view.findViewById(R.id.bottom);
            boomLayout.setVisibility(View.GONE);

        } else {

            next.setOnClickListener(this);
            next.setClickable(false);

            pictureNumber = (TextView) view.findViewById(R.id.picture_number);
            pictureNumber.setText(String.format("已选择 %d/%d", imageList.size(), maxNum));

//
//            preview = (TextView) view.findViewById(R.id.preview);
//            preview.setOnClickListener(this);
//            preview.setClickable(false);

        }

        title = (TextView) view.findViewById(R.id.txt_title);
        title.setText("所有照片 ");
//        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.white_arraw_up, 0);
        title.setOnClickListener(this);

        getLoaderManager().initLoader(0, null, this);
        galleryListPopupWindow = new PictureGalleryPopupWindow(this);
        galleryListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.quncao.lark.R.mipmap.white_arraw_up, 0);
            }
        });


    }

    public int getDirId() {
        return dirId;
    }

    public boolean addImage(String path) {

        if (maxNum == 1) {

            imageList.add(path);
            ArrayList<String> list = new ArrayList<String>();
            list.addAll(imageList);
            getActivity().setResult(Activity.RESULT_OK, new Intent().putStringArrayListExtra("imageList", list));
            getActivity().finish();
            return false;

        }

        if (imageList.size() < maxNum) {
            imageList.add(path);
            next.setClickable(true);
            // preview.setClickable(true);
            pictureNumber.setText(String.format("已选择 %d/%d", imageList.size(), maxNum));
            int start = gridView.getFirstVisiblePosition();
            for (int i = start; i <= gridView.getLastVisiblePosition(); i++) {
                Object itemAtPosition = gridView.getItemAtPosition(i);
                if (path.equals(itemAtPosition)) {
                    View view = gridView.getChildAt(i - start);
                    adapter.getView(i, view, gridView);
                }
            }
            return true;
        } else {
            Toast.makeText(getActivity(), String.format("最多选择%d张图片", maxNum),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void removeImage(String path) {
        int start = gridView.getFirstVisiblePosition();
        imageList.remove(path);
        if (imageList.size() == 0) {
            next.setClickable(false);
            // preview.setClickable(false);
        }
        pictureNumber.setText(String.format("已选择 %d/%d", imageList.size(), maxNum));
        for (int i = start; i <= gridView.getLastVisiblePosition(); i++) {
            Object itemAtPosition = gridView.getItemAtPosition(i);
            if (path.equals(itemAtPosition)) {
                View view = gridView.getChildAt(i - start);
                adapter.getView(i, view, gridView);
            } else {
                for (String s : imageList) {
                    if (!TextUtils.isEmpty(s)) {
                        if (s.equals(itemAtPosition)) {
                            View view = gridView.getChildAt(i - start);
                            adapter.getView(i, view, gridView);
                        }
                    }
                }
            }
        }
    }

    public void takePhoto() {

        // File file = new File(FileConstants.getApiSaveFilePath());
//        File file = new File(getPath());
//        if(!file.exists()){
//            file.mkdirs();
//        }
        // String mPicPath = FileConstants.getApiSaveFilePath() + System.currentTimeMillis() + ".jpg";
        //  String mPicPath = getPath()+System.currentTimeMillis()+".jpg";

        //检测相机硬件
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // imageUri = Uri.fromFile(new File(mPicPath));
            // intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 1);
        } else {
            EUtil.showToast("该设备中的相机不可用！");
        }

    }

    private String getPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/larkcache/image/";
        String MOBILE_PATH = Environment.getDataDirectory().getAbsolutePath() + "/larkcache/image/";


        return !sdCardExist ? MOBILE_PATH : SD_PATH;
    }

    public void switchDir(int dirId, String dirName) {
        Bundle bundle = new Bundle();
        bundle.putInt("dirId", dirId);
        getLoaderManager().restartLoader(0, bundle, this);
        title.setText(dirName + " ");
    }

    public String getNextString() {
        if (imageList.size() > 0) {
            return String.format("下一步 %d/%d", imageList.size(), maxNum);
        }
        return "下一步";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //      Log.d("Test","requestCode==="+requestCode+"===resultCode===="+resultCode);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            ArrayList<String> list = new ArrayList<String>();
            if (data.getData() != null) {
                imageUri = data.getData();
            } else {
                Bundle bundle = data.getExtras();
                Bitmap pic = (Bitmap) bundle.get("data");
                imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), pic, null, null));

            }
            if(imageUri != null) {
                list.add(BitmapUtils.getAbsoluteImagePath(getActivity(), imageUri));
            }
            getActivity().setResult(Activity.RESULT_OK, new Intent().putStringArrayListExtra("imageList", list));
            getActivity().finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        imageLoader.stop();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String select = null;
        if (args != null) {
            dirId = args.getInt("dirId", 0);
            if (dirId != 0) {
                select = String.format("%s == %s", MediaStore.Images.Media.BUCKET_ID, dirId);
            }
        }
        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return new CursorLoader(getActivity(), baseUri,
                new String[]{
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATA,
                }, select, null, MediaStore.Images.Media.DATE_TAKEN + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.txt_action) {
//            ArrayList<String> list = new ArrayList<String>();
//            list.addAll(imageList);
//            getActivity().setResult(Activity.RESULT_OK, new Intent().putStringArrayListExtra("imageList", list));
//            getActivity().finish();
          showLoadingDialog("正在处理图片...", true, new DialogInterface.OnCancelListener() {
              @Override
              public void onCancel(DialogInterface dialog) {
                  position += 1;
              }
          });
            new MyThread().start();
        }
//        else if(i == R.id.preview){
//            getFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, BigPictureFragment.getPreviewInstance(
//                            imageList, PictureGalleryFragment.this))
//                    .addToBackStack(null)
//                    .commit();
//        }
        else if (i == R.id.txt_title) {
//            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.white_arraw_up, 0);
            galleryListPopupWindow.showAsDropDown(v);

        } else if (i == R.id.img_back) {
            getActivity().finish();
        }
    }

    private Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == position) {
//                ArrayList<String> list = new ArrayList<String>();
//                list.addAll(imageList);
                if(getActivity() != null) {

                    getActivity().setResult(Activity.RESULT_OK, new Intent().putStringArrayListExtra("imageList", msg.getData().getStringArrayList("responseList")));
                    getActivity().finish();
                }
            } else {
//                ArrayList<String> list = new ArrayList<String>();
//                list.addAll(imageList);
//                if(getActivity() != null) {
//                    getActivity().setResult(Activity.RESULT_FIRST_USER, new Intent().putStringArrayListExtra("imageList", null));
//                    getActivity().finish();
//                }
            }
        }
    };

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }


    class MyThread extends Thread implements Runnable{

        public void run() {
            int num = position;
            Message message = new Message();
           ArrayList<String> responseList = new ArrayList<String>();

            for (int i = 0; i < imageList.size(); i++) {
                if (getActivity() != null) {
                    String url = BitmapUtils.getThumbUploadPath(getActivity(), imageList.get(i), 720, i);
                    if (url != null && !"".equals(url)) {
                        responseList.add(url);
                    }
                }
            }
            message.what = num;
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("responseList",responseList);
            message.setData(bundle);
            myHandle.handleMessage(message);
        }

    }

}
