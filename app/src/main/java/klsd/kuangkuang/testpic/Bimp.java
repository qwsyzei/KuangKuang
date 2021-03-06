package klsd.kuangkuang.testpic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Bimp {
	public static int max = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();	
	
	//图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static List<String> drr = new ArrayList<String>();
	

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 800)
					&& (options.outHeight >> i <= 800)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}

		return bitmap;
	}
//	/**
//	 * 通过uri获取图片并进行压缩
//	 *
//	 * @param path
//	 */
//	public static Bitmap getBitmapFormUri(Activity ac,String path) throws FileNotFoundException, IOException {
//
//		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
//		onlyBoundsOptions.inJustDecodeBounds = true;
//		onlyBoundsOptions.inDither = true;//optional
//		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
//		BitmapFactory.decodeFile(path, onlyBoundsOptions);
//		int originalWidth = onlyBoundsOptions.outWidth;
//		int originalHeight = onlyBoundsOptions.outHeight;
//		if ((originalWidth == -1) || (originalHeight == -1))
//			return null;
//		//图片分辨率以480x800为标准
//		float hh = 800f;//这里设置高度为800f
//		float ww = 480f;//这里设置宽度为480f
//		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//		int be = 1;//be=1表示不缩放
//		if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
//			be = (int) (originalWidth / ww);
//		} else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
//			be = (int) (originalHeight / hh);
//		}
//		if (be <= 0)
//			be = 1;
//		//比例压缩
//		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//		bitmapOptions.inSampleSize = be;//设置缩放比例
//		bitmapOptions.inDither = true;//optional
//		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
//		Bitmap bitmap = BitmapFactory.decodeFile(path, bitmapOptions);
//
//		return compressImage(bitmap);//再进行质量压缩
//	}
	/**
	 * 质量压缩方法
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		Log.d("我进来质量压缩方法了", "compressImage() returned: " + "");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			//第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

}
