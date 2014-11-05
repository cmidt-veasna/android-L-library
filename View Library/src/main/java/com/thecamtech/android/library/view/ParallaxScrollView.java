package com.thecamtech.android.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.thecamtech.android.library.R;

/**
 * Created by veasnasreng on 9/28/14.
 */
public class ParallaxScrollView extends ScrollView {

    private static final int TO_TOP = 1;
    private static final int TO_BOTTOM = 2;

    private View mParallaxView;
    private float mParallaxDistance;
    private int mParallaxDirection;

    public ParallaxScrollView(Context context) {
        super(context);
        this.init(null, 0);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {

            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ParallaxScrollView, defStyleAttr, 0);

            mParallaxDistance = a.getDimension(R.styleable.ParallaxScrollView_parallaxDistance, 0);
            mParallaxDirection = a.getInt(R.styleable.ParallaxScrollView_parallaxDirection, TO_TOP);

            a.recycle();
        }
    }

    public void setParallaxView(View view) {
        mParallaxView = view;
        invalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mParallaxView != null) {
            final int move = (int) Math.max(0, Math.min(mParallaxDistance, getScrollY()));
            mParallaxView.setTranslationY(mParallaxDirection == TO_TOP ? -move : move);
        }
    }
}
