package com.luoruiyong.caa.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.DiscoverData;

import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_ACTIVITY_DATA;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_ACTIVITY_ID;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_DISCOVER_DATA;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_DISCOVER_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_DATA;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_BROWSE_COMMENT;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_TYPE;

/**
 * Author: luoruiyong
 * Date: 2019/4/1/001
 **/
public class DetailActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private TextView mTitleTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        initView();

        handleIntent();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);

        mBackIv.setOnClickListener(this);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        int type;
        String title = null;
        Fragment fm = null;
        if (intent == null || (type = intent.getIntExtra(KEY_DETAIL_PAGE_TYPE, -1)) == -1) {
            finish();
            return;
        }
        switch (type) {
            case DETAIL_TYPE_ACTIVITY_ID:
                title = getString(R.string.title_activity_detail);
                fm = ActivityDetailFragment.newInstance(intent.getIntExtra(KEY_DETAIL_PAGE_ID, -1));
                break;
            case DETAIL_TYPE_ACTIVITY_DATA:
                title = getString(R.string.title_activity_detail);
                fm = ActivityDetailFragment.newInstance(
                        (ActivityData)intent.getSerializableExtra(KEY_DETAIL_PAGE_DATA),
                        intent.getBooleanExtra(KEY_DETAIL_PAGE_BROWSE_COMMENT, false));
                break;
            case DETAIL_TYPE_DISCOVER_ID:
                title = getString(R.string.title_discover_detail);
                fm = DiscoverDetailFragment.newInstance(intent.getIntExtra(KEY_DETAIL_PAGE_ID, -1));
                break;
            case DETAIL_TYPE_DISCOVER_DATA:
                title = getString(R.string.title_discover_detail);
                fm = DiscoverDetailFragment.newInstance(
                        (DiscoverData) intent.getSerializableExtra(KEY_DETAIL_PAGE_DATA),
                        intent.getBooleanExtra(KEY_DETAIL_PAGE_BROWSE_COMMENT, false));
                break;
            default:
                break;
        }
        if (fm != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fm).commit();
            mTitleTv.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
