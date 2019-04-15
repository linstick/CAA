package com.luoruiyong.caa.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;

public class FindPasswordFragment extends Fragment implements View.OnClickListener{

    private ImageView mUserAvatarIv;
    private EditText mCellPhoneNumberEt;
    private EditText mVerifyCodeEt;
    private TextView mResendTv;
    private TextView mConfirmTv;

    private LoginActivity mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginActivity) {
            mCallback = (LoginActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_password, container, false);
        initView(view);
        return view;
    }

    private void initView(View rootView) {
        mUserAvatarIv = rootView.findViewById(R.id.iv_user_avatar);
        mCellPhoneNumberEt = rootView.findViewById(R.id.et_cell_phone_number);
        mVerifyCodeEt = rootView.findViewById(R.id.et_verify_code);
        mResendTv = rootView.findViewById(R.id.tv_resend);
        mConfirmTv = rootView.findViewById(R.id.tv_confirm);

        mResendTv.setOnClickListener(this);
        mConfirmTv.setOnClickListener(this);
    }

    private void checkAndRequestCode() {
        String cellPhoneNumber = mCellPhoneNumberEt.getText().toString().trim();
        if (TextUtils.isEmpty(cellPhoneNumber)) {
            Toast.makeText(getContext(), getString(R.string.fm_find_password_tip_empty_cell_phone_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCallback != null) {
            mCallback.doRequestCode(cellPhoneNumber);
        }
    }

    private void checkAndVerifyAuth() {
        String cellPhoneNumber = mCellPhoneNumberEt.getText().toString().trim();
        String verifyCode = mVerifyCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(cellPhoneNumber)) {
            Toast.makeText(getContext(), getString(R.string.fm_find_password_tip_empty_cell_phone_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cellPhoneNumber)) {
            Toast.makeText(getContext(), getString(R.string.fm_find_password_tip_empty_verify_code), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCallback != null) {
            mCallback.doVerifyAuth(cellPhoneNumber, verifyCode);
        }
    }

    private void checkAndFetchAvatar() {
        String account = mCellPhoneNumberEt.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_resend:
                checkAndRequestCode();
                break;
            case R.id.tv_confirm:
                checkAndVerifyAuth();
                break;
            default:
                break;
        }
    }
}
