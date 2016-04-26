package com.utils.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.utils.common.EUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片处理的工具类。
 * 
 * @author fengyongqiang
 */
public final class BitmapUtils {

    /* Initial blur radius. */
    private static final int DEFAULT_BLUR_RADIUS = 8;

    /** This class is never instantiated */
    private BitmapUtils() {
    }

    /**
     * Takes a bitmap and creates a new slightly blurry version of it.
     * 
     * @param sentBitmap The {@link Bitmap} to blur.
     * @return A blurred version of the given {@link Bitmap}.
     */
    public static final Bitmap createBlurredBitmap(final Bitmap sentBitmap) {
        if (sentBitmap == null) {
            return null;
        }

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        final Bitmap mBitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        final int w = mBitmap.getWidth();
        final int h = mBitmap.getHeight();

        final int[] pix = new int[w * h];
        mBitmap.getPixels(pix, 0, w, 0, 0, w, h);

        final int wm = w - 1;
        final int hm = h - 1;
        final int wh = w * h;
        final int div = DEFAULT_BLUR_RADIUS + DEFAULT_BLUR_RADIUS + 1;

        final int r[] = new int[wh];
        final int g[] = new int[wh];
        final int b[] = new int[wh];
        final int vmin[] = new int[Math.max(w, h)];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;

        int divsum = div + 1 >> 1;
        divsum *= divsum;
        final int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = i / divsum;
        }

        yw = yi = 0;

        final int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        final int r1 = DEFAULT_BLUR_RADIUS + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -DEFAULT_BLUR_RADIUS; i <= DEFAULT_BLUR_RADIUS; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + DEFAULT_BLUR_RADIUS];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = p & 0x0000ff;
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = DEFAULT_BLUR_RADIUS;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - DEFAULT_BLUR_RADIUS + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + DEFAULT_BLUR_RADIUS + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = p & 0x0000ff;

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -DEFAULT_BLUR_RADIUS * w;
            for (i = -DEFAULT_BLUR_RADIUS; i <= DEFAULT_BLUR_RADIUS; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + DEFAULT_BLUR_RADIUS];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = DEFAULT_BLUR_RADIUS;
            for (y = 0; y < h; y++) {
                pix[yi] = 0xff000000 | dv[rsum] << 16 | dv[gsum] << 8 | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - DEFAULT_BLUR_RADIUS + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        mBitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return mBitmap;
    }

    /**
     * This is only used when the launcher shortcut is created.
     * 
     * @param bitmap The artist, album, genre, or playlist image that's going to
     *            be cropped.
     * @param size The new size.
     * @return A {@link Bitmap} that has been resized and cropped for a launcher
     *         shortcut.
     */
    public static final Bitmap resizeAndCropCenter(final Bitmap bitmap, final int size) {
        final int w = bitmap.getWidth();
        final int h = bitmap.getHeight();
        if (w == size && h == size) {
            return bitmap;
        }

        final float mScale = (float)size / Math.min(w, h);

        final Bitmap mTarget = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        final int mWidth = Math.round(mScale * bitmap.getWidth());
        final int mHeight = Math.round(mScale * bitmap.getHeight());
        final Canvas mCanvas = new Canvas(mTarget);
        mCanvas.translate((size - mWidth) / 2f, (size - mHeight) / 2f);
        mCanvas.scale(mScale, mScale);
        final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        mCanvas.drawBitmap(bitmap, 0, 0, paint);
        return mTarget;
    }

    /**
     * Used to remove the saturation (if saturate) and slightly enlarge a
     * {@link Bitmap}.
     * 
     * @param bitmap The {@link Bitmap} to filer.
     */
    public static final Bitmap createSaturatedBitmap(final Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        final Bitmap mBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.RGB_565);
        final Canvas mCanvas = new Canvas(mBitmap);
        final Paint mPaint = new Paint();
        final ColorMatrix mColorMatrix = new ColorMatrix();
        mColorMatrix.setSaturation(0);
        final ColorMatrix mDarkMatrix = new ColorMatrix();
        mDarkMatrix.setScale(0.3f, 0.3f, 0.3f, 1.0f);
        mColorMatrix.postConcat(mDarkMatrix);
        final ColorMatrixColorFilter mFilter = new ColorMatrixColorFilter(mColorMatrix);
        mPaint.setColorFilter(mFilter);
        mCanvas.drawBitmap(bitmap, 0, 0, mPaint);
        return mBitmap;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
//    Build.VERSION_CODES.KITKAT 19
    @TargetApi(19)
    public static String getAbsoluteImagePath(final Context context, final Uri uri) {
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri)) {
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static Bitmap getBitmapThumbByPath(String path) {
        Bitmap bitmap = null;
        int angle = readOrientation(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = computeSampleSize(options, -1, 500 * 500);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        bitmap = BitmapFactory.decodeFile(path, options);//根据Path读取资源图片
        if (angle != 0) {
            // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(angle); // 旋转angle度
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    m, true);// 从新生成图片
            if (newBitmap != bitmap) {
                bitmap.recycle();
            }
            return newBitmap;
        }
        return bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }
    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 读取图片的翻转角度
     */
    public static int readOrientation(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getThumbUploadPath(Context context, String oldPath, int bitmapMaxValue,int position) {
        int angle = readOrientation(oldPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(oldPath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int reqHeight;
        int reqWidth;

//        if (width > bitmapMaxValue || height > bitmapMaxValue) {
        if(width > bitmapMaxValue){
//            if (width >= height) {
                reqWidth = bitmapMaxValue;
                reqHeight = (reqWidth * height) / width;
//            } else {
//                reqHeight = bitmapMaxValue;
//                reqWidth = (reqHeight * width) / height;
//            }
            // 在内存中创建bitmap对象，这个对象按照缩放大小创建的
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
            if (bitmap == null) {
                return "";
            }
            Bitmap bbb;

            if(angle != 0){
                Matrix m = new Matrix();
                m.setRotate(angle); // 旋转angle度
               bbb = compressImage(Bitmap.createBitmap(bitmap, 0, 0, reqWidth, reqHeight,m, true));
            }else {

                bbb = compressImage(Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true));
            }
            if (bbb == null) {
                return "";
            }
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+"_"+position;
            return saveImg(context, bbb, timeStamp);
        } else {
            options.inJustDecodeBounds = false;
            return oldPath;
        }

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length > 500 * 1024) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
            options -= 10; // 每次都减少10
            baos.reset(); // 重置baos即清空baos
            if (options <= 0) {
                break;
            }
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    // 保存图片
    public static String saveImg(Context context, Bitmap b, String name) {
        try {
            File path = EUtil.getAppCacheFileDir(context);
            File mediaFile = new File(path, name + ".jpg");
            if (mediaFile.exists()) {
                mediaFile.delete();
            }
            if (!path.exists()) {
                path.mkdirs();
            }
            mediaFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(mediaFile);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            b.recycle();
            b = null;
            System.gc();
            return mediaFile.getPath();
        } catch (IOException e) {
            return "";
        }
    }

}
