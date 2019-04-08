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
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.Arrays;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/5/005
 * Description:
 **/
public class DynamicInputView extends LinearLayout implements
        View.OnClickListener,
        ImageViewLayout.OnImageDeletedListener {
    private final static int TYPE_INPUT = 1;
    private final static int TYPE_TEXT = 2;
    private final static int TYPE_SPINNER = 3;
    private final static int TYPE_IMAGE = 4;
    private final static int TYPE_INPUT_AND_IMAGE = TYPE_INPUT | TYPE_IMAGE;

    private final static int DEFAULT_TYPE = TYPE_INPUT;
    private final static int NOT_DEFINED = -1;
    private final static boolean DEFAULT_NULLABLE = true;
    private final static boolean DEFAULT_IGNORE_LAST_IMAGE_ITEM = false;

    private int mType;
    private int mLines;
    private int mMaxLines;
    private int mMinLines;
    private int mMaxImageCount;
    private boolean mNullable;
    private boolean mIgnoreLastImageItem;
    private String mLabelText;
    private String mHintText;
    private String mErrorText;
    private List<String> mSpinnerEntries;
    private OnContentViewClickListener mContentViewClickListener;
    private OnSpinnerSelectedListener mSpinnerSelectedListener;
    private OnFocusChangedListener mOnFocusChangedListener;
    private OnTextChangedListener mOnTextChangedListener;
    private OnFocusLostOrTextChangeListener mOnFocusLostOrTextChangeListener;

    private int mSelectedItem = -1;
    private List<String> mPictureUrls;
    private boolean mIsSpread = false;

    private TextView mLabelTv;
    private TextView mTopRequiredTv;
    private TextView mErrorTv;
    private EditText mInputEt;
    private TextView mRequiredTv;
    private ImageView mUpAndDownIv;
    private View mInputContainerLl;
    private ImageViewLayout mImageViewLayout;
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

    public void setNullable(boolean nullable) {
        mNullable = nullable;
    }

    public void setIgnoreLastImageItem(boolean ignore) {
        mIgnoreLastImageItem = ignore;
    }

    public void setSpinnerEntries(List<String> entries) {
        mSpinnerEntries = entries;
    }

    public boolean checkAndShowErrorTipIfNeed() {
        changeToSpreadUI();
        boolean showError = false;
        if (!mNullable) {
            if (mType == TYPE_IMAGE) {
                showError = ListUtils.isEmpty(mPictureUrls);
            } else {
                showError = TextUtils.isEmpty(getInputText());
            }
        }
        setViewVisibleStatus(mErrorTv, showError);
        return !showError;
    }

    public boolean isEmpty() {
        if (getVisibility() != VISIBLE) {
            return true;
        }
        boolean isEmpty;
        if (mType == TYPE_IMAGE || mType == TYPE_INPUT_AND_IMAGE) {
            isEmpty = isImageEmpty();
            if (mType == TYPE_INPUT_AND_IMAGE) {
                isEmpty &= isInputEmpty();
            }
        } else {
            isEmpty = isInputEmpty();
        }
        return isEmpty;
    }
    public boolean isImageEmpty() {
        return mIgnoreLastImageItem ? ListUtils.getSize(mPictureUrls) == 1 : ListUtils.getSize(mPictureUrls) == 0;
    }

    public boolean isInputEmpty() {
        return TextUtils.isEmpty(getInputText());
    }

    public void setInputText(String content) {
        mInputEt.setText(content);
        notifyInputDataChanged();
    }

    public String getInputText() {
        return mInputEt.getText().toString().trim();
    }

    public void setPictureUrls(List<String> urls) {
        mPictureUrls = urls;
        mImageViewLayout.setPictureUrls(urls);
        notifyInputDataChanged();
    }

    public void notifyInputDataChanged() {
        if (!isEmpty()) {
            changeToSpreadUI();
        } else {
            if (!mInputEt.hasFocus()) {
                changeToCloseUI();
            }
        }
        if (mType == TYPE_IMAGE || mType == TYPE_INPUT_AND_IMAGE) {
            mImageViewLayout.notifyChildViewChanged();
        }
    }

    public List<String> getPictureUrls() {
        return mPictureUrls;
    }

    public void setSpinnerSelectedItem(int position) {
        if (mType != TYPE_SPINNER || !ListUtils.isIndexBetween(mSpinnerEntries, position)) {
            return;
        }
        setViewVisibleStatus(mErrorTv, GONE);
        mInputEt.setText(mSpinnerEntries.get(position));
        changeToSpreadUI();
    }

    public int getSpinnerSelectedItem() {
        return mSelectedItem;
    }

    public void setImageMaxCount(int count) {
        mImageViewLayout.setMaxChildViewCount(count);
    }

    public void setSupportAllChildDelete(boolean supportAllChildDelete) {
        mImageViewLayout.setSupportAllChildDelete(supportAllChildDelete);
    }

    public void setSupportAllChildDeleteBesidesLast(boolean supportAllChildDeleteBesidesLast) {
        mImageViewLayout.setSupportAllChildDeleteBesidesLast(supportAllChildDeleteBesidesLast);
    }

    public void setOnContentViewClickListener (OnContentViewClickListener listener) {
        mContentViewClickListener = listener;
    }

    public void setOnSpinnerSelectedListener (OnSpinnerSelectedListener listener) {
        mSpinnerSelectedListener = listener;
    }

    public void setOnFocusChangedListener (OnFocusChangedListener listener) {
        mOnFocusChangedListener = listener;
    }

    public void setOnTextChangedListener (OnTextChangedListener listener) {
        mOnTextChangedListener = listener;
    }

    public void setOnFocusLostOrTextChangeListener (OnFocusLostOrTextChangeListener listener) {
        mOnFocusLostOrTextChangeListener = listener;
    }

    public void setOnImageClickListener(ImageViewLayout.OnImageClickListener listener) {
        mImageViewLayout.setOnImageClickListener(listener);
    }

    public void setOnImageLongClickListener(ImageViewLayout.OnImageLongClickListener listener) {
        mImageViewLayout.setOnImageLongClickListener(listener);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        // 读取自定义的配置属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicInputView);
        mType = typedArray.getInt(R.styleable.DynamicInputView_type, DEFAULT_TYPE);
        mLines = typedArray.getInt(R.styleable.DynamicInputView_lines, NOT_DEFINED);
        mMaxLines = typedArray.getInt(R.styleable.DynamicInputView_maxLines, NOT_DEFINED);
        mMinLines = typedArray.getInt(R.styleable.DynamicInputView_minLines, NOT_DEFINED);
        mMaxImageCount = typedArray.getInt(R.styleable.DynamicInputView_maxImageCount, NOT_DEFINED);
        mNullable = typedArray.getBoolean(R.styleable.DynamicInputView_nullable, DEFAULT_NULLABLE);
        mIgnoreLastImageItem = typedArray.getBoolean(R.styleable.DynamicInputView_ignoreLastImageItem, DEFAULT_IGNORE_LAST_IMAGE_ITEM);
        mLabelText = typedArray.getString(R.styleable.DynamicInputView_labelText);
        mHintText = typedArray.getString(R.styleable.DynamicInputView_hintText);
        if (TextUtils.isEmpty(mHintText)) {
            mHintText = mLabelText;
        }
        mErrorText = typedArray.getString(R.styleable.DynamicInputView_errorText);
        int entriesResId = typedArray.getResourceId(R.styleable.DynamicInputView_spinnerEntries, -1);
        if (entriesResId != -1) {
            mSpinnerEntries = Arrays.asList(ResourcesUtils.getStringArray(entriesResId));
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
        mInputContainerLl = mRootView.findViewById(R.id.ll_input_container);
        mImageViewLayout = mRootView.findViewById(R.id.image_view_layout);
        mRootView.setOnClickListener(this);
        addView(mRootView);
    }

    private void applyCustomAttrs() {
        // 根据类型调整布局以及响应逻辑
        mLabelTv.setText(mLabelText);
        mErrorTv.setText(mErrorText);
        mInputEt.setHint(mHintText);
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
        if (mMaxImageCount != NOT_DEFINED) {
            mImageViewLayout.setMaxChildViewCount(mMaxImageCount);
        }

        switch (mType) {
            case TYPE_INPUT:
            case TYPE_INPUT_AND_IMAGE:
                initForInputType();
                break;
            case TYPE_TEXT:
            case TYPE_IMAGE:
                initCommonForBesidesInputType();
                break;
            case TYPE_SPINNER:
                initCommonForBesidesInputType();
                mUpAndDownIv.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    private void initForInputType() {
        mInputEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                   changeToSpreadUI();
                   KeyboardUtils.showKeyboard(mInputEt);
                } else {
                    // 失去焦点
                    if (isEmpty()) {
                        changeToCloseUI();
                    }
                }
                if (mOnFocusChangedListener != null) {
                    mOnFocusChangedListener.onFocusChanged(hasFocus);
                }
                if (!hasFocus && mOnFocusLostOrTextChangeListener != null) {
                    mOnFocusLostOrTextChangeListener.onFocusLostOrTextChanged(getInputText());
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
                String text = s.toString().trim();
                if (mOnTextChangedListener != null) {
                    mOnTextChangedListener.onTextChanged(text);
                }
                if (mOnFocusLostOrTextChangeListener != null) {
                    mOnFocusLostOrTextChangeListener.onFocusLostOrTextChanged(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (mType == TYPE_INPUT_AND_IMAGE) {
            mImageViewLayout.setOnImageDeletedListener(this);
        }
    }

    private void initCommonForBesidesInputType() {
        mInputEt.setOnClickListener(this);
        mInputEt.setFocusable(false);
        mInputEt.setCursorVisible(false);
        if (mType == TYPE_IMAGE) {
            mImageViewLayout.setOnImageDeletedListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        // 获取焦点的目的是为了让上一个输入框失去焦点
        requestFocus();
        setViewVisibleStatus(mErrorTv, GONE);

        if (mType == TYPE_INPUT) {
            KeyboardUtils.showKeyboard(mInputEt);
        } else if (mType == TYPE_TEXT || mType == TYPE_IMAGE) {
            if (mContentViewClickListener != null) {
                mContentViewClickListener.onContentViewClick(this);
            }
        } else if (mType == TYPE_SPINNER) {
            changeToSpreadUI();
            mUpAndDownIv.setSelected(true);
            new CommonDialog.Builder(getContext())
                    .type(CommonDialog.TYPE_LIST)
                    .title(null)
                    .items(mSpinnerEntries)
                    .onItem(new CommonDialog.Builder.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            mSelectedItem = position;
                            mUpAndDownIv.setSelected(false);
                            mInputEt.setText(mSpinnerEntries.get(position));
                            if (mSpinnerSelectedListener != null) {
                                mSpinnerSelectedListener.onSpinnerSelected(position, mSpinnerEntries.get(position));
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
            if (mType != TYPE_IMAGE) {
                LayoutParams params = (LayoutParams) mInputEt.getLayoutParams();
                params.weight = 1;
                mInputEt.setLayoutParams(params);
                mInputEt.setHint(null);
            }
            if (mType == TYPE_IMAGE || mType == TYPE_INPUT_AND_IMAGE) {
                if (mType == TYPE_IMAGE) {
                    setViewVisibleStatus(mInputContainerLl, GONE);
                }
                setViewVisibleStatus(mImageViewLayout, ListUtils.isEmpty(mPictureUrls) ? GONE : VISIBLE);
            }
            mIsSpread = true;
        }
    }

    private void changeToCloseUI() {
        if (mIsSpread) {
            setViewVisibleStatus(mLabelTv, GONE);
            setViewVisibleStatus(mTopRequiredTv, GONE);
            setViewVisibleStatus(mRequiredTv, !mNullable);
            setViewVisibleStatus(mErrorTv, GONE);
            setViewVisibleStatus(mInputContainerLl, VISIBLE);
            if (mType != TYPE_IMAGE) {
                LayoutParams params = (LayoutParams) mInputEt.getLayoutParams();
                params.weight = 0;
                mInputEt.setLayoutParams(params);
                mInputEt.setHint(mHintText);
                mInputEt.setText(null);
            }
            if (mType == TYPE_IMAGE || mType == TYPE_INPUT_AND_IMAGE){
                setViewVisibleStatus(mImageViewLayout, GONE);
            }
            mIsSpread = false;
        }
    }

    @Override
    public void onItemDeleted(View parent, int position) {
        notifyInputDataChanged();
    }

    public interface OnContentViewClickListener {
        void onContentViewClick(View v);
    }

    public interface OnSpinnerSelectedListener {
        void onSpinnerSelected(int position, String text);
    }

    public interface OnFocusChangedListener {
        void onFocusChanged(boolean hasFocus);
    }

    public interface OnTextChangedListener {
        void onTextChanged(String text);
    }

    public interface OnFocusLostOrTextChangeListener {
        void onFocusLostOrTextChanged(String text);
    }
}
