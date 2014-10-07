package com.thecamtech.librarysample.view;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * Created by veasnasreng on 8/10/14.
 */
public class MEditText extends EditText {

    private OnFocusChangeListener mOnFocusChangeListener;

    public MEditText(Context context) {
        super(context);
        init();
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        super.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (getBackground() != null && getBackground() instanceof TransitionDrawable) {
                    TransitionDrawable drawable = (TransitionDrawable) getBackground();
                    if (hasFocus) {
                        drawable.startTransition(250);
                    } else {
                        drawable.reverseTransition(250);
                    }
                }
                if (mOnFocusChangeListener != null) {
                    mOnFocusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        });
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }
}