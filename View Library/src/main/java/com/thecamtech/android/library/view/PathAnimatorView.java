package com.thecamtech.android.library.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.thecamtech.android.library.R;
import com.thecamtech.android.library.util.PathHolder;
import com.thecamtech.android.library.util.PathParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by veasnasreng on 9/23/14.
 */
public class PathAnimatorView extends ImageView {

    private int mRawResource;
    private int mMinDuration;
    private int mFrameNumber;
    private boolean mIsAutoStart;
    private boolean mIsRepeat;
    private boolean mIsReverse;
    private Handler mHandler;
    private SvgAnimateDrawable mDrawable;
    private AnimationListener mAnimationListener;

    public PathAnimatorView(Context context) {
        super(context);
        init(null, 0);
    }

    public PathAnimatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PathAnimatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PathAnimatorView, defStyleAttr, 0);
            mRawResource = a.getResourceId(R.styleable.PathAnimatorView_rawResource, 0);
            mMinDuration = a.getInteger(R.styleable.PathAnimatorView_minDuration, 300);
            mFrameNumber = a.getInteger(R.styleable.PathAnimatorView_frameNumber, 0);
            mIsAutoStart = a.getBoolean(R.styleable.PathAnimatorView_autoStart, false);
            mIsRepeat = a.getBoolean(R.styleable.PathAnimatorView_isRepeat, false);
            mIsReverse = a.getBoolean(R.styleable.PathAnimatorView_isReverse, false);
            a.recycle();

            mDrawable = new SvgAnimateDrawable();
            setImageDrawable(mDrawable);
        }
        mHandler = new Handler();
    }

    private void resourceLoaded() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mAnimationListener != null) {
                    mAnimationListener.onResourceLoaded();
                }
                if (mIsAutoStart) {
                    mDrawable.startAnimation();
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawable != null) {
            mDrawable.stopAnimation();
        }
    }

    public void startPathAnimation() {
        mDrawable.startAnimation();
    }

    public void setAnimationListener(AnimationListener animationListener) {
        mAnimationListener = animationListener;
    }

    public static class AnimationListener {
        public void onResourceLoaded() {
        }

        public void onAnimationUpdate(int frame) {
        }

        public void onAnimationStart() {
        }

        public void onAnimationEnd() {
        }
    }

    private class SvgAnimateDrawable extends Drawable {

        private PathHolder[] PATH;
        private Paint mPaint = new Paint();
        private Path mPath = new Path();
        private boolean mInAnimation;
        private boolean mIsReady;
        private ValueAnimator mValueAnimator;
        private int mIndex = 0;

        private SvgAnimateDrawable() {
            if (mRawResource != 0) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getContext().getResources().openRawResource(mRawResource)));

                    PathHolder pathHolder = PathParser.parsePath(bufferedReader.readLine());

                    LoadResource loadResource = new LoadResource();
                    loadResource.PATH = PATH = new PathHolder[mFrameNumber];
                    loadResource.PATH[0] = pathHolder;
                    loadResource.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bufferedReader);

                } catch (Exception ex) {
                }
            }
            mPaint.setAntiAlias(true);
            mIsReady = false;
        }

        private boolean stopAnimation() {
            if (mValueAnimator != null) {
                mValueAnimator.cancel();
            }
            return false;
        }

        private boolean startAnimation() {
            if (mValueAnimator != null) {
                return false;
            }

            mValueAnimator = ValueAnimator.ofInt(0, PATH.length - 1).setDuration(mMinDuration);
            if (mIsRepeat) {
                mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                mValueAnimator.setRepeatMode(mIsReverse ? ValueAnimator.REVERSE : ValueAnimator.RESTART);
            }

            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mIndex = (Integer) animation.getAnimatedValue();
                    invalidateSelf();
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationUpdate(mIndex);
                    }
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationEnd();
                    }
                    if (!mIsRepeat) {
                        mInAnimation = false;
                        mValueAnimator = null;
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationStart();
                    }
                }
            });
            mInAnimation = true;
            mIsReady = true;
            mValueAnimator.start();
            return true;
        }

        @Override
        public void draw(Canvas canvas) {
            if (PATH != null && mIsReady) {
                PathHolder pathHolder = PATH[mIndex];
                if (pathHolder != null) {
                    Rect rect = getBounds();
                    pathHolder.draw(canvas, mPath, mPaint, true, null, rect.width(), rect.height());
                }
            }
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

    private class LoadResource extends AsyncTask<BufferedReader, Void, Void> {

        private PathHolder[] PATH;

        @Override
        protected Void doInBackground(BufferedReader... params) {
            try {
                String line;
                BufferedReader bufferedReader = params[0];
                for (int index = 1; index < mFrameNumber; index++) {
                    if ((line = bufferedReader.readLine()) != null) {
                        PATH[index] = PathParser.parsePath(line);
                    }
                }
                bufferedReader.close();
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            resourceLoaded();
        }
    }
}
