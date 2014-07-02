package com.example.airjam;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import android.util.Log;

import com.example.airjam.ExtremeMotionUtils.NewFrameReadyListener;
import com.xtr3d.extrememotion.api.BaseFrame;
import com.xtr3d.extrememotion.api.ExtremeMotionGenerator;
import com.xtr3d.extrememotion.api.RawImageFrame;
import com.xtr3d.extrememotion.api.Skeleton;
import com.xtr3d.extrememotion.api.SkeletonFrame;
import com.xtr3d.extrememotion.api.StreamType;
import com.xtr3d.extrememotion.api.WarningType;
import com.xtr3d.extrememotion.api.WarningsFrame;

/**
 * The ExtremeMotionGenerator provides API for blocking-wait untill an input arrives.
 * In this class, we create a seperate thread which uses that API and a-synchronically call the NewFrameReadyListener
 * each time a new rgb-image/skeleton arrives.
 * @author assafl
 *
 */
public class AsyncFrameFetcher implements Runnable{	

	private static final String TAG = "EXTREME_MOTION";
	
	/** used to stop the infinite while loop*/ 
	private AtomicBoolean mRunThread  = new AtomicBoolean(true);
	private ExtremeMotionGenerator mEMGenerator;
	private ExtremeMotionUtils.NewFrameReadyListener mNewFrameReadyListener;
	private AtomicReference<FrameInfo> mFrameInfo = new AtomicReference<FrameInfo>();
	private int TIME_OUT_MILLIS = 3000;
	

	public AsyncFrameFetcher(ExtremeMotionGenerator emGenerator, NewFrameReadyListener newFrameReadyListener)
	{
		mEMGenerator = emGenerator;
		mNewFrameReadyListener = newFrameReadyListener;
	}
	
	public void shutdown()
	{
		mRunThread.set(false);
	}
	
	public FrameInfo getLatestFrameInfo()
	{
		return mFrameInfo.get();
	}
	
	
	@Override
	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(mRunThread.get()){
			List<BaseFrame> baseFrameList = null;
			try {
			    // Wait for synchronized frames from streams: Raw Image & Skeleton. Once all frames are received, they are locked until released.
		        // Note that in case the skeleton is not needed to be drawn (e.g. calibration stage), using WaitForAnyFrame will result in better performance.
				baseFrameList = mEMGenerator.waitForAllFrames(EnumSet.of(StreamType.RAW_IMAGE,StreamType.SKELETON,StreamType.WARNINGS), TIME_OUT_MILLIS);
			} catch (Exception e) {
				Log.e(TAG, "waitForAllFrames threw an exception: " + e.toString());
			}
			
			Skeleton skeleton = null;
			byte[] rgb = new byte[0];
			List<WarningType> warningsList = null;
			
			if(baseFrameList != null){
				for(BaseFrame frame: baseFrameList){
					if(frame.getStreamType() ==StreamType.SKELETON){
						skeleton = ((SkeletonFrame)frame).getSkeletons().get(0);
					}
					else if(frame.getStreamType() == StreamType.RAW_IMAGE){
						rgb = ((RawImageFrame)frame).getImageBytes();
					}
					else if(frame.getStreamType() == StreamType.WARNINGS){
						warningsList = ((WarningsFrame)frame).getWarningsList();
					}
				}
				mFrameInfo.set(new FrameInfo(skeleton, rgb, warningsList));
				
				if (mNewFrameReadyListener!=null){
					mNewFrameReadyListener.onNewFrameReady(getLatestFrameInfo());
				}
			}
		}			
	}
}