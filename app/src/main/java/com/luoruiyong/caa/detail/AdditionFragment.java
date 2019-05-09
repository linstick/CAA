package com.luoruiyong.caa.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.base.LoadMoreSupportAdapter;
import com.luoruiyong.caa.bean.AdditionData;
import com.luoruiyong.caa.bean.CommentData;
import com.luoruiyong.caa.eventbus.CommonOperateEvent;
import com.luoruiyong.caa.model.CommonTargetOperator;
import com.luoruiyong.caa.model.puller.CommonPuller;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/3/003
 **/
public class AdditionFragment extends BaseSwipeFragment<AdditionData> {

    private static final String TAG = "AdditionFragment";
    private int mUid;
    private int mActivityId;

    public static AdditionFragment newInstance(int uid, int activityId) {
        AdditionFragment fm = new AdditionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TARGET_UID, uid);
        bundle.putInt(KEY_TARGET_ID, activityId);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageId = Config.PAGE_ID_ACTIVITY_ADDITION;
        setCanPullRefresh(false);
        setSupportImpeach(false);
        handleArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
        return view;
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mUid = bundle.getInt(KEY_TARGET_UID, -1);
        mActivityId = bundle.getInt(KEY_TARGET_ID, -1);
    }

    private String getLastItemTime() {
        if (!ListUtils.isEmpty(mList)) {
            return mList.get(mList.size() - 1).getPublishTime();
        }
        return Config.DEFAULT_TIME_STAMP;
    }

    @Override
    protected void doRefresh() {
        mRefreshLayout.setRefreshing(true);
        CommonPuller.refreshActivityAddition(mActivityId);
    }

    @Override
    protected void doLoadMore() {
        LogUtils.d(TAG, "doLoadMore: " + mPageId);
        if (mAdapter instanceof LoadMoreSupportAdapter) {
            ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_loading_more));
        }
        CommonPuller.loadMoreActivityAddition(mActivityId, getLastItemTime());
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List<AdditionData> list) {
        return new ListAdapter(mList);
    }

    @Override
    protected void setupRecyclerViewDivider() {
    }

    @Override
    protected void onDeleteItem(int position) {
        CommonTargetOperator.doDeleteActivityAddition(mActivityId, mList.get(position).getId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonOperateEvent(CommonOperateEvent<AdditionData> event) {
        if (event.getTargetId() != mActivityId) {
            return;
        }
        switch (event.getType()) {
            case ADD_ACTIVITY_ADDITION:
                if (event.getCode() == Config.CODE_OK) {
                    // 补充内容发送成功
                    hideTipView();
                    AdditionData data = event.getData();
                    if (mList == null) {
                        mList = new ArrayList<>();
                    }
                    mList.add(0, data);
                    initAdapterIfNeed();
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            case DELETE_ACTIVITY_ADDITION:
                if (event.getCode() == Config.CODE_OK) {
                    AdditionData data = event.getData();
                    for (int i = 0; i < ListUtils.getSize(mList); i++) {
                        if (mList.get(i).getId() == data.getId()) {
                            mList.remove(i);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    if (ListUtils.isEmpty(mList)) {
                        showErrorView(R.drawable.bg_load_fail, getString(R.string.common_tip_no_related_content));
                    }
                } else {
                    Toast.makeText(getContext(), event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    class ListAdapter extends LoadMoreSupportAdapter<AdditionData> {

        public ListAdapter(List<AdditionData> list) {
            super(list);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_LOAD_MORE_TIP) {
                return super.onCreateViewHolder(parent, viewType);
            }
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_addition_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position, List payloads) {
            super.onBindViewHolder(viewHolder, position);
            if (viewHolder instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) viewHolder;
                AdditionData data = mList.get(position);
                holder.bindData(data);

                holder.itemView.setTag(position);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (Enviroment.isVisitor()) {
                            Toast.makeText(getContext(), R.string.fm_login_tip_login_before, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        int itemPosition = (int) v.getTag();
                        showMoreOperateDialog(itemPosition, mUid);
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return ListUtils.getSize(mList);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView mPublishTimeTv;
            private TextView mContentTv;

            public ViewHolder(View itemView) {
                super(itemView);
                mPublishTimeTv = itemView.findViewById(R.id.tv_publish_time);
                mContentTv = itemView.findViewById(R.id.tv_content);
            }

            public void bindData(AdditionData data) {
                mPublishTimeTv.setText(TimeUtils.format(data.getPublishTime()));
                mContentTv.setText(data.getContent());
            }
        }
    }
}
