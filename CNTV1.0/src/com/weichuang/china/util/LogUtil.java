package com.weichuang.china.util;


import android.util.Log;
/**
 * 
 * @author yanggf
 *
 */
public class LogUtil {

	public static final String TAG = "chinavideo";
	public static boolean DEBUG = true;
	private static boolean PRINTLOG = false;
	private static boolean IS_DEBUG = false;
	public static final String LOG_PATH = Utils.SAVE_FILE_PATH_DIRECTORY + "/321text.txt";

	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, msg);
		}
		if (PRINTLOG) {
			Utils.writeFile(msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
		if (PRINTLOG) {
			Utils.writeFile(msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.i(tag, msg, tr);
		}
		if (PRINTLOG) {
			Utils.writeFile(msg);
		}
	}

	public static void v(String msg) {
		if (DEBUG || IS_DEBUG) {
			Log.v(TAG, msg);
		}
		if (PRINTLOG) {
			Utils.writeFile(msg);
			FileHelper.WriteStringToFile(msg + "\r\n", LOG_PATH, true);
		}
	}

	public static void v(String tag, String msg) {
		if (DEBUG || IS_DEBUG) {
			Log.v(tag, msg);
		}
		if (PRINTLOG) {
			Utils.writeFile(msg);
			FileHelper.WriteStringToFile(msg + "\r\n", LOG_PATH, true);
		}
	}

	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
		if (PRINTLOG) {
			Utils.writeFile(msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
		if (PRINTLOG) {
			Utils.writeFile(msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.e(tag, msg, tr);
		}
		if (PRINTLOG) {
			Utils.writeFile(msg);
		}
	}

	public static boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean debug) {
		DEBUG = debug;
	}

	public static boolean isPrintlog() {
		return PRINTLOG;
	}

	public static boolean isPRINTLOG() {
		return PRINTLOG;
	}

	public static void setPRINTLOG(boolean pRINTLOG) {
		PRINTLOG = pRINTLOG;
	}
	
	
	public static void Logger(String msg) {
		if (IS_DEBUG) {
			Log.e(TAG, msg);
		}
		if (PRINTLOG) {
			FileHelper.WriteStringToFile(msg + "\r\n", LOG_PATH, true);
		}
	}

}
