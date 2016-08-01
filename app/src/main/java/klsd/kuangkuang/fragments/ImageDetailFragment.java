package klsd.kuangkuang.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import klsd.kuangkuang.R;
import klsd.kuangkuang.photoview.PhotoViewAttacher;
import klsd.kuangkuang.photoview.PhotoViewAttacher.OnPhotoTapListener;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.ExitDialog;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private ExitDialog exitDialog;
	private TextView tv_save;
	Bitmap bitmap1;
	private TextView btn;
	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Context context = getActivity().getApplicationContext();
		initImageLoader(context);

		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
		Log.d("FRAG里的url是", "onCreate() returned: " + mImageUrl);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		btn= (TextView) v.findViewById(R.id.btn_btn);
		btn.setOnLongClickListener(longClickListener);
		mImageView = (ImageView) v.findViewById(R.id.image);
		 bitmap1 = ImageLoader.getInstance().loadImageSync(mImageUrl);//根据url得到bitmap对象
		Log.d("", "onCreateView() returned: " + "走了这一步没");
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}
	private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View view) {
			Log.d("", "onCreateView() returned: " + "进来了吗");
			Cancel_Dialog();
			return true;
		}
	};
	//窗口
	private void Cancel_Dialog() {
		exitDialog = new ExitDialog(getContext(), R.style.MyDialogStyle, R.layout.dialog_cancel);
		exitDialog.setCanceledOnTouchOutside(true);
		exitDialog.show();

		tv_save = (TextView) exitDialog.findViewById(R.id.dialog_tv_cancel_collect);
		tv_save.setText(R.string.save);

		tv_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				savePicture();
				exitDialog.dismiss();
				ToastUtil.show(getActivity(),getString(R.string.save_success));
			}
		});

	}

	/**
	 * 把图片存到相册的方法
	 */
	private void savePicture(){
		MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap1, "title", "description");
		File mPhotoFile=new File("file://"
				+ Environment.getExternalStorageDirectory());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //判断安卓版本是否为高版本
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.fromFile(mPhotoFile); //out is your output file
			mediaScanIntent.setData(contentUri);
			getContext().sendBroadcast(mediaScanIntent);
		} else {
			getContext().sendBroadcast(new Intent(
					Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://"
							+ Environment.getExternalStorageDirectory())));
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ImageLoader.getInstance().displayImage(mImageUrl, mImageView,new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "下载错误";
					break;
				case DECODING_ERROR:
					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
					message = "图片太大无法显示";
					break;
				case UNKNOWN:
					message = "未知的错误";
					break;
				}
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
				mAttacher.update();
			}
		});
	}
}
