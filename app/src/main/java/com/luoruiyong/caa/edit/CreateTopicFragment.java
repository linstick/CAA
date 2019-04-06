package com.luoruiyong.caa.edit;

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
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class CreateTopicFragment extends BaseCreateFragment implements
        DynamicInputView.OnFocusLostOrTextChangeListener,
        EditorActivity.OnActionBarClickListener{

    private DynamicInputView mNameInputView;
    private DynamicInputView mIntroduceInputView;
    private DynamicInputView mCoverInputView;

    private List<DynamicInputView> mCheckEmptyList;

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

        mCheckEmptyList = new ArrayList<>();
        mCheckEmptyList.add(mNameInputView);
        mCheckEmptyList.add(mIntroduceInputView);
        mCheckEmptyList.add(mCoverInputView);
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
        if (mNameInputView.check()) {
            Toast.makeText(getContext(), "can send", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "can not send", Toast.LENGTH_SHORT).show();
        }
    }
}
