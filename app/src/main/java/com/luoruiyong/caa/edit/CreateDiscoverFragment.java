package com.luoruiyong.caa.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.edit.EditorActivity.OnActionBarClickListener;
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;
import com.luoruiyong.caa.widget.imageviewlayout.clickmanager.ChooseImageClickManager;
import com.luoruiyong.caa.widget.imageviewlayout.clickmanager.IClickManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class CreateDiscoverFragment extends BaseCreateFragment implements
        OnActionBarClickListener,
        DynamicInputView.OnContentViewClickListener,
        ImageViewLayout.OnImageClickListener,
        ImageViewLayout.OnImageLongClickListener{

    private DynamicInputView mContentInputView;
    private DynamicInputView mRelatedTopicInputView;
    private DynamicInputView mLocationInputView;

    private List<DynamicInputView> mCheckEmptyList;
    private List<String> mPictureUrls;

    private IClickManager mImageViewLayoutClickManager = new ChooseImageClickManager();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_create_discover, container, false);

        initView(view);

        return view;
    }

    private void initView(View rootView) {
        mContentInputView = rootView.findViewById(R.id.input_view_content);
        mRelatedTopicInputView = rootView.findViewById(R.id.input_view_related_topic);
        mLocationInputView = rootView.findViewById(R.id.input_view_location);

        mContentInputView.setOnImageClickListener(this);
        mContentInputView.setOnImageLongClickListener(this);
        mRelatedTopicInputView.setOnContentViewClickListener(this);
        mLocationInputView.setOnContentViewClickListener(this);

        mCheckEmptyList = new ArrayList<>();
        mCheckEmptyList.add(mContentInputView);
        mCheckEmptyList.add(mRelatedTopicInputView);
        mCheckEmptyList.add(mLocationInputView);

        if (Enviroment.VAR_DEBUG) {
            mPictureUrls = new ArrayList<>();
            mPictureUrls.add("https://www.baidu.com/1.jpg");
            mContentInputView.setPictureUrls(mPictureUrls);
        }
    }

    @Override
    public void onBackClick() {
        boolean hasData = false;
        for (DynamicInputView view : mCheckEmptyList) {
            if (!view.isEmpty()) {
                hasData = true;
                break;
            }
        }
        if (hasData) {
            showConfirmLeftDialog();
        } else {
            finish();
        }
    }

    @Override
    public void onFinishClick() {
        if (mContentInputView.checkAndShowErrorTipIfNeed()) {
            Toast.makeText(getContext(), "can send", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "can not send", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onContentViewClick(View v) {
        switch (v.getId()) {
            case R.id.input_view_related_topic:
                Toast.makeText(getContext(), "click related topic", Toast.LENGTH_SHORT).show();
                break;
            case R.id.input_view_location:
                Toast.makeText(getContext(), "click location", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onImageClick(View parent, int position) {
        if (!mImageViewLayoutClickManager.onImageClick((ImageViewLayout) parent, position)) {
            // 选择图片添加操作

            if (Enviroment.VAR_DEBUG) {
                // for test
                mPictureUrls.add(position, "https://www.baidu.com/1.jpg");
                mContentInputView.notifyInputDataChanged();
            }
        }
    }

    @Override
    public void onImageLongClick(View parent, final int position) {
        mImageViewLayoutClickManager.onImageLongClick(mContentInputView, (ImageViewLayout) parent, position);
    }
}
