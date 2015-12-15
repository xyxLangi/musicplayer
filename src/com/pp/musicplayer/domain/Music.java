package com.pp.musicplayer.domain;

public class Music {

	private String title;
	private String artist;
	private String album;
	private String url;
	private String name;
	private long size;
	private long time;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getArtist(){
		return artist;
	}
	
	public void setArtist(String artist){
		this.artist = artist;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getAlbum(){
		return album;
	}
	
	public void setAlbum(String album){
		this.album = album;
	}
	
	public long getSize(){
		return size;
	}
	
	public void setSize(long size){
		this.size = size;
	}
	
	public long getTime(){
		return time;
	}
	
	public void setTime(long time){
		this.time = time;
	}
}
