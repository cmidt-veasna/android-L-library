package com.thecamtech.librarysample.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by veasnatemp on 10/7/14.
 */
public class MScrollView extends ScrollView {

    public static final int IDLE = 1;
    public static final int DRAGGING = 2;
    public static final int SETTLE = 3;

    private int mStatus = IDLE;

    private OnScrollChangeListener mScrollChangeListener;

    public MScrollView(Context context) {
        super(context);
    }

    public MScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mScrollChangeListener != null) {
            final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
            if (action == MotionEvent.ACTION_MOVE) {
                mStatus = DRAGGING;
                if (!mScrollChangeListener.mDraggingCalled && getScrollY() != 0) {
                    mScrollChangeListener.onScrollStateChange(this, DRAGGING);
                    mScrollChangeListener.mDraggingCalled = true;
                }
            } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                mStatus = SETTLE;
                mScrollChangeListener.onScrollStateChange(this, SETTLE);
                mScrollChangeListener.mDraggingCalled = false;
            }
        }
        return super.onTouchEvent(ev);
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
