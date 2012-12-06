package com.weichuang.china.video.player;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.VideoView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Video.Media;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.hi321.android.player.R;

import com.weichuang.china.BaseActivity;
import com.weichuang.china.VideoInfo;
import com.weichuang.china.util.ActivityHolder;
import com.weichuang.china.util.LogUtil;
import com.weichuang.china.util.Utils;

/**
 * 
 * @author yangguangfu
 * 
 */
public class VideoPlayer extends Activity {
	private final static String TAG = "VideoPlayer";

	private int position;
	private String radia = null;

	private int temposition;

	private int mCurrentPosition = 0;

	private VideoView mVideoView = null;

	private SeekBar mPlayerSeekBar = null;

	private SeekBar mSeekBarvolume = null;

	private TextView mEndTime = null;
	private TextView mCurrentTime = null;

	private TextView mLoadingText = null;
	
	private TextView mLoadingVideoName = null;

	private TextView mLoadingBufferingText = null;

	private TextView mVideoName = null;

	private ImageView mBatteryState = null;

	private TextView mLastModify = null;

	private Button mBtnSetplay = null;

	private GestureDetector mGestureDetector = null;
	private AudioManager mAudioManager = null;

	private int currentVolume = 0;
	private Button mDiaplayMode = null;
	private Button mPrevButton = null;
	private Button mPlayOrPause = null;
	private Button mNextButton = null;
	private Button mPlayerVolume = null;

	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static int controlViewHeight = 0;
	private final static int TIME = 6868;
	private boolean isControllerShow = true;
	private boolean isPaused = false;
	private boolean isFullScreen = false;
	private boolean isSilent = false;

	private boolean isOnline = false;

	private boolean isOnCompletion = false;

	private final static int SCREEN_FULL = 0;
	private final static int SCREEN_DEFAULT = 1;

	private final static int HIDE_CONTROLER = 1;

	private final static int PAUSE = 3;

	private final static int EXIT_REPORTE = 4;

	private final static int EXIT_TEXT = 5;
	private final static int PROGRESS_CHANGED = 0;

	private final static int BUFFER = 6;

	private final static int BUFFERING_TAG = 7;

	private final static int EXIT = 8;

	private final static int SET_PAUSE_BUTTON = 9;

	private final static int IS_PAUSE_BUTTON = 10;

	private final static int SEEK_BACKWARD = 11;

	private final static int SEEK_FORWARD = 12;

	private Intent mIntent;

	private Uri uri;

	private Button mPlayerButtonBack = null;

	private StringBuilder mFormatBuilder;
	private Formatter mFormatter;

	private LinearLayout frame = null;
	private FrameLayout mFrameLayout = null;

	private LinearLayout mPlayerLoading;

	private LinearLayout mVideoBuffer;

	// private PlayHistoryInfo mPlayHistory = null;

	private Media mMedia = null;

	private boolean isLocal = false;

	private String mMtype = null;

	private String mHistoryLoading = null;

	private String currentLanguage = null;

	private String mByPlayHistory = null;

	private boolean isLoading = true;

	private long mIndexStartTime = 0;

	// private SharedPreferences sp = null;

	public class PausePlayerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("yangguangfu.mediaplayer.pause".equals(intent.getAction())) {
				mHandler.sendEmptyMessageDelayed(PAUSE, 950);
			}

		}
	}

	private int level = 0;
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			level = intent.getIntExtra("level", 0);
			// level加%就是当前电量了
		}
	};

	private void setBattery(int level) {
		if (level <= 0) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_0);
		} else if (0 < level && level <= 10) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_10);
		} else if (10 < level && level <= 20) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_20);
		} else if (20 < level && level <= 40) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_40);
		} else if (40 < level && level <= 60) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_60);
		} else if (60 < level && level <= 80) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_80);
		} else if (80 < level && level <= 100) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_100);
		}

	}

//	private VideoInfo videoInfo = null;
	private SharedPreferences preference = null;
	private int histroyPosition = 0;
	private String histroyUri = null;
	private String[] netUris = null;
	private String[] loacaUris = null;
	private boolean isTrue = false;
	private boolean isAutoNext = false;
	private ArrayList<VideoInfo> mCurrentPlayList;
	private VideoInfo videoInfo = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActivityHolder.getInstance().addActivity(this);
		
