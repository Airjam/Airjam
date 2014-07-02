package com.example.airjam;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;

import com.xtr3d.extrememotion.api.Joint;
import com.example.airjam.R;

public class DrumDrawer {
	
	private int mHeight;
	private int mWidth;
	private Paint mPaint;
	private int mNumOfCoordinates = 32;
	private float mPoints[] = new float[mNumOfCoordinates];
	static Context context;
	static MediaPlayer s1;
	
	//0-3: left hand to left elbow
	//4-7: left elbow to left shoulder
	//8-11: right hand to right elbow
	//12-15: right elbow to right shoulder
	//16-19: left shoulder to center
	//20-23: right shoulder to center
	//24-27: head to shoulder center
	//28-31: shoulder center to hip center

	public DrumDrawer(int height, int width, Context cont){
		mHeight = height;
		mWidth = width;
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setAntiAlias(true);
		context=cont;
	}
	
	boolean leftUnder = false;
	boolean rightUnder = false;
	boolean reiterate = true;

	public void drawSkeleton(Canvas canvas, List<Joint> mJoints){
		int lH = 125;
		int lineHeight = 480 - lH;
		
		int divisions = 4;
		int xInt = mWidth / divisions;
		
		float shoulderCenterX = 0, shoulderCenterY=0, hipCenterX = 0, hipCenterY = 0;
		for (Joint joint : mJoints) 
		{
			float x = (joint.getPoint().getImgCoordNormHorizontal() * (float)mWidth);
			float y = (joint.getPoint().getImgCoordNormVertical() * (float)mHeight);
			
			int r = 0, g = 0, b = 0;					
			// select a high-contrast color scheme for the currently available joints
			switch (joint.getJointType()) {
			case HandLeft:
				r = 118;
				g = 42;
				b = 131;
				mPoints[0] = x;
				mPoints[1] = y;
				
				if(y > lineHeight){
					if(leftUnder == false && reiterate == true){
						leftUnder = true;
						reiterate = false;
					}
					if(leftUnder == true && reiterate == false){
						System.out.println("----\nLEFT HAND HIT\n----");
						if(x > 0 && x < xInt){
							s1 = MediaPlayer.create(context, R.raw.bass);
							s1.start();
						}else if(x > xInt && x < 2*xInt){
							s1 = MediaPlayer.create(context, R.raw.ding);
							s1.start();
						}else if(x > 2*xInt && x < 3*xInt){
							s1 = MediaPlayer.create(context, R.raw.cymbal);
							s1.start();
						}else if(x > 3*xInt && x < 4*xInt){
							s1 = MediaPlayer.create(context, R.raw.snare);
							s1.start();
						}
					}
//					System.out.println("LEFT HAND: Working...");
					reiterate = true;
				}else{
					leftUnder = false;
					reiterate = true;
				}
				
				break;
			case HandRight:
				r = 27;
				g = 120;
				b = 55;
				mPoints[8] = x;
				mPoints[9] = y;
				
				if(y > lineHeight){
					if(rightUnder == false && reiterate == true){
						rightUnder = true;
						reiterate = false;
					}
					if(rightUnder == true && reiterate == false){
						System.out.println("----\nRIGHT HAND HIT\n----");
						if(x > 0 && x < xInt){
							s1 = MediaPlayer.create(context, R.raw.bass);
							s1.start();
						}else if(x > xInt && x < 2*xInt){
							s1 = MediaPlayer.create(context, R.raw.ding);
							s1.start();
						}else if(x > 2*xInt && x < 3*xInt){
							s1 = MediaPlayer.create(context, R.raw.cymbal);
							s1.start();
						}else if(x > 3*xInt && x < 4*xInt){
							s1 = MediaPlayer.create(context, R.raw.snare);
							s1.start();
						}
					}
//					System.out.println("LEFT HAND: Working...");
					reiterate = true;
				}else{
					rightUnder = false;
					reiterate = true;
				}
				
				break;				
			default:
				break;
			}
			mPaint.setARGB(255, r, g, b);
			canvas.drawCircle(x, y, 15, mPaint);
		}
		//raise the hip point a little bit
		float newHipCenterX = (float)(hipCenterX + 0.4*(shoulderCenterX-hipCenterX));
		float newHipCenterY = (float)(hipCenterY + 0.4*(shoulderCenterY-hipCenterY));
		mPoints[30] = newHipCenterX;
		mPoints[31] = newHipCenterY;
		mPaint.setARGB(255, 255, 127, 36);
		canvas.drawCircle(newHipCenterX, newHipCenterY, 15, mPaint);

	}
}
