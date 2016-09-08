package yksg.kuangkuang.utils;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import yksg.kuangkuang.R;


public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
				.showImageForEmptyUri(R.mipmap.ic_launcher) //
				.showImageOnFail(R.mipmap.ic_launcher) //
				.cacheInMemory(true) //
				.cacheOnDisk(true) //
				.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
		.Builder(getApplicationContext())//
				.defaultDisplayImageOptions(defaultOptions)//
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs()//
				.build();//
		ImageLoader.getInstance().init(config);
	}

	public static void initImageLoader(Context context) {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
//				.showImageForEmptyUri(R.mipmap.touxiang05) //
//				.showImageOnFail(R.mipmap.touxiang05) //
				.cacheInMemory(true) //
				.cacheOnDisk(true) //
				.displayer(new SimpleBitmapDisplayer())
				.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(defaultOptions)//
									// 内存缓存文件最大质量
				.memoryCacheExtraOptions(480, 800)
// 设置内存缓存 默认为一个当前应用可用内存的1/8大小的LruMemoryCache
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
// 设置内存缓存的最大大小 默认为一个当前应用可用内存的1/8
				.memoryCacheSize(2 * 1024 * 1024)
// 设置内存缓存最大大小占当前应用可用内存的百分比 默认为一个当前应用可用内存的1/8
				.memoryCacheSizePercentage(13)
// 硬盘缓存的设置选项 (最大图片宽度,最大图片高度,压缩格式,压缩质量，处理器)
				.diskCacheExtraOptions(480, 800, null)
// 设置自定义加载和显示内存缓存或者硬盘缓存图片的线程池
				.taskExecutorForCachedImages(
                        DefaultConfigurationFactory.createExecutor(3,

                                Thread.NORM_PRIORITY, QueueProcessingType.FIFO))
// 线程池内加载的数量
				.threadPoolSize(3)
// 设置线程的优先级
				.threadPriority(Thread.NORM_PRIORITY - 2)
//  当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.denyCacheImageMultipleSizesInMemory()
// 将保存的时候的URI名称用MD5 加密
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
//缓存文件的最大个数
				.diskCacheFileCount(100)
// 设置硬盘缓存的最大小
				.diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs()
				.build();

		ImageLoader.getInstance().init(config);
	}
}
