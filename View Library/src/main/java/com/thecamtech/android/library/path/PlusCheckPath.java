package com.thecamtech.android.library.path;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.thecamtech.android.library.R;
import com.thecamtech.android.library.view.DelightfulButton;

/**
 * Created by veasnatemp on 9/9/14.
 */
public class PlusCheckPath extends DelightfulButton.Outline {

    private static final Shape[] SHAPES = new Shape[]{
            new Shape(0.50f, 0.00f, 0.50f, 1.00f, /*second line*/ 0.00f, 0.50f, 1.00f, 0.50f),
            new Shape(0.45f, 0.05f, 0.55f, 0.95f, /*second line*/ 0.02f, 0.52f, 0.96f, 0.44f),
            new Shape(0.40f, 0.10f, 0.60f, 0.90f, /*second line*/ 0.04f, 0.54f, 0.92f, 0.38f),
            new Shape(0.35f, 0.15f, 0.65f, 0.85f, /*second line*/ 0.06f, 0.56f, 0.88f, 0.32f),
            new Shape(0.30f, 0.20f, 0.70f, 0.80f, /*second line*/ 0.08f, 0.58f, 0.84f, 0.26f),
            new Shape(0.25f, 0.25f, 0.75f, 0.75f, /*second line*/ 0.10f, 0.60f, 0.80f, 0.20f),
            new Shape(0.20f, 0.30f, 0.80f, 0.70f, /*second line*/ 0.12f, 0.62f, 0.76f, 0.14f),
            new Shape(0.15f, 0.35f, 0.85f, 0.65f, /*second line*/ 0.14f, 0.64f, 0.72f, 0.08f),
            new Shape(0.10f, 0.40f, 0.90f, 0.60f, /*second line*/ 0.16f, 0.66f, 0.68f, 0.02f),
            new Shape(0.05f, 0.45f, 0.95f, 0.55f, /*second line*/ 0.18f, 0.68f, 0.64f, 0.08f),
            new Shape(0.00f, 0.50f, 1.00f, 0.50f, /*second line*/ 0.20f, 0.70f, 0.60f, 0.14f),
            new Shape(0.05f, 0.55f, 0.95f, 0.45f, /*second line*/ 0.22f, 0.72f, 0.58f, 0.20f),
            new Shape(0.10f, 0.60f, 0.90f, 0.40f, /*second line*/ 0.24f, 0.74f, 0.54f, 0.26f),
            new Shape(0.15f, 0.65f, 0.85f, 0.35f, /*second line*/ 0.26f, 0.76f, 0.50f, 0.32f),
            new Shape(0.20f, 0.70f, 0.85f, 0.30f, /*second line*/ 0.28f, 0.80f, 0.48f, 0.38f),
            new Shape(0.25f, 0.75f, 0.85f, 0.25f, /*second line*/ 0.30f, 0.80f, 0.44f, 0.44f),
            new Shape(0.30f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.32f, 0.80f, 0.40f, 0.50f),
            new Shape(0.35f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.35f, 0.80f, 0.36f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.38f, 0.80f, 0.32f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.28f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.24f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.20f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.16f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.12f, 0.55f)
    };

    private static final int[] PLUS = new int[]{R.attr.state_plus};
    private static final int[] DONE = new int[]{R.attr.state_done};

    private boolean mIsAdd;
    private ValueAnimator mValueAnimator;

    private Path mPath = new Path();
    private Paint mPaint = new Paint();

    @Override
    public ValueAnimator getValueAnimator(boolean isReverse) {
        return ValueAnimator.ofInt(isReverse ? SHAPES.length - 1 : 0, isReverse ? 0 : SHAPES.length - 1).setDuration(getDuration());
    }

    @Override
    public void draw(Canvas canvas) {
        int index = mValueAnimator == null ? 0 : (Integer) mValueAnimator.getAnimatedValue();

        Shape shape = SHAPES[index];
        boolean isConnect = index == SHAPES.length - 1;

        final int width = getBounds().width();
        final int height = getBounds().height();
        final float size = Math.min(width, height);

        canvas.save();
        canvas.translate((width - size) / 2, (height - size) / 2);

        mPath.reset();
        mPath.moveTo(size * shape.p1x, size * shape.p1y);
        mPath.lineTo(size * shape.p2x, size * shape.p2y);
        if (isConnect) {
            mPath.lineTo(size * shape.p3x, size * shape.p3y);
            mPath.lineTo(size * shape.p4x, size * shape.p4y);
        }
        canvas.drawPath(mPath, mPaint);

        if (!isConnect) {
            mPath.reset();
            mPath.moveTo(size * shape.p3x, size * shape.p3y);
            mPath.lineTo(size * shape.p4x, size * shape.p4y);
            canvas.drawPath(mPath, mPaint);
        }

        canvas.restore();
    }

    @Override
    public Paint getPaint() {
        return mPaint;
    }

    @Override
    public void startSwitchAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }

        mValueAnimator = getValueAnimator(!mIsAdd);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setValueAnimator(valueAnimator);
                invalidateSelf();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // enable click or tap when animation finish
                invalidateSelf();
            }
        });
        mValueAnimator.start();
    }

    @Override
    public void onClick(View v) {
        setState(!mIsAdd);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public int[] getState() {
        return mIsAdd ? PLUS : DONE;
    }

    public void setState(boolean isAdd) {
        if (mIsAdd != isAdd) {
            mIsAdd = isAdd;
            mDelightfulButton.invalidate();
            startSwitchAnimation();
        }
    }

    private static class Shape {
        float p1x;
        float p1y;
        float p2x;
        float p2y;
        float p3x;
        float p3y;
        float p4x;
        float p4y;

        public Shape(float p1x, float p1y, float p2x, float p2y, float p3x, float p3y, float p4x, float p4y) {
            this.p1x = p1x;
            this.p1y = p1y;
            this.p2x = p2x;
            this.p2y = p2y;
            this.p3x = p3x;
            this.p3y = p3y;
            this.p4x = p4x;
            this.p4y = p4y;
        }
    }

}
