package com.thecamtech.android.library.drawable;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.thecamtech.android.library.util.PathHolder;
import com.thecamtech.android.library.view.DelightfulButton;

/**
 * Created by veasnasreng on 9/26/14.
 */
public class ReverseStatePath extends AbsOutline {

    private PathHolder[] PATH;

    private int mPathSrc;

    protected boolean mIsChecked;
    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Path mPath = new Path();
    protected int mFrameNumber;

    public ReverseStatePath(Context context, int path, int frameNumber, int duration) {
        super(context, duration);
        mPathSrc = path;
        mFrameNumber = frameNumber;
        mIsChecked = false;
        mLastIndex = mIsChecked ? mFrameNumber - 1 : 0;
        loadResource(mPathSrc);
    }

    @Override
    public ValueAnimator getPathAnimator() {
        if (PATH != null) {
            final int length = PATH.length - 1;
            if (mIsChecked) {
                return ValueAnimator.ofInt(Math.min(length, mLastIndex), 0).setDuration(getDuration());
            } else {
                return ValueAnimator.ofInt(Math.max(0, mLastIndex), length).setDuration(getDuration());
            }
        }
        return null;
    }

    @Override
    public boolean setState(int[] stateSet) {
        mIsChecked = containState(stateSet, DelightfulButton.CHECKED_STATE_SET);
        return super.setState(stateSet);
    }

    @Override
    public Paint getPaint() {
        return mPaint;
    }

    @Override
    public void toggle() {
        mIsChecked = !mIsChecked;
    }

    /**
     * Not running on main thread
     *
     * @param pathHolders
     */
    @Override
    protected void populatePath(int resId, PathHolder[] pathHolders) {
        PATH = pathHolders;
    }

    protected PathHolder[] getPath() {
        return PATH;
    }

    @Override
    public void draw(Canvas canvas) {
        if (PATH != null) {
            PathHolder pathHolder = PATH[mLastIndex];
            Rect rect = getBounds();
            pathHolder.draw(canvas, mPath, mPaint, mMatrix, rect.width(), rect.height());
        }
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return mPaint.getAlpha() / 255;
    }

}
