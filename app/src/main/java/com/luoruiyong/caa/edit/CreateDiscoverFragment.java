package com.luoruiyong.caa.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.edit.EditorActivity.OnActionBarClickListener;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

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
    private ImageViewLayout mImageViewLayout;

    private List<DynamicInputView> mCheckEmptyList;
    private List<String> mPictureUrls;

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
        mImageViewLayout = rootView.findViewById(R.id.image_view_layout);

        mRelatedTopicInputView.setOnContentViewClickListener(this);
        mLocationInputView.setOnContentViewClickListener(this);
        mImageViewLayout.setOnImageClickListener(this);

        mCheckEmptyList = new ArrayList<>();
        mCheckEmptyList.add(mContentInputView);
        mCheckEmptyList.add(mRelatedTopicInputView);
        mCheckEmptyList.add(mLocationInputView);

        mPictureUrls = new ArrayList<>();
        mPictureUrls.add("https://www.baidu.com/1.jpg");
        mImageViewLayout.setPictureUrls(mPictureUrls);
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
        if (mContentInputView.check()) {
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
        if (!ListUtils.isIndexBetween(mPictureUrls, position)) {
            return;
        }
        if (position + 1 == mPictureUrls.size()) {
            // 添加图片
            // for test
            Toast.makeText(getContext(), "选择图片添加", Toast.LENGTH_SHORT).show();
            mPictureUrls.add(mPictureUrls.size() - 1, "https://www.baidu.com/1.jpg");
            mImageViewLayout.notifyChildViewChanged();
        } else {
            // 查看图片
            List list = new ArrayList();
            // 需要移除最后的那一张添加
            for (int i = 0; i < mPictureUrls.size() - 1; i++) {
                list.add(mPictureUrls.get(i));
            }
            PictureBrowseActivity.startAction(getContext(), list, position);

        }
    }

    @Override
    public void onImageLongClick(View parent, final int position) {
        if (position + 1 == mPictureUrls.size()) {
            return;
        }
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.common_str_delete));
        DialogHelper.showListDialog(getContext(), items, new CommonDialog.Builder.OnItemClickListener() {
            @Override
            public void onItemClick(int which) {
                mPictureUrls.remove(position);
                mImageViewLayout.notifyChildViewChanged();
            }
        });
    }
}
