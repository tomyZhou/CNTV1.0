package com.weichuang.china.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;
import cn.hi321.android.player.R;

import com.weichuang.china.BaseActivity;
import com.weichuang.china.VideoInfo;
import com.weichuang.china.video.player.SystemPlayer;
/**
 * 
 * @author yanggf
 * 
 */
public class Utils {
	private static final String TAG = "Utils";

	public static final String APP_VERSION = "1.5";
	
	public static final String ECODER_1 = "VFP.apk";
	public static final String ECODER_2 = "ARMv7-NEON.apk";
	
	
	public static boolean  isErrorNum =  false;
	

	public static final String FS_CACHE = "fscache";

	public static final String synchronizeds = "synchronized";

	public static boolean isLogined = false; 

	public static final String NET_WORK_INVAILABLE = "netInvailable"; 
	public static final String NULL = "null";


	public static Dialog mMediaDialog;

	public static int CurrentPosInMediaIdList;

	public static boolean isFileChange = false;

	private static Dialog dialog = null;



	public final static String PLAY_LOADING_KEY = "play_loading_key";
	public final static String BY_PLAY_HISTORY_KEY = "by_play_histoty_key";

	/** 文件保存路径sdcard/chinavideo/ */

	public static final String SAVE_FILE_PATH_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/" + "chinavideo";
	
	/** 文件保存路径sdcard */

	public static final String SDCARD = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/** 升级请求路径标识 **/

	/** 缓存解码器的目录 */
	public static final String CACHE_IMG_DIR_PATH = "/ecoder/";

	/** 缓存解码APK的目录 */
	public static final String CACHE_ECODER_PATH = "/ecoder/";

	/** 手机网络cmwap */
	public static final String NET_CMWAP = "cmwap";
	/** 手机网络3gwap */
	public static final String NET_WAP_3G = "3gwap";
	/** 手机网络uniwap */
	public static final String NET_UNIWAP = "uniwap";

	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";

	/** session过期，重新请求配置 */
	public static final byte HANDLER_SESSION_EXPIRED = 1;
 
	/** errorMessage */
	public static final String KEY_ERROR_MESSAGE = "errorMessage";
 
	// 图片最大小界限
	public final static int IMAGE_SIZE_COMPRESS = 200 * 1024;

	public static int BITMAP_DENSITY = 160;

	public static int POSITION = 1000;
	public static int CURRENT_SYSTEM_DENSITY = 160;
 
	
	
