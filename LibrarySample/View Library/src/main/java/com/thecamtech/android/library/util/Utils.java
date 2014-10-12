package com.thecamtech.android.library.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veasnasreng on 10/12/14.
 */
public class Utils {

    public static boolean containState(int[] source, int[] search) {
        if (search.length == 0 || source.length == 0) {
            return false;
        }

        int count = 0;
        List<Integer> list = new ArrayList<Integer>();
        for (int state : search) {
            list.add(state);
        }
        for (int state : source) {
            if (list.contains(state)) {
                count++;
            }
        }
        return count == search.length;
    }

}
