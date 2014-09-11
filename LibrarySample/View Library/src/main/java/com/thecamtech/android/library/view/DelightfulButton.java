package com.thecamtech.android.library.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.thecamtech.android.library.R;
import com.thecamtech.android.library.drawable.ColorAnimateDrawable;

/**
 * Created by veasnatemp on 9/9/14.
 */
public class DelightfulButton extends ImageView {

    private int mColor;
    private float mStrokeSize;
    private boolean mIsUseStroke;
    private ColorStateList mColorStateList;

    private ColorAnimateDrawable mBackgroundColor;
    private Outline mOutline;

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
        if (attrs != null) {

            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.DelightfulButton, defStyleAttr, 0);

            mColor = a.getColor(R.styleable.DelightfulButton_color, Color.BLACK);
            mStrokeSize = a.getDimension(R.styleable.DelightfulButton_strokeSize, depthSize);
            mIsUseStroke = a.getBoolean(R.styleable.DelightfulButton_useStroke, true);
            class_ = a.getString(R.styleable.DelightfulButton_shapeClass);

            isCircle = a.getBoolean(R.styleable.DelightfulButton_isCircle, true);
            isDepth = a.getBoolean(R.styleable.DelightfulButton_isDepth, true) && !isInEditMode();
            depthColor = a.getColor(R.styleable.DelightfulButton_depthColor, Color.BLACK);
            depthSize = a.getDimension(R.styleable.DelightfulButton_depthSize, depthSize);

            mColorStateList = a.getColorStateList(R.styleable.DelightfulButton_colorState);

            a.recycle();
        }

        mBackgroundColor = new ColorAnimateDrawable(depthSize, depthColor, isDepth, isCircle);
        if (isDepth) {
            setLayerType(LAYER_TYPE_SOFTWARE, mBackgroundColor.getPaint());
        }
        if (Build.VERSION.SDK_INT >= 16)
            setBackground(mBackgroundColor);
        else
            setBackgroundDrawable(mBackgroundColor);

        if (class_ != null) {
            Class outline = Class.forName(class_);
            mOutline = (Outline) outline.newInstance();
            mOutline.mDelightfulButton = this;
            setImageDrawable(mOutline);

            if (mIsUseStroke) {
                mOutline.getPaint().setStyle(Paint.Style.STROKE);
                mOutline.getPaint().setStrokeWidth(mStrokeSize);
            } else {
                mOutline.getPaint().setStyle(Paint.Style.FILL);
            }

            mOutline.getPaint().setAntiAlias(true);
            mOutline.getPaint().setColor(mColor);
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnClickListener(mOutline);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnClickListener(null);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        mergeDrawableStates(drawableState, mOutline.getState());
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mColorStateList != null && mBackgroundColor != null) {
            final int color = mColorStateList.getColorForState(getDrawableState(), Color.BLACK);
            mBackgroundColor.switchColor(color, mOutline.getDuration());
        }
    }

    public static abstract class Outline extends Drawable implements OnClickListener {

        protected ValueAnimator mValueAnimator;
        protected DelightfulButton mDelightfulButton;

        public void setValueAnimator(ValueAnimator valueAnimator) {
            mValueAnimator = valueAnimator;
        }

        public int getDuration() {
            return 300;
        }

        public abstract ValueAnimator getValueAnimator(boolean isReverse);

        public abstract int[] getState();

        public abstract Paint getPaint();

        public abstract void startSwitchAnimation();
    }

}
