package com.luoruiyong.caa.feedback;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.TagSimpleData;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.edit.BaseCreateFragment;
import com.luoruiyong.caa.edit.EditorActivity;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class ImpeachFragment extends BaseCreateFragment implements
        EditorActivity.OnActionBarClickListener,
        DynamicInputView.OnContentViewClickListener,
        ImageViewLayout.OnImageClickListener {

    public final static String KEY_IMPEACH_DATA = "key_impeach_data";

    public final static int TYPE_FEEDBACK = 0;
    public final static int TYPE_IMPEACH_ACTIVITY = 1;
    public final static int TYPE_IMPEACH_TOPIC = 2;
    public final static int TYPE_IMPEACH_DISCOVER = 3;
    public final static int TYPE_IMPEACH_USER = 4;

    private DynamicInputView mTypeInputView;
    private DynamicInputView mContentInputView;
    private DynamicInputView mPictureInputView;

    private List<DynamicInputView> mCheckEmptyList;
    private List<DynamicInputView> mCheckNonNullList;

    private int mType;
    private ActivitySimpleData mTargetActivity;
    private TagSimpleData mTargetTopic;
    private DiscoverData mTargetDiscover;
    private User mTargetUser;
    private List<String> mPictureUrls;

    private FeedbackActivity mActivity;

    public static ImpeachFragment newInstance(Serializable data) {
        ImpeachFragment fm = new ImpeachFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_IMPEACH_DATA, data);
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

        mPictureUrls = new ArrayList<>();
        mPictureUrls.add("https://www.baidu.com/1.jpg");
        mPictureInputView.setPictureUrls(mPictureUrls);
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        Serializable data;
        if (bundle == null || (data = bundle.getSerializable(KEY_IMPEACH_DATA)) == null) {
            mType = TYPE_FEEDBACK;
            mCheckNonNullList.add(mContentInputView);
            if (mActivity != null) {
                mActivity.setTitle(R.string.title_feedback);
            }
            return;
        }
        int resId = -1;
        if (data instanceof ActivitySimpleData) {
            mType = TYPE_IMPEACH_ACTIVITY;
            mTargetActivity = (ActivitySimpleData) data;
            resId = R.string.title_impeach_activity;
        } else if (data instanceof TagSimpleData) {
            mType = TYPE_IMPEACH_TOPIC;
            mTargetTopic = (TagSimpleData) data;
            resId = R.string.title_impeach_topic;
        } else if (data instanceof DiscoverData) {
            mType = TYPE_IMPEACH_DISCOVER;
            mTargetDiscover = (DiscoverData) data;
            resId = R.string.title_impeach_discover;
        }else if (data instanceof User) {
            mType = TYPE_IMPEACH_USER;
            mTargetUser = (User) data;
            resId = R.string.title_impeach_user;
        } else {
            finish();
        }
        if (mActivity != null) {
            mActivity.setTitle(resId);
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
                    // 第一次点击，响应选择图片

                    if (Enviroment.VAR_DEBUG) {
                        // for test
                        // 成功添加的一张图片
                        mPictureUrls.add("htpps://www.baidu.com/1.jpg");
                        mPictureInputView.notifyInputDataChanged();
                    }
                }
                break;
            default:
                break;
        }
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
}
