package com.thecamtech.android.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.thecamtech.android.library.R;


/**
 * TODO: document your custom view class.
 */
public class CircleImageView extends View {

    private static final int EFFECT_NONE = 0;
    private static final int EFFECT_STROKE = 1;
    private static final int EFFECT_STROKE_PLUS = 2;
    private static final int EFFECT_SHADOW = 3;

    private Drawable mDrawable;
    private BitmapShader mShader;
    private Paint mPaint;
    private Paint mStroke;
    private Bitmap mBitmap;
    private Matrix mDrawMatrix;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private int mUse;
    private int mEffectSize;
    private int mEffectColor;

    public CircleImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mDrawMatrix = new Matrix();

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleImageView, defStyle, 0);

        if (a.hasValue(R.styleable.CircleImageView_drawable)) {
            mDrawable = a.getDrawable(
                    R.styleable.CircleImageView_drawable);
            setDrawable(mDrawable);
        }

        mUse = a.getInt(R.styleable.CircleImageView_use, EFFECT_NONE);
        mEffectSize = a.getDimensionPixelSize(R.styleable.CircleImageView_effectSize, 0);
        mEffectColor = a.getColor(R.styleable.CircleImageView_effectColor, Color.BLACK);

        a.recycle();

        if (mUse == EFFECT_STROKE || mUse == EFFECT_STROKE_PLUS) {
            mStroke = new Paint();
            mStroke.setAntiAlias(true);
            mStroke.setStyle(Paint.Style.STROKE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int size = Math.min(width, height) + ((mUse == EFFECT_NONE) ? 0 : ((mUse == EFFECT_STROKE_PLUS) ? 4 * mEffectSize : 2 * mEffectSize));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();

        final int center = getWidth() / 2;
        final int contentSize = getWidth() - paddingLeft - paddingRight
                - ((mUse == EFFECT_NONE) ? 0 : (mUse == EFFECT_STROKE_PLUS) ? 4 * mEffectSize : 2 * mEffectSize);
        final int radius = (contentSize) / 2;

        if (mBitmap != null) {
            float scale;
            float dx = 0, dy = 0;

            if (mBitmapWidth * contentSize > contentSize * mBitmapHeight) {
                scale = (float) contentSize / (float) mBitmapHeight;
                dx = (contentSize - mBitmapWidth * scale) * 0.5f;
            } else {
                scale = (float) contentSize / (float) mBitmapWidth;
                dy = (contentSize - mBitmapHeight * scale) * 0.5f;
            }

            mDrawMatrix.setScale(scale, scale);
            mDrawMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));

            mShader.setLocalMatrix(mDrawMatrix);
        }

        if (mUse == EFFECT_SHADOW) {
            mPaint.setShadowLayer(mEffectSize, 0, 0, mEffectColor);
            // Important for certain APIs
            setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        } else if (mUse == EFFECT_STROKE) {
            mStroke.setStrokeWidth(mEffectSize);
            mStroke.setColor(mEffectColor);
            canvas.drawCircle(center, center, radius, mStroke);
        } else if (mUse == EFFECT_STROKE_PLUS) {
            mStroke.setStrokeWidth(mEffectSize);
            mStroke.setColor(mEffectColor);
            canvas.drawCircle(center, center, radius + mEffectSize, mStroke);
        }

        canvas.drawCircle(center, center, radius, mPaint);
    }

    public void setBitmap(Bitmap bitmap) {
        setBitmapInternal(bitmap);
        invalidate();
    }

    private void setBitmapInternal(Bitmap bitmap) {
        mDrawable = new BitmapDrawable(getResources(), bitmap);
        mBitmap = bitmap;
        mShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);

        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
        mDrawable.setCallback(this);
        if (mDrawable instanceof BitmapDrawable) {
            setBitmapInternal(((BitmapDrawable) mDrawable).getBitmap());
        } else if (mDrawable instanceof ColorDrawable) {
            mPaint.setColor(((ColorDrawable) mDrawable).getColor());
            mPaint.setShader(null);
            mBitmap = null;
        } else {
            throw new RuntimeException("Drawable must be a color or a bitmap.");
        }
    }
}
