package com.luoruiyong.caa.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.common.fragment.SwipeActivityFragment;
import com.luoruiyong.caa.common.viewholder.ActivityItemViewHolder;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.user.UserProfileActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.ImageViewLayout;
import com.luoruiyong.caa.widget.ImageViewLayoutV2;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/1/001
 **/
public class DetailActivity extends BaseActivity implements View.OnClickListener, ImageViewLayoutV2.OnImageClickListener{

    private final static String KEY_ACTIVITY_DATA = "key_activity_data";

    private ImageView mBackIv;
    private TextView mTitleTv;
    private View mExtrasRootView;
    private LinearLayout mExtrasContainer;
    private ImageView mAddCommentIv;
    private ImageView mAddAdditionIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ActivityItemViewHolder mViewHolder;
    private ActivitySimpleData mData;
    private List<String> mTitleList;
    private List<Fragment> mFragmentList;
    private ViewPagerAdapter mAdapter;

    public static void startAction(Context context, ActivitySimpleData data) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_ACTIVITY_DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();

        handleIntent();

        initFragment();

        bindExtrasInfo();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mViewHolder = new ActivityItemViewHolder(getWindow().getDecorView().getRootView());
        mExtrasRootView = mViewHolder.mExtrasVs.inflate();
        mExtrasContainer = mExtrasRootView.findViewById(R.id.ll_extras_container);
        mAddCommentIv = findViewById(R.id.iv_add_comment);
        mAddAdditionIv = findViewById(R.id.iv_add_addition);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        mBackIv.setOnClickListener(this);
        mAddAdditionIv.setOnClickListener(this);
        mAddCommentIv.setOnClickListener(this);
        mViewHolder.mUserAvatarIv.setOnClickListener(this);
        mViewHolder.mNicknameTv.setOnClickListener(this);
        mViewHolder.mTopicTv.setOnClickListener(this);
        mViewHolder.mCollectTv.setOnClickListener(this);
        mViewHolder.mMoreIv.setOnClickListener(this);
        mViewHolder.mImageViewLayout.setOnImageClickListener(this);

        mTitleTv.setText(R.string.title_activity_detail);
        mViewHolder.mCommentTv.setVisibility(View.GONE);
        mViewHolder.mImageViewLayout.setMaxChildCount(9);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent == null || intent.getSerializableExtra(KEY_ACTIVITY_DATA) == null) {
            finish();
        }
        mData = (ActivitySimpleData) intent.getSerializableExtra(KEY_ACTIVITY_DATA);
        mViewHolder.bindData(mData, -1);
    }

    private void initFragment() {
        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        mTitleList.add(String.format(getString(R.string.activity_detail_str_comment), mData.getCommentCount()));
        mFragmentList.add(CommentFragment.newInstance(CommentFragment.TYPE_ACTIVITY_COMMENT));
        mTitleList.add(String.format(getString(R.string.activity_detail_str_addition), mData.getAdditionCount()));
        mFragmentList.add(AdditionFragment.newInstance(mData.getUid()));
        if (Enviroment.isSelf(mData.getUid())) {
            mAddAdditionIv.setVisibility(View.VISIBLE);
        }

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void bindExtrasInfo() {
        // for test
        mExtrasRootView.setVisibility(View.VISIBLE);
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_extras_list, mExtrasContainer, false);
            ((TextView)view.findViewById(R.id.tv_label)).setText("extra" + (i + 1));
            ((TextView)view.findViewById(R.id.tv_value)).setText("value" + (i + 1));
            mExtrasContainer.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_user_avatar:
            case R.id.tv_nickname:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.tv_topic:
                startActivity(new Intent(this, TopicActivity.class));
                break;
            case R.id.tv_collect:
                Toast.makeText(this, "click collect", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_more:
                List<String>  mItemMoreStringArray = Enviroment.getItemMoreNewStringArray();
                List<String> items = new ArrayList<>();
                if (Enviroment.isSelf(mData.getUid())) {
                    items.addAll(mItemMoreStringArray);
                    items.add(ResourcesUtils.getString(R.string.common_str_delete));
                } else {
                    items = mItemMoreStringArray;
                }
                DialogHelper.showListDialog(this, items, new CommonDialog.Builder.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(DetailActivity.this, "click position = " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onImageClick(View parent, int position) {
        PictureBrowseActivity.startAction(this, mData.getPictureList(), position);
    }
}
