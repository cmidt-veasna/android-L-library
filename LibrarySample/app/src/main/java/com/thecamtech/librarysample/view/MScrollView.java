package com.thecamtech.librarysample.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.thecamtech.librarysample.R;

/**
 * Created by veasnatemp on 10/7/14.
 */
public class MScrollView extends ScrollView {

    public static final int IDLE = 1;
    public static final int DRAGGING = 2;
    public static final int SETTLE = 3;

    private int mStatus = IDLE;
    private boolean mHoverEnable;
    private Hoverable mHoverView;
    private Hover mHover;

    private float mInitY;
    private float mLastY;
    private int mTouchSlop;
    private int mPointerId;

    private OnScrollChangeListener mScrollChangeListener;

    public MScrollView(Context context) {
        super(context);
        init(null, 0);
    }

    public MScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MScrollView);

            mHoverEnable = a.getBoolean(R.styleable.MScrollView_enableHover, mHoverEnable);

            a.recycle();
        }
        if (mHoverEnable) {
            mHover = new Hover();
            final ViewConfiguration configuration = ViewConfiguration.get(getContext());
            mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mScrollChangeListener != null) {
            final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
            switch (action) {
                case MotionEvent.ACTION_DOWN:

                    final float y = ev.getY();
                    mInitY = mLastY = y;
                    mPointerId = MotionEventCompat.getPointerId(ev, 0);

                    if (mHoverEnable) {

                        final float x = ev.getX();

                        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
                        for (int i = 0; i < viewGroup.getChildCount(); i++) {
                            View view = viewGroup.getChildAt(i);
                            if (view instanceof Hoverable && isViewUnderTouch(x, y, getScrollY(), view)) {
                                mHoverView = (Hoverable) view;
                                mHover.x = x;
                                mHover.y = y;
                                mHover.isCalled = false;
                                postDelayed(mHover, ViewConfiguration.getTapTimeout());
                                break;
                            }
                        }

                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mPointerId == -1) {
                        break;
                    }

                    mStatus = DRAGGING;
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mPointerId);
                    final float y_ = MotionEventCompat.getY(ev, pointerIndex);
                    final float yDiff = Math.abs(y_ - mLastY);

                    if (!mScrollChangeListener.mDraggingCalled && yDiff > mTouchSlop) {
                        mScrollChangeListener.onScrollStateChange(this, DRAGGING);
                        mScrollChangeListener.mDraggingCalled = true;
                        if (mHoverEnable && mHoverView != null) {
                            if (mHover.isCalled) {
                                mHoverView.onTouchRelease(ev.getX(), ev.getY());
                            } else {
                                removeCallbacks(mHover);
                            }
                        }
                    }
                    break;

                case MotionEventCompat.ACTION_POINTER_DOWN:
                    final int index = MotionEventCompat.getActionIndex(ev);
                    final float y__ = MotionEventCompat.getY(ev, index);
                    mLastY = y__;
                    mPointerId = MotionEventCompat.getPointerId(ev, index);
                    break;

                case MotionEventCompat.ACTION_POINTER_UP:
                    final int pointerIndex_ = MotionEventCompat.getActionIndex(ev);
                    final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex_);
                    if (pointerId == mPointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex_ == 0 ? 1 : 0;
                        mLastY = MotionEventCompat.getY(ev, newPointerIndex);
                        mPointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                    } else {
                        mLastY = MotionEventCompat.getX(ev,
                                MotionEventCompat.findPointerIndex(ev, mPointerId));
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    mStatus = SETTLE;
                    mScrollChangeListener.onScrollStateChange(this, SETTLE);
                    mScrollChangeListener.mDraggingCalled = false;
                    if (mHoverEnable && mHoverView != null) {
                        if (!mHover.isCalled) {
                            removeCallbacks(mHover);
                            mHover.run();
                        }
                        mHoverView.onTouchRelease(ev.getX(), ev.getY());
                    }
                    mPointerId = -1;
            }
        }
        return super.onTouchEvent(ev);
    }

    private boolean isViewUnderTouch(float x, float y, float scrollOffset, View view) {
        return (x >= view.getLeft() &&
                x < view.getRight() &&
                y >= (view.getTop() - scrollOffset) &&
                y < (view.getBottom() - scrollOffset));
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollChangeListener != null) {
            mScrollChangeListener.onScrollChange(this);
            // remove preview one then schedule next one
            removeCallbacks(mIdle);
            postDelayed(mIdle, 80);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mScrollChangeListener = onScrollChangeListener;
    }

    private class Hover implements Runnable {
        private float x;
        private float y;
        private boolean isCalled;

        @Override
        public void run() {
            mHoverView.onTouchHover(x, y);
            isCalled = true;
        }

    }

    private Runnable mIdle = new Runnable() {
        @Override
        public void run() {
            if (mScrollChangeListener != null) {
                mScrollChangeListener.onScrollStateChange(MScrollView.this, IDLE);
            }
        }
    };

    public static class OnScrollChangeListener {

        private boolean mDraggingCalled = false;

        public void onScrollChange(ScrollView view) {
        }

        public void onScrollStateChange(ScrollView view, int status) {
        }
    }
}
