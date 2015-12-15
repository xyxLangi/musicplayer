package com.pp.musicplayer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class LrcProcess {

	private LrcContent mLrcContent;
	private List<LrcContent> LrcList;
	
	public LrcProcess(){
		mLrcContent = new LrcContent();
		LrcList = new ArrayList<LrcContent>();
	}
	
	/**
	 * 读取歌词文件的内容
	 */
	public String readLRC(String song_path){
		
		StringBuilder stringBuilder = new StringBuilder();
		
		File f = new File(song_path.replace(".mp3", ".lrc"));
		try {
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis,"GB2312");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				//替换字符
				s = s.replace("[", "");
				s = s.replace("]", "@");
				
				String splitLrc_data[] = s.split("@");
				if (splitLrc_data.length > 1) {
					mLrcContent.setLrc(splitLrc_data[1]);
					//处理歌词取得歌曲时间
					int LrcTime = TimeStr(splitLrc_data[0]);
					mLrcContent.setLrc_time(LrcTime);
					LrcList.add(mLrcContent);
					mLrcContent = new LrcContent();
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			stringBuilder.append("没有歌词文件");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			stringBuilder.append("没有读取到歌词");
		}
		return stringBuilder.toString();
		
		
	}
	
	public int TimeStr(String timeStr){
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		
		String timeData[] = timeStr.split("@");
		
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);
		
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		
		return currentTime;
	}
	
	public List<LrcContent> getLrcContet(){
		return LrcList;
	}
	
	public class LrcContent{
		private String Lrc;
		private int Lrc_time;
		
		public String getLrc(){
			return Lrc;
		}
		
		public void setLrc(String Lrc){
			this.Lrc = Lrc;
		}
		
		public int getLrc_time(){
			return Lrc_time;
		}
		
		public void setLrc_time(int Lrc_time){
			this.Lrc_time = Lrc_time;
		}
	}
}
