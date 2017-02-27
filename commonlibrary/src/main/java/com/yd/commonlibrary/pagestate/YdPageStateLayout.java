package com.yd.commonlibrary.pagestate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.yd.commonlibrary.R;
import com.yd.commonlibrary.pagestate.listener.OnEmptyRetryListener;
import com.yd.commonlibrary.pagestate.listener.OnErrorRetryListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Vlonjat Gashi (vlonjatg)
 */
public class YdPageStateLayout extends RelativeLayout implements YdPageState{

    private static final String TAG_LOADING = "YdPageStateLayout.TAG_LOADING";
    private static final String TAG_EMPTY = "YdPageStateLayout.TAG_EMPTY";
    private static final String TAG_ERROR = "YdPageStateLayout.TAG_ERROR";

    final String CONTENT = "type_content";
    final String LOADING = "type_loading";
    final String EMPTY = "type_empty";
    final String ERROR = "type_error";

    protected LayoutInflater inflater;
    protected View view;
    protected LayoutParams layoutParams;
    protected Drawable currentBackground;

    protected List<View> contentViews = new ArrayList<>();

    //loading state
    protected RelativeLayout loadingStateRelativeLayout;
    protected ProgressBar loadingStateProgressBar;

    //emtpty state
    protected RelativeLayout emptyStateRelativeLayout;
    protected ImageView emptyStateImageView;
    protected TextView emptyStateTitleTextView;
    protected TextView emptyStateContentTextView;
    protected Button emptyStateButton;

    //error state
    protected RelativeLayout errorStateRelativeLayout;
    protected ImageView errorStateImageView;
    protected TextView errorStateTitleTextView;
    protected TextView errorStateContentTextView;
    protected Button errorStateButton;

    protected int loadingStateProgressBarWidth;
    protected int loadingStateProgressBarHeight;
    protected int loadingStateBackgroundColor;

    protected int emptyStateImageWidth;
    protected int emptyStateImageHeight;
    protected int emptyStateTitleTextSize;
    protected int emptyStateContentTextSize;
    protected int emptyStateTitleTextColor;
    protected int emptyStateContentTextColor;
    protected int emptyStateBackgroundColor;

    protected int errorStateImageWidth;
    protected int errorStateImageHeight;
    protected int errorStateTitleTextSize;
    protected int errorStateContentTextSize;
    protected int errorStateTitleTextColor;
    protected int errorStateContentTextColor;
    protected int errorStateButtonTextColor;
    protected int errorStateBackgroundColor;

    protected String state = CONTENT;

    public YdPageStateLayout(Context context) {
        this(context, null);
    }

