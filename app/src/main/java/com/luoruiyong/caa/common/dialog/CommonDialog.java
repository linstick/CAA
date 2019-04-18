package com.luoruiyong.caa.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;

import java.util.Arrays;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/13/013
 **/
public class CommonDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener{

    public final static int TYPE_NORMAL = 1;
    public final static int TYPE_INPUT = 2;
    public final static int TYPE_LIST = 3;
    public final static int TYPE_LOADING = 4;

    private TextView mTitleTv;
    private TextView mMessageTv;
    private TextView mPositiveTv;
    private TextView mNegativeTv;
    private EditText mInputEt;
    private View mInputBottomLineView;
    private ListView mListView;
    private View mLoadingLayout;
    private TextView mLoadingTv;

    private Builder mBuilder;

    private CommonDialog(@NonNull Context context) {
        this(context, R.style.CustomDialogTheme);
    }

    private CommonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private void apply(Builder builder) {
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        initView();
    }

    private void initView() {
        mTitleTv = findViewById(R.id.tv_title);
        mMessageTv = findViewById(R.id.tv_message);
        mNegativeTv = findViewById(R.id.tv_negative);
        mPositiveTv = findViewById(R.id.tv_positive);
        mInputEt = findViewById(R.id.et_input);
        mInputBottomLineView = findViewById(R.id.view_input_bottom_line);
        mListView = findViewById(R.id.lv_list_view);
        mLoadingLayout = findViewById(R.id.ll_loading_layout);
        mLoadingTv = findViewById(R.id.tv_loading_tip);

        if (mBuilder == null) {
            return;
        }

        if (!TextUtils.isEmpty(mBuilder.mTitle)) {
            mTitleTv.setText(mBuilder.mTitle);
            mTitleTv.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(mBuilder.mMessage)) {
            mMessageTv.setText(mBuilder.mMessage);
            mMessageTv.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(mBuilder.mPositive)) {
            mPositiveTv.setText(mBuilder.mPositive);
            mPositiveTv.setVisibility(View.VISIBLE);
            mPositiveTv.setOnClickListener(this);
        }

        if (!TextUtils.isEmpty(mBuilder.mNegative)) {
            mNegativeTv.setText(mBuilder.mNegative);
            mNegativeTv.setVisibility(View.VISIBLE);
            mNegativeTv.setOnClickListener(this);
        }

        if (!TextUtils.isEmpty(mBuilder.mLoadingTip)) {
            mLoadingTv.setText(mBuilder.mLoadingTip);
        }

        if (mBuilder.mHasTitleColorSet) {
            mTitleTv.setTextColor(getContext().getResources().getColor(mBuilder.mTitleColor));
        }

        if (mBuilder.mHasMessageColorSet) {
            mMessageTv.setTextColor(getContext().getResources().getColor(mBuilder.mMessageColor));
        }

        if (mBuilder.mHasPositiveColorSet) {
            mPositiveTv.setTextColor(getContext().getResources().getColor(mBuilder.mPositiveColor));
        }

        if (mBuilder.mHasNegativeColorSet) {
            mNegativeTv.setTextColor(getContext().getResources().getColor(mBuilder.mNegativeColor));
        }

        if (mBuilder.mType == TYPE_INPUT) {
            mInputEt.setVisibility(View.VISIBLE);
            mInputBottomLineView.setVisibility(View.VISIBLE);
            mInputEt.setHint(mBuilder.mInputHint);
            mInputEt.setText(mBuilder.mPreInputText);
            mInputEt.setSelection(mBuilder.mPreInputText == null ? 0 : mBuilder.mPreInputText.length());
            if (mBuilder.mHasInputTypeSet) {
                mInputEt.setInputType(mBuilder.mInputType);
            }
        }

        if (mBuilder.mType == TYPE_LIST && mBuilder.mItems != null && mBuilder.mItems.size() > 0) {
            mListView.setAdapter(new ListAdapter(mBuilder.mItems));
            mListView.setOnItemClickListener(this);
            mListView.setVisibility(View.VISIBLE);
        }

        if (mBuilder.mType == TYPE_LOADING) {
            mLoadingLayout.setVisibility(View.VISIBLE);
        }

        setCanceledOnTouchOutside(mBuilder.mCancelTouchOutside);
        setCancelable(mBuilder.mCancelable);
        setOnDismissListener(mBuilder.mDismissListener);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_positive:
                String text = mInputEt.getText().toString().trim();
                if (mBuilder.mType == TYPE_INPUT && TextUtils.isEmpty(text)) {
                    Toast.makeText(getContext(), getContext().getString(R.string.common_dialog_tip_empty_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mBuilder.mAutoDismiss) {
                    dismiss();
                }
                if (mBuilder.mPositiveListener != null) {
                    mBuilder.mPositiveListener.onClick(text);
                }
                break;
            case R.id.tv_negative:
                if (mBuilder.mAutoDismiss) {
                    dismiss();
                }
                if (mBuilder.mNegativeListener != null) {
                    mBuilder.mNegativeListener.onClick(mBuilder.mType == TYPE_INPUT ? mInputEt.getText().toString().trim() : null);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mBuilder.mAutoDismiss) {
            dismiss();
        }
        if (mBuilder.mListItemListener != null) {
            mBuilder.mListItemListener.onItemClick(position);
        }
    }

    private class ListAdapter extends BaseAdapter{

        private List<String> mList;

        public ListAdapter(List<String> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView itemView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_list, parent, false);
            itemView.setText(mList.get(position));
            return itemView;
        }
    }

    public static class Builder {

        private Context mContext;
        private int mType;
        private boolean mCancelTouchOutside = true;
        private boolean mCancelable = true;

        private String mTitle;
        private String mNegative;
        private String mPositive;

        private @ColorRes int mTitleColor;
        private @ColorRes int mMessageColor;
        private @ColorRes int mNegativeColor;
        private @ColorRes int mPositiveColor;

        private boolean mHasTitleColorSet;
        private boolean mHasMessageColorSet;
        private boolean mHasNegativeColorSet;
        private boolean mHasPositiveColorSet;
        private boolean mHasInputTypeSet;

        private boolean mAutoDismiss = true;

        private String mMessage;
        private String mInputHint;
        private String mPreInputText;
        private int mInputType;
        private List<String> mItems;
        private String mLoadingTip;

        private OnClickListener mNegativeListener;
        private OnClickListener mPositiveListener;
        private OnItemClickListener mListItemListener;
        private OnDismissListener mDismissListener;

        public Builder(Context context) {
            this.mContext = context;
            this.mType = TYPE_NORMAL;
        }

        public Builder type(int type) {
            this.mType = type;
            return this;
        }

        public Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder message(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder inputHint(String hint) {
            this.mInputHint = hint;
            return this;
        }

        public Builder inputType(int inputType) {
            this.mInputType = inputType;
            this.mHasInputTypeSet = true;
            return this;
        }

        public Builder preInputText(String text) {
            this.mPreInputText = text;
            return this;
        }

        public Builder loadingTip(String text) {
            this.mLoadingTip = text;
            return this;
        }

        public Builder items(String[] items) {
            this.mItems = Arrays.asList(items);
            return this;
        }

        public Builder items(List<String> items) {
            this.mItems = items;
            return this;
        }

        public Builder negative(String text) {
            this.mNegative = text;
            return this;
        }

        public Builder positive(String text) {
            this.mPositive = text;
            return this;
        }

        public Builder titleColor(@ColorRes int color) {
            this.mTitleColor = color;
            this.mHasTitleColorSet = true;
            return this;
        }

        public Builder messageColor(@ColorRes int color) {
            this.mMessageColor = color;
            this.mHasMessageColorSet = true;
            return this;
        }

        public Builder nagativeColor(@ColorRes int color) {
            this.mNegativeColor = color;
            this.mHasNegativeColorSet = true;
            return this;
        }

        public Builder positiveColor(@ColorRes int color) {
            this.mPositiveColor = color;
            this.mHasPositiveColorSet = true;
            return this;
        }

        public Builder onNegative(OnClickListener listener) {
            mNegativeListener = listener;
            return this;
        }

        public Builder onPositive(OnClickListener listener) {
            mPositiveListener = listener;
            return this;
        }

        public Builder onDismiss(OnDismissListener listener) {
            mDismissListener = listener;
            return this;
        }

        public Builder onItem(OnItemClickListener listener) {
            mListItemListener = listener;
            return this;
        }

        public Builder autoDismiss(boolean autoDismiss) {
            this.mAutoDismiss = autoDismiss;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder cancelTouchOutside(boolean cancelable) {
            this.mCancelTouchOutside = cancelable;
            return this;
        }

        public CommonDialog build() {
            CommonDialog dialog = new CommonDialog(mContext);
            dialog.apply(this);
            return dialog;
        }

        public interface OnClickListener {
            void onClick(String extras);
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }
}
