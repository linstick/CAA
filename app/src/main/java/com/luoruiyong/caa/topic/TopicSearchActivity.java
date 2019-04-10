package com.luoruiyong.caa.topic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.TopicSimpleData;
import com.luoruiyong.caa.utils.KeyboardUtils;

/**
 * Author: luoruiyong
 * Date: 2019/4/7/007
 * Description:
 **/
public class TopicSearchActivity extends BaseActivity implements View.OnClickListener{

    public final static String KEY_RESULT_TYPE = "key_result_type";
    public final static String KEY_CREATE_TOPIC_NAME = "key_create_topic_name";
    public final static String KEY_SELECTED_TOPIC_NAME = "key_selected_topic_name";
    public final static String KEY_SELECTED_TOPIC_ID = "key_selected_topic_id";

    public final static int RELATE_TOPIC_TYPE_NONE = 0;
    public final static int RELATE_TOPIC_TYPE_SELECT = 1;
    public final static int RELATE_TOPIC_TYPE_CREATE = 2;

    private ImageView mBackIv;
    private EditText mInputEt;
    private TextView mSearchTv;
    private TopicSearchResultFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_search);

        initView();

        initFragment();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mInputEt = findViewById(R.id.et_search_input);
        mSearchTv = findViewById(R.id.tv_search);

        mBackIv.setImageResource(R.drawable.ic_clear_white);
        mInputEt.setHint(getString(R.string.topic_relate_str_search_topic));

        mBackIv.setOnClickListener(this);
        mSearchTv.setOnClickListener(this);
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    mFragment.searchQuietly(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initFragment() {
        mFragment = new TopicSearchResultFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, mFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.tv_search:
                KeyboardUtils.hideKeyboard(mInputEt);
                String text = getInputText();
                if (!TextUtils.isEmpty(text)) {
                    mFragment.doSearch(text);
                } else {
                    Toast.makeText(this, R.string.topic_relate_tip_no_search_input, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void setNotRelateResultDataAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_TYPE, RELATE_TOPIC_TYPE_NONE);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setCreateResultDataAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_TYPE, RELATE_TOPIC_TYPE_SELECT);
        intent.putExtra(KEY_CREATE_TOPIC_NAME, getInputText());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setSelectResultDataAndFinish(TopicSimpleData data) {
        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_TYPE, RELATE_TOPIC_TYPE_CREATE);
        intent.putExtra(KEY_SELECTED_TOPIC_NAME, data.getName());
        intent.putExtra(KEY_SELECTED_TOPIC_ID, data.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    public String getInputText() {
        return mInputEt.getText().toString().trim();
    }
}
