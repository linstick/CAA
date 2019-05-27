package com.luoruiyong.caa.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.utils.KeyboardUtils;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class EditorActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_TAB = "key_tab";
    public final static String KEY_TOPIC_ID = "key_topic_id";
    public final static String KEY_TOPIC_NAME = "key_topic_name";

    public final static String TAB_CREATE_ACTIVITY = "create_activity";
    public final static String TAB_CREATE_TOPIC = "create_tag";
    public final static String TAB_CREATE_DISCOVER = "create_discover";

    public final static int BROWSE_PICTURE_REQUEST_CODE = 1;
    public final static int RELATE_TOPIC_REQUEST_CODE = 2;
    public final static int CHOOSE_LOCATION_REQUEST_CODE = 3;

    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mFinishIv;

    private OnActionBarClickListener mListener;

    public static void startAction(Context context, String whichTab) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(KEY_TAB, whichTab);
        context.startActivity(intent);
    }

    public static void startAction(Context context, int topicId, String topicName) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(KEY_TAB, TAB_CREATE_DISCOVER);
        intent.putExtra(KEY_TOPIC_ID, topicId);
        intent.putExtra(KEY_TOPIC_NAME, topicName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initView();

        handleIntent();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mFinishIv = findViewById(R.id.iv_right_operate);

        mBackIv.setOnClickListener(this);
        mFinishIv.setOnClickListener(this);
        mBackIv.setImageResource(R.drawable.ic_clear_white);
        mFinishIv.setImageResource(R.drawable.ic_send_white);
        mFinishIv.setVisibility(View.VISIBLE);
    }

    private void handleIntent() {
        String tab;
        Fragment fm;
        int titleResId;
        Intent intent = getIntent();
        tab = intent == null ? TAB_CREATE_ACTIVITY : intent.getStringExtra(KEY_TAB);
        switch (tab) {
            case TAB_CREATE_DISCOVER:
                fm = CreateDiscoverFragment.newInstance(intent.getIntExtra(KEY_TOPIC_ID, -1), intent.getStringExtra(KEY_TOPIC_NAME));
                titleResId = R.string.title_create_discover;
                break;
            case TAB_CREATE_TOPIC:
                fm = new CreateTopicFragment();
                titleResId = R.string.title_create_topic;
                break;
            case TAB_CREATE_ACTIVITY:
            default:
                fm = new CreateActivityFragment();
                titleResId = R.string.title_create_activity;
                break;

        }
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fm, tab).commit();
        mTitleTv.setText(titleResId);
        if (fm instanceof OnActionBarClickListener) {
            mListener = (OnActionBarClickListener) fm;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (mListener != null) {
                    mListener.onBackClick();
                }
                break;
            case R.id.iv_right_operate:
                KeyboardUtils.hideKeyboard(mFinishIv);
                if (mListener != null) {
                    mListener.onFinishClick();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mListener != null) {
            mListener.onBackClick();
        } else {
            super.onBackPressed();
        }
    }

    public interface OnActionBarClickListener {
        void onBackClick();
        void onFinishClick();
    }
}
