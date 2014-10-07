package com.thecamtech.android.library.util;


import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParser {

    private static Pattern PATTERN = Pattern.compile("(#[0-9a-fA-F]*)|([C|Q|M|Z])\\s*([\\d\\s\\.]*)\\s*");

    /**
     * M 38.08 4.63 C 39.36 4.63 40.64 4.63 41.92 4.63 ... Z
     */
    public static PathHolder parsePath(String svgPath) {

        ArrayList<PathHolder.PathSegment> pathSegments = new ArrayList<PathHolder.PathSegment>();

        String[] linePaths = svgPath.split(",");
        if (linePaths == null || linePaths.length < 2) {
            pathSegments.add(parseSvgToSegment(svgPath));
        } else {
            for (String linePath : linePaths) {
                pathSegments.add(parseSvgToSegment(linePath));
            }
        }

        return new PathHolder(pathSegments.toArray(new PathHolder.PathSegment[0]));
    }

    public static PathHolder.PathSegment parseSvgToSegment(String svgPath) {
        String cmd;
        ArrayList<PathValues> pathValues = new ArrayList<PathValues>();
        Matcher matcher = PATTERN.matcher(svgPath);
        int color = Color.BLACK;
        while (matcher.find()) {
            if (matcher.group(2) == null) {
                color = Color.parseColor(matcher.group(1));
            } else {
                cmd = matcher.group(2);
                if (cmd.equalsIgnoreCase("M")) {
                    // move to
                    pathValues.add(new PathValues(parseSvgPathValue(matcher.group(3)), PathValues.M));

                } else if (cmd.equalsIgnoreCase("C")) {
                    // cubic to
                    pathValues.add(new PathValues(parseSvgPathValue(matcher.group(3)), PathValues.C));

                } else if (cmd.equalsIgnoreCase("Q")) {
                    // cubic to
                    pathValues.add(new PathValues(parseSvgPathValue(matcher.group(3)), PathValues.Q));

                } else if (cmd.equalsIgnoreCase("L")) {
                    // cubic to
                    pathValues.add(new PathValues(parseSvgPathValue(matcher.group(3)), PathValues.L));

                } else if (cmd.equalsIgnoreCase("Z")) {
                    pathValues.add(new PathValues(null, PathValues.Z));
                }
            }
        }
        return new PathHolder.PathSegment(color, pathValues.toArray(new PathValues[0]));
    }

    private static float[] parseSvgPathValue(String svgFloatValue) {
        String[] valueAsString = svgFloatValue.split(" ");
        float[] result = new float[valueAsString.length];
        for (int i = 0; i < valueAsString.length; i++) {
            result[i] = Float.parseFloat(valueAsString[i]);
        }
        return result;
    }

}