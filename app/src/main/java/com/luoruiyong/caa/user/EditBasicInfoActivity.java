package com.luoruiyong.caa.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.MyApplication;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.dialog.common.CommonDialog;
import com.luoruiyong.caa.dialog.common.DialogHelper;

import java.util.ArrayList;
import java.util.List;

public class EditBasicInfoActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTitleTv;
    private TextView mFinishTv;
    private RecyclerView mRecyclerView;

    private User mCurUser;
    private int mLastClickPosition = -1;
    private List<UserBasicMap> mList;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();

        initUserBasicMap();

        initRecyclerView();
    }

    private void initView() {
        mTitleTv = findViewById(R.id.tv_title);
        mFinishTv = findViewById(R.id.tv_cancel);
        mRecyclerView = findViewById(R.id.rv_recycler_view);

        mTitleTv.setText(getString(R.string.title_edit_profile));
        mFinishTv.setText(getString(R.string.common_str_finish));
        mFinishTv.setVisibility(View.VISIBLE);

        findViewById(R.id.iv_back).setOnClickListener(this);
        mFinishTv.setOnClickListener(this);
    }

    private void initUserBasicMap() {
        mList = new ArrayList<>();
        mCurUser = MyApplication.getCurUser();
        if (mCurUser == null) {
            return;
        }

        UserBasicMap info = new UserBasicMap();
        info.setLabel("Avatar");
        info.setValue(mCurUser.getAvatar());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Nickname");
        info.setValue(mCurUser.getNickName());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Real name");
        info.setValue(mCurUser.getRealName());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Gender");
        info.setValue(mCurUser.getGender());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Age");
        info.setValue(String.valueOf(mCurUser.getAge()));
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Cell number");
        info.setValue(mCurUser.getCellNumber());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Email");
        info.setValue(mCurUser.getEmail());
        mList.add(info);

        User.CollegeInfo collegeInfo = mCurUser.getCollegeInfo();
        info = new UserBasicMap();
        info.setLabel("College");
        info.setValue(collegeInfo == null ? "" : collegeInfo.getName());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Department");
        info.setValue(collegeInfo == null ? "" : collegeInfo.getDepartment());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Major");
        info.setValue(collegeInfo == null ? "" : collegeInfo.getMajor());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Class");
        info.setValue(collegeInfo == null ? "" : collegeInfo.getKlass());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel("Description");
        info.setValue(mCurUser.getDescription());
        mList.add(info);
    }

    private void initRecyclerView() {
        mAdapter = new ListAdapter(mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void handleItemClick(int position) {
        mLastClickPosition = position;
        if (position == 0) {
            choosePicture();
        } else {
            UserBasicMap data = mList.get(position);
            CommonDialog dialog = new CommonDialog.Builder(this)
                    .type(CommonDialog.TYPE_INPUT)
                    .title(data.getLabel())
                    .preInputText(data.getValue())
                    .positive(getString(R.string.common_str_confirm))
                    .onPositive(new CommonDialog.Builder.OnClickListener() {
                        @Override
                        public void onClick(String extras) {
                            mList.get(mLastClickPosition).setValue(extras);
                            mAdapter.notifyItemChanged(mLastClickPosition);
                        }
                    })
                    .negative(getString(R.string.common_str_cancel))
                    .build();
            dialog.show();
        }
    }

    private void choosePicture() {
        Toast.makeText(this, "Choose Picture", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            default:
                break;
        }

    }

    class ListAdapter extends RecyclerView.Adapter implements View.OnClickListener{

        private final static int TYPE_AVATAR = 1;
        private final static int TYPE_OTHER = 2;

        private List<UserBasicMap> mList;

        public ListAdapter(List<UserBasicMap> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder viewHolder;
            switch (viewType) {
                case TYPE_AVATAR:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_atavar, parent, false);
                    viewHolder = new AvatarViewHolder(view);
                    break;
                case TYPE_OTHER:
                default:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_basic_info, parent, false);
                    viewHolder = new OtherViewHolder(view);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof AvatarViewHolder) {
                AvatarViewHolder viewHolder = (AvatarViewHolder) holder;
                viewHolder.mAvatarIv.setTag(position);
                viewHolder.mAvatarIv.setOnClickListener(this);
                viewHolder.bindData(mList.get(position));
            } else {
                OtherViewHolder viewHolder = (OtherViewHolder) holder;
                viewHolder.mValueTv.setTag(position);
                viewHolder.mValueTv.setOnClickListener(this);
                viewHolder.bindData(mList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_AVATAR;
            }
            return TYPE_OTHER;
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            handleItemClick(position);
        }

        class AvatarViewHolder extends RecyclerView.ViewHolder{

            private ImageView mAvatarIv;

            public AvatarViewHolder(View itemView) {
                super(itemView);
                mAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
            }

            public void bindData(UserBasicMap data) {
                // set avatar url
            }
        }

        class OtherViewHolder extends RecyclerView.ViewHolder {

            private TextView mLabelTv;
            private TextView mValueTv;

            public OtherViewHolder(View itemView) {
                super(itemView);
                mLabelTv = itemView.findViewById(R.id.tv_label);
                mValueTv = itemView.findViewById(R.id.tv_value);
            }

            public void bindData(UserBasicMap data) {
                mLabelTv.setText(data.getLabel());
                mValueTv.setText(data.getValue());
            }
        }
    }
}
