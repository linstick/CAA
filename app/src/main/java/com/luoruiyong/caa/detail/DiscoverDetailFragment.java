package com.luoruiyong.caa.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseFragment;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.common.viewholder.DiscoverItemViewHolder;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.ArrayList;
import java.util.List;

import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_DISCOVER_DATA;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_DISCOVER_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_DATA;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_TYPE;

/**
 * Author: luoruiyong
 * Date: 2019/4/4/004
 **/
public class DiscoverDetailFragment extends BaseFragment implements
        View.OnClickListener,
        ImageViewLayout.OnImageClickListener{

    private ImageView mAddCommentIv;
    private View mCommentBarLayout;
    private EditText mCommentInputEt;
    private ImageView mSendIv;

    private DiscoverItemViewHolder mViewHolder;
    private DiscoverData mData;

    public static DiscoverDetailFragment newInstance(long id) {
        DiscoverDetailFragment fm = new DiscoverDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_DISCOVER_ID);
        bundle.putLong(KEY_DETAIL_PAGE_ID, id);
        fm.setArguments(bundle);
        return fm;
    }

    public static DiscoverDetailFragment newInstance(DiscoverData data) {
        DiscoverDetailFragment fm = new DiscoverDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_DISCOVER_DATA);
        bundle.putSerializable(KEY_DETAIL_PAGE_DATA, data);
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
        int type;
        if (bundle == null || (type = bundle.getInt(KEY_DETAIL_PAGE_TYPE, -1)) == -1) {
            getActivity().finish();
            return;
        }
        if (type == DETAIL_TYPE_DISCOVER_ID) {
            // 联网拉数据，并展示加载UI

            // 模拟
            showLoadingView();
            mCommentInputEt.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideTipView();
                    mData = new DiscoverData(0);
                    mViewHolder.bindData(mData);
                }
            }, 2000);

        } else {
            mData = (DiscoverData) bundle.getSerializable(KEY_DETAIL_PAGE_DATA);
            mViewHolder.bindData(mData);
            // 联网拉取其他数据，但不需要展示加载UI
        }
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

        setUpErrorViewStub(rootView.findViewById(R.id.vs_error_view));
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
                PageUtils.gotoUserProfilePage(getContext(), mData.getUid());
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
                PageUtils.gotoTopicPage(getContext(), mData.getTopicId());
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
