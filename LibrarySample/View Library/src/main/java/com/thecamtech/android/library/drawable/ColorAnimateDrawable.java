package com.thecamtech.android.library.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.ViewDebug;

/**
 * A specialized Drawable that fills the Canvas with a specified color.
 * Note that a ColorDrawable ignores the ColorFilter.
 * <p/>
 * <p>It can be defined in an XML file with the <code>&lt;color></code> element.</p>
 *
 * @attr ref android.R.styleable#ColorDrawable_color
 */
public class ColorAnimateDrawable extends Drawable {
    @ViewDebug.ExportedProperty(deepExport = true, prefix = "state_")
    protected ColorState mState;
    protected final Paint mPaint = new Paint();
    private boolean mMutated;
    protected boolean mIsCircle;
    protected float mDepthSize;
    protected int mDepthColor;
    protected boolean mIsDepth;

    private ValueAnimator mValueAnimator;

    /**
     * Creates a new black ColorDrawable.
     */
    public ColorAnimateDrawable(float depthSize, int depthColor, boolean isDepth, boolean isCircle) {
        this(null);
        mDepthColor = depthColor;
        mDepthSize = depthSize;
        mIsCircle = isCircle;
        mIsDepth = isDepth;
        if (mIsDepth) {
            mPaint.setShadowLayer(mDepthSize, 0, 0, mDepthColor);
        }
    }

    /**
     * Creates a new ColorDrawable with the specified color.
     *
     * @param color The color to draw.
     */
    public ColorAnimateDrawable(int color) {
        this(null);
        setColor(color);
    }

    private ColorAnimateDrawable(ColorState state) {
        mState = new ColorState(state);
        mPaint.setAntiAlias(true);
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | mState.mChangingConfigurations;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setDrawTypeCircle(boolean circle) {
        if (mIsCircle != circle) {
            mIsCircle = circle;
        }
    }

    public void switchColor(final int color, int duration) {
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }

        mValueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), mState.mUseColor, color);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mState.mAnimateColor = (Integer) animation.getAnimatedValue();
                mState.mInAnimate = true;
                invalidateSelf();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mState.mInAnimate = false;
                mState.mAnimateColor = mState.mBaseColor = mState.mUseColor = color;
                invalidateSelf();
            }
        });
        mValueAnimator.setDuration(duration);
        mState.mInAnimate = true;
        mValueAnimator.start();
    }

    /**
     * A mutable BitmapDrawable still shares its Bitmap with any other Drawable
     * that comes from the same resource.
     *
     * @return This drawable.
     */
    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mState = new ColorState(mState);
            mMutated = true;
        }
        return this;
    }

    @Override
    public void draw(Canvas canvas) {
        if ((mState.mUseColor >>> 24) != 0) {
            if (mState.mInAnimate) {
                mPaint.setColor(mState.mAnimateColor);
            } else {
                mPaint.setColor(mState.mUseColor);
            }

            if (mIsCircle) {
                Rect rect = getBounds();
                final float center = rect.width() / 2;
                final float radius = center - mDepthSize;

                canvas.drawCircle(center, center, radius, mPaint);

            } else {

                RectF rectf = new RectF(getBounds());
                float radius = rectf.height() * 0.10f;
                rectf.inset(mDepthSize, mDepthSize);
                canvas.drawRoundRect(rectf, radius, radius, mPaint);
            }
        }
    }

    /**
     * Gets the drawable's color value.
     *
     * @return int The color to draw.
     */
    public int getColor() {
        return mState.mUseColor;
    }

    /**
     * Sets the drawable's color value. This action will clobber the results of prior calls to
     * {@link #setAlpha(int)} on this object, which side-affected the underlying color.
     *
     * @param color The color to draw.
     */
    public void setColor(int color) {
        if (mState.mBaseColor != color || mState.mUseColor != color) {
            invalidateSelf();
            mState.mBaseColor = mState.mUseColor = color;
        }
    }

    /**
     * Returns the alpha value of this drawable's color.
     *
     * @return A value between 0 and 255.
     */
    @Override
    public int getAlpha() {
        return mState.mUseColor >>> 24;
    }

    /**
     * Sets the color's alpha value.
     *
     * @param alpha The alpha value to set, between 0 and 255.
     */
    public void setAlpha(int alpha) {
        alpha += alpha >> 7;   // make it 0..256
        int baseAlpha = mState.mBaseColor >>> 24;
        int useAlpha = baseAlpha * alpha >> 8;
        int oldUseColor = mState.mUseColor;
        mState.mUseColor = (mState.mBaseColor << 8 >>> 8) | (useAlpha << 24);
        if (oldUseColor != mState.mUseColor) {
            invalidateSelf();
        }
    }

    /**
     * Setting a color filter on a ColorDrawable has no effect.
     *
     * @param colorFilter Ignore.
     */
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public int getOpacity() {
        switch (mState.mUseColor >>> 24) {
            case 255:
                return PixelFormat.OPAQUE;
            case 0:
                return PixelFormat.TRANSPARENT;
        }
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public ConstantState getConstantState() {
        mState.mChangingConfigurations = getChangingConfigurations();
        return mState;
    }

    final static class ColorState extends ConstantState {
        int mBaseColor; // base color, independent of setAlpha()
        @ViewDebug.ExportedProperty
        int mUseColor;  // basecolor modulated by setAlpha()
        int mAnimateColor;
        boolean mInAnimate;
        int mChangingConfigurations;

        ColorState(ColorState state) {
            if (state != null) {
                mBaseColor = state.mBaseColor;
                mUseColor = state.mUseColor;
                mChangingConfigurations = state.mChangingConfigurations;
            }
        }

        @Override
        public Drawable newDrawable() {
            return new ColorAnimateDrawable(this);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new ColorAnimateDrawable(this);
        }

        @Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }
    }
}
