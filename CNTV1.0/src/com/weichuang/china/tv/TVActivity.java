package com.weichuang.china.tv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import cn.hi321.android.player.R;

import com.weichuang.china.BaseActivity;
import com.weichuang.china.VideoInfo;
import com.weichuang.china.util.ActivityHolder;
import com.weichuang.china.util.Utils;
import com.weichuang.china.video.player.SystemPlayer;
import com.weichuang.china.video.view.UserPreference;

public class TVActivity extends ExpandableListActivity {
	
	
	 private static final String TAG = "TVActivity";
	 private static final int IS_CLICK = 1;
//	 private MyListView listview = null;
	 List<Map<String, String>> groups= null;
//	  创建一级条目下的的二级条目
     ;
 	private ArrayList<ArrayList<VideoInfo>>  arr;
     private ArrayList<VideoInfo> videoList; 
     // 将二级条目放在一个集合里，供显示时使用
     List<List<Map<String, String>>> childs ;
	 private RelativeLayout laytout_beij = null; 
	 private boolean isCick = false;
	 private FrameLayout titleBar;//标题栏的背景
     private TextView title_text;
     private Button titleBarLeftButton = null; 
	 private Button titleBarRightButton = null;  
	 
	 
	 
	 @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState); 
			 setContentView(R.layout.tv_list_activity);
			 ActivityHolder.getInstance().addActivity(this);
