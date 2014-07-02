package com.example.airjam;

public class FrameRateCalculator 
{
	private long mTotalFrames =0;
	private int mFrameCounter =0;
    private int mCalculationInterval = 2500;		// Minimum time between frame rate calculations
    private double mFrameRate = 0.0;						// Calculated frame rate
    private long mPrevCalculationTime = System.currentTimeMillis();	// Last frame rate calculation time

    public void sample()
    {
    	mTotalFrames++;
		mFrameCounter++;
		
		long currFrameTime = System.currentTimeMillis();
		long currentInterval = currFrameTime - mPrevCalculationTime;
		if(currentInterval >= mCalculationInterval)
		{
			// Calculate frame rate: frames/interval time
			mFrameRate =  (double)(mFrameCounter*1000)/(double)currentInterval;
			mPrevCalculationTime = currFrameTime;
			mFrameCounter = 0;
		}
    }
    /** 
     * @return frame-rate over time. The returned value will be changed once every mCalculationInterval (few seconds)
     */
    public double getLatestFrameRateOverTime()
    {
    	return mFrameRate;
    }
    public long getTotalCount()
    {
    	return mTotalFrames;
    }
    
    
}