	/**
	 * 设置菜单按钮背景颜色
	 * */
	   public static int color[] = new int[] { Color.BLACK, Color.BLACK }; 
	   public static Drawable setBackgroundType(String tyle ){
		   //Orientation.TOP_BOTTOM这个参数决定渐变是从上到下渐变的
	        GradientDrawable grad = new GradientDrawable(Orientation.BOTTOM_TOP, color); 
	        grad.setGradientType(GradientDrawable.LINEAR_GRADIENT); 
	     
	        if(tyle.equals("半圆形")){
	        	 grad.setCornerRadii(new float[] { 10, 10, 10, 10,10, 10, 10, 10 });
	        }else if(tyle.equals("圆形")){
	        	   grad.setCornerRadii(new float[] { 70, 70,70,70, 70, 70, 70, 70 });
	        }else if(tyle.equals("正方形")){
	        	 grad.setCornerRadii(new float[] { 1, 1,1,1, 1, 1, 1, 1 });
	        }
	       
		   return grad;
	   }

 
	
	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public static String getSDcard() {
		String sdcard = null;
		final String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			sdcard = SDCARD;
		}
		return sdcard;
	}
 

	/**
	 * 验证邮箱格式
	 */
	public static boolean checkEmail(String email) {
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(email);
		return m.find();
	}
 
	private static final String LOG_PATH = SAVE_FILE_PATH_DIRECTORY
			+ "/fslog.txt";
	public static File file = new File(LOG_PATH);

	/**
	 * 写入文件
	 * 
	 * @param str
	 */
	public synchronized static void writeFile(String content) {
		if (TextUtils.isEmpty(content) || !isSDcardExist()) {
			return;
		}
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			long len = raf.length();
			raf.seek(len);
			raf.writeBytes(content);
			raf.close();
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString());
		}
	}

	 
	/**
	 * 判断是否有存储卡，有返回TRUE，否则FALSE
	 * 
	 * @return
	 */
	public static boolean isSDcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
 
	/**
	 * MD5 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			LogUtil.e(TAG, e.toString());
			return null;
		} catch (UnsupportedEncodingException e) {
			LogUtil.e(TAG, e.toString());
			return null;
		}

		final byte[] byteArray = messageDigest.digest();

		final StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		// 16位加密，从第9位到25位
		return md5StrBuff.substring(8, 24).toString().toUpperCase();
	}
 

	/**
	 * 获取源图片的BITMAP，无压缩，本地图片
	 * 
	 * @param sImagePath
	 * @return
	 */
	public static Bitmap getImgCacheFromLocal(String sImagePath) {
		try {
			final File f = new File(sImagePath);
			if (!f.exists()) {
				return null;
			}
			final FileInputStream fis = new FileInputStream(f);
			LogUtil.i(TAG, "fis==" + fis);
			final Bitmap bitmap = BitmapFactory.decodeStream(fis);
			fis.close();
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			LogUtil.e(TAG, ex.toString());
			return null;
		}
	}

	
	public static void showToast(Context act, String content, int length) {
		Toast.makeText(act, content, length).show();
	}

	/**
	 * 保存图片
	 * 
	 * @param sOldImagePath
	 * @param sNewImagePath
	 *            图片路径格式如 /data/data/com.xxx/1.png
	 * @return
	 */
	public static boolean saveImage(Bitmap oldbitmap, String sNewImagePath) {
		LogUtil.e(TAG, "存入本地缓存" + sNewImagePath);
		try {
			final FileOutputStream fileout = new FileOutputStream(sNewImagePath);
			oldbitmap.compress(CompressFormat.PNG, 100, fileout);
			fileout.flush();
			fileout.close();
			LogUtil.e(TAG, "存入本地成功" + sNewImagePath);
			return true;
		} catch (Exception e) {
			LogUtil.e(TAG, "存入本地失败" + sNewImagePath);
			e.printStackTrace();
			LogUtil.e(TAG, e.toString());
			return false;
		}
	}

	public static String getAppFilesDir(Context context) {

		if (isSDcardExist()) {
			return getAppFilesDirBySDCard(context);
		} else {
			return getAppFilesDirByData(context);
		}
	}

	public static String getAppFilesDirByData(Context context) {
		return context.getFilesDir().getAbsolutePath();
	}

	/**
	 * 获取安装在用户手机上的sdcard/下的chianandroid目录
	 * 
	 */
	public static String getAppFilesDirBySDCard(Context context) {
		return SAVE_FILE_PATH_DIRECTORY;
	}

	public static void initCacheFile(Context context) {

		if (isSDcardExist()) {
			initCacheFileBySDCard(context);
		} else {
			initCacheFileByData(context);
		}

	}

	/**
	 * 创建/data/data/com.weichuang.china/files/imgfiles缓存文件夹
	 */
	public static void initCacheFileByData(Context context) {
		final String imageDir = getAppFilesDirByData(context)
				+ CACHE_IMG_DIR_PATH;
		final File imageFileDir = new File(imageDir);
		if (!imageFileDir.exists()) {
			imageFileDir.mkdirs();
		}
	}

	public static void initCacheFileBySDCard(Context context) {
		final String imageDir = getAppFilesDirBySDCard(context)
				+ CACHE_IMG_DIR_PATH;
		final File imageFileDir = new File(imageDir);
		if (!imageFileDir.exists()) {
			imageFileDir.mkdirs();
		}
	}
 
	private static Pattern StringPattern = null;
 

	public static String getNetMode(Context context) {
		String netMode = "";
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				netMode = NET_WORK_INVAILABLE;
			} else {
				int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					netMode = mobNetInfoActivity.getTypeName();
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					netMode = mobNetInfoActivity.getExtraInfo();

				} else {
					// Do nothing
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			netMode = "";
		} finally {
			if ("epc.tmobile.com".equals(netMode) || "".equals(netMode)) {
				netMode = "3G";
				return netMode;
			}
		}
		return netMode;
	}
	
	
	

	 
	/**
	 * 检测手机是否已插入SIM卡
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isCheckSimCardAvailable(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getSimState() != TelephonyManager.SIM_STATE_READY) {
			return false;
		}
		return true;
	}

	/**
	 * 检测是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isCheckNetAvailable(Context context) {
		boolean isCheckNet = false;
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				isCheckNet = false;
				return isCheckNet;
			} else {
				isCheckNet = true;
				return isCheckNet;
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return isCheckNet;
	}

	 
	/**
	 * 获取android系统版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getOSVersion(Context context) {
		String release = android.os.Build.VERSION.RELEASE; // android系统版本号
		release = "android" + release;
		return release;
	}

	/**
	 * 获得android系统sdk版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getOSVersionSDK(Context context) {
		String sdk = android.os.Build.VERSION.SDK; // SDK号
		return sdk;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static int getOSVersionSDKINT(Context context) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		return currentapiVersion;
	}

	/**
	 * 获取设备分辨率和密度信息，判断其属于aphone还是apad，此处判断标准是根据分辨率和dpi综合对比来判断的，大概标准是
	 * 大于800x480的为pad，小于的为phone
	 */

	/**
	 * 获取手机型号
	 */
	public static String getDeviceModel() {
		String model = android.os.Build.MODEL;
//		String StrContent = null;
//		try {
//			StrContent = URLEncoder.encode(model, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return model;
	}

	/**
	 * 获得设备Mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		if (info != null) {
			return info.getMacAddress();
		}
		return null;
	}

	/**
	 * 获得设备ip地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
			LogUtil.e(TAG, "WifiPreference IpAddress");
		}
		return null;
	}
  
	/**
	 * 获得设备的横向dpi
	 */
	public static float getWidthDpi(Context context) {
		DisplayMetrics dm = null;
		try{
			if(context!=null){
				dm = new DisplayMetrics();
				dm = context.getApplicationContext().getResources().getDisplayMetrics();
			}
		
			return dm.densityDpi;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
		
	}

	/**
	 * 获得设备的纵向dpi
	 */
	public static float getHeightDpi(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.ydpi;
	}
	 
	private static AlertDialog.Builder  customBuilder = null;
	public static void showDialog(String title, String positiveButton,
			String negativeButton, String message, final boolean isFinshing) {
		try{
			synchronized (Utils.synchronizeds) {
				// if(customBuilder==null){
//				customBuilder = new Dialog(BaseActivity.mBaseActivity);
				 customBuilder = new AlertDialog.Builder(BaseActivity.mBaseActivity);
				// }

				if (customBuilder != null && title != null) {
					customBuilder.setTitle(title);
				} else {
					if (customBuilder != null) {
					   customBuilder.setTitle("提示");
					}
				}

				if (customBuilder != null && message != null) {
					customBuilder.setMessage(message);
				} else {
					customBuilder.setMessage("确定要退出吗？");
				}

				if (customBuilder != null && positiveButton != null) {

					customBuilder.setPositiveButton(positiveButton,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									if (isFinshing) {
										((Activity) BaseActivity.mBaseActivity)
												.finish();
										((Activity) BaseActivity.mBaseActivity)
												.overridePendingTransition(
														R.anim.fade, R.anim.hold);
									}
									customBuilder = null;
									dialog = null;
								}

							});

				}
				if (customBuilder != null && negativeButton != null) {
					customBuilder.setNegativeButton(negativeButton,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									customBuilder = null;
									dialog = null;
								}
							});
				}

				if (customBuilder != null ) {
					dialog = customBuilder.create();
					
				}

				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}

				if (dialog != null/* &&!dialog.isShowing() */) {
					dialog.show();
				}

			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		

	}

	public static void closeDialog() {
		synchronized (Utils.synchronizeds) {
			if (dialog != null) {
				dialog.dismiss();
			}
		}

	}

	private static Dialog waitingDialog;

	public static void startWaitingDialog(Context context) {

		synchronized (Utils.synchronizeds) {
			// if(waitingDialog==null){
			waitingDialog = new Dialog(context, R.style.waiting);
			// dialog.setCanceledOnTouchOutside(true);
			waitingDialog.setContentView(R.layout.waiting);
			waitingDialog.setCanceledOnTouchOutside(false);
			// dialog.setCancelable(false);
			waitingDialog.show();
			// }else{
			// if(waitingDialog!=null&&!waitingDialog.isShowing()){
			// waitingDialog.show();
			// }
			//				 
			// }
		}

	}

	public static void closeWaitingDialog() {
		synchronized (Utils.synchronizeds) {
			if (waitingDialog != null) {
				waitingDialog.dismiss();
			}
		}
	}

	public static void closeMediaDialog() {
		if (mMediaDialog != null)
			mMediaDialog.dismiss();
	}


	/**
	 * 删除一个目录
	 */
	public static boolean delDir(File dir) {
		if (dir == null || !dir.exists() || dir.isFile()) {
			return false;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				delDir(file);// 递归
			}
		}
		dir.delete();
		return true;
	}
 
	public static String getFileName(String uri) {
		String name = uri;
		if (name != null) {
			String[] content = name.split("/");
			if (content != null && content.length > 1) {
				name = content[content.length - 1];
			}
		}

		return name;
	} 
	
	
	public static boolean isNEON = false;
	
	public static String isNEONStr = "不支持";
	
	public static String isNEONFit = "无法判断";
	
	public static boolean intCPUInfo(){
	
		String cupinfo = getCPUInfos();
		if(cupinfo!=null){
			cupinfo = cupinfo.toLowerCase();
			if(cupinfo!=null&&cupinfo.contains("neon")){
				isNEON = true;
				isNEONStr = "支持";
				
			}else{
				isNEON = false;
				isNEONStr = "不支持";
				
			}
		}
		return isNEON;
	}
	
	public static boolean isNEON(){
		return isNEON;
	}
	

	private static String getCPUInfos() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		StringBuilder resusl = new StringBuilder();
		String resualStr = null;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				resusl.append(str2);
				String cup = str2;
				LogUtil.i(TAG, "---" + cup);
			}
			if (resusl != null) {
				resualStr = resusl.toString();
				return resualStr;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resualStr;
	}

	
	private static String[] getDivceInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return cpuInfo;
	}

	public static String currentEcoder = "无法知晓";
	
	public static String getDeviceCPUInfo() {
		String cpuType = "";

		String[] cpuInfo = getDivceInfo();
		if (cpuInfo != null && cpuInfo.length > 0) {
			cpuType = cpuInfo[0];
			if(cpuType != null){
				if(isNEON){
					isNEONFit = "ARMv7_NEON.apk";
				}else{
					if(cpuType !=null && cpuType.toLowerCase().equals("armv7")){
						isNEONFit = "ARMv7.apk";
					}else if(cpuType !=null && cpuType.toLowerCase().equals("armv6")){
						isNEONFit = "ARMv6.apk";
					}else if(cpuType !=null && cpuType.toLowerCase().equals("armv5")){
						isNEONFit = "VFP.apk";
					}else{
						isNEONFit = "VFP.apk";
					}
				}
				
				String[] cpuInfos =  cpuType.split(" ");
				cpuType = cpuInfos[0];
				
			}
		}
		return cpuType;
	}

	
	//检查某个应用是否安装
	public static boolean checkAPP(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public static void copyAssetsToSdcard(Activity context,
			String assetsFileName, String outFileName) {
		InputStream is = null;
		OutputStream os = null;
		try {
			Utils.initCacheFile(context);
			is = context.getAssets().open(assetsFileName);
			String rootPath = Utils.getAppFilesDir(context)
					+ Utils.CACHE_ECODER_PATH + outFileName;
			
			File file = new File(rootPath);
			if(file.exists()){
				file.delete();
			}
			os = new FileOutputStream(rootPath);
			// String path = Utils.getAppFilesDir(context);
			// // OutputStream outStream = new
			// OutputStream outStream = new FileOutputStream(path+"/ecoder/" +
			// outFileName);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			if(os!=null)
			os.flush();
			if(os!=null)
				os.close();
			
			install(context, rootPath);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null)
				is.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * 安装复制到SD卡中的apk文件
	 * @param context
	 * @param outFileName
	 */
	private static void install(Activity context, String outFileName) {

		// [文件夹705:drwx---r-x，给此文件夹目录设置读写权限]
		String[] args1 = { "chmod", "705",
				Utils.getAppFilesDir(context) + Utils.CACHE_ECODER_PATH };
		exec(args1);
		// [文件604:-rw----r--，给此文件目录下的文件设置读写权限]
		String[] args2 = { "chmod", "604", outFileName };
		exec(args2);

		// 点击安装PendingIntent
		File updateFile = new File(outFileName);
		// String path = Utils.getAppFilesDir(context);
		// File updateFile = new File(path +"/ecoder/"+ outFileName);
		
		
		final Uri uri = Uri.fromFile(updateFile);
		final Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setDataAndType(uri,
				"application/vnd.android.package-archive");
		installIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivityForResult(installIntent, 8);
		((Activity) context)
				.overridePendingTransition(R.anim.fade, R.anim.hold);
		isClicked=true;
		// finish();

	}
	
	public static void  setingCheckAPPEcoderDialog(final Activity context) {
		Dialog dialog = null;
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle("提示");
		ad.setMessage("需要安装辅助解码器");
		ad.setPositiveButton("安装", new DialogInterface.OnClickListener() {// 退出按钮
				 
					public void onClick(DialogInterface dialog, int i) {
						if(isClicked){
							isClicked = false;
							if(isNEON){
								Utils.copyAssetsToSdcard(context, ECODER_2,ECODER_2);
							}else{
								Utils.copyAssetsToSdcard(context, ECODER_1,ECODER_1);
							}
						}
						if(dialog!=null)
							dialog.dismiss();
						// context.finish();
						// ((Activity) context).overridePendingTransition(
						// R.anim.fade, R.anim.hold);

					}
				});
		/*ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		 
			public void onClick(DialogInterface dialog, int i) {
				dialog.dismiss();
				context.finish();
				((Activity) context).overridePendingTransition(R.anim.fade,
						R.anim.hold);
			}
		});*/
		if (dialog == null) {
			dialog = ad.create();
		}
		if (dialog != null) {
			dialog.show();// 显示对话框
		}

		}
	
	private static boolean isClicked = true;
	private static void checkAPPEcoderDialog(final Activity context) {
		Dialog dialog = null;
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle("提示");
		ad.setMessage("需要安装辅助解码器");
		ad.setPositiveButton("安装", new DialogInterface.OnClickListener() {// 退出按钮
				 
					public void onClick(DialogInterface dialog, int i) {
						if(isClicked){
							isClicked = false;
							if(isNEON){
								Utils.copyAssetsToSdcard(context, ECODER_2,ECODER_2);
							}else{
								Utils.copyAssetsToSdcard(context, ECODER_1,ECODER_1);
							}
							
						}
						if(dialog!=null)
						dialog.dismiss();
					}
		});
		ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		 
			public void onClick(DialogInterface dialog, int i) {
				dialog.dismiss();
				context.finish();
				((Activity) context).overridePendingTransition(R.anim.fade,
						R.anim.hold);
			}
		});
		if (dialog == null) {
			dialog = ad.create();
		}
		if (dialog != null) {
			dialog.show();// 显示对话框
		}

		/*
		 * Dialog dialog = null; Dialog.Builder customBuilder = new
		 * Dialog.Builder(context);
		 * customBuilder.setTitle("提示").setMessage("播放该影片时需要安装第三方解码器(Vitamio)")
		 * .setPositiveButton("安装", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int which) {
		 * Utils.copyAssetsToSdcard(context, "ARMv7.apk", "ARMv7.apk");
		 * dialog.dismiss(); // context.finish(); // ((Activity)
		 * context).overridePendingTransition( // R.anim.fade, R.anim.hold);
		 * 
		 * } })
		 * 
		 * .setNegativeButton("取消", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss(); context.finish(); ((Activity)
		 * context).overridePendingTransition( R.anim.fade, R.anim.hold); } });
		 * 
		 * dialog = customBuilder.create(); dialog.show();
		 */}

	
	public static String checkCurrentEcoder(Activity context) {
		String ecader = "还没有安装解码器";
		try{
		
			boolean ARMv7_NEON = Utils.checkAPP(context, "io.vov.vitamio");
			boolean ARMv7 = Utils.checkAPP(context, "io.vov.vitamio.v7vfpv3");
			boolean ARMv6 = Utils.checkAPP(context, "io.vov.vitamio.v6");
			boolean VFP = Utils.checkAPP(context, "io.vov.vitamio.v6vfp");
		
			if(ARMv7_NEON){
				ecader = "ARMv7_NEON.apk";
			}else if(ARMv7){
				ecader = "ARMv7.apk";
			}else if(ARMv6){
				ecader = "ARMv6.apk";
			}else if(VFP){
				ecader = "VFP.apk";
			}else{
				ecader = "还没有安装解码器";
			}
			
			
		}catch (Exception e) {
			return ecader;
		}
		return ecader;
	}
	
	
	public static boolean checkAPPEcoder(Activity context) {
		boolean ARMv7_NEON = Utils.checkAPP(context, "io.vov.vitamio");
		boolean ARMv6 = Utils.checkAPP(context, "io.vov.vitamio.v6");
		boolean VFP = Utils.checkAPP(context, "io.vov.vitamio.v6vfp");
		boolean ARMv7 = Utils.checkAPP(context, "io.vov.vitamio.v7vfpv3");
		if (!ARMv7_NEON && !ARMv7 && !VFP && !ARMv6) {
			checkAPPEcoderDialog(context);
			return false;
		} else {
			return true;
		}

	}
	
	public static boolean setingCheckAPPEcoder(Activity context) {
		boolean ARMv7_NEON = Utils.checkAPP(context, "io.vov.vitamio");
		boolean ARMv6 = Utils.checkAPP(context, "io.vov.vitamio.v6");
		boolean VFP = Utils.checkAPP(context, "io.vov.vitamio.v6vfp");
		boolean ARMv7 = Utils.checkAPP(context, "io.vov.vitamio.v7vfpv3");
		if (!ARMv7_NEON && !ARMv7 && !VFP && !ARMv6) {
			setingCheckAPPEcoderDialog(context);
			return false;
		} else {
			return true;
		}

	}

	public static boolean checkPlayerAPPEcoder(Activity context) {
		boolean ARMv7_NEON = Utils.checkAPP(context, "io.vov.vitamio");
		boolean ARMv6 = Utils.checkAPP(context, "io.vov.vitamio.v6");
		boolean VFP = Utils.checkAPP(context, "io.vov.vitamio.v6vfp");
		boolean ARMv7 = Utils.checkAPP(context, "io.vov.vitamio.v7vfpv3");
		if (!ARMv7_NEON && !ARMv7 && !VFP && !ARMv6) {
			return false;
		} else {
			return true;
		}

	}

	public static boolean checkUri(Context context, Uri uri) {
		boolean isUri = false;
		try {
			if (uri != null) {
				if (uri.toString() != null && uri.toString().contains("http")&&uri.getScheme()!=null&&uri.getScheme().toLowerCase().contains("http")) {
					isUri = true;
				}else if (uri.toString() != null && uri.toString().contains("rtsp")&&uri.getScheme()!=null&&uri.getScheme().toLowerCase().contains("mms")){
					isUri = true;
				}else{
					isUri = false;
				}
				LogUtil
						.i(TAG, "---checkUri()--getScheme()=="
								+ uri.getScheme());
			}
		} catch (Exception e) {
			return isUri;
		}
		return isUri;

	}

	/** 执行Linux命令，并返回执行结果。用于得到data/data/installpackage/xxx.file目录及文件的安装权限（读写权限） */
	public static String exec(String[] args) {
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			// baos.write('/n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}
	
	public static String readAssetsToString(Activity context,String assetsFileName) {
		InputStream is = null;
		String temStr = null;
		try {
			is = context.getAssets().open(assetsFileName);
			
			if (is != null) {
	               StringBuilder sb = new StringBuilder();
	               String line;
	                  BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	                   while ((line = reader.readLine()) != null) {
	                     sb.append(line);
	                   }
	                   if(sb !=null){
	      				 temStr = sb.toString();
	      		      }
	      		     return temStr;

	             } 
			

		} catch (IOException e) {
			e.printStackTrace();
			  return temStr;

		} finally {
			try {
				if(is != null)
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return temStr;

	}
	
	public static boolean isUri(Context context, Uri uri) {
		boolean isUri = false;
		try {
			if (uri != null) {
				if (uri.toString() != null && uri.toString().contains("http")||uri.toString().contains("www")
						||uri.toString().contains("rtsp")||uri.toString().contains("file")||uri.toString().contains("mms")) {
					isUri = true;
				} else {
					isUri = false;
				}
				LogUtil
						.i(TAG, "---checkUri()--getScheme()=="
								+ uri.getScheme());
			}
		} catch (Exception e) {
			return isUri;
		}
		return isUri;

	}
	
	public static boolean isCheckUriByM3u8(Context context, Uri uri) {
		boolean isUri = false;
		try {
			if (uri != null) {
				if (uri.toString() != null && uri.toString().toLowerCase().contains("m3u8")
						||uri.toString().contains("rtsp")) {
					isUri = true;
				} else {
					isUri = false;
				}
				LogUtil
						.i(TAG, "---checkUri()--getScheme()=="
								+ uri.getScheme());
			}
		} catch (Exception e) {
			return isUri;
		}
		return isUri;

	}

	public static void netCheckDialog() {// 退出确认
		AlertDialog.Builder ad = new AlertDialog.Builder(BaseActivity.mBaseActivity);
		ad.setTitle("提示");
		ad.setMessage("网络不可用，请检查网络再试");
		ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 退出按钮
		
					public void onClick(DialogInterface dialog, int i) {

					}
				});
		/*ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
			}
		});*/
		ad.show();// 显示对话框
	}
	
	public static void netNoPlayeDialog() {// 退出确认
		AlertDialog.Builder ad = new AlertDialog.Builder(BaseActivity.mBaseActivity);
		ad.setTitle("提示");
		ad.setMessage("暂时只支持android_2.1以上系统");
		ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 退出按钮
					
					public void onClick(DialogInterface dialog, int i) {

					}
				});
		/*ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
			}
		});*/
		ad.show();// 显示对话框
	}
	
	public static boolean isVideoFile(String end){
		boolean isVideo = false;
		if(end.equals("3gp")||end.equals("mp4") || end.equals("ndivx") || end.equals("xvid")
		|| end.equals("flv") || end.equals("ts") || end.equals("rmvb")
		|| end.equals("rm") || end.equals("mkv") || end.equals("mov")
		|| end.equals("avi") || end.equals("mpg") || end.equals("v8")
		|| end.equals("asf") || end.equals("wmv") || end.equals("ram")
		|| end.equals("mpeg") || end.equals("swf")|| end.equals("3gpp")
		|| end.equals("m2v") || end.equals("asx") || end.equals("ra")
		|| end.equals("3g2") || end.equals("3gpp2")|| end.equals("divx") 
		|| end.equals("f4v")|| end.equals("rv") 
		
		
		)
			
		{
			isVideo = true;
			
		}else{
			isVideo = false;
		}
		
		return isVideo;
	}
	
	public static boolean isMusicFile(String end){
		boolean isMusic = false;
		if(end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")||end.equals("wma")
				||end.equals("aac")||end.equals("mpa")||end.equals("midi")||end.equals("ar")
				
		
		){
			isMusic = true;
			
		}else{
			isMusic = false;
		}
		
		return isMusic;
	}
	
	
	public static void startSystemPlayer(Context context,ArrayList<VideoInfo> mCurrentPlayList,int position){
		Intent intent = new Intent(context,SystemPlayer.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("MediaIdList", mCurrentPlayList);
		intent.putExtras(mBundle);
		intent.putExtra("CurrentPosInMediaIdList", position);
		context.startActivity(intent);
		((Activity) BaseActivity.mBaseActivity).overridePendingTransition(
				R.anim.fade, R.anim.hold);
	}

}
