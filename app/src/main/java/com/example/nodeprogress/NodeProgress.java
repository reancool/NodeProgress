package com.example.nodeprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
//http://www.mamicode.com/info-detail-1017561.html
public class NodeProgress extends View{

	private int nodesNum ;
	private Drawable progressingDrawable;
	private Drawable unprogressingDrawable;
	private Drawable progresFailDrawable;
	private Drawable progresSuccDrawable;
	private int nodeRadius;
	private int processingLineColor;

//	private int progressLineHeight;
	private int currNodeNO;
	private int currNodeState;
//	private int textSize;
	Context mContext;

	int mWidth,mHeight;
	private Paint mPaint;
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private ArrayList<Node> nodes;

	private int DEFAULT_LINE_COLOR = Color.BLUE;
	public NodeProgress(Context context) {
		this(context,null);

	}
	public NodeProgress(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}
	public NodeProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,R.styleable.MutiProgress);
		nodesNum = mTypedArray.getInteger(R.styleable.MutiProgress_nodesNum, 1);
		nodeRadius = mTypedArray.getDimensionPixelSize(R.styleable.MutiProgress_nodeRadius, 10);
		progressingDrawable = mTypedArray.getDrawable(R.styleable.MutiProgress_progressingDrawable);
		unprogressingDrawable = mTypedArray.getDrawable(R.styleable.MutiProgress_unprogressingDrawable);
		progresFailDrawable = mTypedArray.getDrawable(R.styleable.MutiProgress_progresFailDrawable);
		progresSuccDrawable = mTypedArray.getDrawable(R.styleable.MutiProgress_progresSuccDrawable);
		processingLineColor = mTypedArray.getColor(R.styleable.MutiProgress_processingLineColor, DEFAULT_LINE_COLOR);
		currNodeState = mTypedArray.getInt(R.styleable.MutiProgress_currNodeState, 1);
		currNodeNO = mTypedArray.getInt(R.styleable.MutiProgress_currNodeNO, 1);

	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		
		mBitmap = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		mPaint = new Paint();
		mPaint.setColor(processingLineColor);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mCanvas = new Canvas(mBitmap);
		
		nodes = new ArrayList<Node>();
		float nodeWidth = ((float)mWidth)/(nodesNum-1);
		for(int i=0;i<nodesNum;i++)
		{
			Node node = new Node();
			if(i==0)
				node.mPoint = new Point(((int)nodeWidth*i),mHeight/2-nodeRadius);
			else if(i==(nodesNum-1))
				node.mPoint = new Point(((int)nodeWidth*i)-nodeRadius*2,mHeight/2-nodeRadius);
			else
				node.mPoint = new Point(((int)nodeWidth*i)-nodeRadius,mHeight/2-nodeRadius);
			if(currNodeNO == i)
				node.type = 1;
			else
				node.type = 0;
			nodes.add(node);
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		DrawProgerss();
		Log.v("ondraw", "mBitmap="+mBitmap);
		if(mBitmap!=null)
		{
			canvas.drawBitmap(mBitmap, new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight()), new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight()), mPaint);
		}
		for(int i=0;i<nodes.size();i++)
		{
			Node node = nodes.get(i);
			Log.v("ondraw", node.mPoint.x +";y="+node.mPoint.y);
			if(i<currNodeNO)
			{
				progressingDrawable.setBounds(node.mPoint.x,  node.mPoint.y , node.mPoint.x + nodeRadius*2,node.mPoint.y + nodeRadius*2);
				progressingDrawable.draw(canvas);
			}
			else if(i==currNodeNO)
			{
				if(currNodeState == 1)
				{
					progresSuccDrawable.setBounds(node.mPoint.x,  node.mPoint.y , node.mPoint.x + nodeRadius*2,node.mPoint.y + nodeRadius*2);
					progresSuccDrawable.draw(canvas);
				}
				else
				{
					progresFailDrawable.setBounds(node.mPoint.x,  node.mPoint.y , node.mPoint.x + nodeRadius*2,node.mPoint.y + nodeRadius*2);
					progresFailDrawable.draw(canvas);
				}
			}
			else
			{
				unprogressingDrawable.setBounds(node.mPoint.x,  node.mPoint.y , node.mPoint.x + nodeRadius*2,node.mPoint.y + nodeRadius*2);
				unprogressingDrawable.draw(canvas);
			}
		}
	}
	private void DrawProgerss()
	{
		Paint bgPaint = new Paint();
		bgPaint.setColor(Color.parseColor("#f0f0f0"));
		mCanvas.drawRect(0, 0, mWidth, mHeight, bgPaint);
		mPaint.setStrokeWidth(nodeRadius/2);
//		mCanvas.drawLine(nodeRadius, mHeight/2, mWidth-nodeRadius, mHeight/2, mPaint);  //�߶�2��ȥ��nodeRadius
		mCanvas.drawLine(nodeRadius, mHeight/2, nodes.get(currNodeNO).mPoint.x + nodeRadius, nodes.get(currNodeNO).mPoint.y + nodeRadius, mPaint);  //�߶�2��ȥ��nodeRadius
		mPaint.setColor(Color.parseColor("#dddddd"));
		mCanvas.drawLine(nodes.get(currNodeNO).mPoint.x +nodeRadius, nodes.get(currNodeNO).mPoint.y + nodeRadius, mWidth-nodeRadius, mHeight/2, mPaint);  //�߶�2��ȥ��nodeRadius
	}
	class Node
	{
		Point mPoint;
		int type;
	}
}
