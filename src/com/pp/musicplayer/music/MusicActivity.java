package com.pp.musicplayer.music;

import java.util.List;

import com.pp.musicplayer.R;
import com.pp.musicplayer.domain.Music;
import com.pp.musicplayer.util.LrcView;
import com.pp.musicplayer.util.MusicList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MusicActivity extends Activity implements SensorEventListener {

	private TextView textName;
	private TextView textArtist;
	private TextView textStartTime;
	private TextView textEndTime;
	private ImageButton imageBtnLast;
	private ImageButton imageBtnRewind;
	public  static ImageButton imageBtnPlay;
	private ImageButton imageBtnForward;
	private ImageButton imageBtnNext;
	private ImageButton imageBtnLoop;
	private ImageButton imageBtnRandom;
	public static LrcView lrc_view;
	private SeekBar seekBar;
	private AudioManager audioManager;
	private int maxVolume;
	private int currentVolume;
	private SeekBar seekBarVolume;
	private List<Music> lists;
	private Boolean isPlaying = false;
	private static int id = 1;
	private static int currentId = 2;
	private static Boolean replaying =false;
	private MyProgressBroadCastReceiver receiver;
	private MyCompletionListner completionListner;
	public static Boolean isLoop = true;
	public static Boolean isRandom = true;
	private SensorManager sensorManager;
	private boolean mRegisteredSensor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.music);
		
		textName = (TextView)findViewById(R.id.music_name);
		textArtist = (TextView)findViewById(R.id.music_artist);
		textStartTime = (TextView)findViewById(R.id.music_start_time);
		textEndTime = (TextView)findViewById(R.id.music_end_time);
		imageBtnLast = (ImageButton)findViewById(R.id.music_lasted);
		imageBtnRewind = (ImageButton)findViewById(R.id.music_rewind);
		imageBtnPlay = (ImageButton)findViewById(R.id.music_play);
		imageBtnForward = (ImageButton)findViewById(R.id.music_foward);
		imageBtnNext = (ImageButton)findViewById(R.id.music_next);
		imageBtnLoop = (ImageButton)findViewById(R.id.music_loop);
		imageBtnRandom = (ImageButton)findViewById(R.id.music_random);
		seekBar = (SeekBar)findViewById(R.id.music_seekBar);
		seekBarVolume = (SeekBar)findViewById(R.id.music_volume);
		lrc_view = (LrcView)findViewById(R.id.LyricShow);
		
		imageBtnLast.setOnClickListener(new MyListener());
		imageBtnRewind.setOnClickListener(new MyListener());
		imageBtnPlay.setOnClickListener(new MyListener());
		imageBtnForward.setOnClickListener(new MyListener());
		imageBtnNext.setOnClickListener(new MyListener());
		imageBtnLoop.setOnClickListener(new MyListener());
		imageBtnRandom.setOnClickListener(new MyListener());
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
		lists = MusicList.getMusicData(this);
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		//获得最大音量
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//获得当前音量
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		seekBarVolume.setMax(maxVolume);
		seekBarVolume.setProgress(currentVolume);
		seekBarVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
						AudioManager.FLAG_ALLOW_RINGER_MODES);
				
			}
		});
		
		//电话状态监听
		TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MobilePhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				seekBar.setProgress(seekBar.getProgress());
				Intent intent = new Intent("com.pp.musicplayer.seekBar");
				intent.putExtra("seekBarPosition", seekBar.getProgress());
				sendBroadcast(intent);
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		completionListner = new MyCompletionListner();
		IntentFilter filter = new IntentFilter("com.pp.musicplayer.completion");
		this.registerReceiver(completionListner, filter);
	}
	
	private class MobilePhoneStateListener extends PhoneStateListener{
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			switch (state) {
			//空闲状态时
			case TelephonyManager.CALL_STATE_IDLE:
				Intent intent = new Intent(MusicActivity.this, MusicService.class);
				intent.putExtra("play", "playing");
				intent.putExtra("id", id);
				startService(intent);
				isPlaying = true;
				imageBtnPlay.setImageResource(R.drawable.pause1);
				replaying = true;
				break;
				
			//接上电话时	
			case TelephonyManager.CALL_STATE_OFFHOOK:
			//电话进来时
			case TelephonyManager.CALL_STATE_RINGING:
				Intent intent2 = new Intent(MusicActivity.this, MusicService.class);
				intent2.putExtra("play", "pause");
				startService(intent2);
				isPlaying = false;
				imageBtnPlay.setImageResource(R.drawable.play1);
				replaying = false;

			default:
				break;
			}
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		receiver  = new MyProgressBroadCastReceiver();
		IntentFilter filter = new IntentFilter("com.pp.musicplayer.progress");
		this.registerReceiver(receiver, filter);
		
		id = getIntent().getIntExtra("id", 1);
		if (id == currentId) {
			Music m = lists.get(id);
			textName.setText(m.getTitle());
			textArtist.setText(m.getArtist());
			textEndTime.setText(toTime((int)m.getTime()));
			Intent intent = new Intent(MusicActivity.this, MusicService.class);
			intent.putExtra("play", "replaying");
			intent.putExtra("id", id);
			startService(intent);
			if (replaying == true) {
				imageBtnPlay.setImageResource(R.drawable.pause1);
				isPlaying = true;
			} else {
				imageBtnPlay.setImageResource(R.drawable.play1);
				isPlaying = false;
			} 
			
		}else {
			Music m = lists.get(id);
			textName.setText(m.getTitle());
			textArtist.setText(m.getArtist());
			textEndTime.setText(toTime((int)m.getTime()));
			Intent intent = new Intent(MusicActivity.this, MusicService.class);
			intent.putExtra("play", "play");
			intent.putExtra("id", id);
			startService(intent);
			isPlaying = true;
			replaying = true;
			currentId = id;
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			Sensor sensor = sensors.get(0);
			mRegisteredSensor = sensorManager.registerListener(this, sensor, 
					sensorManager.SENSOR_DELAY_FASTEST);
			Log.d("----------", sensor.getName());
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		if (mRegisteredSensor) {
			sensorManager.unregisterListener(this);
			mRegisteredSensor = false;
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(receiver);
		this.unregisterReceiver(completionListner);
		super.onDestroy();
	}
	
	public class MyProgressBroadCastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int position = intent.getIntExtra("position", 0);
			int total = intent.getIntExtra("total", 0);
			int progress = position * 100 / total;
			textStartTime.setText(toTime(position));
			seekBar.setProgress(progress);
			seekBar.invalidate();
			
		}
	}
	
	private class MyListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == imageBtnLast) {
				//第一首
				id = 0;
				Music m = lists.get(0);
				textName.setText(m.getTitle());
				textArtist.setText(m.getArtist());
				textEndTime.setText(toTime((int)m.getTime()));
				imageBtnPlay.setImageResource(R.drawable.pause1);
				Intent intent = new Intent(MusicActivity.this, MusicService.class);
				intent.putExtra("play", "first").toString();
				intent.putExtra("id", id);
				startService(intent);
				isPlaying = true;
			} else if (v ==imageBtnRewind) {
				//上一首
				int id = MusicService._id - 1;
				if (id >= lists.size() - 1) {
					id = lists.size() - 1;
				} else if (id <= 0) {
					id = 0;
				}
				Music m = lists.get(id);
				textName.setText(m.getTitle());
				textArtist.setText(m.getArtist());
				textEndTime.setText(toTime((int)m.getTime()));
				imageBtnPlay.setImageResource(R.drawable.pause1);
				Intent intent = new Intent(MusicActivity.this, MusicService.class);
				intent.putExtra("play", "rewind").toString();
				intent.putExtra("id", id);
				startService(intent);
				isPlaying = true;
				
			} else if (v == imageBtnPlay) {
				//正在播放
				if (isPlaying == true) {
					Intent intent = new Intent(MusicActivity.this, MusicService.class);
					intent.putExtra("play", "pause").toString();
					startService(intent);
					isPlaying = false; 
					imageBtnPlay.setImageResource(R.drawable.play1);
					replaying = false;
				} else {
					Intent intent = new Intent(MusicActivity.this, MusicService.class);
					intent.putExtra("play", "playing").toString();
					intent.putExtra("id", id);
					startService(intent);
					isPlaying = true;
					imageBtnPlay.setImageResource(R.drawable.pause1);
					replaying = false;
				}
			} else if (v == imageBtnForward) {
				//下一首
				int id = MusicService._id + 1;
				if (id >= lists.size() - 1) {
					id = lists.size() - 1;
				} else if (id <= 0) {
					id = 0;
				}
				Music m = lists.get(id);
				textName.setText(m.getTitle());
				textArtist.setText(m.getArtist());
				textEndTime.setText(toTime((int)m.getTime()));
				imageBtnPlay.setImageResource(R.drawable.pause1);
				Intent intent = new Intent(MusicActivity.this, MusicService.class);
				intent.putExtra("play", "forward").toString();
				intent.putExtra("id", id);
				startService(intent);
				isPlaying = true;
			} else if (v == imageBtnNext) {
				//最后一首
				int id = lists.size() - 1;
				Music m = lists.get(id);
				textName.setText(m.getTitle());
				textArtist.setText(m.getArtist());
				textEndTime.setText(toTime((int)m.getTime()));
				imageBtnPlay.setImageResource(R.drawable.pause1);
				Intent intent = new Intent(MusicActivity.this, MusicService.class);
				intent.putExtra("play", "last").toString();
				intent.putExtra("id", id);
				startService(intent);
				isPlaying = true;
			} else if (v == imageBtnLoop) {
				if (isLoop == true) {
					//非单曲循环
					imageBtnLoop.setBackgroundResource(R.drawable.play_loop_spec);
					isLoop = false;
				} else {
					//单曲循环
					imageBtnLoop.setBackgroundResource(R.drawable.play_loop_sel);
					isLoop = true;
				}
			} else if (v == imageBtnRandom) {
				if (isRandom == true) {
					imageBtnRandom.setImageResource(R.drawable.play_random_sel);
					isRandom = false;
				} else {
					imageBtnRandom.setImageResource(R.drawable.play_random);
					isRandom = true;
				}
			}
		}
	}
	
	public class MyCompletionListner extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Music m = lists.get(MusicService._id);
			textName.setText(m.getTitle());
			textArtist.setText(m.getArtist());
			textEndTime.setText(toTime((int)m.getTime()));
			imageBtnPlay.setImageResource(R.drawable.pause1);
		}
	}
	
	public String toTime(int time){
		time = time / 1000;
		int minute = time / 60;
		int second = time % 60;
		minute = minute % 60;
		return String.format("%02d:%02d", minute, second);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
}
