package com.thecamtech.librarysample.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.thecamtech.librarysample.R;

/**
 * Created by veasnasreng on 10/11/14.
 */
public class ImageFrameLayout extends FrameLayout implements Hoverable {

    private View mContainerHover;
    private Frame mFrame;
    private float mFramePadding;
    private ImageView mImageView;
    private OnHoverViewClickListener mOnHoverViewClickListener;

    public ImageFrameLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public ImageFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ImageFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageFrameLayout);

            final int layout = a.getResourceId(R.styleable.ImageFrameLayout_layout, 0);
            mFramePadding = a.getDimension(R.styleable.ImageFrameLayout_framePadding, 0f);
            final Drawable drawable = a.getDrawable(R.styleable.ImageFrameLayout_android_src);

            a.recycle();

            if (drawable != null) {
                mImageView = new ImageView(getContext());
                mImageView.setImageDrawable(drawable);
                mImageView.setAdjustViewBounds(true);

                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                addView(mImageView, params);

                if (layout != 0) {
                    mContainerHover = LayoutInflater.from(getContext()).inflate(layout, this, false);
                    mContainerHover.setVisibility(View.GONE);
                    mFrame = new Frame(getContext());
                    mFrame.setAlpha(0);

                    params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    addView(mFrame, params);

                    params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER;
                    addView(mContainerHover, params);

                    if (mContainerHover instanceof ViewGroup) {
                        ViewGroup viewGroup = (ViewGroup) mContainerHover;
                        final OnClickListener onClickListener = new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mOnHoverViewClickListener != null) {
                                    mOnHoverViewClickListener.onHoverViewClick(getId(), v.getId());
                                }
                            }
                        };
                        for (int i = 0; i < viewGroup.getChildCount(); i++) {
                            viewGroup.getChildAt(i).setOnClickListener(onClickListener);
                        }
                    }
                }
            }
        }
    }

    public void setOnHoverViewClickListener(OnHoverViewClickListener onHoverViewClickListener) {
        mOnHoverViewClickListener = onHoverViewClickListener;
    }

    @Override
    public void onTouchHover(float x, float y) {
        removeCallbacks(mHideHover);
        if (mContainerHover.getVisibility() == View.GONE) {
            mFrame.startAnimation(true);
            mContainerHover.setTranslationY(mContainerHover.getHeight());
            mContainerHover.setAlpha(0f);
            mContainerHover.setVisibility(View.VISIBLE);
            mContainerHover.animate().alpha(1f).translationY(0);
        }
    }

    @Override
    public void onTouchRelease(float x, float y) {
        postDelayed(mHideHover, 1800);
    }

    @Override
    public void dismiss(boolean animate, final Runnable endAcRunnable) {
        if (animate) {
            mFrame.startAnimation(false);
            mContainerHover.animate().alpha(0f).translationY(mContainerHover.getHeight()).withEndAction(new Runnable() {
                @Override
                public void run() {
                    if (endAcRunnable != null)
                        endAcRunnable.run();
                    mContainerHover.setVisibility(View.GONE);
                }
            });
        } else {
            mFrame.setAlpha(0f);
            if (endAcRunnable != null)
                endAcRunnable.run();
            mFrame.setVisibility(View.GONE);
        }
    }

    private Runnable mHideHover = new Runnable() {
        @Override
        public void run() {
            mFrame.startAnimation(false);
            mContainerHover.animate().alpha(0f).translationY(mContainerHover.getHeight()).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mContainerHover.setVisibility(View.GONE);
                }
            });
        }
    };

    private class Frame extends View {

        private Paint mPaint;
        private int mCrossPad;
        private float mFactor;
        private ValueAnimator mValueAnimator;
        private boolean mShow;

        public Frame(Context context) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 1.5f);
            setBackgroundColor(getResources().getColor(R.color.darker_transparent));
            mCrossPad = (int) (getResources().getDisplayMetrics().density * 15);
            mFactor = 0;
        }

        private void startAnimation(boolean show) {
            if (mShow != show) {
                mShow = show;
                if (mValueAnimator != null) {
                    mValueAnimator.cancel();
                }

                mValueAnimator = show ? ValueAnimator.ofFloat(mFactor, 1) : ValueAnimator.ofFloat(mFactor, 0);
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mFactor = (Float) animation.getAnimatedValue();
                        setAlpha(mFactor);
                        invalidate();
                    }
                });
                mValueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mValueAnimator.removeAllUpdateListeners();
                        mValueAnimator.removeAllListeners();
                        mValueAnimator = null;
                    }
                });
                mValueAnimator.start();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            final float left = mFramePadding;
            final float top = mFramePadding;
            final float right = ImageFrameLayout.this.getWidth() - mFramePadding;
            final float bottom = ImageFrameLayout.this.getHeight() - mFramePadding;
            final float width = right - left;
            final float height = bottom - top;

            float x, y;

            y = top + mCrossPad;
            canvas.drawLine(left, y, right * mFactor, y, mPaint);

            x = right - mCrossPad;
            canvas.drawLine(x, bottom - (height * mFactor), x, bottom, mPaint);

            y = bottom - mCrossPad;
            canvas.drawLine(right - (width * mFactor), y, right, y, mPaint);

            x = left + mCrossPad;
            canvas.drawLine(x, top, x, bottom * mFactor, mPaint);
        }
    }

    public static interface OnHoverViewClickListener {
        public void onHoverViewClick(int imageFrameId, int id);
    }

}