    public YdPageStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public YdPageStateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.YdPageStateLayout);

        //Loading state attrs
        loadingStateProgressBarWidth =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_loadingProgressBarWidth, 250);

        loadingStateProgressBarHeight =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_loadingProgressBarHeight, 250);

        loadingStateBackgroundColor =
                typedArray.getColor(R.styleable.YdPageStateLayout_loadingBackgroundColor, Color.TRANSPARENT);

        //Empty state attrs
        emptyStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_emptyImageWidth, 308);

        emptyStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_emptyImageHeight, 308);

        emptyStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_emptyTitleTextSize, 15);

        emptyStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_emptyContentTextSize, 14);

        emptyStateTitleTextColor =
                typedArray.getColor(R.styleable.YdPageStateLayout_emptyTitleTextColor, Color.BLACK);

        emptyStateContentTextColor =
                typedArray.getColor(R.styleable.YdPageStateLayout_emptyContentTextColor, Color.BLACK);

        emptyStateBackgroundColor =
                typedArray.getColor(R.styleable.YdPageStateLayout_emptyBackgroundColor, Color.TRANSPARENT);

        //Error state attrs
        errorStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_errorImageWidth, 308);

        errorStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_errorImageHeight, 308);

        errorStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_errorTitleTextSize, 15);

        errorStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable.YdPageStateLayout_errorContentTextSize, 14);

        errorStateTitleTextColor =
                typedArray.getColor(R.styleable.YdPageStateLayout_errorTitleTextColor, Color.BLACK);

        errorStateContentTextColor =
                typedArray.getColor(R.styleable.YdPageStateLayout_errorContentTextColor, Color.BLACK);

        errorStateButtonTextColor =
                typedArray.getColor(R.styleable.YdPageStateLayout_errorButtonTextColor, Color.BLACK);

        errorStateBackgroundColor =
                typedArray.getColor(R.styleable.YdPageStateLayout_errorBackgroundColor, Color.TRANSPARENT);

        typedArray.recycle();

        currentBackground = this.getBackground();
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR))) {
            Log.d("YdPageStateLayout", "child|" + child);
            contentViews.add(child);
        }
    }

    /**
     * Hide all other states and show content
     */
    public void showContent() {
        switchState(CONTENT, null, null, null, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Hide all other states and show content
     *
     * @param skipIds Ids of views not to show
     */
    public void showContent(List<Integer> skipIds) {
        switchState(CONTENT, null, null, null, null, null, null, skipIds);
    }

    /**
     * Hide content and show the pagestate_progress bar
     */
    public void showLoading() {
        switchState(LOADING, null, null, null, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Hide content and show the pagestate_progress bar
     *
     * @param skipIds Ids of views to not hide
     */
    public void showLoading(List<Integer> skipIds) {
        switchState(LOADING, null, null, null, null, null, null, skipIds);
    }

    /**
     * Show empty view when there are not data to show
     *
     * @param emptyImageDrawable   Drawable to show
     * @param emptyTextTitle       Title of the empty view to show
     * @param emptyTextContent     Content of the empty view to show
     * @param onEmptyRetryListener listener of empty button
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent,
                          OnEmptyRetryListener onEmptyRetryListener) {
        switchState(EMPTY, emptyImageDrawable, emptyTextTitle, emptyTextContent, null,
                onEmptyRetryListener, null, Collections.<Integer>emptyList());
    }

    /**
     * Show empty view when there are not data to show
     *
     * @param emptyImageDrawable   Drawable to show
     * @param emptyTextTitle       Title of the empty view to show
     * @param emptyTextContent     Content of the empty view to show
     * @param onEmptyRetryListener listener of empty button
     * @param skipIds              Ids of views to not hide
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent,
                          OnEmptyRetryListener onEmptyRetryListener, List<Integer> skipIds) {
        switchState(EMPTY, emptyImageDrawable, emptyTextTitle, emptyTextContent, null, onEmptyRetryListener, null, skipIds);
    }

    /**
     * Show error view with a button when something goes wrong and prompting the user to try again
     *
     * @param errorImageDrawable   Drawable to show
     * @param errorTextTitle       Title of the error view to show
     * @param errorTextContent     Content of the error view to show
     * @param errorButtonText      Text on the error view button to show
     * @param onErrorRetryListener Listener of the error view button
     */
    public void showError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent,
                          String errorButtonText, OnErrorRetryListener onErrorRetryListener) {
        switchState(ERROR, errorImageDrawable, errorTextTitle, errorTextContent, errorButtonText, null,
                onErrorRetryListener, Collections.<Integer>emptyList());
    }

    /**
     * Show error view with a button when something goes wrong and prompting the user to try again
     *
     * @param errorImageDrawable   Drawable to show
     * @param errorTextTitle       Title of the error view to show
     * @param errorTextContent     Content of the error view to show
     * @param errorButtonText      Text on the error view button to show
     * @param onErrorRetryListener Listener of the error view button
     * @param skipIds              Ids of views to not hide
     */
    public void showError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent,
                          String errorButtonText, OnErrorRetryListener onErrorRetryListener, List<Integer> skipIds) {
        switchState(ERROR, errorImageDrawable, errorTextTitle, errorTextContent, errorButtonText, null,
                onErrorRetryListener, skipIds);
    }

    /**
     * Get which state is set
     *
     * @return State
     */
    public String getState() {
        return state;
    }

    /**
     * Check if content is shown
     *
     * @return boolean
     */
    public boolean isContent() {
        return state.equals(CONTENT);
    }

    /**
     * Check if loading state is shown
     *
     * @return boolean
     */
    public boolean isLoading() {
        return state.equals(LOADING);
    }

    /**
     * Check if empty state is shown
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return state.equals(EMPTY);
    }

    /**
     * Check if error state is shown
     *
     * @return boolean
     */
    public boolean isError() {
        return state.equals(ERROR);
    }

    private void switchState(String state, Drawable drawable, String errorText, String errorTextContent,
                             String errorButtonText, final OnEmptyRetryListener onEmptyRetryListener,
                             final OnErrorRetryListener onErrorRetryListener, List<Integer> skipIds) {
        this.state = state;

        switch (state) {
            case CONTENT:
                //Hide all state views to display content
                hideLoadingView();
                hideEmptyView();
                hideErrorView();

                setContentVisibility(true, skipIds);
                break;
            case LOADING:
                hideEmptyView();
                hideErrorView();

                setLoadingView();
                setContentVisibility(false, skipIds);
                break;
            case EMPTY:
                hideLoadingView();
                hideErrorView();

                setEmptyView();
                emptyStateImageView.setImageDrawable(drawable);
                emptyStateTitleTextView.setText(errorText);
                emptyStateContentTextView.setText(errorTextContent);
                if (onEmptyRetryListener != null) {
                    emptyStateButton.setVisibility(View.VISIBLE);
                    emptyStateButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onEmptyRetryListener.onEmptyRetry(v);
                        }
                    });
                } else {
                    emptyStateButton.setVisibility(View.GONE);
                }
                setContentVisibility(false, skipIds);
                break;
            case ERROR:
                hideLoadingView();
                hideEmptyView();

                setErrorView();
                errorStateImageView.setImageDrawable(drawable);
                errorStateTitleTextView.setText(errorText);
                errorStateContentTextView.setText(errorTextContent);
                errorStateButton.setText(errorButtonText);
                errorStateButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onErrorRetryListener != null) {
                            onErrorRetryListener.onErrorRetry(v);
                        }
                    }
                });
                setContentVisibility(false, skipIds);
                break;
        }
    }

    private void setLoadingView() {
        if (loadingStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.pagestate_loading_view, null);
            loadingStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.loadingStateRelativeLayout);
            loadingStateRelativeLayout.setTag(TAG_LOADING);

            loadingStateProgressBar = (ProgressBar) view.findViewById(R.id.loadingStateProgressBar);

            loadingStateProgressBar.getLayoutParams().width = loadingStateProgressBarWidth;
            loadingStateProgressBar.getLayoutParams().height = loadingStateProgressBarHeight;
            loadingStateProgressBar.requestLayout();

            //Set background color if not TRANSPARENT
            if (loadingStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(loadingStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(loadingStateRelativeLayout, layoutParams);
        } else {
            loadingStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setEmptyView() {
        if (emptyStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.pagestate_empty_view, null);
            emptyStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.emptyStateRelativeLayout);
            emptyStateRelativeLayout.setTag(TAG_EMPTY);

            emptyStateImageView = (ImageView) view.findViewById(R.id.emptyStateImageView);
            emptyStateTitleTextView = (TextView) view.findViewById(R.id.emptyStateTitleTextView);
            emptyStateContentTextView = (TextView) view.findViewById(R.id.emptyStateContentTextView);
            emptyStateButton = (Button) view.findViewById(R.id.emptyStateButton);

            //Set empty state image width and height
            emptyStateImageView.getLayoutParams().width = emptyStateImageWidth;
            emptyStateImageView.getLayoutParams().height = emptyStateImageHeight;
            emptyStateImageView.requestLayout();

            emptyStateTitleTextView.setTextSize(emptyStateTitleTextSize);
            emptyStateContentTextView.setTextSize(emptyStateContentTextSize);
            emptyStateTitleTextView.setTextColor(emptyStateTitleTextColor);
            emptyStateContentTextView.setTextColor(emptyStateContentTextColor);

            //Set background color if not TRANSPARENT
            if (emptyStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(emptyStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(emptyStateRelativeLayout, layoutParams);
        } else {
            emptyStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setErrorView() {
        if (errorStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.pagestate_error_view, null);
            errorStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.errorStateRelativeLayout);
            errorStateRelativeLayout.setTag(TAG_ERROR);

            errorStateImageView = (ImageView) view.findViewById(R.id.errorStateImageView);
            errorStateTitleTextView = (TextView) view.findViewById(R.id.errorStateTitleTextView);
            errorStateContentTextView = (TextView) view.findViewById(R.id.errorStateContentTextView);
            errorStateButton = (Button) view.findViewById(R.id.errorStateButton);

            //Set error state image width and height
            errorStateImageView.getLayoutParams().width = errorStateImageWidth;
            errorStateImageView.getLayoutParams().height = errorStateImageHeight;
            errorStateImageView.requestLayout();

            errorStateTitleTextView.setTextSize(errorStateTitleTextSize);
            errorStateContentTextView.setTextSize(errorStateContentTextSize);
            errorStateTitleTextView.setTextColor(errorStateTitleTextColor);
            errorStateContentTextView.setTextColor(errorStateContentTextColor);
            errorStateButton.setTextColor(errorStateButtonTextColor);

            //Set background color if not TRANSPARENT
            if (errorStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(errorStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(errorStateRelativeLayout, layoutParams);
        } else {
            errorStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setContentVisibility(boolean visible, List<Integer> skipIds) {
        for (View v : contentViews) {
            if (!skipIds.contains(v.getId())) {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }

    private void hideLoadingView() {
        if (loadingStateRelativeLayout != null) {
            loadingStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (loadingStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
            }
        }
    }

    private void hideEmptyView() {
        if (emptyStateRelativeLayout != null) {
            emptyStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (emptyStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
            }
        }
    }

    private void hideErrorView() {
        if (errorStateRelativeLayout != null) {
            errorStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (errorStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
            }
        }
    }
}