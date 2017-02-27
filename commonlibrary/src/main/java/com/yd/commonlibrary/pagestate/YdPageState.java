package com.yd.commonlibrary.pagestate;

import android.graphics.drawable.Drawable;

import com.yd.commonlibrary.pagestate.listener.OnEmptyRetryListener;
import com.yd.commonlibrary.pagestate.listener.OnErrorRetryListener;


/**
 * Created by yedong on 2017/2/24.
 */

public interface YdPageState {

    public void showContent();

    public void showLoading();

    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent,
                          OnEmptyRetryListener listener);

    public void showError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent,
                          String errorButtonText, OnErrorRetryListener listener);
}
