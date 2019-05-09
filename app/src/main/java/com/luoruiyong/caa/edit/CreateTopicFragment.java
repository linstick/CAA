package com.luoruiyong.caa.edit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.MyApplication;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.OnPermissionCallback;
import com.luoruiyong.caa.bean.ImageBean;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.CommonChecker;
import com.luoruiyong.caa.model.CommonPoster;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.PictureUtils;
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class CreateTopicFragment extends BaseCreateFragment implements
        DynamicInputView.OnFocusChangedListener,
        DynamicInputView.OnTextChangedListener,
        EditorActivity.OnActionBarClickListener,
        DynamicInputView.OnContentViewClickListener,
        ImageViewLayout.OnImageClickListener {

    private DynamicInputView mNameInputView;
    private DynamicInputView mIntroduceInputView;
    private DynamicInputView mCoverInputView;

    private List<DynamicInputView> mCheckEmptyList;
    private List<ImageBean> mTopicCoverList;
    private String mLastTopicName;

    private boolean mHasCheckName;
    private boolean mIsNameExists;

    private OnPermissionCallback mStoragePermissionCallback = new StoragePermissionCallback();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_create_tag, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View rootView) {
        mNameInputView = rootView.findViewById(R.id.input_view_topic_name);
        mIntroduceInputView = rootView.findViewById(R.id.input_view_topic_introduce);
        mCoverInputView = rootView.findViewById(R.id.input_view_topic_cover);

        mNameInputView.setOnFocusChangedListener(this);
        mNameInputView.setOnTextChangedListener(this);
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
    public void onFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            String text = mNameInputView.getInputText();
            if (!TextUtils.isEmpty(text)) {
                checkTopicName(text);
            }
        }
    }

    @Override
    public void onTextChanged(String text) {
        if (mHasCheckName && !TextUtils.equals(text, mLastTopicName)) {
            mHasCheckName = false;
            mNameInputView.setErrorText(getString(R.string.common_tip_no_empty), false);
        }
    }

    private void checkTopicName(String name) {
        if (TextUtils.equals(name, mLastTopicName)) {
            return;
        }
        mLastTopicName = name;
        CommonChecker.doCheckTopicName(name);
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
        if (!mNameInputView.checkAndShowErrorTipIfNeed()) {
            return;
        }
        TopicData topic = new TopicData();
        topic.setUid(Enviroment.getCurUid());
        topic.setName(mNameInputView.getInputText());
        topic.setIntroduction(mIntroduceInputView.getInputText());
        topic.setCover(ListUtils.getSize(mTopicCoverList) > 0 ? mTopicCoverList.get(0).toString() : null);

        showLoadingDialog(R.string.common_tip_on_publish);
        CommonPoster.doCreateTopic(topic);
    }

    @Override
    public void onImageClick(View parent, int position) {
        // 选择封面图片
        requestStoragePermission(mStoragePermissionCallback);
    }

    @Override
    public void onContentViewClick(View v) {
        requestStoragePermission(mStoragePermissionCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentEvent(CommonEvent event) {
        switch (event.getType()) {
            case CHECK_TOPIC_NAME:
                String text = mNameInputView.getInputText();
                if (TextUtils.equals(mLastTopicName, text)) {
                    if (event.getCode() == Config.CODE_OK) {
                        mHasCheckName = true;
                        mIsNameExists = (boolean) event.getData();
                        if (mIsNameExists) {
                            mNameInputView.setErrorText(getString(R.string.fm_create_topic_tip_topic_name_exists), true);
                        }
                    }
                }
                break;
            case CREATE_TOPIC:
                if (event.getCode() == Config.CODE_OK) {
                    TopicData topic = (TopicData) event.getData();
                    Toast.makeText(MyApplication.getAppContext(), R.string.fm_create_topic_tip_publish_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    hideLoadingDialog();
                    Toast.makeText(getContext(), event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    class StoragePermissionCallback implements OnPermissionCallback {
        @Override
        public void onGranted() {
            PageUtils.gotoSystemAlbum(CreateTopicFragment.this, REQUEST_CHOOSE_COVER_CODE);
        }

        @Override
        public void onDenied() {

        }
    }
}
