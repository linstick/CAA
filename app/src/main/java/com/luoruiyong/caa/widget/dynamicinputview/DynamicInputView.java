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
import com.luoruiyong.caa.utils.KeyboardUtils;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

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
    private final static int NOT_DEFINED = -1;
    private final static boolean DEFAULT_NULLABLE = true;

    private int mType;
    private int mLines;
    private int mMaxLines;
    private int mMinLines;
    private boolean mNullable;
    private String mLabelText;
    private String mErrorText;
    private List<String> mEntries;
    private OnContentViewClickListener mContentViewClickListener;
    private OnSpinnerSelectedListener mSpinnerSelectedListener;

    private int mSelectedItem = -1;
    private boolean mIsSpread = false;

    private TextView mLabelTv;
    private TextView mTopRequiredTv;
    private TextView mErrorTv;
    private EditText mInputEt;
    private TextView mRequiredTv;
    private ImageView mUpAndDownIv;
    private View mRootView;

    public DynamicInputView(Context context) {
        this(context, null);
    }

    public DynamicInputView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs);
        initView(context);
        applyCustomAttrs();
    }

    public void setType(int type) {
        mType = type;
    }

    public void setLabelText(String text) {
        mLabelText = text;
        mLabelTv.setText(text);
    }

    public void setErrorText(String text) {
        mErrorTv.setText(text);
    }

    public void setEntries(List<String> entries) {
        mEntries = entries;
    }

    public boolean check() {
        changeToSpreadUI();
        boolean showError = !mNullable && TextUtils.isEmpty(getInputText());
        setViewVisibleStatus(mErrorTv, showError);
        return !showError;
    }

    public void setInputText(String content) {
        if (content != null && !TextUtils.isEmpty(content.trim())) {
            mInputEt.setText(content.trim());
            changeToSpreadUI();
        } else {
            changeToCloseUI();
        }
    }

    public String getInputText() {
        return mInputEt.getText().toString().trim();
    }

    public int getSpinnerSelectedItem() {
        return mSelectedItem;
    }

    public void setSpinnerSelectedItem(int position) {
        if (mType != TYPE_SPINNER || !ListUtils.isIndexBetween(mEntries, position)) {
            return;
        }
        setViewVisibleStatus(mErrorTv, GONE);
        mInputEt.setText(mEntries.get(position));
        changeToSpreadUI();
    }

    public void setOnContentViewClickListener (OnContentViewClickListener listener) {
        mContentViewClickListener = listener;
    }

    public void setOnSpinnerSelectedListener (OnSpinnerSelectedListener listener) {
        mSpinnerSelectedListener = listener;
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        // 读取自定义的配置属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicInputView);
        mType = typedArray.getInt(R.styleable.DynamicInputView_type, DEFAULT_TYPE);
        mLines = typedArray.getInt(R.styleable.DynamicInputView_lines, NOT_DEFINED);
        mMaxLines = typedArray.getInt(R.styleable.DynamicInputView_maxLines, NOT_DEFINED);
        mMinLines = typedArray.getInt(R.styleable.DynamicInputView_minLines, NOT_DEFINED);
        mNullable = typedArray.getBoolean(R.styleable.DynamicInputView_nullable, DEFAULT_NULLABLE);
        mLabelText = typedArray.getString(R.styleable.DynamicInputView_labelText);
        mErrorText = typedArray.getString(R.styleable.DynamicInputView_errorText);
        int entriesResId = typedArray.getResourceId(R.styleable.DynamicInputView_entries, -1);
        if (entriesResId != -1) {
            mEntries = Arrays.asList(ResourcesUtils.getStringArray(entriesResId));
        }
        typedArray.recycle();
    }

    private void initView(Context context) {
        // 添加布局
        mRootView = LayoutInflater.from(context).inflate(R.layout.layout_dynamic_input_view, this, false);
        mLabelTv = mRootView.findViewById(R.id.tv_label);
        mErrorTv = mRootView.findViewById(R.id.tv_error);
        mInputEt = mRootView.findViewById(R.id.et_input);
        mRequiredTv = mRootView.findViewById(R.id.tv_required);
        mTopRequiredTv = mRootView.findViewById(R.id.tv_top_required);
        mUpAndDownIv = mRootView.findViewById(R.id.iv_up_and_down);
        mRootView.setOnClickListener(this);
        addView(mRootView);
    }

    private void applyCustomAttrs() {
        // 根据类型调整布局以及响应逻辑
        mLabelTv.setText(mLabelText);
        mErrorTv.setText(mErrorText);
        mInputEt.setHint(mLabelText);
        setViewVisibleStatus(mRequiredTv, !mNullable);
        if (mLines != NOT_DEFINED) {
            mInputEt.setMaxLines(mLines);
            mInputEt.setMinLines(mLines);
        }
        if (mMaxLines != NOT_DEFINED) {
            mInputEt.setMaxLines(mMaxLines);
        }
        if (mMinLines != NOT_DEFINED) {
            mInputEt.setMinLines(mMinLines);
        }

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
        mInputEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                   changeToSpreadUI();
                   KeyboardUtils.showKeyboard(mInputEt);
                } else {
                    // 失去焦点
                    if (TextUtils.isEmpty(getInputText())) {
                        changeToCloseUI();
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
                if (mInputEt.hasFocus()) {
                    setViewVisibleStatus(mErrorTv, GONE);
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
        mUpAndDownIv.setVisibility(VISIBLE);
    }

    private void initCommonForTextOrSpinner() {
        mInputEt.setFocusable(false);
        mInputEt.setCursorVisible(false);
    }

    @Override
    public void onClick(View v) {
        // 获取焦点的目的是为了让上一个输入框失去焦点
        requestFocus();
        setViewVisibleStatus(mErrorTv, GONE);

        if (mType == TYPE_INPUT) {
            KeyboardUtils.showKeyboard(mInputEt);
        } else if (mType == TYPE_TEXT) {
            if (mContentViewClickListener != null) {
                mContentViewClickListener.onContentViewClick();
            }
        } else if (mType == TYPE_SPINNER) {
            changeToSpreadUI();
            mUpAndDownIv.setSelected(true);
            new CommonDialog.Builder(getContext())
                    .type(CommonDialog.TYPE_LIST)
                    .title(null)
                    .items(mEntries)
                    .onItem(new CommonDialog.Builder.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            mSelectedItem = position;
                            mUpAndDownIv.setSelected(false);
                            mInputEt.setText(mEntries.get(position));
                            if (mSpinnerSelectedListener != null) {
                                mSpinnerSelectedListener.onSpinnerSelected(position, mEntries.get(position));
                            }
                        }
                    })
                    .onDismiss(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mUpAndDownIv.setSelected(false);
                            if (TextUtils.isEmpty(getInputText())) {
                                changeToCloseUI();
                            }
                        }
                    })
                    .build()
                    .show();
        }
    }

    private void setViewVisibleStatus(View view, boolean visible) {
        setViewVisibleStatus(view, visible ? VISIBLE : GONE);
    }

    private void setViewVisibleStatus(View view, int status) {
        if (view.getVisibility() != status) {
            view.setVisibility(status);
        }
    }

    private void changeToSpreadUI() {
        if (!mIsSpread) {
            setViewVisibleStatus(mLabelTv, VISIBLE);
            setViewVisibleStatus(mTopRequiredTv, !mNullable);
            setViewVisibleStatus(mRequiredTv, GONE);
            setViewVisibleStatus(mErrorTv, GONE);
            LayoutParams params = (LayoutParams) mInputEt.getLayoutParams();
            params.weight = 1;
            mInputEt.setLayoutParams(params);
            mInputEt.setHint(null);
            mIsSpread = true;
        }
    }

    private void changeToCloseUI() {
        if (mIsSpread) {
            setViewVisibleStatus(mLabelTv, GONE);
            setViewVisibleStatus(mTopRequiredTv, GONE);
            setViewVisibleStatus(mRequiredTv, !mNullable);
            setViewVisibleStatus(mErrorTv, GONE);
            LayoutParams params = (LayoutParams) mInputEt.getLayoutParams();
            params.weight = 0;
            mInputEt.setLayoutParams(params);
            mInputEt.setHint(mLabelText);
            mInputEt.setText(null);
            mIsSpread = false;
        }
    }

    public interface OnContentViewClickListener {
        void onContentViewClick();
    }

    public interface OnSpinnerSelectedListener {
        void onSpinnerSelected(int position, String text);
    }
}
