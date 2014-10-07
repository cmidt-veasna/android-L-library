package com.thecamtech.android.library.drawable;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.thecamtech.android.library.util.PathHolder;

/**
 * Created by veasnasreng on 9/26/14.
 */
public class DoubleStatePath extends ReverseStatePath {

    private PathHolder[] LAST_PATH;

    private int mLastPath;

    public DoubleStatePath(Context context, int first, int last, int frameNumber, int duration) {
        super(context, first, frameNumber, duration);
        mLastPath = last;
        loadResource(mLastPath);
    }

    @Override
    public ValueAnimator getPathAnimator() {
        final int length;
        if (getPath() != null && LAST_PATH != null) {
            length = (mIsChecked) ? getPath().length - 1 : LAST_PATH.length - 1;
        } else {
            length = 0;
        }
        return ValueAnimator.ofInt(0, length).setDuration(getDuration());
    }

    /**
     * Not running on main thread
     *
     * @param pathHolders
     */
    @Override
    protected void populatePath(int resId, PathHolder[] pathHolders) {
        if (resId == mLastPath) {
            LAST_PATH = pathHolders;
        } else {
            super.populatePath(resId, pathHolders);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (getPath() != null && LAST_PATH != null) {
            final PathHolder[] pathHolders = (mInAnimation ^ mIsChecked) ? getPath() : LAST_PATH;
            final int index = mInAnimation ? (Integer) mPathAnimator.getAnimatedValue() : 0;
            PathHolder pathHolder = pathHolders[index];
            Rect rect = getBounds();
            pathHolder.draw(canvas, mPath, mPaint, mMatrix, rect.width(), rect.height());
        }
    }

}
