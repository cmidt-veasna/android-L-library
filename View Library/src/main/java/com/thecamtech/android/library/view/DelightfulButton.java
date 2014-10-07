package com.thecamtech.android.library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import com.thecamtech.android.library.R;
import com.thecamtech.android.library.drawable.AbsOutline;
import com.thecamtech.android.library.drawable.ColorAnimatorDrawable;
import com.thecamtech.android.library.drawable.DoubleStatePath;
import com.thecamtech.android.library.drawable.ReverseStatePath;

/**
 * Created by veasnatemp on 9/9/14.
 */
public class DelightfulButton extends ImageView implements Checkable {

    public static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private int mColor;
    private ColorStateList mColorStateList;
    private ColorStateList mBgColorStateList;

    private ColorAnimatorDrawable mBackgroundColor;
    private AbsOutline mOutline;
    private boolean mIsReverseMorph;
    private boolean mIsClick;
    private boolean mBroadcasting;
    private boolean mIsChecked;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public DelightfulButton(Context context) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(context);
        init(null, 0);
    }

    public DelightfulButton(Context context, AttributeSet attrs) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(context, attrs);
        init(attrs, 0);
    }

    public DelightfulButton(Context context, AttributeSet attrs, int defStyleAttr) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void init(AttributeSet attrs, int defStyleAttr) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        final float density = getResources().getDisplayMetrics().density;
        String class_ = null;
        boolean isCircle = true;
        boolean isDepth = true;
        int depthColor = Color.BLACK;
        float depthSize = 4 * density;
        int checkToUnchecked = 0, uncheckedToCheck = 0, rawResource = 0;
        float strokeSize = 0;
        boolean isUseStroke = false;
        int duration = 300;
        int frameNumber = 21;
        if (attrs != null) {

            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.DelightfulButton, defStyleAttr, 0);

            mColor = a.getColor(R.styleable.DelightfulButton_color, Color.BLACK);
            strokeSize = a.getDimension(R.styleable.DelightfulButton_strokeSize, strokeSize);
            isUseStroke = a.getBoolean(R.styleable.DelightfulButton_useStroke, isUseStroke);
            class_ = a.getString(R.styleable.DelightfulButton_shapeClass);

            isCircle = a.getBoolean(R.styleable.DelightfulButton_isCircle, true);
            isDepth = a.getBoolean(R.styleable.DelightfulButton_isDepth, true) && !isInEditMode();
            depthColor = a.getColor(R.styleable.DelightfulButton_depthColor, Color.BLACK);
            depthSize = a.getDimension(R.styleable.DelightfulButton_depthSize, depthSize);

            mColorStateList = a.getColorStateList(R.styleable.DelightfulButton_colorState);
            mBgColorStateList = a.getColorStateList(R.styleable.DelightfulButton_backgroundColorState);

            mIsReverseMorph = a.getBoolean(R.styleable.DelightfulButton_isReverse, false);
            checkToUnchecked = a.getResourceId(R.styleable.DelightfulButton_checkToUnchecked, checkToUnchecked);
            uncheckedToCheck = a.getResourceId(R.styleable.DelightfulButton_uncheckedToCheck, uncheckedToCheck);
            rawResource = a.getResourceId(R.styleable.DelightfulButton_rawResource, rawResource);
            duration = a.getInt(R.styleable.DelightfulButton_duration, duration);
            frameNumber = a.getInt(R.styleable.DelightfulButton_frameNumber, frameNumber);

            a.recycle();
        }

        mBackgroundColor = new ColorAnimatorDrawable(depthSize, depthColor, isDepth, isCircle);
        if (isDepth) {
            setLayerType(LAYER_TYPE_SOFTWARE, mBackgroundColor.getPaint());
        }
        if (Build.VERSION.SDK_INT >= 16)
            setBackground(mBackgroundColor);
        else
            setBackgroundDrawable(mBackgroundColor);

        if (class_ != null) {
            Class outline = Class.forName(class_);
            // TODO: make support later
            mOutline = (AbsOutline) outline.newInstance();

        } else if (mIsReverseMorph) {
            mOutline = new ReverseStatePath(getContext(), rawResource, frameNumber, duration);
        } else {
            mOutline = new DoubleStatePath(getContext(), checkToUnchecked, uncheckedToCheck, frameNumber, duration);
        }

        setImageDrawable(mOutline);
        mOutline.getPaint().setColor(mColor);
        if (isUseStroke) {
            mOutline.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
            mOutline.getPaint().setStrokeWidth(strokeSize);
        } else {
            mOutline.getPaint().setStyle(Paint.Style.FILL);
        }

        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() != getMeasuredWidth()) {
            mBackgroundColor.setDrawTypeCircle(false);
        }
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mBgColorStateList != null && mBackgroundColor != null) {
            final int color = mBgColorStateList.getColorForState(getDrawableState(), Color.BLACK);
            mBackgroundColor.switchColor(color, mOutline.getDuration());
        }
        if (mColorStateList != null && mOutline != null) {
            final int color = mColorStateList.getColorForState(getDrawableState(), Color.BLACK);
            if (mIsClick) {
                mOutline.setSwitchToColor(color);
                mOutline.startAnimation();
                mIsClick = false;
            } else {
                mOutline.getPaint().setColor(color);
            }
        }
    }

    @Override
    public boolean performClick() {
        mIsClick = true;
        toggle();
        return super.performClick();
    }

    @Override
    public void setChecked(boolean checked) {
        if (mIsChecked != checked) {
            mIsChecked = checked;
            refreshDrawableState();

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mIsChecked);
            }

            mBroadcasting = false;
        }
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }

    /**
     * Path animation will change as linear line.<br>
     * Example: A ->(Property C)-> B ->(Property C)-> A.
     * The path change from B to A will be a reverse of the path from A to B.
     *
     * @param rawResource
     */
    public void setPathResource(int rawResource) {

    }

    /**
     * Path animation will change as follow circle graph.<br>
     * Example: A ->(Property C)-> B ->(Property D)-> A.
     * The path change from A to B will not look the same as the change from B back to A.<br>
     * It's just like clockwise where it start from 0 to 6 but in order to return to 0 position the clock will<br>
     * continue from 6 to 12 where 12 is stand the same position as 0.
     *
     * @param rawUnCheckToChecked
     * @param rawCheckedToUnCheck
     */
    public void setPathResource(int rawUnCheckToChecked, int rawCheckedToUnCheck) {

    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(DelightfulButton buttonView, boolean isChecked);
    }
}
