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
import android.view.ViewStub;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseCreateFragment;
import com.luoruiyong.caa.common.callback.OnPermissionCallback;
import com.luoruiyong.caa.bean.DiscoverCreateResult;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.ImageBean;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.edit.EditorActivity.OnActionBarClickListener;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.location.LocationActivity;
import com.luoruiyong.caa.model.CommonPoster;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.topic.TopicSearchActivity;
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
public class CreateDiscoverFragment extends BaseCreateFragment implements
        OnActionBarClickListener,
        DynamicInputView.OnContentViewClickListener,
        ImageViewLayout.OnImageClickListener {

    private DynamicInputView mContentInputView;
    private DynamicInputView mRelatedTopicInputView;
    private DynamicInputView mLocationInputView;
    private DynamicInputView mIntroduceInputView;
    private DynamicInputView mTopicCoverInputView;
    private ViewStub mRelateTopicExtrasViewStub;
    private View mRelateTopicExtrasContainer;

    private List<DynamicInputView> mCheckEmptyList;
    private List<ImageBean> mPictureDataList;
    private List<ImageBean> mTopicCoverList;

    private int mRelateTopicId = -1;
    private int mRelateTopicType = TopicSearchActivity.RELATE_TOPIC_TYPE_NONE;
    private int mChoosePictureRequestCode = -1;
    private OnPermissionCallback mStoragePermissionCallback = new StoragePermissionCallback();

    public static CreateDiscoverFragment newInstance(int topicId, String topicName) {
        Bundle bundle = new Bundle();
        bundle.putInt(EditorActivity.KEY_TOPIC_ID, topicId);
        bundle.putString(EditorActivity.KEY_TOPIC_NAME, topicName);
        CreateDiscoverFragment fm = new CreateDiscoverFragment();
        fm.setArguments(bundle);
        return fm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_create_discover, container, false);

        initView(view);

        handleArgument();

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
        mContentInputView = rootView.findViewById(R.id.input_view_content);
        mRelatedTopicInputView = rootView.findViewById(R.id.input_view_related_topic);
        mLocationInputView = rootView.findViewById(R.id.input_view_location);
        mRelateTopicExtrasViewStub = rootView.findViewById(R.id.vs_create_relate_topic_extras);

        mContentInputView.setOnImageClickListener(this);
        mRelatedTopicInputView.setOnContentViewClickListener(this);
        mLocationInputView.setOnContentViewClickListener(this);

        mCheckEmptyList = new ArrayList<>();
        mCheckEmptyList.add(mContentInputView);
        mCheckEmptyList.add(mRelatedTopicInputView);
        mCheckEmptyList.add(mLocationInputView);

        ImageBean imageBean = new ImageBean();
        imageBean.setType(ImageBean.TYPE_RESOURCE_ID);
        imageBean.setResId(R.drawable.bg_choose_picture);
        mPictureDataList = new ArrayList<>();
        mPictureDataList.add(imageBean);
        mContentInputView.setPictureDataList(mPictureDataList);
    }

    private void handleArgument() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        int topicId = bundle.getInt(EditorActivity.KEY_TOPIC_ID, -1);
        String topicName = bundle.getString(EditorActivity.KEY_TOPIC_NAME);
        if (topicId != -1 && !TextUtils.isEmpty(topicName)) {
            mRelateTopicType = TopicSearchActivity.RELATE_TOPIC_TYPE_SELECT;
            mRelateTopicId = topicId;
            mRelatedTopicInputView.setInputText(topicName);
        }
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
                mChoosePictureRequestCode = REQUEST_CHOOSE_COVER_CODE;
                requestStoragePermission(mStoragePermissionCallback);
            }
        });

        mTopicCoverList = new ArrayList<>();
        mTopicCoverInputView.setPictureDataList(mTopicCoverList);

        mTopicCoverInputView.setOnContentViewClickListener(this);
        mTopicCoverInputView.setSupportAllChildDelete(true);

        mCheckEmptyList.add(mIntroduceInputView);
        mCheckEmptyList.add(mTopicCoverInputView);
    }

    private void resetRelateTopicExtras() {
        if (mRelateTopicExtrasContainer == null) {
            return;
        }
        mIntroduceInputView.setInputText(null);
        mTopicCoverList.clear();
        mTopicCoverInputView.notifyInputDataChanged();
    }

    private void handleChooseRelateTopicResult(Intent data) {
        String topicName = null;
        mRelateTopicType = data.getIntExtra(TopicSearchActivity.KEY_RESULT_TYPE, -1);
        if (mRelateTopicType == TopicSearchActivity.RELATE_TOPIC_TYPE_CREATE) {
            topicName = data.getStringExtra(TopicSearchActivity.KEY_CREATE_TOPIC_NAME);
            inflateRelateTopicExtras();
            resetRelateTopicExtras();
            mRelateTopicExtrasContainer.setVisibility(View.VISIBLE);
        } else if (mRelateTopicType == TopicSearchActivity.RELATE_TOPIC_TYPE_SELECT) {
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
        if (!mContentInputView.checkAndShowErrorTipIfNeed()) {
            return;
        }
        DiscoverData discover = new DiscoverData();
        discover.setUid(Enviroment.getCurUid());
        discover.setContent(mContentInputView.getInputText());
        discover.setPictureList(mContentInputView.getPictureUrls());
        discover.setLocation(mLocationInputView.getInputText());
        TopicData topic = null;
        if (mRelateTopicType == TopicSearchActivity.RELATE_TOPIC_TYPE_SELECT) {
            discover.setTopic(mRelatedTopicInputView.getInputText());
            discover.setTopicId(mRelateTopicId);
        } else if (mRelateTopicType == TopicSearchActivity.RELATE_TOPIC_TYPE_CREATE) {
            topic = new TopicData();
            topic.setUid(Enviroment.getCurUid());
            topic.setName(mRelatedTopicInputView.getInputText());
            topic.setIntroduction(mIntroduceInputView.getInputText());
            topic.setCover(ListUtils.getSize(mTopicCoverList) > 0 ? mTopicCoverList.get(0).toString() : "");
        }

        showLoadingDialog(R.string.common_tip_on_publish);
        CommonPoster.doCreateDiscover(discover, topic);
    }

    @Override
    public void onContentViewClick(View v) {
        switch (v.getId()) {
            case R.id.input_view_related_topic:
                startActivityForResult(new Intent(getContext(), TopicSearchActivity.class), EditorActivity.RELATE_TOPIC_REQUEST_CODE);
                break;
            case R.id.input_view_location:
                startActivityForResult(new Intent(getContext(), LocationActivity.class), EditorActivity.CHOOSE_LOCATION_REQUEST_CODE);
                break;
            case R.id.input_view_topic_cover:
                mChoosePictureRequestCode = REQUEST_CHOOSE_COVER_CODE;
                requestStoragePermission(new OnPermissionCallback() {
                    @Override
                    public void onGranted() {
                        startActivityForResult(new Intent(getContext(), LocationActivity.class), EditorActivity.CHOOSE_LOCATION_REQUEST_CODE);
                    }

                    @Override
                    public void onDenied() {
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onImageClick(View parent, int position) {
        if (position + 1 == mPictureDataList.size()) {
            // 添加图片
            mChoosePictureRequestCode = REQUEST_CHOOSE_PICTURE_CODE;
            requestStoragePermission(mStoragePermissionCallback);
        } else {
            // 查看图片
            List<String> list = ImageBean.toStringList(mPictureDataList);
            if (ListUtils.getSize(list) > 1) {
                list.remove(list.size() - 1);
            }
            PictureBrowseActivity.startAction(this, list, position, true, EditorActivity.BROWSE_PICTURE_REQUEST_CODE);
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
                        mPictureDataList.remove(index);
                    }
                    mContentInputView.notifyInputDataChanged();
                    break;
                case EditorActivity.RELATE_TOPIC_REQUEST_CODE:
                    handleChooseRelateTopicResult(data);
                    break;
                case EditorActivity.CHOOSE_LOCATION_REQUEST_CODE:
                    String location = data.getStringExtra(LocationActivity.KEY_LOCATION_INFO);
                    mLocationInputView.setInputText(location);
                    break;
                case REQUEST_CHOOSE_PICTURE_CODE:
                case REQUEST_CHOOSE_COVER_CODE:
                    Uri uri = data.getData();
                    String path = PictureUtils.getPath(getContext(), uri);
                    ImageBean imageBean = new ImageBean();
                    imageBean.setType(ImageBean.TYPE_LOCAL_FILE);
                    imageBean.setPath(path);
                    if (requestCode == REQUEST_CHOOSE_PICTURE_CODE) {
                        mPictureDataList.add(mPictureDataList.size() - 1, imageBean);
                        mContentInputView.notifyInputDataChanged();
                    } else {
                        mTopicCoverList.clear();
                        mTopicCoverList.add(imageBean);
                        mTopicCoverInputView.notifyInputDataChanged();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentEvent(CommonEvent<DiscoverCreateResult> event) {
        switch (event.getType()) {
            case CREATE_DISCOVER:
                hideLoadingDialog();
                if (event.getCode() == Config.CODE_OK) {
                    DiscoverData discover = event.getData().getDiscover();
                    TopicData topic = event.getData().getTopic();
                    toast(topic == null ? R.string.fm_create_discover_tip_publish_success : R.string.fm_create_discover_tip_publish_and_topic_success);
                    finish();
                } else {
                    toast(event.getStatus());
                }
                break;
            default:
                break;
        }
    }

    class StoragePermissionCallback implements OnPermissionCallback {
        @Override
        public void onGranted() {
            PageUtils.gotoSystemAlbum(CreateDiscoverFragment.this, mChoosePictureRequestCode);
        }

        @Override
        public void onDenied() {

        }
    }
}
