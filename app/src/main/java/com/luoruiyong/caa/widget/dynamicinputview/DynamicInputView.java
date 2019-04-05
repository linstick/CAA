package com.luoruiyong.caa.widget.dynamicinputview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.utils.ResourcesUtils;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/5/005
 * Description:
 **/
public class DynamicInputView extends LinearLayout implements View.OnClickListener {
    private final static int TYPE_INPUT = 0;
    private final static int TYPE_TEXT = 1;
    private final static int TYPE_SPINNER = 2;

    private final static int DEFAULT_TYPE = TYPE_INPUT;

    private int mType;
    private String mLabelText;
    private String mErrorText;
    private List<String> mEntries;
    private OnContentViewClickListener mContentViewClickListener;
    private OnSpinnerSelectedListener mSpinnerSelectedListener;

    private TextView mLabelTv;
    private TextView mContentTv;
    private EditText mInputEt;
    private ImageView mUpAndDownIv;
    private TextView mErrorTv;

    public DynamicInputView(Context context) {
        this(context, null);
    }

    public DynamicInputView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setType(int type) {
        mType = type;
    }

    public void setLabelText(String text) {
        mLabelText = text;
        mLabelTv.setText(text);
    }

    public void setErrorText(String text) {
        mErrorText = text;
        mErrorTv.setText(text);
    }

    public void setEntries(List<String> entries) {
        mEntries = entries;
    }

    public void setOnContentViewClickListener (OnContentViewClickListener listener) {
        mContentViewClickListener = listener;
    }

    public void setOnSpinnerSelectedListener (OnSpinnerSelectedListener listener) {
        mSpinnerSelectedListener = listener;
    }

    public void showLabelView() {
        mLabelTv.setVisibility(VISIBLE);
    }

    public void hideLabelView() {
        mLabelTv.setVisibility(GONE);
        if (mType == TYPE_TEXT) {
            if (TextUtils.isEmpty(mContentTv.getText())) {
                mContentTv.setHint(mLabelText);
            }
        }
    }

    public void setInputContent(String content) {
        switch (mType) {
            case TYPE_INPUT:
                mInputEt.setText(content);
                break;
            case TYPE_SPINNER:
            case TYPE_TEXT:
                mContentTv.setText(content);
                break;
        }
    }

    public String getInputContent() {
        String result;
        switch (mType) {
            case TYPE_INPUT:
                result = mInputEt.getText().toString().trim();
                break;
            case TYPE_SPINNER:
            case TYPE_TEXT:
                result = mContentTv.getText().toString().trim();
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    private void init(Context context, AttributeSet attrs) {
        // 读取自定义的配置属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicInputView);
        mType = typedArray.getInt(R.styleable.DynamicInputView_type, DEFAULT_TYPE);
        mLabelText = typedArray.getString(R.styleable.DynamicInputView_labelText);
        mErrorText = typedArray.getString(R.styleable.DynamicInputView_errorText);
        int entriesResId = typedArray.getResourceId(R.styleable.DynamicInputView_entries, -1);
        if (entriesResId != -1) {
            mEntries = Arrays.asList(ResourcesUtils.getStringArray(entriesResId));
        }
        typedArray.recycle();

        // 添加布局
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dynamic_input_view, this, false);
        mLabelTv = view.findViewById(R.id.tv_label);
        mErrorTv = view.findViewById(R.id.tv_error);
        mContentTv = view.findViewById(R.id.tv_content);
        mInputEt = view.findViewById(R.id.et_input);
        mUpAndDownIv = view.findViewById(R.id.iv_up_and_down);
        addView(view);

        // 根据类型调整布局以及响应逻辑
        switch (mType) {
            case TYPE_INPUT:
                initForInput();
                break;
            case TYPE_TEXT:
                initForText();
                break;
            case TYPE_SPINNER:
                initForSpinner();
                break;
            default:
                break;

        }
    }

    private void initForInput() {
        mLabelTv.setText(mLabelText);
        mInputEt.setHint(mLabelText);
        mInputEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mLabelTv.setVisibility(VISIBLE);
                    mInputEt.setHint(null);
                } else {
                    if (TextUtils.isEmpty(mInputEt.getText())) {
                        mLabelTv.setVisibility(GONE);
                        mInputEt.setHint(mLabelText);
                    }
                }
            }
        });
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mErrorTv.getVisibility() == VISIBLE) {
                    mErrorTv.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initForText() {
        initCommonForTextOrSpinner();
    }

    private void initForSpinner() {
        initCommonForTextOrSpinner();
        mUpAndDownIv.setOnClickListener(this);
        mUpAndDownIv.setVisibility(VISIBLE);
    }

    private void initCommonForTextOrSpinner() {
        mLabelTv.setText(mLabelText);
        mContentTv.setHint(mLabelText);
        mContentTv.setVisibility(VISIBLE);
        mInputEt.setVisibility(GONE);
        mContentTv.setOnClickListener(this);
        mLabelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mErrorTv.getVisibility() == VISIBLE) {
            mErrorTv.setVisibility(GONE);
        }

        if (mType == TYPE_TEXT) {
            if (mContentViewClickListener != null) {
                mContentViewClickListener.onContentViewClick();
            }
        } else if (mType == TYPE_SPINNER) {
            mLabelTv.setVisibility(VISIBLE);
            mContentTv.setHint(null);
            mUpAndDownIv.setSelected(true);
            new CommonDialog.Builder(getContext())
                    .type(CommonDialog.TYPE_LIST)
                    .title(null)
                    .items(mEntries)
                    .onItem(new CommonDialog.Builder.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            mUpAndDownIv.setSelected(false);
                            mContentTv.setText(mEntries.get(position));
                            if (mSpinnerSelectedListener != null) {
                                mSpinnerSelectedListener.onSpinnerSelected(position, mEntries.get(position));
                            }
                        }
                    })
                    .onDismiss(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mUpAndDownIv.setSelected(false);
                            if (TextUtils.isEmpty(mContentTv.getText())) {
                                mContentTv.setHint(mLabelText);
                                mLabelTv.setVisibility(View.GONE);
                            }
                        }
                    })
                    .build()
                    .show();
        }
    }

    public interface OnContentViewClickListener {
        void onContentViewClick();
    }

    public interface OnSpinnerSelectedListener {
        void onSpinnerSelected(int position, String text);
    }
}
