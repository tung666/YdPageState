package com.yd.commonlibrary.pagestate;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yd.commonlibrary.R;
import com.yd.commonlibrary.pagestate.listener.OnEmptyRetryListener;
import com.yd.commonlibrary.pagestate.listener.OnErrorRetryListener;


/**
 * 管理页面加载，加载结果处理
 * Created by yedong on 17/2/25.
 */
public class YdPageStateManager implements YdPageState {

    private Context context;
    private YdPageStateLayout mYdPageStateLayout;

    public static YdPageStateManager generate(Object activityOrFragment) {
        return new YdPageStateManager(activityOrFragment);
    }

    public static YdPageStateManager generate(Activity aty, int viewId) {
        return new YdPageStateManager(aty, viewId);
    }

    public static YdPageStateManager generate(Fragment fragment, int viewId) {
        return new YdPageStateManager(fragment.getView(), viewId);
    }

    public YdPageStateManager(Activity activity, int viewId) {
        this(activity.findViewById(viewId));
    }

    public YdPageStateManager(View contentView, int viewId) {
        this(contentView.findViewById(viewId));
    }

    public YdPageStateManager(Object activityOrFragmentOrView) {
        if (activityOrFragmentOrView == null) {
            throw new IllegalArgumentException("the argument's (Fragment or Activity or View) is null)");
        }

        ViewGroup contentParent = null;
//        Context context;
        if (activityOrFragmentOrView instanceof Activity) {
            Activity activity = (Activity) activityOrFragmentOrView;
            context = activity;
            contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        } else if (activityOrFragmentOrView instanceof Fragment) {
            Fragment fragment = (Fragment) activityOrFragmentOrView;
            context = fragment.getActivity();
            contentParent = (ViewGroup) (fragment.getView().getParent());
        } else if (activityOrFragmentOrView instanceof View) {
            View view = (View) activityOrFragmentOrView;
            contentParent = (ViewGroup) (view.getParent());
            context = view.getContext();
        } else {
            throw new IllegalArgumentException("the argument's type must be Fragment or Activity or View: init(context)");
        }
        int childCount = contentParent.getChildCount();
        //get contentParent
        int index = 0;
        View oldContent;
        if (activityOrFragmentOrView instanceof View) {
            oldContent = (View) activityOrFragmentOrView;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    index = i;
                    break;
                }
            }
        } else {
            oldContent = contentParent.getChildAt(0);
        }
        contentParent.removeView(oldContent);
        //setup content layout
        YdPageStateLayout loadingAndRetryLayout = new YdPageStateLayout(context);
        ViewGroup.LayoutParams lp = oldContent.getLayoutParams();
        contentParent.addView(loadingAndRetryLayout, index, lp);
        loadingAndRetryLayout.addView(oldContent, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mYdPageStateLayout = loadingAndRetryLayout;
        mYdPageStateLayout.showContent();
    }

    @Override
    public void showContent() {
        mYdPageStateLayout.showContent();
    }

    public void showLoading() {
        mYdPageStateLayout.showLoading();
    }

    public void showEmpty(OnEmptyRetryListener listener) {
        mYdPageStateLayout.showEmpty(context.getResources().getDrawable(R.mipmap.monkey_nodata),
                context.getString(R.string.ydPageState_empty_title), context.getString(R.string.ydPageState_empty_details), null);
    }

    public void showError(OnErrorRetryListener listener) {
        mYdPageStateLayout.showError(context.getResources().getDrawable(R.mipmap.monkey_cry),
                context.getString(R.string.ydPageState_error_title), context.getString(R.string.ydPageState_error_details),
                context.getString(R.string.ydPageState_retry), new OnErrorRetryListener() {
                    @Override
                    public void onErrorRetry(View view) {
                        mYdPageStateLayout.showLoading();
                    }
                });
    }

    @Override
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent,
                          OnEmptyRetryListener listener) {
        mYdPageStateLayout.showEmpty(emptyImageDrawable, emptyTextTitle, emptyTextContent, listener);
    }

    @Override
    public void showError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent,
                          String errorButtonText, OnErrorRetryListener listener) {
        mYdPageStateLayout.showError(errorImageDrawable, errorTextTitle, errorTextContent, errorButtonText, listener);
    }

}
