package com.luoruiyong.caa.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luoruiyong.caa.R;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class ErrorTipView extends FrameLayout implements View.OnClickListener{

    private View mRootView;
    private ImageView mErrorImageIv;
    private TextView mErrorInfoTv;
    private TextView mRefreshTv;
    private View mErrorLayout;
    private ProgressBar mProgressBar;

    private boolean mRefreshNeedHide;
    private OnRefreshClickCallBack mCallback;

    public ErrorTipView(@NonNull Context context){
        this(context, null);
    }

    public ErrorTipView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorTipView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.layout_error, this, false);
        mErrorImageIv = mRootView.findViewById(R.id.iv_error_image);
        mErrorInfoTv = mRootView.findViewById(R.id.tv_error_info);
        mRefreshTv = mRootView.findViewById(R.id.tv_refresh);
        mProgressBar = mRootView.findViewById(R.id.progress_bar);
        mErrorLayout = mRootView.findViewById(R.id.ll_error_layout);

        TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.ErrorTipView);
        int errorImageResId = typedArray.getResourceId(R.styleable.ErrorTipView_errorImage, -1);
        if (errorImageResId != -1) {
            mErrorImageIv.setImageResource(errorImageResId);
        }
        mErrorInfoTv.setText(typedArray.getString(R.styleable.ErrorTipView_errorInfoText));
        mRefreshTv.setText(typedArray.getString(R.styleable.ErrorTipView_refreshText));
        mRefreshNeedHide = typedArray.getBoolean(R.styleable.ErrorTipView_refreshNeedHide, false);

        mRefreshTv.setOnClickListener(this);
        addView(mRootView);
    }

    public void setRefreshNeedHide(boolean needHide) {
        mRefreshNeedHide = needHide;
    }

    public void setErrorImage(@DrawableRes int resId) {
        mErrorImageIv.setImageResource(resId);
    }

    public void setErrorImage(String url) {
//        mErrorImageIv.setImageUrl(url);
    }

    public void setErrorInfo(String info) {
        mErrorInfoTv.setText(info);
    }

    public void setRefreshText(String text) {
        mRefreshTv.setText(text);
    }

    public void setOnRefreshCallback(OnRefreshClickCallBack callback) {
        mCallback = callback;
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void show(String info) {
        setErrorInfo(info);
        show();
    }

    public void show(int resId, String info) {
        setErrorImage(resId);
        setErrorInfo(info);
        show();
    }

    public void hide() {
        mErrorLayout.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
        setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        if (mCallback != null) {
            mCallback.onRefreshClick();
            if (!mRefreshNeedHide) {
                mProgressBar.setVisibility(VISIBLE);
                mErrorLayout.setVisibility(GONE);
            } else {
                setVisibility(GONE);
            }
        }
    }

    public interface OnRefreshClickCallBack {
        void onRefreshClick();
    }
}
