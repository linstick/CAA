package com.luoruiyong.caa.edit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.ImageBean;
import com.luoruiyong.caa.utils.PictureUtils;
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class CreateTopicFragment extends BaseCreateFragment implements
        DynamicInputView.OnFocusLostOrTextChangeListener,
        EditorActivity.OnActionBarClickListener,
        DynamicInputView.OnContentViewClickListener,
        ImageViewLayout.OnImageClickListener {

    private DynamicInputView mNameInputView;
    private DynamicInputView mIntroduceInputView;
    private DynamicInputView mCoverInputView;

    private List<DynamicInputView> mCheckEmptyList;
    private List<ImageBean> mTopicCoverList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_create_tag, container, false);

        initView(view);

        return view;
    }

    private void initView(View rootView) {
        mNameInputView = rootView.findViewById(R.id.input_view_topic_name);
        mIntroduceInputView = rootView.findViewById(R.id.input_view_topic_introduce);
        mCoverInputView = rootView.findViewById(R.id.input_view_topic_cover);

        mNameInputView.setOnFocusLostOrTextChangeListener(this);
        mCoverInputView.setOnImageClickListener(this);
        mCoverInputView.setOnContentViewClickListener(this);

        mCoverInputView.setSupportAllChildDelete(true);

        mCheckEmptyList = new ArrayList<>();
        mCheckEmptyList.add(mNameInputView);
        mCheckEmptyList.add(mIntroduceInputView);
        mCheckEmptyList.add(mCoverInputView);

        mTopicCoverList = new ArrayList<>();
        mCoverInputView.setPictureDataList(mTopicCoverList);
    }

    @Override
    protected void onStoragePermissionGranted() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_COVER_CODE);
    }

    @Override
    public void onFocusLostOrTextChanged(String text) {
        Toast.makeText(getContext(), "onFocusLostOrTextChanged", Toast.LENGTH_SHORT).show();
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
        if (mNameInputView.checkAndShowErrorTipIfNeed()) {
            Toast.makeText(getContext(), "can send", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "can not send", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onImageClick(View parent, int position) {
        // 选择封面图片
        requestStoragePermission();
    }

    @Override
    public void onContentViewClick(View v) {
        requestStoragePermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CHOOSE_COVER_CODE:
                    Uri uri = data.getData();
                    String path = PictureUtils.getPath(getContext(), uri);
                    ImageBean imageBean = new ImageBean();
                    imageBean.setType(ImageBean.TYPE_LOCAL_FILE);
                    imageBean.setPath(path);
                    mTopicCoverList.clear();
                    mTopicCoverList.add(imageBean);
                    mCoverInputView.notifyInputDataChanged();
                    break;
                default:
                    break;
            }
        }
    }
}
