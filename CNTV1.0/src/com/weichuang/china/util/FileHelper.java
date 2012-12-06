package com.weichuang.china.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import android.text.TextUtils;
/**
 * 
 * @author yanggf
 *
 */
public class FileHelper {

	/**
	 * write string to file with endcode
	 * 
	 * @param sToSave
	 * @param sFileName
	 * @return
	 */
	private static FileHelper fileHelper;

	private FileHelper() {
		super();
	}

	public static FileHelper getInstance() {
		if (fileHelper == null)
			fileHelper = new FileHelper();
		return fileHelper;
	}
	
	/**
	 * delete share image cache
	 */
	public static void deleteFile(String sFileName) {
		final File file = new File(sFileName);
		if (file.exists()) {
			boolean is = file.delete();
			LogUtil.e("�Ƿ�ɾ�������ļ�" + is);
		}
	}

	/**
	 * read string from file with decoce
	 * 
	 * @param sFileName
	 * @return
	 */
	public static String ReadStringFromFile(String sFileName) {
		if (TextUtils.isEmpty(sFileName))
			return null;
		String sDest = null;
		File f = new File(sFileName);
		if (!f.exists()){
			return null;
		}
		try {
			FileInputStream is = new FileInputStream(f);
			ByteArrayOutputStream bais = new ByteArrayOutputStream();
			try {
				byte[] buffer = new byte[1];// [512];
				while (is.read(buffer) != -1) {
					bais.write(buffer);
				}
				sDest = bais.toString().trim();
			} catch (IOException ioex) {
				LogUtil.e("Excetion : ioexception  at read string from file! ");
			} finally {
				is.close();
				bais.close();
			}
		} catch (Exception ex) {
			LogUtil.e("Exception : read string from file!" + ex.getMessage());
		}
		return sDest;
	}

	/**
	 * �����ļ�
	 * 
	 * @param sToSave
	 * @param sFileName
	 * @param isAppend
	 * @return
	 */
	public static boolean WriteStringToFile(String content, String fileName, boolean isAppend) {
		boolean bFlag = false;
		final int iLen = content.length();
		final File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			final FileOutputStream fos = new FileOutputStream(file, isAppend);
			byte[] buffer = new byte[iLen];
			try {
				buffer = content.getBytes();
				fos.write(buffer);
				fos.flush();
				bFlag = true;
			} catch (IOException ioex) {
				LogUtil.e("Excetion : ioexception  at write string to file! ");
			} finally {
				fos.close();
			}
		} catch (Exception ex) {
			LogUtil.e("Exception : write string to file");
		}
		return bFlag;
	}
}
