package com.thecamtech.android.library.util;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by veasnasreng on 9/23/14.
 */
public class PathHolder {

    public PathSegment[] pathSegments;

    /**
     * @param paths
     */

    public PathHolder(PathSegment[] paths) {
        this.pathSegments = paths;
    }

    public void draw(Canvas canvas, Path path, Paint paint, Matrix matrix, int width, int height) {
        draw(canvas, path, paint, false, matrix, width, height);
    }

    public void draw(Canvas canvas, Path path, Paint paint, boolean adaptedColor, Matrix matrix, int width, int height) {
        canvas.save();
        if (matrix != null) {
            canvas.setMatrix(matrix);
        }

        for (PathSegment pathSegment : pathSegments) {
            path.reset();
            path = pathSegment.computePath(width, height, path);
            if (adaptedColor) {
                paint.setColor(pathSegment.mColor);
            }
            canvas.drawPath(path, paint);
        }
        canvas.restore();
    }

    public static class PathSegment {

        private int mColor;
        private PathValues[] mPathValues;

        public PathSegment(int color, PathValues[] pathValues) {
            mColor = color;
            mPathValues = pathValues;
        }

        public Path computePath(int width, int height, Path path) {
            for (PathValues values : mPathValues) {
                path = values.toPath(width, height, path);
            }
            return path;
        }

    }

}
