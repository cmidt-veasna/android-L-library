package com.thecamtech.android.library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.thecamtech.android.library.R;
import com.thecamtech.android.library.drawable.CheckMarkPath;
import com.thecamtech.android.library.drawable.ReverseStatePath;

/**
 * Created by veasnasreng on 9/26/14.
 */
public class DelightfulRadioButton extends RadioButton {

    private ReverseStatePath mDrawable;

    private int mRawResource;
    private ColorStateList mColorStateList;
    private boolean mIsClick;

    public DelightfulRadioButton(Context context) {
        super(context);
        this.init(null, 0);
    }

    public DelightfulRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0);
    }

    public DelightfulRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int defStyleAttr) {
        mRawResource = R.raw.radio;
        int color = Color.BLACK;
        float extraSpace = 8 * getResources().getDisplayMetrics().density;
        float strokeSize = 0;
        int frameNumber = 21;
        int duration = 300;
        if (attrs != null) {

            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.DelightfulCheckBox, defStyleAttr, 0);

            mRawResource = a.getResourceId(R.styleable.DelightfulCheckBox_rawResource, mRawResource);
            color = a.getColor(R.styleable.DelightfulCheckBox_color, color);
            strokeSize = a.getDimension(R.styleable.DelightfulCheckBox_strokeSize, 0);
            extraSpace = a.getDimension(R.styleable.DelightfulCheckBox_extraSpace, extraSpace);
            mColorStateList = a.getColorStateList(R.styleable.DelightfulCheckBox_colorState);
            frameNumber = a.getInt(R.styleable.DelightfulCheckBox_frameNumber, frameNumber);
            duration = a.getInt(R.styleable.DelightfulCheckBox_duration, duration);

            a.recycle();
        }

        mDrawable = new CheckMarkPath(getContext(), mRawResource, (int) (getLineHeight() + extraSpace),
                getCompoundDrawablePadding(), frameNumber, duration);

        if (mColorStateList == null) {
            mDrawable.getPaint().setColor(color);
        }
        if (strokeSize > 0) {
            mDrawable.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
            mDrawable.getPaint().setStrokeWidth(strokeSize);
        } else {
            mDrawable.getPaint().setStyle(Paint.Style.FILL);
        }
        setButtonDrawable(mDrawable);
    }

    @Override
    public boolean performClick() {
        mIsClick = true;
        return super.performClick();
    }

    @Override
    public void setChecked(boolean checked) {
        mIsClick = isShown();
        super.setChecked(checked);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mColorStateList != null && mDrawable != null) {
            final int color = mColorStateList.getColorForState(getDrawableState(), Color.BLACK);
            if (mIsClick) {
                mDrawable.setSwitchToColor(color);
                mDrawable.startAnimation();
                mIsClick = false;
            } else {
                mDrawable.getPaint().setColor(color);
                mDrawable.invalidateSelf();
            }
        }
    }

}
