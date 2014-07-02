package com.example.airjam;

import java.util.List;

import com.xtr3d.extrememotion.api.Skeleton;
import com.xtr3d.extrememotion.api.WarningType;

/**
 * Contains the extreme-motion data of a full single-frame, including the source rgb-image, 
 * the skeleton data, and warnings.
 * @author assafl
 */
public class FrameInfo
{
	Skeleton mSkeleton;
	byte[] mRgbImage;
	List<WarningType> mWarningsList;
	
	public Skeleton getSkeleton() {
		return mSkeleton;
	}
	
	public byte[] getRgbImage() {
		return mRgbImage;
	}
	
	public List<WarningType> getWarningsList() {
		return mWarningsList;
	}
	
	protected FrameInfo(Skeleton skeleton, byte[] rgbImage, List<WarningType> warningsList) {
		super();
		mSkeleton = skeleton;
		mRgbImage = rgbImage;
		mWarningsList = warningsList;
	}
}