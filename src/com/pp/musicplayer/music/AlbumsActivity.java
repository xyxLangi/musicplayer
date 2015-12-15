package com.pp.musicplayer.music;

import com.pp.musicplayer.R;
import com.pp.musicplayer.adapter.AlbumsAdapter;
import com.pp.musicplayer.util.MusicList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class AlbumsActivity extends Activity {

	private ListView albumListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.albums);
		
		albumListView = (ListView) findViewById(R.id.albumListView);
		AlbumsAdapter adapter=new AlbumsAdapter(this, MusicList.getMusicData(this));
		albumListView.setAdapter(adapter);
		albumListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AlbumsActivity.this, MusicActivity.class);
				intent.putExtra("id", arg2);
				startActivity(intent);
			}
		});
		
	}
}
