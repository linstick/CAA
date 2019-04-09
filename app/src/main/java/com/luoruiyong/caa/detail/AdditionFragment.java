package com.luoruiyong.caa.detail;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.AdditionData;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ListUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/3/003
 **/
public class AdditionFragment extends BaseSwipeFragment<AdditionData> {

    private static final String KEY_UID = "key_uid";
    private long mUid;

    public static AdditionFragment newInstance(long uid) {
        AdditionFragment fm = new AdditionFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_UID, uid);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleArguments();

        // for test
        for (int i = 0; i < 30; i++) {
            mList.add(new AdditionData(i));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = DisplayUtils.dp2px(1);
                }
            }
        });
        return view;
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mUid = bundle.getLong(KEY_UID, -1);
    }

    @Override
    protected void initListAdapter(List<AdditionData> list) {
        mAdapter = new ListAdapter(mList);
    }

    @Override
    protected void setupRecyclerViewDivider() {
    }

    @Override
    protected void doRefresh() {
        Toast.makeText(getContext(), "doRefresh", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void doLoadMore() {
        Toast.makeText(getContext(), "doLoadMore", Toast.LENGTH_SHORT).show();
    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        private List<AdditionData> mList;

        public ListAdapter(List<AdditionData> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_addition_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            AdditionData data = mList.get(position);
            holder.bindData(data);

            holder.itemView.setTag(position);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int itemPosition = (int) v.getTag();
                    showMoreOperateDialog(itemPosition, mUid);
                    return true;
                }
            });
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
//                mPublishTimeTv.setText(data.getPublishTime() + "");
                mContentTv.setText(data.getContent());
            }
        }
    }
}
