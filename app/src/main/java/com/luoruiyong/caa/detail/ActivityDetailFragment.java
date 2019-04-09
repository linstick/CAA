package com.luoruiyong.caa.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseFragment;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.common.viewholder.ActivityItemViewHolder;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.ArrayList;
import java.util.List;

import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_ACTIVITY_DATA;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_ACTIVITY_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_DATA;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_TYPE;

/**
 * Author: luoruiyong
 * Date: 2019/4/4/004
 **/
public class ActivityDetailFragment extends BaseFragment implements
        View.OnClickListener,
        ImageViewLayout.OnImageClickListener{

    private static final int INPUT_TYPE_COMMENT = 1;
    private static final int INPUT_TYPE_ADDITION = 2;

    private View mExtrasRootView;
    private LinearLayout mExtrasContainer;
    private ImageView mAddOperateIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private View mCommentBarLayout;
    private EditText mCommentInputEt;
    private ImageView mSendIv;

    private ActivityItemViewHolder mViewHolder;
    private ActivitySimpleData mData;
    private List<String> mTitleList;
    private List<Fragment> mFragmentList;
    private ViewPagerAdapter mAdapter;

    private int mInputType = INPUT_TYPE_COMMENT;

    public static ActivityDetailFragment newInstance(long id) {
        ActivityDetailFragment fm = new ActivityDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_DETAIL_PAGE_ID, id);
        bundle.putInt(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_ACTIVITY_ID);
        fm.setArguments(bundle);
        return fm;
    }

    public static ActivityDetailFragment newInstance(ActivitySimpleData data) {
        ActivityDetailFragment fm = new ActivityDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DETAIL_PAGE_DATA, data);
        bundle.putInt(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_ACTIVITY_DATA);
        fm.setArguments(bundle);
        return fm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_detail, container, false);
        initView(view);

        handleArguments();

        return view;
    }

    private void initView(View rootView) {
        mViewHolder = new ActivityItemViewHolder(rootView);
        mExtrasRootView = mViewHolder.mExtrasVs.inflate();
        mExtrasContainer = mExtrasRootView.findViewById(R.id.ll_extras_container);
        mAddOperateIv = rootView.findViewById(R.id.iv_add_operate);
        mTabLayout = rootView.findViewById(R.id.tab_layout);
        mViewPager = rootView.findViewById(R.id.view_pager);
        mCommentBarLayout = rootView.findViewById(R.id.ll_comment_bar_layout);
        mCommentInputEt = rootView.findViewById(R.id.et_input);
        mSendIv = rootView.findViewById(R.id.iv_send);

        mAddOperateIv.setOnClickListener(this);
        mSendIv.setOnClickListener(this);
        mViewHolder.mUserAvatarIv.setOnClickListener(this);
        mViewHolder.mNicknameTv.setOnClickListener(this);
        mViewHolder.mTopicTv.setOnClickListener(this);
        mViewHolder.mCollectTv.setOnClickListener(this);
        mViewHolder.mMoreIv.setOnClickListener(this);
        mViewHolder.mImageViewLayout.setOnImageClickListener(this);

        mViewHolder.mCommentTv.setVisibility(View.GONE);
        mViewHolder.mImageViewLayout.setMaxChildViewCount(9);

        setUpErrorViewStub(rootView.findViewById(R.id.vs_error_view));
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        int type;
        if (bundle == null || (type = bundle.getInt(KEY_DETAIL_PAGE_TYPE, -1)) == -1) {
            getActivity().finish();
            return;
        }
        if (type == DETAIL_TYPE_ACTIVITY_ID) {
            // 联网拉数据，并展示加载UI

            // 模拟
            showLoadingView();
            mCommentInputEt.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideTipView();
                    mData = new ActivitySimpleData(0, 1);
                    mViewHolder.bindData(mData, -1);
                    initFragment();
                }
            }, 2000);

        } else {
            mData = (ActivitySimpleData) bundle.getSerializable(KEY_DETAIL_PAGE_DATA);
            mViewHolder.bindData(mData, -1);
            initFragment();
            // 联网拉取其他数据，但不需要展示加载UI
        }
    }

    private void initFragment() {
        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        mTitleList.add(String.format(getString(R.string.activity_detail_str_comment), mData.getCommentCount()));
        mFragmentList.add(CommentFragment.newInstance(CommentFragment.TYPE_ACTIVITY_COMMENT));
        mTitleList.add(String.format(getString(R.string.activity_detail_str_addition), mData.getAdditionCount()));
        mFragmentList.add(AdditionFragment.newInstance(mData.getUid()));

        mAdapter = new ViewPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    if (Enviroment.isSelf(mData.getUid())) {
                        mAddOperateIv.setImageResource(R.drawable.ic_edit_light_gray);
                        mCommentInputEt.setHint(R.string.activity_detail_str_addition_bar_hint);
                    } else {
                        mAddOperateIv.setVisibility(View.GONE);
                    }
                    mInputType = INPUT_TYPE_ADDITION;
                } else {
                    mAddOperateIv.setVisibility(View.VISIBLE);
                    mAddOperateIv.setImageResource(R.drawable.ic_comment_light_gray);
                    mCommentInputEt.setHint(R.string.activity_detail_str_comment_bar_hint);
                    mInputType = INPUT_TYPE_COMMENT;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);

        // 模拟拉取额外的数据
        mCommentInputEt.postDelayed(new Runnable() {
            @Override
            public void run() {
                bindExtrasInfo();
            }
        }, 1200);
    }

    private void bindExtrasInfo() {
        // for test
        mExtrasRootView.setVisibility(View.VISIBLE);
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_extras_list, mExtrasContainer, false);
            ((TextView)view.findViewById(R.id.tv_label)).setText("extra" + (i + 1));
            ((TextView)view.findViewById(R.id.tv_value)).setText("value" + (i + 1));
            mExtrasContainer.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_avatar:
            case R.id.tv_nickname:
                PageUtils.gotoUserProfilePage(getContext(), mData.getUid());
                break;
            case R.id.tv_topic:
                PageUtils.gotoTopicPage(getContext(), mData.getTopicId());
                break;
            case R.id.tv_collect:
                Toast.makeText(getContext(), "click collect", Toast.LENGTH_SHORT).show();
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
                DialogHelper.showListDialog(getContext(), items, new CommonDialog.Builder.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(ActivityDetailFragment.this.getContext(), "click position = " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.iv_add_operate:
                toggleCommentBar();
                break;
            case R.id.iv_send:
                Toast.makeText(getContext(), "send", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onImageClick(View parent, int position) {
        PictureBrowseActivity.startAction(getActivity(), mData.getPictureList(), position, true);
    }

    private void toggleCommentBar() {
        if (mCommentBarLayout.getVisibility() == View.VISIBLE) {
            mCommentBarLayout.setVisibility(View.GONE);
            mCommentInputEt.clearFocus();
        } else {
            mCommentBarLayout.setVisibility(View.VISIBLE);
            mCommentInputEt.requestFocus();
        }
    }
}
