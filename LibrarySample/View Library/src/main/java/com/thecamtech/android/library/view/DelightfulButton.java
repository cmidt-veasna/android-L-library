package com.thecamtech.android.library.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.ImageView;

import com.thecamtech.android.library.R;
import com.thecamtech.android.library.drawable.AbsOutline;
import com.thecamtech.android.library.drawable.ColorAnimatorDrawable;
import com.thecamtech.android.library.drawable.DoubleStatePath;
import com.thecamtech.android.library.drawable.ReverseStatePath;
import com.thecamtech.android.library.util.Utils;

/**
 * Created by veasnatemp on 9/9/14.
 */
public class DelightfulButton extends ImageView implements Checkable {

    public static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public static final int[] PRESSED_STATE_SET = {
            android.R.attr.state_pressed
    };

    private int mColor;
    private ColorStateList mColorStateList;
    private ColorStateList mBgColorStateList;

    private ColorAnimatorDrawable mBackgroundColor;
    private AbsOutline mOutline;
    private boolean mIsReverseMorph;
    private boolean mIsClick;
    private boolean mIsCircle;
    private boolean mBroadcasting;
    private boolean mIsChecked;
    private boolean mIsManuallyAnimate;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    //
    private RippleDrawable mRippleDrawable;

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void init(AttributeSet attrs, int defStyleAttr) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        final float density = getResources().getDisplayMetrics().density;
        String class_ = null;
        mIsCircle = true;
        boolean isDepth = true;
        int depthColor = Color.BLACK;
        int rippleColor = Color.BLACK;
        float depthSize = 4 * density;
        int checkToUnchecked = 0, uncheckedToCheck = 0, rawResource = 0;
        float strokeSize = 0;
        float roundedRadius = 0;
        boolean isUseStroke = false;
        int duration = 300;
        int frameNumber = 21;
        RippleDrawable ripple = null;
        if (attrs != null) {

            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.DelightfulButton, defStyleAttr, 0);

            mColor = a.getColor(R.styleable.DelightfulButton_color, Color.BLACK);
            strokeSize = a.getDimension(R.styleable.DelightfulButton_strokeSize, strokeSize);
            isUseStroke = a.getBoolean(R.styleable.DelightfulButton_useStroke, isUseStroke);
            class_ = a.getString(R.styleable.DelightfulButton_shapeClass);

            mIsCircle = a.getBoolean(R.styleable.DelightfulButton_isCircle, true);
            isDepth = a.getBoolean(R.styleable.DelightfulButton_isDepth, true) && !isInEditMode();
            roundedRadius = a.getDimension(R.styleable.DelightfulButton_roundedRadius, roundedRadius);
            depthColor = a.getColor(R.styleable.DelightfulButton_depthColor, Color.BLACK);
            depthSize = a.getDimension(R.styleable.DelightfulButton_depthSize, depthSize);
            rippleColor = a.getColor(R.styleable.DelightfulButton_rippleColor, rippleColor);
            if (Build.VERSION.SDK_INT >= 21)
                ripple = (RippleDrawable) a.getDrawable(R.styleable.DelightfulButton_ripple);

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

        mBackgroundColor = new ColorAnimatorDrawable(depthSize, depthColor, isDepth, mIsCircle, roundedRadius);
        if (isDepth) {
            setLayerType(LAYER_TYPE_SOFTWARE, mBackgroundColor.getPaint());
        }

        if (Build.VERSION.SDK_INT >= 21) {
            if (ripple != null) {
                mRippleDrawable = ripple;
            } else {
                ColorStateList colorStateList = new ColorStateList(new int[][]{new int[0]}, new int[]{rippleColor});
                mRippleDrawable = new RippleDrawable(colorStateList, mBgColorStateList == null ? null : mBackgroundColor,
                        new ShapeDrawable(mIsCircle ? new OvalShape() : new RoundRectShape(new float[]{
                                roundedRadius, roundedRadius, roundedRadius, roundedRadius, roundedRadius, roundedRadius, roundedRadius, roundedRadius
                        }, null, null)));
            }

            ViewOutlineProvider provider = new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.L)
                @Override
                public void getOutline(View view, Outline outline) {
                    if (mIsCircle) {
                        outline.setOval(0, 0, view.getWidth(), view.getHeight());
                    } else {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), mBackgroundColor.getRadius());
                    }
                }
            };
            setOutlineProvider(provider);
            setBackground(mRippleDrawable);

        } else if (Build.VERSION.SDK_INT >= 16)
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(DelightfulButton.class.getName());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(DelightfulButton.class.getName());
    }

    public void setIconColorStateList(ColorStateList colorStateList) {
        mColorStateList = colorStateList;
        invalidate();
    }

    public void setBackgroundColorStateList(ColorStateList colorStateList) {
        mBgColorStateList = colorStateList;
        invalidate();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() != getMeasuredWidth()) {
            mBackgroundColor.setDrawTypeCircle(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mRippleDrawable.setHotspotBounds(0, 0, getWidth(), getHeight());
            }
        }
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onDraw(Canvas canvas) {
//        final Drawable background = getBackground();
//        background.setBounds(0, 0, getWidth(), getHeight());
//        background.setHotspotBounds(0, 0, getWidth(), getHeight());
//        super.onDraw(canvas);
//    }

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
        // ignore pressed state, leave path as it was.
        if (!Utils.containState(getDrawableState(), PRESSED_STATE_SET)) {
            if (mBgColorStateList != null && mBackgroundColor != null) {
                final int color = mBgColorStateList.getColorForState(getDrawableState(), Color.BLACK);
                mBackgroundColor.switchColor(color, mOutline.getDuration());
            }
            if (mColorStateList != null && mOutline != null) {
                final int color = mColorStateList.getColorForState(getDrawableState(), Color.BLACK);
                if (mIsClick || mIsManuallyAnimate) {
                    mOutline.setSwitchToColor(color);
                    mOutline.startAnimation();
                    mIsClick = false;
                    mIsManuallyAnimate = false;
                } else {
                    mOutline.getPaint().setColor(color);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        mRippleDrawable.setHotspot(x, y);
    }

    @Override
    public boolean performClick() {
        mIsClick = true;
        setChecked(!mIsChecked, false);
        return super.performClick();
    }

    private void setChecked(boolean checked, boolean animate) {
        if (mIsChecked != checked) {
            mIsChecked = checked;
            mIsManuallyAnimate = animate;
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
    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked, true);
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

    static class SavedState extends BaseSavedState {
        boolean checked;

        /**
         * Constructor called from {@link com.thecamtech.android.library.view.DelightfulButton#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "DelightfulButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }
}
