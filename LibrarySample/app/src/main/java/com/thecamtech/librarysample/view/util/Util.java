package com.thecamtech.librarysample.view.util;

import android.graphics.Color;

/**
 * Created by veasnasreng on 9/27/14.
 */
public class Util {

    public static int getArgbFromRgb(int alpha, int rgb) {
        return Color.argb(alpha, Color.red(rgb), Color.green(rgb), Color.blue(rgb));
    }

}
