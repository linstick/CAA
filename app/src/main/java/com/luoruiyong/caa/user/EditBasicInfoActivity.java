package com.luoruiyong.caa.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.MyApplication;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.ImageBean;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.bean.UserBasicMap;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.CommonPoster;
import com.luoruiyong.caa.model.ImageLoader;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.PictureUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditBasicInfoActivity extends BaseActivity implements View.OnClickListener{

    private final static int CHOOSE_PICTURE_REQUEST_CODE = 1;

    private TextView mTitleTv;
    private TextView mFinishTv;
    private RecyclerView mRecyclerView;

    private User mCurUser;
    private int mLastClickPosition = -1;
    private List<UserBasicMap> mList;
    private ListAdapter mAdapter;

    private String mNewAvatar;

    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();

        initUserBasicMap();

        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
        mCurUser = Enviroment.getCurUser();
        if (mCurUser == null) {
            return;
        }

        UserBasicMap info = new UserBasicMap();
        info.setLabel(getString(R.string.common_str_avatar));
        info.setValue(new ImageBean(mCurUser.getAvatar()));
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel(getString(R.string.common_str_account));
        info.setValue(mCurUser.getId());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel(getString(R.string.common_str_nickname));
        info.setValue(mCurUser.getNickname());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel(getString(R.string.modify_profile_str_gender));
        info.setValue(mCurUser.getGender());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel(getString(R.string.modify_profile_str_age));
        info.setValue(String.valueOf(mCurUser.getAge()));
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel(getString(R.string.modify_profile_str_email));
        info.setValue(mCurUser.getEmail());
        mList.add(info);

        User.CollegeInfo collegeInfo = mCurUser.getCollegeInfo();
        info = new UserBasicMap();
        info.setLabel(getString(R.string.modify_profile_str_college));
        info.setValue(collegeInfo == null ? "" : collegeInfo.getName());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel(getString(R.string.modify_profile_str_department));
        info.setValue(collegeInfo == null ? "" : collegeInfo.getDepartment());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel(getString(R.string.modify_profile_str_major));
        info.setValue(collegeInfo == null ? "" : collegeInfo.getMajor());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel(getString(R.string.modify_profile_str_klass));
        info.setValue(collegeInfo == null ? "" : collegeInfo.getKlass());
        mList.add(info);

        info = new UserBasicMap();
        info.setLabel(getString(R.string.modify_profile_str_description));
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
            showChoosePictureTypeDialog();
        } else if (position == 1) {
            Toast.makeText(this, R.string.modify_profile_tip_can_no_modify_id, Toast.LENGTH_SHORT).show();
        } else {
            UserBasicMap data = mList.get(position);
            CommonDialog dialog = new CommonDialog.Builder(this)
                    .type(CommonDialog.TYPE_INPUT)
                    .title(data.getLabel())
                    .preInputText(data.getValue().toString())
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

    private void showChoosePictureTypeDialog() {
        DialogHelper.showListDialog(this, ResourcesUtils.getStringArray(R.array.str_arr_choose_picture), new CommonDialog.Builder.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        PageUtils.gotoSystemCamera(EditBasicInfoActivity.this, PageUtils.REQUEST_CODE_USE_CAMERA);
                        break;
                    case 1:
                        PageUtils.gotoSystemAlbum(EditBasicInfoActivity.this, PageUtils.REQUEST_CODE_USE_SYSTEM_ALBUM);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void doModifyProfile() {
        mLoadingDialog = DialogHelper.showLoadingDialog(this, getString(R.string.modify_profile_on_modify), false);
        User user = new User();
        user.setUid(mCurUser.getUid());

        user.setNickname((String) mList.get(2).getValue());
        user.setGender((String) mList.get(3).getValue());
        user.setAge(Integer.valueOf((String) mList.get(4).getValue()));
        user.setEmail((String) mList.get(5).getValue());

        User.CollegeInfo info = new User.CollegeInfo();
        info.setName((String) mList.get(6).getValue());
        info.setDepartment((String) mList.get(7).getValue());
        info.setMajor((String) mList.get(8).getValue());
        info.setKlass((String) mList.get(9).getValue());
        user.setCollegeInfo(info);

        user.setDescription((String) mList.get(10).getValue());
        CommonPoster.doModifyProfile(user, mNewAvatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel:
                doModifyProfile();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String path;
            switch (requestCode) {
                case PageUtils.REQUEST_CODE_USE_CAMERA:
                    PageUtils.gotoSystemCropForAvatar(this, Uri.fromFile(new File(Enviroment.getCacheFolder(), Enviroment.TEMP_FILE_NAME)), PageUtils.REQUEST_CODE_USE_SYSTEM_CROP);
                    break;
                case PageUtils.REQUEST_CODE_USE_SYSTEM_ALBUM:
                    if (data != null) {
                        path = PictureUtils.getPath(this, data.getData());
                        PageUtils.gotoSystemCropForAvatar(this, Uri.fromFile(new File(path)), PageUtils.REQUEST_CODE_USE_SYSTEM_CROP);
                    }
                    break;
                case PageUtils.REQUEST_CODE_USE_SYSTEM_CROP:
                    mNewAvatar = Enviroment.getTempFilePath();
                    ImageBean imageBean = (ImageBean) mList.get(0).getValue();
                    imageBean.setPath(mNewAvatar);
                    imageBean.setType(ImageBean.TYPE_LOCAL_FILE);
                    mAdapter.notifyItemChanged(0);
                    break;
                default:
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentEvent(CommonEvent<User> event) {
        switch (event.getType()) {
            case MODIFY_PROFILE:
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                if (event.getCode() == Config.CODE_OK) {
                    Enviroment.setCurUser(event.getData());
                    Toast.makeText(MyApplication.getAppContext(), R.string.modify_profile_tip_modify_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, event.getStatus(), Toast.LENGTH_SHORT).show();
                }
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

            private TextView mLabelTv;
            private SimpleDraweeView mAvatarIv;

            public AvatarViewHolder(View itemView) {
                super(itemView);
                mLabelTv = itemView.findViewById(R.id.tv_label);
                mAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
            }

            public void bindData(UserBasicMap data) {
                mLabelTv.setText(data.getLabel());
                ImageLoader.setImageSourceWithoutCache(mAvatarIv, (ImageBean) data.getValue());
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
                mValueTv.setText(data.getValue() != null ? data.getValue().toString() : null);
            }
        }
    }
}
