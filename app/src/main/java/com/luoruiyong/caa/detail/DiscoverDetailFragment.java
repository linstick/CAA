package com.luoruiyong.caa.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseFragment;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.DiscoverDynamicData;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.common.viewholder.DiscoverItemViewHolder;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.eventbus.CommonOperateEvent;
import com.luoruiyong.caa.login.LoginActivity;
import com.luoruiyong.caa.model.CommonFetcher;
import com.luoruiyong.caa.model.CommonTargetOperator;
import com.luoruiyong.caa.model.bean.GlobalSource;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.KeyboardUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_DISCOVER_DATA;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_DISCOVER_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_DATA;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_BROWSE_COMMENT;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_TYPE;

/**
 * Author: luoruiyong
 * Date: 2019/4/4/004
 **/
public class DiscoverDetailFragment extends BaseFragment implements
        View.OnClickListener,
        ImageViewLayout.OnImageClickListener,
        DetailActivity.OnBackClickListener{

    private TextView mCommentLabelTv;
    private ImageView mAddCommentIv;
    private View mCommentBarLayout;
    private EditText mCommentInputEt;
    private ImageView mSendIv;
    private AppBarLayout mAppBarLayout;

    private DiscoverItemViewHolder mViewHolder;
    private int mDiscoverId;
    private DiscoverData mData;

    private boolean mMaybeDelete = false;

    public static DiscoverDetailFragment newInstance(int id) {
        DiscoverDetailFragment fm = new DiscoverDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_DISCOVER_ID);
        bundle.putInt(KEY_DETAIL_PAGE_ID, id);
        fm.setArguments(bundle);
        return fm;
    }

    public static DiscoverDetailFragment newInstance(DiscoverData data, boolean isComment) {
        DiscoverDetailFragment fm = new DiscoverDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_DISCOVER_DATA);
        bundle.putBoolean(KEY_DETAIL_PAGE_BROWSE_COMMENT, isComment);
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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        int type;
        if (bundle == null || (type = bundle.getInt(KEY_DETAIL_PAGE_TYPE, -1)) == -1) {
            getActivity().finish();
            return;
        }
        if (type == DETAIL_TYPE_DISCOVER_ID) {
            if ((mDiscoverId = bundle.getInt(KEY_DETAIL_PAGE_ID, -1)) == -1) {
                getActivity().finish();
                return;
            }
            showLoadingView();
            getActivity().getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    CommonFetcher.doFetchDiscoverDetail(mDiscoverId);
                }
            });
        } else {
            if ((mData = (DiscoverData) bundle.getSerializable(KEY_DETAIL_PAGE_DATA)) == null) {
                getActivity().finish();
                return;
            }
            mDiscoverId = mData.getId();
            mViewHolder.bindData(mData);
            mCommentLabelTv.setText(String.format(getString(R.string.activity_detail_str_comment), mData.getCommentCount()));
            initFragment();
            CommonTargetOperator.doFetchDiscoverDynamicData(mDiscoverId);
            if (bundle.getBoolean(KEY_DETAIL_PAGE_BROWSE_COMMENT, false)) {
                toggleCommentBar();
            }
        }
    }

    private void initView(View rootView) {
        mViewHolder = new DiscoverItemViewHolder(rootView);
        mCommentLabelTv = rootView.findViewById(R.id.tv_comment_label);
        mAddCommentIv = rootView.findViewById(R.id.iv_add_comment);
        mCommentBarLayout = rootView.findViewById(R.id.ll_comment_bar_layout);
        mCommentInputEt = rootView.findViewById(R.id.et_input);
        mSendIv = rootView.findViewById(R.id.iv_send);
        mAppBarLayout = rootView.findViewById(R.id.app_bar_layout);

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
        CommentFragment fm = CommentFragment.newInstance(Config.PAGE_ID_DISCOVER_COMMENT, mData.getId());
        fm.setOnAddCommentClickListener(new CommentFragment.OnAddCommentClickListener() {
            @Override
            public void onAddCommentClick() {
                addOperate();
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.fl_container, fm).commit();
    }

    private void checkAndSendComment() {
        String text = mCommentInputEt.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            toast(R.string.activity_detail_tip_empty_comment_input);
            return;
        }
        CommonTargetOperator.doAddDiscoverComment(mDiscoverId, text);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.iv_user_avatar:
            case R.id.tv_nickname:
                PageUtils.gotoUserProfilePage(getContext(), mData.getUid());
                break;
            case R.id.tv_top_like:
                if (Enviroment.isVisitor()) {
                    toast(R.string.fm_login_tip_login_before);
                    LoginActivity.startAction(getActivity(), LoginActivity.LOGIN_TAB);
                    return;
                }
                CommonTargetOperator.doLikeDiscover(mDiscoverId, !mData.isHasLike());
                break;
            case R.id.iv_more:
                if (Enviroment.isVisitor()) {
                    toast(R.string.fm_login_tip_login_before);
                    LoginActivity.startAction(getActivity(), LoginActivity.LOGIN_TAB);
                    return;
                }
                List<String> items = new ArrayList<>();
                items.add(getString(R.string.common_str_impeach));
                if (Enviroment.isSelf(mData.getUid())) {
                    items.add(ResourcesUtils.getString(R.string.common_str_delete));
                }
                DialogHelper.showListDialog(getContext(), items, new CommonDialog.Builder.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch (position) {
                            case 0:
                                PageUtils.gotoFeedbackPage(getContext(), mData);
                                break;
                            case 1:
                                CommonTargetOperator.doDeleteDiscover(mDiscoverId);
                                break;
                        }
                    }
                });
                break;
            case R.id.tv_topic:
                PageUtils.gotoTopicPage(getContext(), mData.getTopicId());
                break;
            case R.id.iv_add_comment:
                addOperate();
                break;
            case R.id.iv_send:
                checkAndSendComment();
                break;
            default:
                break;
        }
    }

    private void addOperate() {
        if (Enviroment.isVisitor()) {
            toast(R.string.fm_login_tip_login_before);
            LoginActivity.startAction(getActivity(), LoginActivity.LOGIN_TAB);
            return;
        }
        toggleCommentBar();
    }

    @Override
    protected void onRefreshClick() {
        if (mMaybeDelete) {
            getActivity().finish();
        } else {
            showLoadingView();
            CommonFetcher.doFetchDiscoverDetail(mDiscoverId);
        }
    }

    @Override
    public void onImageClick(View parent, int position) {
        PictureBrowseActivity.startAction(getActivity(), mData.getPictureList(), position, false);
    }

    private void toggleCommentBar() {
        if (mCommentBarLayout.getVisibility() == View.VISIBLE) {
            mCommentBarLayout.setVisibility(View.GONE);
            mCommentInputEt.clearFocus();
        } else {
            mCommentBarLayout.setVisibility(View.VISIBLE);
            mCommentInputEt.requestFocus();
            KeyboardUtils.showKeyboard(mCommentInputEt);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(CommonEvent<DiscoverData> event) {
        hideTipView();
        switch (event.getType()) {
            case FETCH_DISCOVER_DETAIL:
                if (event.getCode() == Config.CODE_OK) {
                    mData = event.getData();
                    mViewHolder.bindData(mData);
                    mCommentLabelTv.setText(String.format(getString(R.string.activity_detail_str_comment), mData.getCommentCount()));
                    initFragment();
                } else if (event.getCode() == Config.CODE_NO_DATA) {
                    mMaybeDelete = true;
                    showErrorView(getString(R.string.common_tip_no_data), getString(R.string.common_tip_turn_back));
                } else if (event.getCode() == Config.CODE_REQUEST_ERROR) {
                    showErrorView(R.drawable.bg_no_network, getString(R.string.common_tip_no_network));
                } else {
                    showErrorView(event.getStatus());
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonOperateEvent(CommonOperateEvent event) {
        if (event.getTargetId() != mDiscoverId) {
            return;
        }
        switch (event.getType()) {
            case LIKE_DISCOVER:
                if (event.getCode() == Config.CODE_OK) {
                    boolean isLike = !mData.isHasLike();
                    int likeCount = mData.getLikeCount() + (isLike ? 1 : -1);
                    mData.setHasLike(isLike);
                    mData.setLikeCount(likeCount);
                    mViewHolder.mTopLikeTv.setSelected(isLike);
                    mViewHolder.mTopLikeTv.setText(likeCount <= 0 ? getString(R.string.common_str_like) : String.valueOf(likeCount));
                } else {
                    toast(event.getStatus());
                }
                break;
            case ADD_DISCOVER_COMMENT:
                if (event.getCode() == Config.CODE_OK) {
                    mData.setCommentCount(mData.getCommentCount() + 1);
                    mCommentLabelTv.setText(String.format(getString(R.string.activity_detail_str_comment), mData.getCommentCount()));
                    mCommentInputEt.setText(null);
                    toggleCommentBar();
                    toast(R.string.common_tip_comment_success);
                } else {
                    toast(event.getStatus());
                }
                break;
            case DELETE_ACTIVITY_COMMENT:
                if (event.getCode() == Config.CODE_OK) {
                    mData.setCommentCount(mData.getCommentCount() - 1);
                    mCommentLabelTv.setText(String.format(getString(R.string.activity_detail_str_comment), mData.getCommentCount()));
                    toast(R.string.common_str_delete_success);
                } else {
                    toast(event.getStatus());
                }
                break;
            case FETCH_DISCOVER_DYNAMIC_DATA:
                if (event.getCode() == Config.CODE_OK) {
                    DiscoverDynamicData data = (DiscoverDynamicData) event.getData();
                    mData.setCommentCount(data.getCommentCount());
                    mData.setLikeCount(data.getLikeCount());
                    mCommentLabelTv.setText(String.format(getString(R.string.activity_detail_str_comment), data.getCommentCount()));
                    mViewHolder.mTopLikeTv.setText(
                            mData.getLikeCount() <= 0
                                    ? getString(R.string.common_str_like) : String.valueOf(mData.getLikeCount()));
                }
                break;
            case DELETE_DISCOVER:
                if (event.getCode() == Config.CODE_OK) {
                    GlobalSource.deleteDiscoverItemDataIfNeed((DiscoverData) event.getData());
                    getActivity().finish();
                } else {
                    toast(event.getStatus());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackClick() {
        GlobalSource.updateDiscoverItemDataIfNeed(mData);
    }
}
