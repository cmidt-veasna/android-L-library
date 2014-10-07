package com.thecamtech.android.library.util;

import android.graphics.Path;

/**
 * Created by veasnasreng on 9/23/14.
 */
public class PathValues {

    public static final int M = 1;
    public static final int C = 2;
    public static final int Q = 3;
    public static final int L = 4;
    public static final int Z = 5;

    private float[] mFloats;
    private int mType;

    public PathValues(float[] floats, int type) {
        mFloats = floats;
        mType = type;
    }

    public Path toPath(int width, int height, Path path) {
        switch (mType) {
            case M:
                path.moveTo(mFloats[0] * width, mFloats[1] * height);
                break;
            case C:
                path.cubicTo(mFloats[0] * width, mFloats[1] * height,
                        mFloats[2] * width, mFloats[3] * height,
                        mFloats[4] * width, mFloats[5] * height);
                break;
            case Q:
                path.quadTo(mFloats[0] * width, mFloats[1] * height,
                        mFloats[2] * width, mFloats[3] * height);
                break;
            case L:
                path.lineTo(mFloats[0] * width, mFloats[1] * height);
                break;
            case Z:
                path.close();
                break;
        }

        return path;
    }

}
