package com.luoruiyong.caa.feedback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.OnPermissionCallback;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.CommentData;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.ImageBean;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.edit.BaseCreateFragment;
import com.luoruiyong.caa.edit.CreateActivityFragment;
import com.luoruiyong.caa.edit.EditorActivity;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.PictureUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.luoruiyong.caa.utils.PageUtils.FEEDBACK_TYPE_IMPEACH_ACTIVITY;
import static com.luoruiyong.caa.utils.PageUtils.FEEDBACK_TYPE_IMPEACH_COMMENT;
import static com.luoruiyong.caa.utils.PageUtils.FEEDBACK_TYPE_IMPEACH_DISCOVER;
import static com.luoruiyong.caa.utils.PageUtils.FEEDBACK_TYPE_IMPEACH_TOPIC;
import static com.luoruiyong.caa.utils.PageUtils.FEEDBACK_TYPE_IMPEACH_USER;
import static com.luoruiyong.caa.utils.PageUtils.FEEDBACK_TYPE_SUGGESTION_OR_PROBLEM;
import static com.luoruiyong.caa.utils.PageUtils.KEY_FEEDBACK_PAGE_DATA;
import static com.luoruiyong.caa.utils.PageUtils.KEY_FEEDBACK_PAGE_TYPE;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class ImpeachFragment extends BaseCreateFragment implements
        EditorActivity.OnActionBarClickListener,
        DynamicInputView.OnContentViewClickListener,
        ImageViewLayout.OnImageClickListener {


    private DynamicInputView mTypeInputView;
    private DynamicInputView mContentInputView;
    private DynamicInputView mPictureInputView;

    private List<DynamicInputView> mCheckEmptyList;
    private List<DynamicInputView> mCheckNonNullList;

    private int mType;
    private ActivityData mTargetActivity;
    private TopicData mTargetTopic;
    private DiscoverData mTargetDiscover;
    private User mTargetUser;
    private CommentData mTargetComment;

    private List<ImageBean> mPictureDataList;

    private FeedbackActivity mActivity;

    private StoragePermissionCallback mStoragePermissionCallback = new StoragePermissionCallback();

    public static ImpeachFragment newInstance(int type) {
        return newInstance(type, null);
    }

    public static ImpeachFragment newInstance(Serializable data) {
        return newInstance(-1, data);
    }

    public static ImpeachFragment newInstance(int type, Serializable data) {
        ImpeachFragment fm = new ImpeachFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FEEDBACK_PAGE_TYPE, type);
        bundle.putSerializable(KEY_FEEDBACK_PAGE_DATA, data);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FeedbackActivity) {
            mActivity = (FeedbackActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_feedback, container, false);

        initView(view);

        handleArguments();

        return view;
    }

    private void initView(View view) {
        mTypeInputView = view.findViewById(R.id.input_view_type);
        mContentInputView = view.findViewById(R.id.input_view_content);
        mPictureInputView = view.findViewById(R.id.input_view_picture);

        mPictureInputView.setOnContentViewClickListener(this);
        mPictureInputView.setOnImageClickListener(this);

        mCheckNonNullList = new ArrayList<>();
        mCheckNonNullList.add(mTypeInputView);

        mCheckEmptyList = new ArrayList<>();
        mCheckEmptyList.add(mTypeInputView);
        mCheckEmptyList.add(mContentInputView);
        mCheckEmptyList.add(mPictureInputView);

        mPictureDataList = new ArrayList<>();
        ImageBean imageBean = new ImageBean();
        imageBean.setType(ImageBean.TYPE_RESOURCE_ID);
        imageBean.setResId(R.drawable.bg_add_picture);
        mPictureDataList.add(imageBean);
        mPictureInputView.setPictureDataList(mPictureDataList);
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        Serializable data = null;
        if (bundle == null || ((mType = bundle.getInt(KEY_FEEDBACK_PAGE_TYPE, -1)) == -1 && (data = bundle.getSerializable(KEY_FEEDBACK_PAGE_DATA)) == null)){
            // 启动参数不合理
            finish();
            return;
        }
        if (mType == FEEDBACK_TYPE_SUGGESTION_OR_PROBLEM) {
            mCheckNonNullList.add(mContentInputView);
            if (mActivity != null) {
                mActivity.setTitle(R.string.title_feedback);
            }
            return;
        }
        int titleResId = -1;
        if (data instanceof ActivityData) {
            mType = FEEDBACK_TYPE_IMPEACH_ACTIVITY;
            mTargetActivity = (ActivityData) data;
            titleResId = R.string.title_impeach_activity;
        } else if (data instanceof TopicData) {
            mType = FEEDBACK_TYPE_IMPEACH_TOPIC;
            mTargetTopic = (TopicData) data;
            titleResId = R.string.title_impeach_topic;
        } else if (data instanceof DiscoverData) {
            mType = FEEDBACK_TYPE_IMPEACH_DISCOVER;
            mTargetDiscover = (DiscoverData) data;
            titleResId = R.string.title_impeach_discover;
        }else if (data instanceof User) {
            mType = FEEDBACK_TYPE_IMPEACH_USER;
            mTargetUser = (User) data;
            titleResId = R.string.title_impeach_user;
        } else if (data instanceof CommentData) {
            mType = FEEDBACK_TYPE_IMPEACH_COMMENT;
            mTargetComment = (CommentData) data;
            titleResId = R.string.title_impeach_comment;
        } else {
            finish();
            return;
        }
        if (mActivity != null && titleResId != -1) {
            mActivity.setTitle(titleResId);
        }
        mTypeInputView.setLabelAndHintText(getString(R.string.fm_impeach_str_impeach_type));
        mTypeInputView.setSpinnerEntries(Arrays.asList(ResourcesUtils.getStringArray(R.array.str_array_impeach_type)));
        mContentInputView.setLabelAndHintText(getString(R.string.fm_impeach_str_impeach_description));
        mContentInputView.setNullable(true);
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

            // do send

        } else {
            Toast.makeText(getContext(), "can not send", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onContentViewClick(View v) {
        switch (v.getId()) {
            case R.id.input_view_picture:
                if (mPictureInputView.isImageEmpty()) {
                    requestStoragePermission(mStoragePermissionCallback);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onImageClick(View parent, int position) {
        if (position + 1 == mPictureDataList.size()) {
            // 添加图片
            requestStoragePermission(mStoragePermissionCallback);
        } else {
            // 查看图片
            List<String> list = ImageBean.toStringList(mPictureDataList);
            // 需要移除最后的那一张添加
            if (ListUtils.getSize(list) > 1) {
                list.remove(list.size() - 1);
            }
            PictureBrowseActivity.startAction(this, list, position, true, EditorActivity.BROWSE_PICTURE_REQUEST_CODE);
        }
    }

    class StoragePermissionCallback implements OnPermissionCallback {
        @Override
        public void onGranted() {
            PageUtils.gotoSystemAlbum(ImpeachFragment.this, BaseCreateFragment.REQUEST_CHOOSE_PICTURE_CODE);
        }

        @Override
        public void onDenied() {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CHOOSE_PICTURE_CODE:
                    String path = PictureUtils.getPath(getContext(), data.getData());
                    ImageBean imageBean = new ImageBean();
                    imageBean.setType(ImageBean.TYPE_LOCAL_FILE);
                    imageBean.setPath(path);
                    mPictureDataList.add(mPictureDataList.size() - 1, imageBean);
                    mPictureInputView.notifyInputDataChanged();
                    break;
            }
        }
    }
}
