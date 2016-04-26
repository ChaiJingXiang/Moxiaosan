package com.moxiaosan.both.consumer.ui.customView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * 
 * @author fengyongqiang
 *
 */
public class ImageUtils {
	
	public static final int GET_IMAGE_BY_CAMERA = 5001;
	public static final int GET_IMAGE_FROM_PHONE = 5002;
	public static final int CROP_IMAGE = 5003;
	public static Uri imageUriFromCamera;
	public static Uri cropImageUri;

	public static void openCameraImage(final Activity activity) {
		ImageUtils.imageUriFromCamera = ImageUtils.createImagePathUri(activity);
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
		// 返回图片在onActivityResult中通过以下代码获取
		// Bitmap bitmap = (Bitmap) data.getExtras().get("data"); 
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.imageUriFromCamera);
		activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_BY_CAMERA);
	}
	
	public static void openLocalImage(final Activity activity) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_FROM_PHONE);
	}

	/**
	 * 根据图片路径返回缩略图Bitmap
	 */
	public static Bitmap getBitmapThumbByPath(String path) {
		Bitmap bitmap;
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
	 * 计算缩略图所需的inSampleSize
	 * @param options 原本Bitmap的options
	 * @param minSideLength 希望生成的缩略图的宽高中的较小的值
	 * @param maxNumOfPixels 希望生成的缩量图的总像素
	 */
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

	/**
	 * 计算缩略图所需的inSampleSize
	 * @param options 原本Bitmap的options
	 * @param minSideLength 希望生成的缩略图的宽高中的较小的值
	 * @param maxNumOfPixels 希望生成的缩量图的总像素
	 */
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

	public static void cropImage(Activity activity, Uri srcUri) {
		ImageUtils.cropImageUri = ImageUtils.createImagePathUri(activity);
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(srcUri, "image/*");
		intent.putExtra("crop", "true");
		
		////////////////////////////////////////////////////////////////
		// 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
		////////////////////////////////////////////////////////////////
		// 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
		////////////////////////////////////////////////////////////////
		// 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
		////////////////////////////////////////////////////////////////
		// 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
		//	会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
		//  不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
		////////////////////////////////////////////////////////////////
		
		// aspectX aspectY 是裁剪框宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪后生成图片的宽高
//		intent.putExtra("outputX", 300);
//		intent.putExtra("outputY", 100);
		
		// return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
		// return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
		intent.putExtra("return-data", false);
		
		activity.startActivityForResult(intent, CROP_IMAGE);
		
	}
	
	/**
	 * 创建一条图片地址uri,用于保存拍照后的照片
	 * 
	 * @param context
	 * @return 图片的uri
	 */
	private static Uri createImagePathUri(Context context) {
		Uri imageFilePath = null;
		String status = Environment.getExternalStorageState();
		SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
		long time = System.currentTimeMillis();
		String imageName = timeFormatter.format(new Date(time));
		// ContentValues是我们希望这条记录被创建时包含的数据信息
		ContentValues values = new ContentValues(3);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
		values.put(MediaStore.Images.Media.DATE_TAKEN, time);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
			imageFilePath = context.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		} else {
			imageFilePath = context.getContentResolver().insert(
					MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
		}
		Log.i("", "生成的照片输出路径：" + imageFilePath.toString());
		return imageFilePath;
	}
}
