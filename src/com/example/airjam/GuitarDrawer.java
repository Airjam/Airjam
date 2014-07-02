package com.example.airjam;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;

import com.xtr3d.extrememotion.api.Joint;
import com.example.airjam.R;

public class GuitarDrawer {
	
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

	public GuitarDrawer(int height, int width, Context cont){
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

	int currChord = 0;
	
	public void drawSkeleton(Canvas canvas, List<Joint> mJoints){
		int lH = 125;
		int lineHeight = 480 - lH;
		
		int divisions = 4;
		int xInt = mWidth / divisions;
		
		float shoulderCenterX = 0, shoulderCenterY=0, hipCenterX = 0, hipCenterY = 0; int lW = 100;

		int yInt = mHeight / divisions;
		
		s1 = MediaPlayer.create(context, R.raw.c_major);
		
		
		for (Joint joint : mJoints) 
		{
			float x = (joint.getPoint().getImgCoordNormHorizontal() * (float)mWidth);
			float y = (joint.getPoint().getImgCoordNormVertical() * (float)mHeight);
			
			int r = 0, g = 0, b = 0;					
			switch (joint.getJointType()) {
			case HandLeft:
				r = 118;
				g = 42;
				b = 131;
				mPoints[0] = x;
				mPoints[1] = y;
				
				if(x < lW){
					if(leftUnder == false && reiterate == true){
						leftUnder = true;
						reiterate = false;
					}
					
						System.out.println("----\nLEFT HAND HIT\n----");
						if(y > 0 && y < yInt){ //C MAJOR
							currChord = 0;
						}else if(y > yInt && y < 2*yInt){// A MINOR
							currChord = 1;
						}else if(y > 2*yInt && y < 3*yInt){ //G MAJOR
							currChord = 2;
						}else if(y > 3*yInt && y < 4*yInt){ //F MAJOR
							currChord = 3;
						}
					
//					System.out.println("LEFT HAND: Working...");
					reiterate = true;
				}else{
					leftUnder = false;
					reiterate = true;
				}
				
				//sound.release();
				mPaint.setARGB(255, r, g, b);
				canvas.drawCircle(x, y, 15, mPaint);
				break;
			case HandRight:
				r = 27;
				g = 120;
				b = 55;
				mPoints[8] = x;
				mPoints[9] = y;
				
				if(y > mHeight/(float) 1.5 && x > mWidth-3*lW){
					if(rightUnder == false && reiterate == true){
						rightUnder = true;
						reiterate = false;
					}
					if(rightUnder == true && reiterate == false){
							System.out.println("----\nRIGHT HAND HIT\n----");
							if(currChord == 0){
								s1 = MediaPlayer.create(context, R.raw.c_major);
								s1.start();
							}else if(currChord == 1){
								s1 = MediaPlayer.create(context, R.raw.a_minor);
								s1.start();
							}else if(currChord == 2){
								s1 = MediaPlayer.create(context, R.raw.g_major);
								s1.start();
							}else if(currChord == 3){
								s1 = MediaPlayer.create(context, R.raw.f_major);
								s1.start();
							}
							System.out.println(currChord);
					}
//					System.out.println("LEFT HAND: Working...");
					reiterate = true;
				}else{
					rightUnder = false;
					reiterate = true;
				}
				
				//sound.release();
				mPaint.setARGB(255, r, g, b);
				canvas.drawCircle(x, y, 15, mPaint);
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
