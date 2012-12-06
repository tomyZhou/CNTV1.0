package cn.hi321.android.player;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.weichuang.china.BaseActivity;
import com.weichuang.china.tv.TVActivity;
import com.weichuang.china.util.ActivityHolder;
import com.weichuang.china.util.LogUtil;
import com.weichuang.china.util.Utils;
import com.weichuang.china.video.view.UserPreference;
/**
 * 
 * @author yanggf
 *
 */
public class LoginActivity extends Activity {

	private static final String TAG = "LoginAtivity";
	private boolean isClosed = false;;
	private final static int SEND_MESSAGE_AND_CLOSE_WINDOW = 1;
	private final int MSG_ID_CLOSE = 2;
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEND_MESSAGE_AND_CLOSE_WINDOW:
				if (!isClosed) {
					closeWindow();
					isClosed = true;
					handler.sendEmptyMessageDelayed(MSG_ID_CLOSE, 1000);
				}
				super.handleMessage(msg);
			case MSG_ID_CLOSE:
				finish();

				break;
			default:
				break;
			}
		}

	};
	private Intent mIntent;
	private SharedPreferences preference = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		   setContentView(R.layout.start_activity);
			UserPreference.ensureIntializePreference(this);  
		    int defaultColor = UserPreference.read("defaultColor", 0);   
		    RelativeLayout background_id = (RelativeLayout)findViewById(R.id.background_id); 
//			if(defaultColor == 0){ 
//				background_id.setBackgroundResource(R.drawable.moren_beijing); 
//	        }else if (defaultColor == 1){
//	        	background_id.setBackgroundResource(R.drawable.moren_beijing1); 
//	        }else if (defaultColor == 2){
//	        	background_id.setBackgroundResource(R.drawable.moren_beijing2); 
//	        }else if (defaultColor == 3){
//	        	background_id.setBackgroundResource(R.drawable.moren_beijing3); 
//	        }else{
//	    	  background_id.setBackgroundColor(defaultColor); 
//	        }
//		    Log.v(TAG, "onCreate()");
//			
			BaseActivity.mBaseActivity = this;
			ActivityHolder.getInstance().addActivity(this);
			mIntent = getIntent();
			preference = PreferenceManager.getDefaultSharedPreferences(this);
			handler	.sendEmptyMessageDelayed(SEND_MESSAGE_AND_CLOSE_WINDOW,	3000);
			if(mIntent!=null){
				LogUtil.i(TAG, "onCreate() ----------getIntent()=="+getIntent());
				LogUtil.i(TAG, "onCreate() -------getFlags()=="+mIntent.getFlags());
				LogUtil.i(TAG, "onCreate() -------getAction()=="+mIntent.getAction());
				LogUtil.i(TAG, "onCreate() -------getDataString()=="+mIntent.getDataString());
				LogUtil.i(TAG, "onCreate() -------getPackage()=="+mIntent.getPackage());
				LogUtil.i(TAG, "onCreate() -------getScheme()=="+mIntent.getScheme());
				LogUtil.i(TAG, "onCreate() -------getType()=="+mIntent.getType());
				LogUtil.i(TAG, "onCreate() -------getCategories()=="+mIntent.getCategories());
				LogUtil.i(TAG, "onCreate() -------getComponent()=="+mIntent.getComponent());
//				LogUtil.i(TAG, "onCreate()-------getDeviceCPUInfo()=="+Utils.getDeviceCPUInfo());
				Utils.intCPUInfo();
				
			} 
			if(preference!=null){
				boolean isExit = preference.getBoolean("shortcut", false);
				if(preference!=null&&!isExit){
					createShortCut();
					SharedPreferences.Editor editor = preference.edit();
					if(editor != null){
					     editor.putBoolean("shortcut", true);
					     editor.commit();
					}
			}
			
			
			
		       
			
				
			}
			
	} 
	public void createShortCut(){
		try{
			//创建快捷方式的Intent
	        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
	        //不允许重复创建
	        shortcutintent.putExtra("duplicate", false);
	        //需要现实的名称
	        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
	        //快捷图片
	        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.icon);
	        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
	        //点击快捷图片，运行的程序主入口
	        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , LoginActivity.class));
	        //发送广播。OK
	        sendBroadcast(shortcutintent);
		}catch (Exception e) { 
			closeWindow();
		}
	
	}
 

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		handler.sendEmptyMessage(SEND_MESSAGE_AND_CLOSE_WINDOW);
		Log.v(TAG, "onTouchEvent(MotionEvent event)");
		return true;
	}

	private void closeWindow() {

		Log.v(TAG, "closeWindow()");
		Intent intent = new Intent(LoginActivity.this,TVActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}
	
	   @Override
	    protected void onDestroy() {
	    	  super.onDestroy(); 
	        handler.removeMessages(SEND_MESSAGE_AND_CLOSE_WINDOW);
	        handler.removeMessages(MSG_ID_CLOSE);
	        ActivityHolder.getInstance().removeActivity(this);
	      
	        
	    }





	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BaseActivity.mBaseActivity = this;
	}




}