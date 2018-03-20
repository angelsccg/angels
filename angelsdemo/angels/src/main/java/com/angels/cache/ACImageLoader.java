package com.angels.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

import com.angels.R;

/***
 * 
 * @ClassName: ACImageLoader
 * @Description: 异步加载图片
 * @author angelsC
 * @date 2015-10-28 上午11:20:00
 * 
 *       使用方法： 
 *       初始化：
 *       ACImageLoader.getInstance().init();
 *       
 *       ACImageLoader.getInstance().LoadImage(productItems.get(position).getPic(),
 *       (ImageView)view.findViewById(R.id.pic));
 */
public class ACImageLoader {
	public static final String IMG_PATH = Environment.getExternalStorageDirectory().getPath() + "/angels/image/";
	/*** 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动） */
	private Map<String, SoftReference<Drawable>> imageCache;
	/** 固定n个线程来执行任务 */
	private ExecutorService executorService;
	/**SD卡上图片储存地址**/
	private String path;
	
	private final Handler handler = new Handler();
	/**加载中的时候显示的图片**/
	private int loadingImageId;
	/**加载错误的时候显示的图片**/
	private int errorImageId;
	/**
	 * 单例对象实例
	 */
	private static ACImageLoader instance = null;

	public static ACImageLoader getInstance() {
		if (instance == null) {
			instance = new ACImageLoader();
		}
		return instance;
	}
	
	/**初始化**/
	public void init(){
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		executorService = Executors.newFixedThreadPool(5);
		loadingImageId = R.drawable.ac_empty;
		errorImageId = R.drawable.ac_error;
		path = IMG_PATH;
	}
	/**
	 * 初始化
	 * @param ThreadNum  线程池中线程数
	 * @param loadingImageId （加载中的时候显示的图片）
	 * @param errorImageId  加载错误的时候显示的图片
	 * @param path SD卡上图片储存地址
	 */
	public void init(int ThreadNum,int loadingImageId,int errorImageId,String path){
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		executorService = Executors.newFixedThreadPool(ThreadNum);
		this.loadingImageId = loadingImageId;
		this.errorImageId = errorImageId;
		if(path!=null){
			this.path = path;
		}else{
			path = IMG_PATH;
		}
	}

	/**
	 * 
	 * @param imageUrl
	 *            图像url地址
	 * @param callback
	 *            回调接口
	 * @return 返回内存中缓存的图像，第一次加载返回null
	 */
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback callback) {
		// 如果缓存过就从缓存中取出数据
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		} else if (useTheImage(imageUrl) != null) {
			return useTheImage(imageUrl);
		}
		// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
		executorService.submit(new Runnable() {
			public void run() {
				try {
					final Drawable drawable = Drawable.createFromStream(
							new URL(imageUrl).openStream(), "image.png");
					imageCache.put(imageUrl, new SoftReference<Drawable>(
							drawable));
					handler.post(new Runnable() {
						public void run() {
							callback.imageLoaded(drawable);
						}
					});
					saveFile(drawable, imageUrl);
				} catch (Exception e) {
					callback.imageLoadedError();
					throw new RuntimeException(e);
				}
			}
		});
		return null;
	}

	// 从网络上取数据方法
	public Drawable loadImageFromUrl(String imageUrl) {
		try {
			return Drawable.createFromStream(new URL(imageUrl).openStream(),"image.png");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对外界开放的回调接口
	*
	 */
	public interface ImageCallback {
		/** 注意 此方法是用来设置目标对象的图像资源 **/
		public void imageLoaded(Drawable imageDrawable);
		/** 图片加载失败的时候处理**/
		public void imageLoadedError();
	}

	/**
	 * 
	 * @param url 图片的url
	 * @param iv  ImageView控件
	 * 
	 * 引入线程池，并引入内存缓存功能,并对外部调用封装了接口，简化调用过程
	 */
	public void LoadImage(final String url, final ImageView iv) {
		LoadImage(url, iv, 0 ,0);
	}
	/**
	 * 
	 * @param url 图片的url
	 * @param iv  ImageView控件
	 * @param loadingImageId  加载中的时候显示的图片
	 *  @param errorImageId 加载错误显示的图片
	 * 
	 * 引入线程池，并引入内存缓存功能,并对外部调用封装了接口，简化调用过程
	 */
	public void LoadImage(final String url, final ImageView iv,int loadingImageId,final int errorImageId) {
		//TODO  iv.getImageMatrix() == null 不知道什么用处 
//		if (iv.getImageMatrix() == null) {
			if(loadingImageId==0){
				iv.setImageResource(this.loadingImageId);
			}else{
				iv.setImageResource(loadingImageId);
			}
//		}
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = loadDrawable(url,
				new ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					@Override
					public void imageLoaded(Drawable imageDrawable) {
						iv.setImageDrawable(imageDrawable);
					}

					@Override
					public void imageLoadedError() {
						if(errorImageId==0){
							iv.setImageResource(ACImageLoader.this.errorImageId);
						}else{
							iv.setImageResource(errorImageId);
						}
					}
				});
		if (cacheImage != null) {
			iv.setImageDrawable(cacheImage);
		}
	}

	/**
	 * 保存图片到SD卡上
	 * 
	 * @param bm
	 * @param fileName
	 * 
	 */
	public void saveFile(Drawable dw, String url) {
		try {
			BitmapDrawable bd = (BitmapDrawable) dw;
			Bitmap bm = bd.getBitmap();

			// 获得文件名字
			final String fileNa = url.substring(url.lastIndexOf("/") + 1,
					url.length()).toLowerCase();
			File file = new File(path + fileNa);
			// 创建图片缓存文件夹
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				File maiduo = new File(path);
				// 如果文件夹不存在
				if (!maiduo.exists()) {
					// 按照指定的路径创建文件夹
					maiduo.mkdirs();
				}
				// 检查图片是否存在
				if (!file.exists()) {
					file.createNewFile();
				}
			}

			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 使用SD卡上的图片
	 * 
	 */
	public Drawable useTheImage(String imageUrl) {

		Bitmap bmpDefaultPic = null;

		// 获得文件路径
		String imageSDCardPath = path + imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
						imageUrl.length()).toLowerCase();
		File file = new File(imageSDCardPath);
		// 检查图片是否存在
		if (!file.exists()) {
			return null;
		}
		bmpDefaultPic = BitmapFactory.decodeFile(imageSDCardPath, null);

		if (bmpDefaultPic != null || bmpDefaultPic.toString().length() > 3) {
			Drawable drawable = new BitmapDrawable(bmpDefaultPic);
			return drawable;
		} else
			return null;
	}

}