package com.luoruiyong.caa.edit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.ImageViewLayoutV2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class CreateActivityFragment extends Fragment implements
        View.OnClickListener, View.OnFocusChangeListener{

    private TextView mTypeLabelTv;
    private TextView mTypeTv;
    private ImageView mUpAndDownIv;
    private TextView mTitleLabelTv;
    private EditText mTitleInputEt;
    private TextView mContentLabelTv;
    private EditText mContentInputEt;
    private TextView mHostLabelTv;
    private EditText mHostInputEt;
    private TextView mTimeLabelTv;
    private EditText mTimeInputEt;
    private TextView mAddressLabelTv;
    private EditText mAddressInputEt;
    private TextView mRemarkLabelTv;
    private EditText mRemarkInputEt;
    private TextView mRelatedTopicLabelTv;
    private TextView mRelatedTopicTv;
    private TextView mLocationLabelTv;
    private TextView mLocationTv;
    private ImageViewLayoutV2 mImageViewLayout;

    private List<String> mActivityTypeList;
    private int mActivityType = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_create_activity, container, false);

        initView(view);

        return view;
    }

    private void initView(View rootView) {
        mTypeLabelTv = rootView.findViewById(R.id.tv_activity_type_label);
        mTypeTv = rootView.findViewById(R.id.tv_activity_type);
        mUpAndDownIv = rootView.findViewById(R.id.iv_up_and_down);
        mTitleLabelTv = rootView.findViewById(R.id.tv_activity_title_label);
        mTitleInputEt = rootView.findViewById(R.id.et_activity_title_input);
        mContentLabelTv = rootView.findViewById(R.id.tv_activity_content_label);
        mContentInputEt = rootView.findViewById(R.id.et_activity_content_input);
        mHostLabelTv = rootView.findViewById(R.id.tv_activity_host_label);
        mHostInputEt = rootView.findViewById(R.id.et_activity_host_input);
        mTimeLabelTv = rootView.findViewById(R.id.tv_activity_time_label);
        mTimeInputEt = rootView.findViewById(R.id.et_activity_time_input);
        mAddressLabelTv = rootView.findViewById(R.id.tv_activity_address_label);
        mAddressInputEt = rootView.findViewById(R.id.et_activity_address_input);
        mRemarkLabelTv = rootView.findViewById(R.id.tv_activity_remark_label);
        mRemarkInputEt = rootView.findViewById(R.id.et_activity_remark_input);
        mRelatedTopicLabelTv = rootView.findViewById(R.id.tv_related_topic_label);
        mRelatedTopicTv = rootView.findViewById(R.id.tv_related_topic);
        mLocationLabelTv = rootView.findViewById(R.id.tv_location_label);
        mLocationTv = rootView.findViewById(R.id.tv_location);
        mImageViewLayout = rootView.findViewById(R.id.image_view_layout);

        mTypeTv.setOnClickListener(this);
        mTypeLabelTv.setOnClickListener(this);
        mUpAndDownIv.setOnClickListener(this);
        mRelatedTopicTv.setOnClickListener(this);
        mLocationTv.setOnClickListener(this);

        mTitleInputEt.setOnFocusChangeListener(this);
        mContentInputEt.setOnFocusChangeListener(this);
        mHostInputEt.setOnFocusChangeListener(this);
        mTimeInputEt.setOnFocusChangeListener(this);
        mAddressInputEt.setOnFocusChangeListener(this);
        mRemarkInputEt.setOnFocusChangeListener(this);
    }

    private void showActivityTypeDialog() {
        mTypeLabelTv.setVisibility(View.VISIBLE);
        mUpAndDownIv.setSelected(true);
        mTypeTv.setHint(null);
        if (mActivityTypeList == null) {
            String[] array = ResourcesUtils.getStringArray(R.array.str_array_activity_type);
            mActivityTypeList = new ArrayList<>(array.length - 1);
            for (int i = 1; i < array.length; i++) {
                mActivityTypeList.add(array[i]);
            }
        }
        new CommonDialog.Builder(getContext())
                .type(CommonDialog.TYPE_LIST)
                .title(null)
                .items(mActivityTypeList)
                .onItem(new CommonDialog.Builder.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        mActivityType = position + 1;
                        mTypeTv.setText(mActivityTypeList.get(position));
                        mUpAndDownIv.setSelected(false);
                    }
                })
                .onDismiss(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (mActivityType == -1) {
                            mTypeTv.setHint(R.string.fm_create_activity_str_type);
                            mTypeLabelTv.setVisibility(View.GONE);
                        }
                        mUpAndDownIv.setSelected(false);
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_type:
            case R.id.tv_activity_type_label:
            case R.id.iv_up_and_down:
                showActivityTypeDialog();
                break;
            case R.id.tv_related_topic:
                Toast.makeText(getContext(), "点击关联话题", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_location:
                Toast.makeText(getContext(), "点击定位", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_activity_title_input:
               onFocusChanged(mTitleInputEt, mTitleLabelTv, hasFocus, R.string.fm_create_activity_str_title);
                break;
            case R.id.et_activity_content_input:
                onFocusChanged(mContentInputEt, mContentLabelTv, hasFocus, R.string.fm_create_activity_str_content);
                break;
            case R.id.et_activity_host_input:
                onFocusChanged(mHostInputEt, mHostLabelTv, hasFocus, R.string.common_str_host);
                break;
            case R.id.et_activity_time_input:
                onFocusChanged(mTimeInputEt, mTimeLabelTv, hasFocus, R.string.common_str_time);
                break;
            case R.id.et_activity_address_input:
                onFocusChanged(mAddressInputEt, mAddressLabelTv, hasFocus, R.string.common_str_address);
                break;
            case R.id.et_activity_remark_input:
                onFocusChanged(mRemarkInputEt, mRemarkLabelTv, hasFocus, R.string.common_str_remark);
                break;
            default:
                break;
        }
    }

    private void onFocusChanged(EditText editText, TextView labelText, boolean hasFocus, @StringRes int hintResId) {
        if (hasFocus) {
            editText.setHint(null);
            labelText.setVisibility(View.VISIBLE);
        } else {
            editText.setHint(hintResId);
            labelText.setVisibility(TextUtils.isEmpty(editText.getText()) ? View.GONE : View.VISIBLE);
        }
    }
}
