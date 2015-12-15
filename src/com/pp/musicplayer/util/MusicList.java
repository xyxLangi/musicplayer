package com.pp.musicplayer.util;

import java.util.ArrayList;
import java.util.List;

import com.pp.musicplayer.domain.Music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MusicList {

	public static List<Music> getMusicData(Context context){
		List<Music> musicList = new ArrayList<Music>();
		ContentResolver cr = context.getContentResolver();
		if (cr != null) {
			//获取所有音乐
			Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
					null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			if (cursor == null) {
				return null;
			}
			if (cursor.moveToFirst()) {
				do {
					Music m = new Music();
					String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
					String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
					if ("<unknown>".equals(artist)) {
						artist = "不详";
					}
					String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
					String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
					String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
					long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
					long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
					String sbr = name.substring(name.length() - 3, name.length());
					if (sbr.equals("mp3")) {
						m.setAlbum(album);
						m.setArtist(artist);
						m.setName(name);
						m.setSize(size);
						m.setTime(time);
						m.setTitle(title);
						m.setUrl(url);
						musicList.add(m);
						
					}
				} while (cursor.moveToNext());
			}
		}
		return musicList;
	}
}
