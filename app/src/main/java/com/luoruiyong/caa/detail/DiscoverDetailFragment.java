package com.luoruiyong.caa.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.common.viewholder.DiscoverItemViewHolder;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.user.UserProfileActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/4/004
 **/
public class DiscoverDetailFragment extends Fragment implements View.OnClickListener, ImageViewLayout.OnImageClickListener{

    private static final String KEY_DISCOVER_DATA = "key_discover_data";

    private ImageView mAddCommentIv;
    private View mCommentBarLayout;
    private EditText mCommentInputEt;
    private ImageView mSendIv;

    private DiscoverItemViewHolder mViewHolder;
    private DiscoverData mData;

    public static DiscoverDetailFragment newInstance(DiscoverData data) {
        DiscoverDetailFragment fm = new DiscoverDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DISCOVER_DATA, data);
        fm.setArguments(bundle);
        return fm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_detail, container, false);

        initView(view);

        handleArguments();

        initFragment();

        return view;
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mData = (DiscoverData) bundle.getSerializable(KEY_DISCOVER_DATA);
        mViewHolder.bindData(mData);
    }

    private void initView(View rootView) {
        mViewHolder = new DiscoverItemViewHolder(rootView);
        mAddCommentIv = rootView.findViewById(R.id.iv_add_comment);
        mCommentBarLayout = rootView.findViewById(R.id.ll_comment_bar_layout);
        mCommentInputEt = rootView.findViewById(R.id.et_input);
        mSendIv = rootView.findViewById(R.id.iv_send);

        mAddCommentIv.setOnClickListener(this);
        mSendIv.setOnClickListener(this);
        mViewHolder.mUserAvatarIv.setOnClickListener(this);
        mViewHolder.mNicknameTv.setOnClickListener(this);
        mViewHolder.mTopLikeTv.setOnClickListener(this);
        mViewHolder.mMoreIv.setOnClickListener(this);
        mViewHolder.mTopicTv.setOnClickListener(this);
        mViewHolder.mImageViewLayout.setOnImageClickListener(this);

        mViewHolder.mTopLikeTv.setVisibility(View.VISIBLE);
        mViewHolder.mDividerView.setVisibility(View.GONE);
        mViewHolder.mLikeTv.setVisibility(View.GONE);
        mViewHolder.mCommentTv.setVisibility(View.GONE);
        mViewHolder.mImageViewLayout.setMaxChildViewCount(9);
    }

    private void initFragment() {
        CommentFragment fm = CommentFragment.newInstance(CommentFragment.TYPE_DISCOVER_COMMENT);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_container, fm).commit();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.iv_user_avatar:
            case R.id.tv_nickname:
                startActivity(new Intent(getContext(), UserProfileActivity.class));
                break;
            case R.id.tv_top_like:
                Toast.makeText(getContext(), "click like", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_more:
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
                        Toast.makeText(DiscoverDetailFragment.this.getContext(), "click position = " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.tv_topic:
                startActivity(new Intent(getContext(), TopicActivity.class));
                break;
            case R.id.iv_add_comment:
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
        PictureBrowseActivity.startAction(getContext(), mData.getPictureList(), position, true);
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