//		Utils.intCPUInfo();

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		BaseActivity.mBaseActivity = this;
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		ActivityHolder.getInstance().addActivity(this);
		setContentView(R.layout.video_player);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		preference = PreferenceManager.getDefaultSharedPreferences(this);

		LogUtil.v(TAG, "onCreate()");

		LogUtil.v(TAG, getIntent().toString());
		// uri = getIntent().getData();

		LogUtil.v(TAG, "The main thread id = " + Thread.currentThread().getId()
				+ "\n");

		IntentFilter filter = new IntentFilter("yangguangfu.mediaplayer.pause");
		if (mPausePlayerReceiver == null)
			mPausePlayerReceiver = new PausePlayerReceiver();
		registerReceiver(mPausePlayerReceiver, filter);
		registerReceiver(batteryReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		initView();

		mIntent = getIntent();
		if (mIntent != null) {
			mIntent = getIntent();
			if (mIntent != null) {
				uri = mIntent.getData();
				if (uri != null) {
					String name = Utils.getFileName(uri.toString());
					mVideoName.setText(name);
					mLoadingVideoName.setText(name);
					mLoadingVideoName.setVisibility(View.GONE);
					isLocal = false;
				} else {
					isLocal = true;
				}

				mCurrentPlayList = (ArrayList<VideoInfo>) mIntent
						.getSerializableExtra("MediaIdList");
		  		position = mIntent.getIntExtra("CurrentPosInMediaIdList", 0);
		  		radia = mIntent.getStringExtra("radia");
		  	    videoInfo = (VideoInfo) mIntent.getSerializableExtra("VideoInfo");
		  		String strLocaluri = null;
			    if(videoInfo != null){
			    	strLocaluri = videoInfo.getUrl();
			    }
				if (strLocaluri == null&&mCurrentPlayList!=null) {
					videoInfo = mCurrentPlayList.get(position);
					strLocaluri = mCurrentPlayList.get(position).getUrl();
				}

				if (strLocaluri != null) {
					String name = Utils.getFileName(strLocaluri);
					uri = Uri.parse(strLocaluri);
					
					if(isCheckButton&&mCurrentPlayList!=null&&mCurrentPlayList.size()>1){
						mLoadingVideoName.setText(mCurrentPlayList.get(position).getTitle());
						mVideoName.setText(mCurrentPlayList.get(position).getTitle());
					}else{
						 if(videoInfo != null){
							   mLoadingVideoName.setText(videoInfo.getTitle());
							   mVideoName.setText(videoInfo.getTitle());
						    }else{
						    	mLoadingVideoName.setText(name);
						       mVideoName.setText(name);
						    	
						    	
						    }
						
					}

				}


			}

			if (uri != null) {
				isHttp = Utils.checkUri(VideoPlayer.this, uri);
				String content = uri.toString().replace("?", "yangguangfu");
				if (content != null)
					netUris = content.split("yangguangfu");
			}
			if (isHttp) {
				isCheckButton = Utils.isCheckUriByM3u8(VideoPlayer.this, uri);
				if(isCheckButton&&mCurrentPlayList!=null){
					mVideoName.setText(mCurrentPlayList.get(position).getTitle());
					mLoadingVideoName.setText(mCurrentPlayList.get(position).getTitle());
					mPlayerSeekBar.setEnabled(false);
				}
				
			}
			
			
			if(mCurrentPlayList!=null&&mCurrentPlayList.size()==1){
				setNextEnabled(false);
				setPrevEnabled(false);
			}else if(mCurrentPlayList!=null&&mCurrentPlayList.size()>1){
				if(position==0){
					setPrevEnabled(false);
					setNextEnabled(true);
				}else if(position==(mCurrentPlayList.size()-1)){
					setPrevEnabled(true);
					setNextEnabled(false);
				}else{
					setPrevEnabled(true);
					setNextEnabled(true);
				}
				
			}else {
					setPlaySeekBarEnabled(true);
					setNextEnabled(true);
					setPrevEnabled(true);
					
			}
			

			if (preference != null) {
//				isAutoNext = preference.getBoolean(SettingActivity.key_5, false);
				histroyUri = preference.getString("histroyUri", null);
				histroyPosition = preference.getInt("CurrentPosition", 0);
				if (histroyUri != null) {
					String content = histroyUri.replace("?", "yangguangfu");
					if (content != null)
						loacaUris = content.split("yangguangfu");
				}

			}

			// uri=Uri.parse("rtsp://live.android.maxlab.cn/maxtv-ln.sdp");
			// uri=Uri.parse("http://cms.doplive.com.cn/video1/index_multi.m3u8?date=20120302220001&uid=0&rnd=2012030222000112069&deviceid=123&key=5311e457e8b47402676dae4cd2368118&count=1330675490");

			// mMedia
			// =(Media)mIntentContent.getSerializableExtra(Utils.MEDIA_KEY);
			// mPlayHistory =
			// (PlayHistoryInfo)mIntentContent.getSerializableExtra(Utils.PLAY_HISTORY_KEY);
			mHistoryLoading = (String) mIntent
					.getStringExtra(Utils.PLAY_LOADING_KEY);
			// sp =
			// getApplicationContext().getSharedPreferences("play_loading",0);
			// if("play_history_loading".equals(mHistoryLoading)){
			// mLoadingText.setText("正在加载中...");
			// }

			isOnCompletion = false;

		}

		// if(uri==null&&mMedia==null&&mPlayHistory==null){
		// mPlayerLoading.setVisibility(View.GONE);
		// }
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

		getScreenSize();

		mVideoView.setOnErrorListener(new OnErrorListener() {

			public boolean onError(MediaPlayer mp, int what, int extra) {
				LogUtil.i(TAG, " ---出错了Error: " + what + "," + extra);
				if(isCheckButton&&!Utils.isErrorNum ){
					LogUtil.i(TAG, " ---再次播放了: " + what + "," + extra);
					Utils.isErrorNum=true;
//					startSelfPlayer();
					ConfirmExit();
					
					
				}else{
					LogUtil.i(TAG, " ---要报错了: " + what + "," + extra);
					isError = true;
					if (isError) {
						if (mVideoBuffer != null) {
							mVideoBuffer.setVisibility(View.GONE);
						}
					}
					errorType = what;
					LogUtil.i(TAG, "Error: " + what + "," + extra);
					if (uri != null) {
						ConfirmExit();
						// errorsInformationDialog();
					}
					mHandler.sendEmptyMessage(SET_PAUSE_BUTTON);
				}
				
				return true;

			}

		});
		mVideoView
				.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

					public void onBufferingUpdate(MediaPlayer arg0,
							int bufferingProgress) {

					}
				});

		mVideoView.setOnInfoListener(new OnInfoListener() {

			public boolean onInfo(MediaPlayer mp, int what, int extra) {

				return false;
			}
		});

		// mVideoView.setMySizeChangeLinstener(new MySizeChangeLinstener() {
		//
		// @Override
		// public void doMyThings() {
		// setVideoScale(SCREEN_DEFAULT);
		// }
		//
		// });

		mPlayerSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean fromUser) {
						if (fromUser) {
							mVideoView.seekTo(progress);
							cancelDelayHide();
						}
					}

					public void onStartTrackingTouch(SeekBar arg0) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						if (uri == null && !isPaused) {
							isBuffering = false;
							if (mVideoBuffer != null) {
								mVideoBuffer.setVisibility(View.VISIBLE);
							}
							mHandler.sendEmptyMessageDelayed(BUFFERING_TAG,
									1000);
						}
						mHandler.sendEmptyMessage(SET_PAUSE_BUTTON);
						hideControllerDelay();
					}
				});

		mSeekBarvolume
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean fromUser) {
						currentVolume = mAudioManager
								.getStreamVolume(AudioManager.STREAM_MUSIC);

						LogUtil.i(TAG, "progress��" + progress + "---fromUser="
								+ fromUser + "------currentVolume="
								+ currentVolume);
						if (fromUser) {
							if (progress >= 15) {
								isSilent = false;
								updateVolume(15);
							} else if (progress <= 0) {
								isSilent = true;
								updateVolume(0);
							} else {
								isSilent = false;
								updateVolume(progress);
							}

						}

						cancelDelayHide();

					}

					public void onStartTrackingTouch(SeekBar arg0) {

					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						hideControllerDelay();
					}
				});

		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// long time = System.currentTimeMillis();
				// if (time - lastTimeonDoubleTap < CLICK_INTERVAL) {
				// return true;
				// }
				// lastTimeonDoubleTap = time;
				//
				// if (isFullScreen) {
				// setVideoScale(SCREEN_DEFAULT);
				// } else {
				// setVideoScale(SCREEN_FULL);
				// }
				// isFullScreen = !isFullScreen;
				//
				// if (isControllerShow) {
				// showController();
				// }

				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// long time = System.currentTimeMillis();
				// if (time - lastTimeonSingleTapConfirmed < CLICK_INTERVAL) {
				// return true;
				// }
				// lastTimeonSingleTapConfirmed = time;

				if (!isControllerShow) {
					isControllerShow = false;
					showController();
					cancelDelayHide();
					hideControllerDelay();
				} else {
					isControllerShow = true;
					hideController();
					cancelDelayHide();
				}

				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {/*
													 * long time =
													 * System.currentTimeMillis
													 * (); if (time -
													 * lastTimeonLongPress <
													 * CLICK_INTERVAL) { return;
													 * } lastTimeonLongPress =
													 * time; if (isPaused) {
													 * mVideoView.start();
													 * mPlayOrPause
													 * .setBackgroundResource
													 * (R.drawable.pausebtn_bg);
													 * //
													 * mPlay.setText(R.string.
													 * control_pause_state);
													 * cancelDelayHide();
													 * hideControllerDelay(); }
													 * else {
													 * mVideoView.pause();
													 * mPlayOrPause
													 * .setBackgroundResource
													 * (R.drawable.playbtn_bg);
													 * //
													 * mPlay.setText(R.string.
													 * control_play_state);
													 * cancelDelayHide();
													 * showController(); }
													 * isPaused = !isPaused;
													 */
			}
		});

		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer arg0) {

				mPlayerLoading.setVisibility(View.VISIBLE);
				isControllerShow = false;

				isBuffering = true;

				setVideoScale(SCREEN_DEFAULT);

				if (!isLoading) {
					hideController();
				}

				int i = (int) mVideoView.getDuration();
				Log.d("onCompletion", "" + i);
				mPlayerSeekBar.setMax(i);
				mEndTime.setText(stringForTime(i));

				//				
				String netUri = uri.toString();
				// LogUtil.i(TAG, "urill==="+netUri);
				String loacaUri = histroyUri;
				// LogUtil.i(TAG, "his==="+loacaUri);
				isTrue = false;

				if (uri != null && loacaUri != null) {

					if (isHttp) {
						if (!isTrue && netUri.equals(loacaUri)) {
							isTrue = true;
							if (histroyPosition > 0)
								mVideoView.seekTo((int) histroyPosition);
						}
						if (!isTrue && netUris[0].equals(loacaUris[0])
								&& netUris[1].equals(loacaUris[1])) {
							isTrue = true;
							if (histroyPosition > 0)
								mVideoView.seekTo((int) histroyPosition);
						}

						if (!isTrue && netUris[0].equals(loacaUris[0])) {
							isTrue = true;
							if (histroyPosition > 0)
								mVideoView.seekTo((int) histroyPosition);
						}
					} else {
						if (!isTrue && netUri.equals(loacaUri)) {
							isTrue = true;
							if (histroyPosition > 0)
								mVideoView.seekTo((int) histroyPosition);
						}
					}

				}

				mVideoView.start();
				LogUtil.i(TAG, " ---播放成功了: " );
				isLoading = false;
				Utils.isErrorNum = false;
				mPlayerLoading.setVisibility(View.GONE);
				isOnCompletion = false;
				isError = false;
				mHandler.sendEmptyMessage(SET_PAUSE_BUTTON);
				cancelDelayHide();
				hideControllerDelay();
				mHandler.removeMessages(PROGRESS_CHANGED);
				mHandler.sendEmptyMessage(PROGRESS_CHANGED);

				mHandler.removeMessages(BUFFER);
				mHandler.sendEmptyMessage(BUFFER);

			}
		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer arg0) {
				LogUtil.i(TAG, "onCompletion()================="
						+ isOnCompletion);

				if (!isOnCompletion) {
					isOnCompletion = true;
					LogUtil.i(TAG,
							"onCompletion()===============+isOnCompletion");
					isBuffering = false;
					if (uri != null) {

						if (preference != null) {
							SharedPreferences.Editor editor = preference.edit();
							if (editor != null) {

								if (mCurrentPosition > 0) {
									editor.putInt("CurrentPosition", 0);
									if (uri != null) {
										editor.putString("histroyUri", uri
												.toString());
									}

								} else {
									editor.putInt("CurrentPosition", 0);
									if (uri != null) {
										editor.putString("histroyUri", uri
												.toString());
									}

								}
								editor.commit();
							}

						}

						if (!isLocal) {
							ConfirmExit();
						} else {
							if(isCheckButton){
								startSelfPlayer();
							}else{
								if (!isAutoNext) {
									if (mCurrentPlayList != null
											&& mCurrentPlayList.size() > 1) {

										int n = mCurrentPlayList.size();
										if (++position < n) {
											// mVideoView.setVideoPath(mCurrentPlayList
											// .get(position).getUrl());
											if (mVideoView != null) {
												mVideoView.stopPlayback();
											}
											Utils.startSystemPlayer(
													VideoPlayer.this,
													mCurrentPlayList, position);
											// finish();
											mExitHandler.sendEmptyMessage(EXIT);
										} else {
											// Toast.makeText(SystemPlayer.this, "",
											// 0).show();
											--position;
											// finish();
											mExitHandler.sendEmptyMessage(EXIT);

										}

									} else {

										if (isHttp) {
											if (mVideoView != null) {
												// finish();
												mVideoView.stopPlayback();
												mExitHandler.sendEmptyMessage(EXIT);
											}

										} else {
											if (mVideoView != null) {
												// finish();
												mVideoView.stopPlayback();
												mExitHandler.sendEmptyMessage(EXIT);
											}

										}

									}
								}else{
									finish();
								}
							}
							
						}

					}
				}
			}
		});

		startPlay();

	}

	private int errorType = 0;

	public void setPauseButtonImage() {
		if (mVideoView != null) {
			LogUtil.i(TAG, "setPauseButtonImage()=============");
			try {
				if (mVideoView.isPlaying()) {
					// if(isCheckButton){
					// mPlayOrPause.setBackgroundResource(R.drawable.video_puase_gray);
					// }else{
					mPlayOrPause.setBackgroundResource(R.drawable.btn_pause);
					// }

				} else {
					mPlayOrPause.setBackgroundResource(R.drawable.btn_play);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void initView() {

		frame = (LinearLayout) findViewById(R.id.frame);
		mFrameLayout = (FrameLayout) findViewById(R.id.mFrameLayout);

		//
		mPlayerLoading = (LinearLayout) findViewById(R.id.player_loading);

		mVideoBuffer = (LinearLayout) findViewById(R.id.video_buffer);

		mVideoView = (VideoView) findViewById(R.id.video_view);

		mLoadingText = (TextView) findViewById(R.id.loading_text);
		
		mLoadingVideoName = (TextView) findViewById(R.id.loading_video_name);
		
		

		mLoadingBufferingText = (TextView) findViewById(R.id.loading_text);

		mVideoName = (TextView) findViewById(R.id.video_name);

		mBatteryState = (ImageView) findViewById(R.id.battery_state);

		mLastModify = (TextView) findViewById(R.id.last_modify);

		mBtnSetplay = (Button) findViewById(R.id.btn_setplay);

		mPlayerButtonBack = (Button) findViewById(R.id.btn_exit);

		mPlayerSeekBar = (SeekBar) findViewById(R.id.PlaybackProgressBar);

		mSeekBarvolume = (SeekBar) findViewById(R.id.VioceProgressBar);

		mCurrentTime = (TextView) findViewById(R.id.current_time);

		mEndTime = (TextView) findViewById(R.id.total_time);

		mDiaplayMode = (Button) findViewById(R.id.diaplay_mode);

		mPrevButton = (Button) findViewById(R.id.btn_back);

		mPlayOrPause = (Button) findViewById(R.id.btn_play_pause);

		mNextButton = (Button) findViewById(R.id.btn_forward);

		mPlayerVolume = (Button) findViewById(R.id.btn_voice);

		// mPlayerPlayList = (ImageButton) findViewById(R.id.player_play_list);

		if (currentVolume <= 0) {
			mPlayerVolume.setBackgroundDrawable(VideoPlayer.this.getResources()
					.getDrawable(R.drawable.btn_voice));
		} else {
			mPlayerVolume.setBackgroundDrawable(VideoPlayer.this.getResources()
					.getDrawable(R.drawable.btn_voice));
		}

		mPlayerSeekBar.setThumbOffset(13);
		mPlayerSeekBar.setMax(100);
		mPlayerSeekBar.setSecondaryProgress(0);

		mSeekBarvolume.setThumbOffset(13);
		mSeekBarvolume.setMax(15);
		mSeekBarvolume.setProgress(currentVolume);

		// if(mPlayHistory!=null){
		// mPlayerMediaTitle.setText(mPlayHistory.getMedianame());
		// }

		mPlayerButtonBack.setOnClickListener(mListener);

		mPlayOrPause.setOnClickListener(mListener);

		mPrevButton.setOnClickListener(mListener);
		mNextButton.setOnClickListener(mListener);
		// mPlayerPlayList.setOnClickListener(mListener);
		mDiaplayMode.setOnClickListener(mListener);
		mPlayerVolume.setOnClickListener(mListener);

		mBtnSetplay.setOnClickListener(mListener);

		// hideFoot();
		hideController();

	}

	private View.OnClickListener mListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_exit:
				isBuffering = false;
				if (!isBack) {
					// exit();
					isBack = true;
					mExitHandler.sendEmptyMessage(EXIT_TEXT);
					mPlayerButtonBack.setEnabled(false);
				}

				break;
			case R.id.btn_back:
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {
					int n = mCurrentPlayList.size();
					if (--position >= 0 && position < n) {
						
						if (mVideoView != null) {
							mVideoView.stopPlayback();
							hideController();
						}
						isBack = true;
						Utils.startSystemPlayer(VideoPlayer.this, mCurrentPlayList, position);
						finish();
					} else {
						position = 0;
						if (position >= 0 && position < n) {
							if (mVideoView != null) {
								mVideoView.stopPlayback();
								hideController();
							}
							isBack = true;
							Utils.startSystemPlayer(VideoPlayer.this, mCurrentPlayList, position);
							finish();
//							mVideoView.setVideoPath(mCurrentPlayList.get(position).getUrl());
						}

					}
					
				} else {
					mHandler.sendEmptyMessage(SEEK_BACKWARD);
				}

				break;

			case R.id.btn_play_pause:
				mHandler.sendEmptyMessage(IS_PAUSE_BUTTON);
				break;
			case R.id.btn_forward:
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {

					int n = mCurrentPlayList.size();
					if (++position < n && position >= 0) {
						if (mVideoView != null) {
							mVideoView.stopPlayback();
							hideController();
						}
						isBack = true;
//						mVideoView.setVideoPath(mCurrentPlayList.get(position).getUrl());
						Utils.startSystemPlayer(VideoPlayer.this, mCurrentPlayList, position);
						finish();
//						cancelDelayHide();
//						hideControllerDelay();
					} else {
						//
						if (position > 0) {
							--position;
						}
						if (position >= 0 && position < n) {
							if (mVideoView != null) {
								mVideoView.stopPlayback();
								hideController();
							}
							isBack = true;
//							mVideoView.setVideoPath(mCurrentPlayList.get(position).getUrl());
							Utils.startSystemPlayer(VideoPlayer.this, mCurrentPlayList, position);
							finish();
						}

					}
				
				} else {
					mHandler.sendEmptyMessage(SEEK_FORWARD);
				}
				break;
			case R.id.btn_voice:
				if (mAudioManager != null) {
					if (isSilent) {
						isSilent = false;
					} else {
						isSilent = true;
					}
					updateVolume(currentVolume);
				}

				break;

			case R.id.diaplay_mode:
				if (isFullScreen) {
					setVideoScale(SCREEN_DEFAULT);
				} else {
					setVideoScale(SCREEN_FULL);
				}

				break;
			case R.id.btn_setplay:

				openSetPlay();

				break;

			}

		}
	};
	
	private void startSelfPlayer() {

//		String eCodeUri = null;
		isBack = true;
		Intent intent = new Intent(VideoPlayer.this, VideoPlayer.class);
		if (mVideoView != null) {
			mVideoView.stopPlayback();
		}
		if (uri != null) {
			if (!isLocal) {
//				if (uri != null) {
//					eCodeUri = uri.toString();
//				}
//				intent.putExtra("localuri", eCodeUri);
				intent.setData(uri);

			} else {
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {
					Bundle mBundle = new Bundle();
					mBundle
							.putSerializable("MediaIdList",
									mCurrentPlayList);
					intent.putExtras(mBundle);
					intent.putExtra("CurrentPosInMediaIdList", position);

				} else {
					Bundle mBundle = new Bundle();
					mBundle.putSerializable("VideoInfo", videoInfo);
					intent.putExtras(mBundle);
				}

			}

		} 
		
		
		startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);
		mExitHandler.sendEmptyMessageDelayed(EXIT,2000);

	}
	

	private void startVideoPlayer() {

//		String eCodeUri = null;
		isBack = true;
		Intent intent = new Intent(VideoPlayer.this, SystemPlayer.class);
		if (mVideoView != null) {
			mVideoView.stopPlayback();
		}
		if (uri != null) {
			if (!isLocal) {
//				if (uri != null) {
//					eCodeUri = uri.toString();
//				}
//				intent.putExtra("localuri", eCodeUri);
				intent.setData(uri);

			} else {
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {
					Bundle mBundle = new Bundle();
					mBundle
							.putSerializable("MediaIdList",
									mCurrentPlayList);
					intent.putExtras(mBundle);
					intent.putExtra("CurrentPosInMediaIdList", position);

				} else {
					Bundle mBundle = new Bundle();
					mBundle.putSerializable("VideoInfo", videoInfo);
					intent.putExtras(mBundle);
				}

			}

		} 
		
		
		startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);
		mExitHandler.sendEmptyMessageDelayed(EXIT,2000);

	}

	private boolean isCick;
	private Dialog dialog;

	private void openSetPlay() {
		try {
			dialog = new Dialog(VideoPlayer.this, R.style.player_dialog_list);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setContentView(R.layout.play_video_detail);
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.alpha = 0.95f; // 0.0-1.0
			dialog.getWindow().setAttributes(lp);

			TextView mlinearLanguage = (TextView) dialog
					.findViewById(R.id.set_player_text);
			mlinearLanguage.setText(getString(R.string.setplay_for_system));
			Button linearGridView = (Button) dialog
					.findViewById(R.id.set_player);
			linearGridView.setText("系统解码播放");

			linearGridView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!isCick) {
						isCick = true;
						startVideoPlayer();
						if (dialog != null)
							dialog.dismiss();
					}
				}
			});

			if (dialog != null && !dialog.isShowing()) {
				dialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	private static long CLICK_INTERVAL = 300;
	private long lastTimeonSingleTapConfirmed;

	private void startPlay() {
		if (uri != null && mVideoView != null) {
			if (mVideoBuffer != null) {
				mVideoBuffer.setVisibility(View.GONE);
			}
			mVideoView.stopPlayback();
			LogUtil.i(TAG, "playUri ===" + String.valueOf(uri));
			mVideoView.setVideoURI(uri);
			isOnline = true;

		} else {
			finish();
		}
		mHandler.removeMessages(PROGRESS_CHANGED);
		mHandler.sendEmptyMessage(PROGRESS_CHANGED);
	}

	public void setPlaySeekBarEnabled(boolean enabled) {
		if (mPlayerSeekBar != null) {
			mPlayerSeekBar.setEnabled(enabled && mPlayerSeekBar != null);

		}
	}

	public void setNextEnabled(boolean enabled) {
		if (mNextButton != null) {
			mNextButton.setEnabled(enabled && mListener != null);
			if (enabled) {
				

				if(mCurrentPlayList !=null && mCurrentPlayList.size()>0){
					if(position == (mCurrentPlayList.size()-1)){
						mNextButton.setBackgroundDrawable(VideoPlayer.this
								.getResources().getDrawable(
										R.drawable.btn_forward));
					}
				}else{
					 mNextButton.setBackgroundDrawable(VideoPlayer.this
							 .getResources().getDrawable(
							 R.drawable.btn_forward_one));
						}
				
			
				
				// mNextButton.setBackgroundDrawable(VideoPlayer.this.getResources().getDrawable(R.drawable.videonextbtn_bg));
			} else {
				if(mCurrentPlayList !=null && mCurrentPlayList.size()>0){
					if(position == (mCurrentPlayList.size()-1)){
						mNextButton.setBackgroundDrawable(VideoPlayer.this
								.getResources().getDrawable(
										R.drawable.video_next_btn_bg));
					}
				}else{
					mNextButton.setBackgroundDrawable(VideoPlayer.this
							.getResources().getDrawable(
									R.drawable.btn_forward_one_huise));
				}
				
			}
		}
	}

	public void setPrevEnabled(boolean enabled) {
		
		

		if (mPrevButton != null) {
			mPrevButton.setEnabled(enabled);
			if (enabled) {
				

				if(mCurrentPlayList !=null && mCurrentPlayList.size()>0){
					if(position == (mCurrentPlayList.size()-1)){
						mPrevButton.setBackgroundDrawable(VideoPlayer.this
								.getResources().getDrawable(
										R.drawable.btn_back));
					}
				}else{
					mPrevButton.setBackgroundDrawable(VideoPlayer.this
							.getResources()
							.getDrawable(R.drawable.btn_back_one));
				}
				
			
				
			} else {
				if(mCurrentPlayList !=null && mCurrentPlayList.size()>0){
					if(position == 0){
						mPrevButton.setBackgroundDrawable(VideoPlayer.this
								.getResources().getDrawable(
										R.drawable.video_pre_gray));
					}
				}else{
					mPrevButton.setBackgroundDrawable(VideoPlayer.this
							.getResources().getDrawable(
									R.drawable.btn_back_one_huise));
				}
				
			}
		}
	
		
		
		
//		if (mPrevButton != null) {
//			mPrevButton.setEnabled(enabled && mListener != null);
//
//			if (enabled) {
//
//				// mPrevButton.setBackgroundDrawable(VideoPlayer.this.getResources().getDrawable(R.drawable.videoprebtn_bg));
//			} else {
//				mPrevButton.setBackgroundDrawable(SystemPlayer.this
//						.getResources().getDrawable(R.drawable.video_pre_gray));
//			}
//
//		}
//		
		
	}

	public void setPlayOrPauseEnabled(boolean enabled) {
		if (mPlayOrPause != null) {
			mPlayOrPause.setEnabled(enabled && mPlayOrPause != null);

			if (enabled) {
				// mPlayOrPause.setBackgroundDrawable(VideoPlayer.this.getResources().getDrawable(R.drawable.videonextbtn_bg));
			} else {
				if (!isPaused) {
					mPlayOrPause
							.setBackgroundResource(R.drawable.video_puase_gray);

				} else {
					mPlayOrPause.setBackgroundResource(R.drawable.btn_play);
				}

			}
		}
	}

	private void setVideoScale(int flag) {

		switch (flag) {
		case SCREEN_FULL:
			mDiaplayMode.setBackgroundResource(R.drawable.btn_original_size);
			Log.d(TAG, "screenWidth: " + screenWidth + " screenHeight: "
					+ screenHeight);
			// mVideoView.setVideoScale(screenWidth, screenHeight);
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ZOOM, 0);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			isFullScreen = true;
			break;

		case SCREEN_DEFAULT:

			mDiaplayMode.setBackgroundResource(R.drawable.btn_full_screen);

			int videoWidth = mVideoView.getVideoWidth();
			int videoHeight = mVideoView.getVideoHeight();
			int mWidth = screenWidth;
			int mHeight = screenHeight - 25;

			if (videoWidth > 0 && videoHeight > 0) {
				if (videoWidth * mHeight > mWidth * videoHeight) {

					mHeight = mWidth * videoHeight / videoWidth;
				} else if (videoWidth * mHeight < mWidth * videoHeight) {

					mWidth = mHeight * videoWidth / videoHeight;
				} else {

				}
			}
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
			// mVideoView.setVideoScale(mWidth, mHeight);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			isFullScreen = false;
			break;
		}
	}

	private void hideControllerDelay() {
		mHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	// private void hideTop(){
	// mPlayerTop.setVisibility(View.GONE);
	// }
	//	

	private void hideController() {

		// if(/*!isFromApplication&&*/isLoading){
		// // mPlayerTop.setVisibility(View.VISIBLE);
		// // mPlayerFootCtrBar.setVisibility(View.GONE);
		//		    	
		// }else if(/*!isFromApplication&&*/!isLoading){
		//		    	
		// // mPlayerTop.setVisibility(View.GONE);
		// // mPlayerFootCtrBar.setVisibility(View.GONE);
		// }
		if (isLoading && isBuffering) {
			frame.setVisibility(View.GONE);
			mFrameLayout.setVisibility(View.GONE);
		} else if (!isLoading && isBuffering) {
			frame.setVisibility(View.GONE);
			mFrameLayout.setVisibility(View.GONE);
		}

		isControllerShow = false;
		// isSoundShow = false;

	}

	private void cancelDelayHide() {
		mHandler.removeMessages(HIDE_CONTROLER);
	}

	private void showController() {

		if (!isLoading && isBuffering) {
			frame.setVisibility(View.VISIBLE);
			mFrameLayout.setVisibility(View.VISIBLE);
		}

		isControllerShow = true;

	}

	private boolean isBuffering = false;
	private boolean isSoftBuffering = false;
	private boolean isHttp = false;
	private boolean isCheckButton = false;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case PROGRESS_CHANGED:

				if (mVideoView == null) {
					return;
				}
				if (!Utils.isCheckNetAvailable(VideoPlayer.this) && isHttp) {
					Utils.showDialog(" 提示", "确定", "取消", "网络不可用，请检查网络", true);
					mVideoView.stopPlayback();
					return;
				}

				int i = (int) mVideoView.getCurrentPosition();
				
				if (uri != null && !isBack && isBuffering && !isPaused
						&& !isError&&!isCheckButton) {
					int isBuffer = i - mCurrentPosition;
				
					if (isBuffer < -500 || isBuffer < 500) {
						if (mVideoBuffer != null) {
							 mVideoBuffer.setVisibility(View.VISIBLE);
						}

					} else {
						if (mVideoBuffer != null) {
							 mVideoBuffer.setVisibility(View.GONE);
						}
					}

				} else {
					if (!isCheckButton&&isPaused) {
						if (mVideoBuffer != null) {
							 mVideoBuffer.setVisibility(View.GONE);

						}
					}

				}

				isSoftBuffering = mVideoView.isBuffering();
				if (isCheckButton && !isBack && isSoftBuffering && !isPaused
						&& !isError) {
					LogUtil.i(TAG, "--buffering....isSoftBuffering=="+isSoftBuffering+"----CurrentPosition=="+i);
					int isBuffer = i - mCurrentPosition;
				
					if (isBuffer < -500 || isBuffer < 500) {
						if (mVideoBuffer != null) {
							 mVideoBuffer.setVisibility(View.VISIBLE);
						}

					} else {
						if (mVideoBuffer != null) {
							mVideoBuffer.setVisibility(View.GONE);

						}
					}

				} else {
					if (isPaused || !isSoftBuffering) {
						if (mVideoBuffer != null) {
							mVideoBuffer.setVisibility(View.GONE);
						}
					}

				}
				mCurrentPosition = i;
				Calendar calendar = Calendar.getInstance();
				// int year = calendar.get(Calendar.YEAR);
				// int month = calendar.get(Calendar.MONTH);
				// int day = calendar.get(Calendar.DAY_OF_MONTH);
				String hourStr = null;
				String minuteStr = null;
				String timeStr = null;
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				int second = calendar.get(Calendar.SECOND);
				if (hour == 0) {
					hourStr = "00";
				} else if (0 < hour && hour < 10) {
					hourStr = "0" + hour;
				} else {
					hourStr = String.valueOf(hour);
				}

				if (minute == 0) {
					minuteStr = "00";
				} else if (0 < minute && minute < 10) {
					minuteStr = "0" + minute;
				} else {
					minuteStr = String.valueOf(minute);
				}

				if (second == 0) {
					timeStr = "00";
				} else if (0 < second && second < 10) {
					timeStr = "0" + second;
				} else {
					timeStr = String.valueOf(second);
				}
				String time = hourStr + ":" + minuteStr + ":" + timeStr;
				mLastModify.setText(time);

				mPlayerSeekBar.setProgress(i);
				if (isHttp) {
					int j = mVideoView.getBufferPercentage();
					int setSecondaryProgress = j * mPlayerSeekBar.getMax()
							/ 100;
					mPlayerSeekBar.setSecondaryProgress(setSecondaryProgress);

				} else {
					mPlayerSeekBar.setSecondaryProgress(0);
				}
				setBattery(level);
				mCurrentTime.setText(stringForTime(i));
				if (!isOnCompletion && !isLoading) {
					SharedPreferences.Editor editor = preference.edit();
					if (editor != null) {

						if (mCurrentPosition > 0 && uri != null) {
							editor.putInt("CurrentPosition", mCurrentPosition);
							// editor.putInt("position", position);
							if (uri != null) {
								editor.putString("histroyUri", uri.toString());
							}

						}/*
						 * else{ // editor.putInt("CurrentPosition", 0); //
						 * editor.putInt("position", 0); if(uri!=null){
						 * editor.putString("histroyUri", uri.toString()); }
						 * 
						 * }
						 */
						editor.commit();
					}

				}
				if(!isBack&&!isError){
					mHandler.removeMessages(PROGRESS_CHANGED);
					mHandler.sendEmptyMessageDelayed(PROGRESS_CHANGED, 1000);
				}

				break;

			case HIDE_CONTROLER:
				hideController();
				break;
			case BUFFERING_TAG:
				isBuffering = true;
				break;
			case PAUSE:
				if (mVideoView != null) {
					mVideoView.pause();
				}
				break;

			case SET_PAUSE_BUTTON:
				setPauseButtonImage();
				break;

			case IS_PAUSE_BUTTON:
				if (isPaused) {
					mVideoView.start();
					// if(isCheckButton){
					// mPlayOrPause.setBackgroundResource(R.drawable.video_puase_gray);
					// }else{
					mPlayOrPause.setBackgroundResource(R.drawable.btn_pause);
					// }
					isBuffering = true;
					cancelDelayHide();
					hideControllerDelay();
				} else {
					mVideoView.pause();
					mPlayOrPause.setBackgroundResource(R.drawable.btn_play);
					cancelDelayHide();
					showController();
					isBuffering = false;

				}

				isPaused = !isPaused;
				break;

			case SEEK_BACKWARD:
//				if (mVideoView != null) {
//					int pos = (int) mVideoView.getCurrentPosition();
//					Integer times = 10;
//					String key_2 = "10";
//					if (preference != null) {
//						key_2 = preference.getString(SettingActivity.key_2,
//								"10");
//						if (key_2 != null) {
//							times = Integer.valueOf(key_2);
//						}
//
//					}
//					pos -= (times * 1000);
//					// pos -= 15000;
//					mVideoView.seekTo(pos);
//				}
				cancelDelayHide();
				hideControllerDelay();
				break;

			case SEEK_FORWARD:
//				if (mVideoView != null) {
//					int pos = (int) mVideoView.getCurrentPosition();
//					Integer times = 10;
//					String key_2 = "10";
//					if (preference != null) {
//						key_2 = preference.getString(SettingActivity.key_2,
//								"10");
//						if (key_2 != null) {
//							times = Integer.valueOf(key_2);
//						}
//
//					}
//
//					pos += (times * 1000);
//					// pos += 15000;
//					mVideoView.seekTo(pos);
//				}
				cancelDelayHide();
				hideControllerDelay();
				break;

			}

			super.handleMessage(msg);
		}
	};

	Handler mExitHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EXIT_TEXT:
				if (isBack) {
					mLoadingText.setText(VideoPlayer.this.getBaseContext()
							.getResources()
							.getString(R.string.exit_player_text));
					mLoadingBufferingText.setText(VideoPlayer.this
							.getBaseContext().getResources().getString(
									R.string.exit_player_text));
					if (mVideoBuffer != null) {
						mVideoBuffer.setVisibility(View.VISIBLE);
					}
				}
				mExitHandler.sendEmptyMessage(EXIT);
				break;

			case EXIT:
				exit();
			}
		}
	};
	private int mAudioMax;
	private int mAudioDisplayRange;
	private float mTouchY, mVol;
	private boolean mIsAudioChanged;
	private String[] mAudioTracks;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mAudioDisplayRange == 0)
			mAudioDisplayRange = Math.min(getWindowManager()
					.getDefaultDisplay().getWidth(), getWindowManager()
					.getDefaultDisplay().getHeight());

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			mTouchY = event.getY();
			mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			mIsAudioChanged = false;
			break;

		case MotionEvent.ACTION_MOVE:
			float y = event.getY();

			int delta = (int) (((mTouchY - y) / mAudioDisplayRange) * mAudioMax);
			int vol = (int) Math.min(Math.max(mVol + delta, 0), mAudioMax);
			if (delta != 0) {
				updateVolume(vol);
				// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol,
				// AudioManager.FLAG_SHOW_UI);
				mIsAudioChanged = true;
			}
			break;

		case MotionEvent.ACTION_UP:
			if (!mIsAudioChanged) {
				if (!isControllerShow) {
					isControllerShow = false;
					showController();
					cancelDelayHide();
					hideControllerDelay();
				} else {
					isControllerShow = true;
					hideController();
					cancelDelayHide();
				}
			}
			break;
		}
		return mIsAudioChanged;

		// if(mGestureDetector!=null){
		// result = mGestureDetector.onTouchEvent(event);
		// if (!result) {
		// if (event.getAction() == MotionEvent.ACTION_UP) {
		// }
		// result = super.onTouchEvent(event);
		// }
		// return result;
		// }
		// return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.v(TAG, " onConfigurationChanged()");

		getScreenSize();
		if (isControllerShow) {
			hideController();
			showController();
			cancelDelayHide();
			hideControllerDelay();
		}

		super.onConfigurationChanged(newConfig);
	}

	private void updateVolume(int index) {
		LogUtil.i(TAG, "updateVolume==" + index + "----------currentVolume="
				+ currentVolume);
		if (mAudioManager != null) {
			if (isSilent) {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
				mSeekBarvolume.setProgress(0);
			} else {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index,
						0);
				mSeekBarvolume.setProgress(index);
				if (index == 0) {
				} else {
				}

			}
			currentVolume = index;
		}
	}

	private void getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		controlViewHeight = screenHeight / 4;

	}

	private AlertDialog alertDialog = null;
	AlertDialog.Builder aler = null;

	private void ConfirmExit() {// 退出确认
		aler = new AlertDialog.Builder(VideoPlayer.this);
		aler.setTitle("提示");

		if (uri != null && isLocal) {

			if (!isOnCompletion) {
				setErrorTyp(errorType);
				aler.setNegativeButton("确定", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						alertDialog.hide();
						finish();
						alertDialog = null;

					}
				});

			}
		}

		if (uri != null && !isLocal) {

			if (isOnCompletion) {
				aler.setMessage(getString(R.string.play_comper));
				aler.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {// 退出按钮

							public void onClick(DialogInterface dialog, int i) {

								alertDialog.hide();
								finish();
								alertDialog = null;

							}
						});
				aler.setNegativeButton("进入", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
//						Intent intent = new Intent();
//						intent.setClass(VideoPlayer.this, MainActivity.class);
//						startActivity(intent);
//						overridePendingTransition(R.anim.fade, R.anim.hold);
//						alertDialog.hide();
//						finish();
//						alertDialog = null;

					}
				});
			} else {
				aler.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {// 退出按钮

							public void onClick(DialogInterface dialog, int i) {
								alertDialog.hide();
								finish();
								alertDialog = null;

							}
						});
				setErrorTyp(errorType);
				aler.setNegativeButton("进入", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

//						Intent intent = new Intent();
//						intent.setClass(VideoPlayer.this, MainActivity.class);
//						startActivity(intent);
//						overridePendingTransition(R.anim.fade, R.anim.hold);
//						alertDialog.hide();
//						finish();
//						alertDialog = null;

					}
				});
			}

		}
		if (alertDialog == null) {
			alertDialog = aler.create();
		}
		if (alertDialog != null && !alertDialog.isShowing()) {
			alertDialog.show();
		}

	}

	private void setErrorTyp(int errorType) {
		switch (errorType) {
		// case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
		// aler.setMessage("抱歉，播放器出错了！");
		// break;

		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			aler.setMessage("抱歉，该视频无法拖动！");
			break;

		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
				
			aler.setMessage("抱歉，播放出错了!");
			
			break;
		// case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
		// aler.setMessage("抱歉，该视频文件格式错误！");
		// break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
			aler.setMessage("抱歉，解码时出现");
			break;

		default:
			aler.setMessage("抱歉，该视频无法播放！");
			break;
		}
	}

	private void exit() {
		// if(isBack){
		// updateByHashid();
		// }
		try {
			if (mVideoView != null) {
				mVideoView.stopPlayback();
			}
		} catch (Exception e) {
			finish();
			overridePendingTransition(R.anim.fade, R.anim.hold);
		}
		finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

	private boolean isError = false;

	@Override
	protected void onPause() {
		Log.v(TAG, " onPause()");

		if (mVideoView != null && !isOnCompletion && !isError) {
			mCurrentPosition = (int) mVideoView.getCurrentPosition();
			// mVideoView.pause();
		}
		if (mHandler != null&&radia==null) {
			mHandler.sendEmptyMessage(PAUSE);
		}
		// modif by yangguangfu
		// updateByHashid();

		super.onPause();
	}

	private PausePlayerReceiver mPausePlayerReceiver;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return true;
	}

	@Override
	protected void onResume() {
		Log.v(TAG, "onResume()");
		Log.d("REQUEST", "NEW AD !");
		BaseActivity.mBaseActivity = this;
		Log.v(TAG, "onStart())");
		if (mVideoView != null && mVideoView.isPlaying()) {
			showController();
			cancelDelayHide();
			hideControllerDelay();
		} else if (!isCheckButton&&mVideoView != null) {
			if (mCurrentPosition > 0) {
				mVideoView.seekTo(mCurrentPosition);
				mVideoView.start();
			}

			showController();
			cancelDelayHide();
			hideControllerDelay();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Log.v(TAG, " onDestroy()");
		ActivityHolder.getInstance().removeActivity(this);
		if (mPausePlayerReceiver != null)
			unregisterReceiver(mPausePlayerReceiver);
		unregisterReceiver(batteryReceiver);
		mHandler.removeMessages(PROGRESS_CHANGED);
		mHandler.removeMessages(HIDE_CONTROLER);

		// if (playList != null) {
		// playList.clear();
		// }

//		if (mMedia != null) {
//			mMedia = null;
//		}
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v(TAG, " onRestart()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.v(TAG, "onSaveInstanceState()");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	
	}

	@Override
	protected void onStop() {

		super.onStop();
		Log.v(TAG, "onStop()");
	}

	private boolean isBack = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (!isBack) {
				ActivityHolder.getInstance().removeActivity(this);
				isBack = true;
				// finish();
				mExitHandler.sendEmptyMessage(EXIT_TEXT);
				// exit();

			}
			return true;
			// return super.onKeyDown(keyCode, event);

		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			if (currentVolume >= 1) {
				currentVolume--;
			}
			updateVolume(currentVolume);

			return super.onKeyDown(keyCode, event);

		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			if (currentVolume < 15) {
				currentVolume++;
			}

			updateVolume(currentVolume);

			return super.onKeyDown(keyCode, event);

		}
		return false;
	}

	

}
