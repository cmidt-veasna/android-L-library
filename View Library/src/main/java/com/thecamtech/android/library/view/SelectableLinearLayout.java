package com.thecamtech.android.library.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.thecamtech.android.library.R;

/**
 * Created by veasnasreng on 9/29/14.
 */
public class SelectableLinearLayout extends LinearLayout {

    private Drawable mDrawable;

    private int mSelectedIndex = -1;
    private int mMinSelectedIndex;
    private int mMaxSelectedIndex;

    private int mPointerId;
    private int mTouchSlop;
    private float mLastX;
    private float mLastY;
    private Rect mRect;

    private boolean mInAnimation;
    private AnimatorSet mAnimatorSet;

    private OnItemCheckListener mOnItemCheckListener;

    public SelectableLinearLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public SelectableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SelectableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SelectableLinearLayout, defStyle, 0);

            mDrawable = a.getDrawable(R.styleable.SelectableLinearLayout_drawable);
            mSelectedIndex = a.getInt(R.styleable.SelectableLinearLayout_startSelectedItem, 0);
            mMinSelectedIndex = a.getInt(R.styleable.SelectableLinearLayout_minSelectedItem, 0);
            mMaxSelectedIndex = a.getInt(R.styleable.SelectableLinearLayout_maxSelectedItem, 0);

            a.recycle();
        }

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        if (mDrawable != null) {
            setWillNotDraw(false);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mMaxSelectedIndex == 0) {
            mMaxSelectedIndex = getChildCount() - 1;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction() & MotionEventCompat.ACTION_MASK;
        boolean claim = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                claim = true;
                mLastX = event.getX();
                mLastY = event.getY();
                mPointerId = MotionEventCompat.getPointerId(event, 0);
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerIndex = MotionEventCompat.findPointerIndex(event, mPointerId);
                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float xDiff = Math.abs(x - mLastX);
                final float y = MotionEventCompat.getY(event, pointerIndex);
                final float yDiff = Math.abs(y - mLastY);
                claim = (xDiff > mTouchSlop || yDiff > mTouchSlop);
                break;

            case MotionEvent.ACTION_CANCEL:
                claim = true;
                break;

            case MotionEvent.ACTION_UP:
                claim = true;
                final int findIndex = findViewUnderTouch(event.getX(), event.getY(), this);
                final int newIndex = Math.max(mMinSelectedIndex, Math.min(mMaxSelectedIndex, findIndex));
                if (findIndex != -1 && mSelectedIndex != newIndex) {
                    final boolean isUp = mSelectedIndex > newIndex;
                    final int oldIndex = mSelectedIndex;
                    mSelectedIndex = newIndex;
                    if (mDrawable != null) {
                        if (isUp) {
                            runAnimationUp();
                        } else {
                            runAnimationDown();
                        }
                    }
                    if (mOnItemCheckListener != null) {
                        mOnItemCheckListener.onItemChecked(oldIndex, false);
                        mOnItemCheckListener.onItemChecked(newIndex, true);
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(event);
                final float x_ = MotionEventCompat.getX(event, index);
                final float y_ = MotionEventCompat.getY(event, index);
                mLastX = x_;
                mLastY = y_;
                mPointerId = MotionEventCompat.getPointerId(event, index);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex_ = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex_);
                if (pointerId == mPointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex_ == 0 ? 1 : 0;
                    mLastX = MotionEventCompat.getX(event, newPointerIndex);
                    mLastY = MotionEventCompat.getY(event, newPointerIndex);
                    mPointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }
                break;
        }
        return claim;
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        mOnItemCheckListener = onItemCheckListener;
    }

    protected boolean isInAnimation() {
        return mInAnimation;
    }

    protected int getSelectedIndex() {
        return mSelectedIndex;
    }

    private void runAnimationUp() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
        mAnimatorSet = getAnimatorSetMovingTop();
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorSet = null;
                mInAnimation = false;
                if (mOnItemCheckListener != null) {
                    mOnItemCheckListener.onAnimationDone(mSelectedIndex);
                }
            }
        });
        mInAnimation = true;
        mAnimatorSet.start();
        invalidate();
    }

    private void runAnimationDown() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
        mAnimatorSet = getAnimatorSetMovingDown();
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorSet = null;
                mInAnimation = false;
                if (mOnItemCheckListener != null) {
                    mOnItemCheckListener.onAnimationDone(mSelectedIndex);
                }
            }
        });
        mInAnimation = true;
        mAnimatorSet.start();
        invalidate();
    }

    protected AnimatorSet getAnimatorSetMovingTop() {
        View view = getChildAt(mSelectedIndex);
        final ValueAnimator valueAnimatorBottom = ValueAnimator.ofInt(mRect.bottom, view.getBottom());
        valueAnimatorBottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRect.bottom = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorBottom.setInterpolator(new AccelerateDecelerateInterpolator());

        final ValueAnimator valueAnimatorTop = ValueAnimator.ofInt(mRect.top, view.getTop());
        valueAnimatorTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRect.top = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorTop.setInterpolator(new DecelerateInterpolator());

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(valueAnimatorBottom, valueAnimatorTop);
        return mAnimatorSet;
    }

    protected AnimatorSet getAnimatorSetMovingDown() {
        View view = getChildAt(mSelectedIndex);
        final ValueAnimator valueAnimatorBottom = ValueAnimator.ofInt(mRect.bottom, view.getBottom());
        valueAnimatorBottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRect.bottom = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorBottom.setInterpolator(new DecelerateInterpolator());

        final ValueAnimator valueAnimatorTop = ValueAnimator.ofInt(mRect.top, view.getTop());
        valueAnimatorTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRect.top = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorTop.setInterpolator(new AccelerateDecelerateInterpolator());

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(valueAnimatorBottom, valueAnimatorTop);
        return mAnimatorSet;
    }

    private void updateSelectedBound(View view) {
        if (mRect == null) {
            mRect = new Rect();
        }

        mRect.left = view.getLeft();
        mRect.top = view.getTop();
        mRect.right = view.getRight();
        mRect.bottom = view.getBottom();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mInAnimation && mSelectedIndex != -1) {
            updateSelectedBound(getChildAt(mSelectedIndex));
        }
        if (mSelectedIndex != -1) {
            mDrawable.setBounds(mRect);
            mDrawable.draw(canvas);
        }
        super.draw(canvas);
    }

    private int findViewUnderTouch(float x, float y, ViewGroup viewGroup) {
        View view = null;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            view = viewGroup.getChildAt(i);
            final LayoutParams params = (LayoutParams) view.getLayoutParams();
            if (params.gravity != Gravity.CENTER && x >= view.getLeft() &&
                    x < view.getRight() &&
                    y >= view.getTop() &&
                    y < view.getBottom()) {
                return i;
            }
        }
        return -1;
    }

    public static interface OnItemCheckListener {
        public void onItemChecked(int index, boolean checked);

        public void onAnimationDone(int index);
    }

}
