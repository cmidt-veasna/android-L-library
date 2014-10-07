package com.thecamtech.android.library.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.thecamtech.android.library.util.PathHolder;

/**
 * Created by veasnasreng on 9/26/14.
 */
public class CheckMarkPath extends ReverseStatePath {

    private int mDrawableSize;
    private int mPaddingWithText;
    private int mFrameNumber;

    public CheckMarkPath(Context context, int path, int drawableSize, int padding, int frameNumber, int duration) {
        super(context, path, frameNumber, duration);
        mDrawableSize = drawableSize;
        mPaddingWithText = padding;
        mFrameNumber = frameNumber;
        mIsChecked = false;
        mLastIndex = mIsChecked ? mFrameNumber - 1 : 0;
    }

    @Override
    public int getDuration() {
        return 300;
    }

    public void invalidateSelf() {
        if (mInAnimation) {
            super.invalidateSelf();
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mDrawableSize + mPaddingWithText;
    }

    @Override
    public int getIntrinsicHeight() {
        return mDrawableSize;
    }

    @Override
    public void draw(Canvas canvas) {
        PathHolder[] path = getPath();
        if (path != null && path[mLastIndex] != null) {
            Rect rect = getBounds();
            rect.right -= mPaddingWithText;
            path[mLastIndex].draw(canvas, mPath, mPaint, null, rect.width(), rect.height());
        }
    }

}

