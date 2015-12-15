package com.pp.musicplayer.util;

import java.util.ArrayList;
import java.util.List;

import com.pp.musicplayer.util.LrcProcess.LrcContent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义歌词
 */
public class LrcView extends TextView {

	private float width;
	private float high;
	private Paint CurrentPaint;
	private Paint NotCurrentPaint;
	private float TextHigh = 25;
	private float TextSize = 18;
	private int Index = 0;
	
	private List<LrcContent> mSentenceEntities = new ArrayList<LrcContent>();
	
	public void setSentenceEntities(List<LrcContent> mSentenceEntities){
		this.mSentenceEntities = mSentenceEntities;
	}
	
	
	
	public LrcView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs , defStyle);
		init();
	}
	
	public LrcView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		init();
	}
	
	public LrcView(Context context){
		super(context);
		init();
	}
	
	private void init(){
		setFocusable(true);
		
		//高亮部分
		CurrentPaint = new Paint();
		CurrentPaint.setAntiAlias(true);
		CurrentPaint.setTextAlign(Paint.Align.CENTER);
		
		//非高亮部分
		NotCurrentPaint = new Paint();
		NotCurrentPaint.setAntiAlias(true);
		NotCurrentPaint.setTextAlign(Paint.Align.CENTER);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (canvas == null) {
			return;
		}
		CurrentPaint.setColor(Color.argb(210, 251, 248, 29));
		NotCurrentPaint.setColor(Color.argb(140, 255, 255, 255));
		
		CurrentPaint.setTextSize(24);
		CurrentPaint.setTypeface(Typeface.SERIF);
		
		NotCurrentPaint.setTextSize(TextSize);
		NotCurrentPaint.setTypeface(Typeface.DEFAULT);
		
		try {
			setText("");
			canvas.drawText(mSentenceEntities.get(Index).getLrc(), width / 2,
					high / 2, CurrentPaint);
			float tempY = high / 2;
			//画出本句前的句子
			for (int i = Index - 1; i >= 0; i--) {
				//向上推移
				tempY = tempY - TextHigh;
				canvas.drawText(mSentenceEntities.get(i).getLrc(), width / 2,
						tempY, CurrentPaint);
			}
			tempY = high / 2;
			//画出本句之后的句子
			for (int i = Index + 1; i < mSentenceEntities.size(); i++) {
				tempY = tempY + TextHigh;
				canvas.drawText(mSentenceEntities.get(i).getLrc(), width / 2,
						tempY, NotCurrentPaint);
			}
		} catch (Exception e) {
			// TODO: handle exception
			setText("..没有歌词,去下载吧..");
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		this.width = w;
		this.high = h;
		
	}
	public void SetIndex(int index){
		this.Index = index;
	}

}
