package cn.hi321.android.player;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.weichuang.china.video.player.SystemPlayer;

public class HttpDemo extends Activity {
	
	private static final String TAG = "HttpDemo";
	private Button mButton1;
	private TextView mTextView1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		String uri =HttpUtil.getUri("http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=cctv5&urlType=highEdition");
		setContentView(R.layout.main);
		findViewById(R.id.mButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uri =HttpUtil.getUri("http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=cctv5&urlType=highEdition");
//				Intent mIntent = new Intent(HttpDemo.this,SystemPlayer.class);
				Intent mIntent = new Intent(Intent.ACTION_VIEW);
//				mIntent.setData(Uri.parse("http://rtmp.cntv.lxdns.com/live/cctv5/playlist.m3u8"));
				mIntent.setDataAndType(Uri.parse(uri),"video/*");
				startActivity(mIntent);
			}
		});
		
//		Log.e(TAG,uri);

		
		
//		

//		mButton1 = (Button) findViewById(R.id.myButton1);
//		mTextView1 = (TextView) findViewById(R.id.myTextView1);
//		try {
//		
//		HttpClient httpClient = new HttpClient();
//		String url ="http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=cctv5&urlType=highEdition";
//		Log.v("family", "-------------url=" + url);
//		
//		PostMethod postMethod = new PostMethod(url);
//		
//		Log.v("family", "-------------1=");
//		int statusCode = httpClient.executeMethod(postMethod);
//
//		Log.v("family", "-------------2=url=" + postMethod.getURI());
//
//		if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
//				|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
//			// 从头中取出转向的地址
//			Header locationHeader = postMethod
//					.getResponseHeader("location");
//			String location = null;
//			if (locationHeader != null) {
//				location = locationHeader.getValue();
//				Log.v("family", "The page was redirected to:"
//						+ location);
//			} else {
//				Log.v("family", "Location field value is null.");
//			}
//			return;
//		}
//
//		Header locationHeader = postMethod
//				.getResponseHeader("location");
//		String location = null;
//		if (locationHeader != null) {
//			location = locationHeader.getValue();
//			Log.v("family", "The page was redirected to:"
//					+ location);
//		}
//		} catch (Exception e) {
////			mTextView1.setText(e.getMessage().toString());
//			e.printStackTrace();
//		}
//		mButton1.setOnClickListener(new Button.OnClickListener() {
//			public void onClick(View v) {
//				try {
//					HttpClient httpClient = new HttpClient();
//					String url = " http://vdn.apps.cntv.cn/api/getLiveUrlCommonRedirectApi.do?channel=cctv5&urlType=highEdition";
//					Log.v("family", "-------------url=" + url);
//					
//					PostMethod postMethod = new PostMethod(url);
//					Log.v("family", "-------------1=");
//					int statusCode = httpClient.executeMethod(postMethod);
//
//					Log.v("family", "-------------2=url=" + postMethod.getURI());
//
//					if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
//							|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
//						// 从头中取出转向的地址
//						Header locationHeader = postMethod
//								.getResponseHeader("location");
//						String location = null;
//						if (locationHeader != null) {
//							location = locationHeader.getValue();
//							Log.v("family", "The page was redirected to:"
//									+ location);
//						} else {
//							Log.v("family", "Location field value is null.");
//						}
//						return;
//					}
//
//					Header locationHeader = postMethod
//							.getResponseHeader("location");
//					String location = null;
//					if (locationHeader != null) {
//						location = locationHeader.getValue();
//						Log.v("family", "The page was redirected to:"
//								+ location);
//					}
//
//					mTextView1.setText("POST Response statusCode: "
//							+ statusCode + " url:" + locationHeader);
//				} catch (Exception e) {
//					mTextView1.setText(e.getMessage().toString());
//					e.printStackTrace();
//				}
//
//			}
//		});
	}
}