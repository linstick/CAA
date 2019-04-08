package com.luoruiyong.caa.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.location.LocationActivity;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.topic.TopicSearchActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class CreateActivityFragment extends BaseCreateFragment implements
        ImageViewLayout.OnImageClickListener,
        EditorActivity.OnActionBarClickListener,
        DynamicInputView.OnContentViewClickListener{

    private DynamicInputView mTypeInputView;
    private DynamicInputView mTitleInputView;
    private DynamicInputView mContentInputView;
    private DynamicInputView mHostInputView;
    private DynamicInputView mTimeInputView;
    private DynamicInputView mAddressInputView;
    private DynamicInputView mRemarkInputView;
    private DynamicInputView mRelatedTopicInputView;
    private DynamicInputView mLocationInputView;
    private DynamicInputView mPictureInputView;
    private DynamicInputView mIntroduceInputView;
    private DynamicInputView mTopicCoverInputView;
    private ViewStub mRelateTopicExtrasViewStub;
    private View mRelateTopicExtrasContainer;

    private int mRelateTopicId;
    private int mRelateTopicType = TopicSearchActivity.RELATE_TOPIC_TYPE_NONE;
    private List<String> mPictureUrls;
    private List<String> mTopicCoverUrls;
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
        mPictureInputView = rootView.findViewById(R.id.input_view_picture);
        mRelateTopicExtrasViewStub = rootView.findViewById(R.id.vs_create_relate_topic_extras);

        mRelatedTopicInputView.setOnContentViewClickListener(this);
        mLocationInputView.setOnContentViewClickListener(this);
        mPictureInputView.setOnContentViewClickListener(this);
        mPictureInputView.setOnImageClickListener(this);

        mCheckNonNullList = new ArrayList<>();
        mCheckNonNullList.add(mTypeInputView);
        mCheckNonNullList.add(mTitleInputView);
        mCheckNonNullList.add(mContentInputView);
        mCheckNonNullList.add(mHostInputView);
        mCheckNonNullList.add(mTimeInputView);
        mCheckNonNullList.add(mAddressInputView);

        mCheckEmptyList = new ArrayList<>();
        mCheckEmptyList.addAll(mCheckNonNullList);
        mCheckEmptyList.add(mRemarkInputView);
        mCheckEmptyList.add(mRelatedTopicInputView);
        mCheckEmptyList.add(mLocationInputView);
        mCheckEmptyList.add(mPictureInputView);

        mPictureUrls = new ArrayList<>();
        // 加号引导添加的图片
        mPictureUrls.add("https://www.baidu.com/guide.jpg");
        mPictureInputView.setPictureUrls(mPictureUrls);
    }

    private void inflateRelateTopicExtras() {
        if (mRelateTopicExtrasContainer != null) {
            return;
        }
        mRelateTopicExtrasContainer = mRelateTopicExtrasViewStub.inflate();
        mIntroduceInputView = mRelateTopicExtrasContainer.findViewById(R.id.input_view_topic_introduce);
        mTopicCoverInputView = mRelateTopicExtrasContainer.findViewById(R.id.input_view_topic_cover);

        mTopicCoverInputView.setOnImageClickListener(new ImageViewLayout.OnImageClickListener() {
            @Override
            public void onImageClick(View parent, int position) {

            }
        });
        mTopicCoverInputView.setOnContentViewClickListener(this);

        mCheckEmptyList.add(mIntroduceInputView);
        mCheckEmptyList.add(mTopicCoverInputView);
    }

    private void resetRelateTopicExtras() {
        if (mRelateTopicExtrasContainer == null) {
            return;
        }
        mIntroduceInputView.setInputText(null);
        mTopicCoverUrls = null;
        mTopicCoverInputView.setPictureUrls(null);
    }

    private void handleChooseRelateTopicResult(Intent data) {
        String topicName = null;
        mRelateTopicType = data.getIntExtra(TopicSearchActivity.KEY_RESULT_TYPE, -1);
        if (mRelateTopicType == TopicSearchActivity.RELATE_TOPIC_TYPE_SELECT) {
            topicName = data.getStringExtra(TopicSearchActivity.KEY_CREATE_TOPIC_NAME);
            inflateRelateTopicExtras();
            resetRelateTopicExtras();
            mRelateTopicExtrasContainer.setVisibility(View.VISIBLE);
        } else if (mRelateTopicType == TopicSearchActivity.RELATE_TOPIC_TYPE_CREATE) {
            mRelateTopicId = data.getIntExtra(TopicSearchActivity.KEY_SELECTED_TOPIC_ID, -1);
            topicName = data.getStringExtra(TopicSearchActivity.KEY_SELECTED_TOPIC_NAME);
            if (mRelateTopicExtrasContainer != null) {
                mRelateTopicExtrasContainer.setVisibility(View.GONE);
            }
        } else if (mRelateTopicType == TopicSearchActivity.RELATE_TOPIC_TYPE_NONE) {
            if (mRelateTopicExtrasContainer != null) {
                mRelateTopicExtrasContainer.setVisibility(View.GONE);
            }
        }
        mRelatedTopicInputView.setInputText(topicName);
    }

    @Override
    public void onImageClick(View parent, int position) {
        if (position + 1 == mPictureUrls.size()) {
            // 添加图片
            // for test
            if (Enviroment.VAR_DEBUG) {
                Toast.makeText(getContext(), "选择图片添加", Toast.LENGTH_SHORT).show();
                mPictureUrls.add(mPictureUrls.size() - 1, "https://www.baidu.com/1.jpg");
                mPictureInputView.notifyInputDataChanged();
            }
        } else {
            // 查看图片
            List list = new ArrayList();
            // 需要移除最后的那一张添加
            for (int i = 0; i < mPictureUrls.size() - 1; i++) {
                list.add(mPictureUrls.get(i));
            }
            PictureBrowseActivity.startAction(this, list, position, false, true, EditorActivity.BROWSE_PICTURE_REQUEST_CODE);
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
        boolean canSend = true;
        for (DynamicInputView view : mCheckNonNullList) {
            canSend &= view.checkAndShowErrorTipIfNeed();
        }
        if (canSend) {
            Toast.makeText(getContext(), "can send", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "can not send", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onContentViewClick(View v) {
       switch (v.getId()) {
           case R.id.input_view_picture:
               if (mPictureInputView.isImageEmpty()) {
                   // 第一次点击，响应选择图片

                   if (Enviroment.VAR_DEBUG) {
                       // for test
                       // 成功添加的一张图片
                       mPictureUrls.add("htpps://www.baidu.com/1.jpg");
                       mPictureInputView.notifyInputDataChanged();
                   }
               }
               break;
           case R.id.input_view_related_topic:
               startActivityForResult(new Intent(getContext(), TopicSearchActivity.class), EditorActivity.RELATE_TOPIC_REQUEST_CODE);
               break;
           case R.id.input_view_location:
               startActivityForResult(new Intent(getContext(), LocationActivity.class), EditorActivity.CHOOSE_LOCATION_REQUEST_CODE);
               break;
           case R.id.input_view_topic_cover:
               // for test
               mTopicCoverUrls = new ArrayList<>();
               mTopicCoverUrls.add("htpps://www.baidu.com/1.jpg");
               mTopicCoverInputView.setPictureUrls(mTopicCoverUrls);
               break;
           default:
               break;
       }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case EditorActivity.BROWSE_PICTURE_REQUEST_CODE:
                    List<Integer> deleteList = data.getIntegerArrayListExtra(PictureBrowseActivity.KEY_DELETE_LIST);
                    for (int i = 0; i < ListUtils.getSize(deleteList); i++) {
                        int index = deleteList.get(i);
                        mPictureUrls.remove(index);
                    }
                    mPictureInputView.notifyInputDataChanged();
                    break;
                case EditorActivity.RELATE_TOPIC_REQUEST_CODE:
                    handleChooseRelateTopicResult(data);
                    break;
                case EditorActivity.CHOOSE_LOCATION_REQUEST_CODE:
                    String location = data.getStringExtra(LocationActivity.KEY_LOCATION_INFO);
                    mLocationInputView.setInputText(location);
                    break;
                default:
                    break;
            }
        }
    }
}
