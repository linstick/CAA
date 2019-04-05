package com.luoruiyong.caa.edit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
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
public class CreateActivityFragment extends Fragment implements
        ImageViewLayout.OnImageClickListener,
        ImageViewLayout.OnImageLongClickListener,
        EditorActivity.OnActionBarClickListener {

    private DynamicInputView mTypeInputView;
    private DynamicInputView mTitleInputView;
    private DynamicInputView mContentInputView;
    private DynamicInputView mHostInputView;
    private DynamicInputView mTimeInputView;
    private DynamicInputView mAddressInputView;
    private DynamicInputView mRemarkInputView;
    private DynamicInputView mRelatedTopicInputView;
    private DynamicInputView mLocationInputView;
    private ImageViewLayout mImageViewLayout;

    private List<String> mPictureUrls;
    private List<DynamicInputView> mCheckNonNullList;
    private List<DynamicInputView> mCheckEmptyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_create_activity, container, false);

        initView(view);

        return view;
    }

    private void initView(View rootView) {
        mTypeInputView = rootView.findViewById(R.id.input_view_type);
        mTitleInputView = rootView.findViewById(R.id.input_view_title);
        mContentInputView = rootView.findViewById(R.id.input_view_content);
        mHostInputView = rootView.findViewById(R.id.input_view_host);
        mTimeInputView = rootView.findViewById(R.id.input_view_time);
        mAddressInputView = rootView.findViewById(R.id.input_view_address);
        mRemarkInputView = rootView.findViewById(R.id.input_view_remark);
        mRelatedTopicInputView = rootView.findViewById(R.id.input_view_related_topic);
        mLocationInputView = rootView.findViewById(R.id.input_view_location);
        mImageViewLayout = rootView.findViewById(R.id.image_view_layout);

        mImageViewLayout.setOnImageClickListener(this);
        mImageViewLayout.setOnImageLongClickListener(this);

        mCheckNonNullList = new ArrayList<>();
        mCheckNonNullList.add(mTypeInputView);
        mCheckNonNullList.add(mTitleInputView);
        mCheckNonNullList.add(mContentInputView);
        mCheckNonNullList.add(mHostInputView);
        mCheckNonNullList.add(mTimeInputView);
        mCheckNonNullList.add(mAddressInputView);
        mCheckNonNullList.add(mLocationInputView);

        mCheckEmptyList = new ArrayList<>();
        mCheckEmptyList.addAll(mCheckNonNullList);
        mCheckEmptyList.add(mRemarkInputView);
        mCheckEmptyList.add(mRelatedTopicInputView);
        mCheckEmptyList.add(mLocationInputView);

        // for test
        mPictureUrls = new ArrayList<>();
        mPictureUrls.add("https://www.baidu.com");
        mImageViewLayout.setPictureUrls(mPictureUrls);
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


    @Override
    public void onBackClick() {
        boolean hasData = false;
        if (ListUtils.getSize(mPictureUrls) > 1) {
            showComfirmLeftDialog();
        } else {
            for (DynamicInputView view : mCheckEmptyList) {
                if (!view.isEmpty()) {
                    hasData = true;
                    break;
                }
            }
            if (hasData) {
                showComfirmLeftDialog();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onFinishClick() {
        boolean canSend = true;
        for (DynamicInputView view : mCheckNonNullList) {
            canSend &= view.check();
        }
        if (canSend) {
            Toast.makeText(getContext(), "can send", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "can not send", Toast.LENGTH_SHORT).show();
        }
    }

    private void showComfirmLeftDialog() {
        new CommonDialog.Builder(getContext())
                .type(CommonDialog.TYPE_NORMAL)
                .title(getString(R.string.common_str_tip))
                .message(getString(R.string.common_tip_no_save))
                .positive(getString(R.string.common_str_left))
                .negative(getString(R.string.common_str_cancel))
                .onPositive(new CommonDialog.Builder.OnClickListener() {
                    @Override
                    public void onClick(String extras) {
                       finish();
                    }
                })
                .build()
                .show();
    }

    private void finish() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }
}
