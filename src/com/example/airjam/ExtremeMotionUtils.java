package com.example.airjam;

import java.util.EnumSet;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.xtr3d.extrememotion.api.ExtremeMotionGenerator;
import com.xtr3d.extrememotion.api.StreamType;

/**
 * Utility class with two functions:
 * 1. Inits the AsyncFrameFetcher, which registers a listener (NewFrameReadyListener) to be waked-up 
 * each time a new rgb/skeleton data is found in the sdk.
 * 2. Life-cycle methods which should be called on the activity onCreate,onResume,onStop,onDestroy
 * 
 * @author assafl
 *
 */
public class ExtremeMotionUtils {

	private static final String TAG = "EXTREME_MOTION";
	
	private Thread mFetchFrameThread;
	private AsyncFrameFetcher mAsyncFrameFetcher;
	private NewFrameReadyListener mNewFrameReadyListener;
	private boolean isResumed= false;
	private EnumSet<StreamType> mStreamTypeSet;

	private static ExtremeMotionUtils instance = new ExtremeMotionUtils();
	private ExtremeMotionUtils()
	{
		 //private constructor for singleton pattern
	}
	public static ExtremeMotionUtils getInstance() 
	{
		return instance;
	}
	
	/**
	 * should be called once per application life.
	 * @param activity
	 * @param newFrameReadyListener
	 * @return a View which must be added to the layout.
	 */
	public View onCreate(final Activity activity, final NewFrameReadyListener newFrameReadyListener)
	{
		mStreamTypeSet = EnumSet.of(StreamType.RAW_IMAGE,StreamType.SKELETON, StreamType.WARNINGS);
		View[] cameraView = new View[1];
		  try {
			  ExtremeMotionGenerator.getInstance().initialize(mStreamTypeSet, activity, cameraView);
		  } catch (Exception exception) {
			  Log.e(TAG, "initialize was not successful, exception of type: " + exception.getClass().toString());
			  return null;
		  } 
		  mNewFrameReadyListener = newFrameReadyListener;
		  return cameraView[0];
	}
	
	
	private void startAsyncFetcherSeperateThread()
	{
		mAsyncFrameFetcher = new AsyncFrameFetcher(ExtremeMotionGenerator.getInstance(), mNewFrameReadyListener);
		mFetchFrameThread = new Thread(mAsyncFrameFetcher);
		mFetchFrameThread.start();
	}
	
	
	public synchronized void onDestroy() {
		if(mAsyncFrameFetcher != null)
		{
			mAsyncFrameFetcher.shutdown();
		}
		ExtremeMotionGenerator.getInstance().shutdown();
	}
	
	public synchronized void onPause() {
		isResumed= false;
		mAsyncFrameFetcher.shutdown();
		try {
			ExtremeMotionGenerator.getInstance().stopStreams(mStreamTypeSet);
		} catch (Exception e) {
			Log.e(TAG, "Error stopping streams: " + e.toString());
		}
	}
	
	public synchronized void onResume() {
		if (isResumed)
			return; //onResume can be called twice in a row without onStop in the middle, for example: on SamsungNote3 when clicking power-button twice.
		else 
			isResumed = true;
		
		try {
			ExtremeMotionGenerator.getInstance().startStreams(mStreamTypeSet);
		} catch (Exception e) {
			Log.e(TAG, "Error starting streams: " + e.toString());
			return;
		}
		startAsyncFetcherSeperateThread();
	}
	
	//Note: this method can be called in a non-main thread
	public synchronized void reset() { 
		onPause();
		onResume();
	}
	
	public FrameInfo getLatestFrameInfo()
	{
		return mAsyncFrameFetcher.getLatestFrameInfo();
	}
	
	public interface NewFrameReadyListener
	{
		public void onNewFrameReady(FrameInfo newFrameInfo);
	}	
	
}