//			 setTitleRightButtonHide();
			 BaseActivity.mBaseActivity = this;
			 laytout_beij = (RelativeLayout)findViewById(R.id.layout);
		     titleBar =(FrameLayout)findViewById(R.id.titlebar);
		 	 title_text = (TextView)findViewById(R.id.title_text);
		 	 titleBarRightButton =(Button)findViewById(R.id.title_change_list); 
			 titleBarLeftButton =(Button)findViewById(R.id.title_search); 
		     titleBarLeftButton.setOnClickListener(listener); 
			 titleBarRightButton.setOnClickListener(listener);  
		 	 title_text.setText("电视直播"); 
			 String jsonStr = Utils.readAssetsToString(TVActivity.this, "tv.txt");
			 testPullRead(jsonStr); 
		     UserPreference.ensureIntializePreference(this); 
			
			 
	         /**
	          * 使用SimpleExpandableListAdapter显示ExpandableListView
	          * 参数1.上下文对象Context
	          * 参数2.一级条目目录集合
	          * 参数3.一级条目对应的布局文件
	          * 参数4.fromto，就是map中的key，指定要显示的对象
	          * 参数5.与参数4对应，指定要显示在groups中的id
	          * 参数6.二级条目目录集合
	          * 参数7.二级条目对应的布局文件
	          * 参数8.fromto，就是map中的key，指定要显示的对象
	          * 参数9.与参数8对应，指定要显示在childs中的id
	          */
	         SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
	                 this, groups, R.layout.groups, new String[] { "group" },
	                 new int[] { R.id.group }, childs, R.layout.childs,
	                 new String[] { "child" }, new int[] { R.id.child });
	         setListAdapter(adapter); 
	 
	 }
	 
	 private OnClickListener listener = new OnClickListener() { 
			public void onClick(View view) {
				int id = view.getId();
				switch (id) { 
				case R.id.title_search:
					TVActivity.this.finish(); 
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case R.id.title_change_list:
			
					break;
				}
			}
		};
	 /**
      * 设置哪个二级目录被默认选中
      */
     @Override
     public boolean setSelectedChild(int groupPosition, int childPosition,
             boolean shouldExpandGroup) {
             //do something
         return super.setSelectedChild(groupPosition, childPosition,
                 shouldExpandGroup);
     }
     /**
      * 设置哪个一级目录被默认选中
      */
     @Override
     public void setSelectedGroup(int groupPosition) {
         //do something
         super.setSelectedGroup(groupPosition);
     }
     /**
      * 当二级条目被点击时响应
      */
     @Override
     public boolean onChildClick(ExpandableListView parent, View v,
             int groupPosition, int childPosition, long id) {
          
    	
    	   System.out.println("groupPosition------"+groupPosition);
    	   System.out.println("childPosition------"+childPosition);
          Map<String, String> mapFather = (Map<String, String>)parent.getAdapter().getItem(groupPosition);//获取子父节点里面的内容
    	  Map<String, String> map = (Map<String, String>)parent.getAdapter().getItem(childPosition);//获取子节点里面的内容
    	
    	 ;
    	 ArrayList<VideoInfo>  videoinfo_array = null;
    	 if(groupPosition < arr.size()){
    		 System.out.println("size()-----"+arr.size());
    	
    		 videoinfo_array = arr.get(groupPosition);
    		 System.out.println("videoinfo_array.size()-----"+videoinfo_array.size());
    		 if(Utils.isCheckNetAvailable(TVActivity.this)){
 				if(Utils.getOSVersionSDKINT(TVActivity.this)>=7){
 					if(!isCick){
 						isCick = true;
 						 
 						 Intent intent = new Intent(TVActivity.this,SystemPlayer.class);
 					     Bundle mBundle = new Bundle();
 						 mBundle.putSerializable("MediaIdList", videoinfo_array);
 						 intent.putExtras(mBundle);
 						 intent.putExtra("CurrentPosInMediaIdList", childPosition);
 						 startActivity(intent);
 						 overridePendingTransition(R.anim.fade, R.anim.hold); 
 						mExitHandler.sendEmptyMessageDelayed(IS_CLICK,4000);
 					 }
 				}else{
 					 Utils.netNoPlayeDialog();
 				}
 				 
 				}else{
 				 Utils.netCheckDialog();
 			} 
     	 
    		 
    	 }
    	 
    	 
         return super.onChildClick(parent, v, groupPosition, childPosition, id);
      
     } 
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 ActivityHolder.getInstance().removeActivity(this);
	}


	@Override
	protected void onResume() {
		BaseActivity.mBaseActivity = this;
		super.onResume();
		SharedPreferences preference = null;
		preference = PreferenceManager.getDefaultSharedPreferences(this);
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		    super.onKeyDown(keyCode, event);
			if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0 ) {
				finish();
				isCick = false;
				overridePendingTransition(R.anim.fade, R.anim.hold);
				return true;
			}
			return false;
		}

	

	    public void testPullRead(String jsonObject){  
	        groups = new ArrayList<Map<String, String>>();  
	        childs = new ArrayList<List<Map<String, String>>>();
	        arr = new ArrayList<ArrayList<VideoInfo>>();
	    	try {
				JSONObject jsonObject_1 = new JSONObject(jsonObject);
				String video = jsonObject_1.get("video").toString();
				if(video !=null){
					 JSONArray  jsonarray_video_person = jsonObject_1.getJSONArray("video");
					 for(int i=0;i < jsonarray_video_person.length();i++){
						 Map<String, String> group1 = new HashMap<String, String>();
						 List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
						 videoList = new ArrayList<VideoInfo>(); 
						 JSONObject jsonObject_video = (JSONObject)jsonarray_video_person.opt(i);
						 String videoNamePerson = jsonObject_video.getString("videoNamePerson");
						 group1.put("group", videoNamePerson);
						 JSONArray jsonObject_son = jsonObject_video.getJSONArray("videos");
						 for(int j= 0;j <jsonObject_son.length();j++ ){ 
							 JSONObject json = (JSONObject)jsonObject_son.opt(j); 
							 Map<String, String> group2 = new HashMap<String, String>();
							 VideoInfo videoInfo = new VideoInfo(); 
							 videoInfo.setImage(json.getString("image"));
							 videoInfo.setUrl(json.getString("url"));
							 videoInfo.setTitle( json.getString("title"));
							 group2.put("child", json.getString("title")); 
							 child1.add(group2) ;
							 videoList.add(videoInfo);
						 }
						 childs.add(child1);
						 groups.add(group1);
						 arr.add(videoList);
					 }
				} 
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    	 
	    }  
		Handler mExitHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case IS_CLICK:
					isCick = false;
					break;
				}
			}
		}; 
}
