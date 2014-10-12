package com.thecamtech.android.library.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;

import com.thecamtech.android.library.util.PathHolder;
import com.thecamtech.android.library.util.PathParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by veasnasreng on 9/26/14.
 */
public abstract class AbsOutline extends Drawable {

    protected Context mContext;
    protected AnimatorSet mAnimatorSet;
    protected ValueAnimator mPathAnimator;
    protected ValueAnimator mColorSwitchAnimator;
    protected boolean mInAnimation;
    protected boolean mIsSwitchColor;
    protected int mLastIndex = 0;
    protected int mSwitchToColor;
    protected Matrix mMatrix = null;
    private Handler mHandler;
    private int mDuration;

    public AbsOutline(Context context, int duration) {
        mContext = context;
        mHandler = new Handler();
        mDuration = duration;
    }

    public void setValueAnimator(ValueAnimator valueAnimator) {
        mPathAnimator = valueAnimator;
        mLastIndex = (Integer) mPathAnimator.getAnimatedValue();
    }

    public int getDuration() {
        return mDuration;
    }

    public abstract ValueAnimator getPathAnimator();

    public abstract Paint getPaint();

    public void toggle() {
    }

    protected ValueAnimator getSwitchColorAnimator() {
        return ValueAnimator.ofObject(new ArgbEvaluator(), getPaint().getColor(), mSwitchToColor).setDuration(getDuration());
    }

    protected void loadResource(Integer... rawResources) {
        loadResource(mContext, this, rawResources);
    }

    /**
     * Not running on main thread
     *
     * @param pathHolders
     */
    protected void populatePath(int resId, PathHolder[] pathHolders) {
    }

    protected void populatePathReady() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateSelf();
            }
        });
    }

    public void startAnimation() {
        toggle();
        internalStartAnimation();
    }

    /**
     * To disable color animator, set it to -1
     *
     * @param color
     */
    public void setSwitchToColor(int color) {
        mIsSwitchColor = mSwitchToColor != color;
        mSwitchToColor = color;
    }

    private void internalStartAnimation() {
        if (mIsSwitchColor) {

            if (mAnimatorSet != null) {
                mAnimatorSet.cancel();
            } else {
                if (mPathAnimator != null) {
                    mPathAnimator.cancel();
                }
                if (mColorSwitchAnimator != null) {
                    mColorSwitchAnimator.cancel();
                }
            }

            mPathAnimator = getPathAnimator();
            mColorSwitchAnimator = getSwitchColorAnimator();

            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.setDuration(getDuration());

            setupPathAnimator();
            setupColorSwitchAnimator();

            mAnimatorSet.playTogether(mPathAnimator, mColorSwitchAnimator);
            mAnimatorSet.start();
            mIsSwitchColor = false;

        } else {
            if (mPathAnimator != null) {
                mPathAnimator.cancel();
            }

            mPathAnimator = getPathAnimator();
            setupPathAnimator();
            mPathAnimator.start();
        }
    }

    private void setupColorSwitchAnimator() {
        mColorSwitchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                getPaint().setColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        mColorSwitchAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                getPaint().setColor((Integer) mColorSwitchAnimator.getAnimatedValue());
                mColorSwitchAnimator = null;
            }
        });
    }

    private void setupPathAnimator() {
        mPathAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setValueAnimator(valueAnimator);
                invalidateSelf();
            }
        });
        mPathAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPathAnimator = null;
                invalidateSelf();
                mInAnimation = false;
            }
        });
        mInAnimation = true;
    }

    // =========================================================

    protected static class LoadResource extends AsyncTask<BufferedReader, Void, Void> {

        private int res;
        private PathHolder[] pathHolders;
        private AbsOutline outline;

        @Override
        protected Void doInBackground(BufferedReader... params) {
            try {
                String line;
                BufferedReader bufferedReader = params[0];
                for (int index = 1; index < 20; index++) {
                    if ((line = bufferedReader.readLine()) != null) {
                        pathHolders[index] = PathParser.parsePath(line);
                    }
                }
                bufferedReader.close();
                outline.populatePath(res, pathHolders);
            } catch (Exception ex) {
            }
            return null;
        }
    }

    protected static void loadResource(Context context, AbsOutline outline, Integer... params) {
        for (int res : params) {
            if (res != 0) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(res)));
                    PathHolder[] pathHolders = new PathHolder[21];

                    pathHolders[20] = PathParser.parsePath(bufferedReader.readLine());
                    pathHolders[0] = PathParser.parsePath(bufferedReader.readLine());

                    outline.populatePath(res, pathHolders);
                    outline.populatePathReady();

                    LoadResource loadResource = new LoadResource();
                    loadResource.res = res;
                    loadResource.pathHolders = pathHolders;
                    loadResource.outline = outline;
                    loadResource.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bufferedReader);

                } catch (Exception ex) {
                }
            }
        }
    }

}
