package com.thecamtech.android.library.path;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;

import com.thecamtech.android.library.R;
import com.thecamtech.android.library.view.DelightfulButton;

/**
 * Created by veasnatemp on 9/9/14.
 */
public class PlusCheckPath extends DelightfulButton.Outline {

    private static final Shape[] DONE_PLUS = new Shape[]{
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.12f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.16f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.20f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.24f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.40f, 0.80f, 0.28f, 0.55f),
            new Shape(0.40f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.38f, 0.80f, 0.32f, 0.55f),
            new Shape(0.35f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.35f, 0.80f, 0.36f, 0.55f),
            new Shape(0.30f, 0.80f, 0.85f, 0.20f, /*second line*/ 0.32f, 0.80f, 0.40f, 0.50f),
            new Shape(0.25f, 0.75f, 0.85f, 0.25f, /*second line*/ 0.30f, 0.80f, 0.44f, 0.44f),
            new Shape(0.20f, 0.70f, 0.85f, 0.30f, /*second line*/ 0.28f, 0.80f, 0.48f, 0.38f),
            new Shape(0.15f, 0.65f, 0.85f, 0.35f, /*second line*/ 0.26f, 0.76f, 0.50f, 0.32f),
            new Shape(0.10f, 0.60f, 0.90f, 0.40f, /*second line*/ 0.24f, 0.74f, 0.54f, 0.26f),
            new Shape(0.05f, 0.55f, 0.95f, 0.45f, /*second line*/ 0.22f, 0.72f, 0.58f, 0.20f),
            new Shape(0.00f, 0.50f, 1.00f, 0.50f, /*second line*/ 0.20f, 0.70f, 0.60f, 0.14f),
            new Shape(0.05f, 0.45f, 0.95f, 0.55f, /*second line*/ 0.18f, 0.68f, 0.64f, 0.08f),
            new Shape(0.10f, 0.40f, 0.90f, 0.60f, /*second line*/ 0.16f, 0.66f, 0.68f, 0.02f),
            new Shape(0.15f, 0.35f, 0.85f, 0.65f, /*second line*/ 0.14f, 0.64f, 0.72f, 0.08f),
            new Shape(0.20f, 0.30f, 0.80f, 0.70f, /*second line*/ 0.12f, 0.62f, 0.76f, 0.14f),
            new Shape(0.25f, 0.25f, 0.75f, 0.75f, /*second line*/ 0.10f, 0.60f, 0.80f, 0.20f),
            new Shape(0.30f, 0.20f, 0.70f, 0.80f, /*second line*/ 0.08f, 0.58f, 0.84f, 0.26f),
            new Shape(0.35f, 0.15f, 0.65f, 0.85f, /*second line*/ 0.06f, 0.56f, 0.88f, 0.32f),
            new Shape(0.40f, 0.10f, 0.60f, 0.90f, /*second line*/ 0.04f, 0.54f, 0.92f, 0.38f),
            new Shape(0.45f, 0.05f, 0.55f, 0.95f, /*second line*/ 0.02f, 0.52f, 0.96f, 0.44f),
            new Shape(0.50f, 0.00f, 0.50f, 1.00f, /*second line*/ 0.00f, 0.50f, 1.00f, 0.50f)
    };

    private static final Shape[] PLUS_DONE = new Shape[]{
            new Shape(0.00f, 0.50f, 1.00f, 0.50f, /*second line*/ 0.50f, 0.00f, 0.50f, 1.00f),
            new Shape(0.05f, 0.45f, 0.95f, 0.55f, /*second line*/ 0.55f, 0.05f, 0.48f, 0.97f),
            new Shape(0.10f, 0.40f, 0.90f, 0.60f, /*second line*/ 0.60f, 0.10f, 0.46f, 0.94f),
            new Shape(0.15f, 0.35f, 0.85f, 0.65f, /*second line*/ 0.65f, 0.15f, 0.44f, 0.91f),
            new Shape(0.20f, 0.30f, 0.80f, 0.70f, /*second line*/ 0.70f, 0.20f, 0.42f, 0.88f),
            new Shape(0.25f, 0.25f, 0.75f, 0.75f, /*second line*/ 0.75f, 0.25f, 0.40f, 0.85f),
            new Shape(0.30f, 0.20f, 0.70f, 0.80f, /*second line*/ 0.80f, 0.30f, 0.38f, 0.82f),
            new Shape(0.35f, 0.15f, 0.65f, 0.85f, /*second line*/ 0.85f, 0.35f, 0.36f, 0.79f),
            new Shape(0.40f, 0.10f, 0.60f, 0.90f, /*second line*/ 0.90f, 0.40f, 0.34f, 0.76f),
            new Shape(0.45f, 0.05f, 0.55f, 0.95f, /*second line*/ 0.95f, 0.45f, 0.32f, 0.73f),
            new Shape(0.50f, 0.00f, 0.50f, 1.00f, /*second line*/ 1.00f, 0.50f, 0.30f, 0.70f),
            new Shape(0.55f, 0.03f, 0.48f, 0.97f, /*second line*/ 0.94f, 0.55f, 0.28f, 0.67f),
            new Shape(0.60f, 0.06f, 0.47f, 0.94f, /*second line*/ 0.88f, 0.60f, 0.26f, 0.64f),
            new Shape(0.65f, 0.09f, 0.46f, 0.91f, /*second line*/ 0.82f, 0.65f, 0.24f, 0.61f),
            new Shape(0.70f, 0.12f, 0.44f, 0.88f, /*second line*/ 0.76f, 0.70f, 0.22f, 0.59f),
            new Shape(0.75f, 0.15f, 0.43f, 0.85f, /*second line*/ 0.72f, 0.75f, 0.20f, 0.56f),
            new Shape(0.80f, 0.18f, 0.42f, 0.82f, /*second line*/ 0.66f, 0.80f, 0.18f, 0.55f),
            new Shape(0.80f, 0.18f, 0.42f, 0.82f, /*second line*/ 0.65f, 0.85f, 0.16f, 0.55f),
            new Shape(0.80f, 0.18f, 0.42f, 0.82f, /*second line*/ 0.60f, 0.85f, 0.14f, 0.55f),
            new Shape(0.80f, 0.18f, 0.42f, 0.82f, /*second line*/ 0.55f, 0.85f, 0.12f, 0.55f),
            new Shape(0.80f, 0.18f, 0.42f, 0.82f, /*second line*/ 0.50f, 0.80f, 0.12f, 0.55f),
            new Shape(0.80f, 0.18f, 0.42f, 0.82f, /*second line*/ 0.45f, 0.80f, 0.12f, 0.55f),
            new Shape(0.80f, 0.18f, 0.42f, 0.82f, /*second line*/ 0.45f, 0.80f, 0.12f, 0.55f),
            new Shape(0.80f, 0.18f, 0.42f, 0.82f, /*second line*/ 0.45f, 0.80f, 0.12f, 0.55f),
            new Shape(0.85f, 0.20f, 0.40f, 0.80f, /*second line*/ 0.40f, 0.80f, 0.12f, 0.55f)
    };

    public static final int[] PLUS = new int[]{R.attr.state_plus};
    public static final int[] DONE = new int[]{R.attr.state_done};

    private boolean mIsPlus = true;

    private Path mPath = new Path();
    private Paint mPaint = new Paint();

    @Override
    public ValueAnimator getValueAnimator() {
        final int length = (mIsPlus) ? DONE_PLUS.length - 1 : PLUS_DONE.length - 1;
        return ValueAnimator.ofInt(0, length).setDuration(getDuration());
    }

    @Override
    public void draw(Canvas canvas) {
        int index = mInAnimation ? (Integer) mValueAnimator.getAnimatedValue() : 0;

        Shape[] current = (mIsPlus ^ mInAnimation) ? PLUS_DONE : DONE_PLUS;
        Shape shape = current[index];
        boolean isConnect = (!mIsPlus || mInAnimation) && (index == current.length - 1 || (index == 0 && !mInAnimation));

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
    public void trigger() {
        mIsPlus = !mIsPlus;
        mDelightfulButton.invalidate();
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
    public int[] getMergeState() {
        return mIsPlus ? PLUS : DONE;
    }

    @Override
    protected boolean setDrawableState(int[] stateSet) {
        if ((containState(stateSet, PLUS)) != mIsPlus) {
            mIsPlus = !mIsPlus;
            mDelightfulButton.invalidate();
            return true;
        }
        return false;
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
