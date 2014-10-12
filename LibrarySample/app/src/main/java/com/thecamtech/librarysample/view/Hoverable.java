package com.thecamtech.librarysample.view;

/**
 * Created by veasnasreng on 10/11/14.
 */
public interface Hoverable {

    public void onTouchHover(float x, float y);

    public void onTouchRelease(float x, float y);

    public void dismiss(boolean animate, Runnable endAction);

}
