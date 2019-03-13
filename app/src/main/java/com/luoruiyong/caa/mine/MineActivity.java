package com.luoruiyong.caa.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;

/**
 * Author: luoruiyong
 * Date: 2019/3/13/013
 **/
public class MineActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_TAB = "key_tab";
    public final static String TAB_NOTES = "notes";
    public final static String TAB_TAGS = "tags";
    public final static String TAB_COLLECTIONS = "collections";

    private ImageView mBackIv;
    private TextView mTitleTv;

    public static void startAction(Context context, String whichTab) {
        Intent intent = new Intent(context, MineActivity.class);
        intent.putExtra(KEY_TAB, whichTab);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        initView();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);

        mTitleTv.setText(R.string.title_mine);

        mBackIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
