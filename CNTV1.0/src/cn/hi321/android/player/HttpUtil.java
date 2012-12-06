package cn.hi321.android.player;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import android.util.Log;

public class HttpUtil {
	
	public static String getUri(String uri){
		
		String url = uri;
		try {
			
			HttpClient httpClient = new HttpClient();
//			String url ="http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=cctv5&urlType=highEdition";
			
			PostMethod postMethod = new PostMethod(url);
			
			Log.v("family", "-------------1=");
			int statusCode = httpClient.executeMethod(postMethod);


			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				// 从头中取出转向的地址
				Header locationHeader = postMethod
						.getResponseHeader("location");
				String location = null;
				if (locationHeader != null) {
					location = locationHeader.getValue();
					Log.v("family", "The page was redirected to:"
							+ location);
					return location;
				} else {
					Log.v("family", "Location field value is null.");
				}
				return url;
			}

//			Header locationHeader = postMethod
//					.getResponseHeader("location");
//			String location = null;
//			if (locationHeader != null) {
//				location = locationHeader.getValue();
//				Log.v("family", "The page was redirected to:"
//						+ location);
//			}
			} catch (Exception e) {
//				mTextView1.setText(e.getMessage().toString());
				e.printStackTrace();
			}
		
		return url;
	}

}
