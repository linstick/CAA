package com.luoruiyong.caa.simple;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.common.callback.OnLoadAndSaveCallback;
import com.luoruiyong.caa.model.ImageLoader;
import com.luoruiyong.caa.utils.ListUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class PictureBrowseActivity extends BaseActivity implements View.OnClickListener {

    private final static String KEY_URL_LIST = "key_url_list";
    private final static String KEY_CUR_POSITION = "key_cur_position";
    private final static String KEY_IS_LOCAL_FILE = "key_is_local_file";
    public final static String KEY_DELETE_LIST = "key_delete_list";

    private ViewPager mViewPager;
    private ImageView mBackIv;
    private TextView mCurIndexTv;
    private TextView mTotalIndexTv;
    private ImageView mDownloadIv;
    private ImageView mDeleteIv;

    private SimpleViewPagerAdapter mAdapter;
    private List<String> mUrls;
    private int mCurPosition;
    private boolean mIsLocalFile;

    private ArrayList<Integer> mDeleteList;

    public static void startAction(Fragment fragment, List<String> list) {
        startAction(fragment, list, 0);
    }

    public static void startAction(Fragment fragment, List<String> list, int position) {
        startAction(fragment, list, position, false);
    }

    public static void startAction(Fragment fragment, List<String> list, int position, boolean isLocalFile) {
        startAction(fragment, list, position, isLocalFile, -1);
    }

    public static void startAction(Fragment fragment, List<String> list, int position, boolean isLocalFile, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), PictureBrowseActivity.class);
        intent.putStringArrayListExtra(KEY_URL_LIST, (ArrayList<String>) list);
        intent.putExtra(KEY_CUR_POSITION, position);
        intent.putExtra(KEY_IS_LOCAL_FILE, isLocalFile);
        if (requestCode != -1) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            fragment.startActivity(intent);
        }
    }

    public static void startAction(Activity activity, List<String> list) {
        startAction(activity, list, 0);
    }

    public static void startAction(Activity activity, List<String> list, int position) {
        startAction(activity, list, position, false);
    }

    public static void startAction(Activity fragment, List<String> list, int position, boolean isLocalFile) {
        startAction(fragment, list, position, isLocalFile, -1);
    }

    public static void startAction(Activity fragment, List<String> list, int position, boolean isLocalFile, int requestCode) {
        Intent intent = new Intent(fragment, PictureBrowseActivity.class);
        intent.putStringArrayListExtra(KEY_URL_LIST, (ArrayList<String>) list);
        intent.putExtra(KEY_CUR_POSITION, position);
        intent.putExtra(KEY_IS_LOCAL_FILE, isLocalFile);
        if (requestCode != -1) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            fragment.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_browse);

        initView();

        handleIntent();
    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager);
        mCurIndexTv = findViewById(R.id.tv_cur_index);
        mTotalIndexTv = findViewById(R.id.tv_total_index);
        mDownloadIv = findViewById(R.id.iv_download);
        mDeleteIv = findViewById(R.id.iv_delete);
        mBackIv = findViewById(R.id.iv_back);

        mBackIv.setOnClickListener(this);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mUrls = intent.getStringArrayListExtra(KEY_URL_LIST);
            mCurPosition = intent.getIntExtra(KEY_CUR_POSITION, -1);
            mIsLocalFile = intent.getBooleanExtra(KEY_IS_LOCAL_FILE, false);
        }
        if (ListUtils.isEmpty(mUrls) || !ListUtils.isIndexBetween(mUrls, mCurPosition)) {
            finish();
            return;
        }
        mAdapter = new SimpleViewPagerAdapter(mUrls, mIsLocalFile);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurPosition = position;
                mCurIndexTv.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(mCurPosition, false);
        mCurIndexTv.setText(String.valueOf(mCurPosition + 1));
        mTotalIndexTv.setText(String.valueOf(mUrls.size()));

        if (!mIsLocalFile) {
            mDownloadIv.setVisibility(View.VISIBLE);
            mDownloadIv.setOnClickListener(this);
        }
        if (mIsLocalFile) {
            mDeleteIv.setVisibility(View.VISIBLE);
            mDeleteIv.setOnClickListener(this);
            mDeleteList = new ArrayList<>();
        }
    }

    private void setResultDataBeforeFinish() {
        Intent intent = new Intent();
        if (ListUtils.isEmpty(mDeleteList)) {
            setResult(RESULT_CANCELED);
        } else {
            intent.putIntegerArrayListExtra(KEY_DELETE_LIST, mDeleteList);
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_download:
                ImageLoader.loadAndSave(mUrls.get(mCurPosition), new OnLoadAndSaveCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, String path) {
                        toast(String.format(getString(R.string.common_image_save_success), path));
                    }

                    @Override
                    public void onFail(String error) {
                        toast(error);
                    }
                });
                break;
            case R.id.iv_delete:
                mDeleteList.add(mCurPosition);
                mUrls.remove(mCurPosition);
                if (ListUtils.isEmpty(mUrls)) {
                    setResultDataBeforeFinish();
                    finish();
                }
                mAdapter.notifyDataSetChanged();
                mTotalIndexTv.setText(String.valueOf(mUrls.size()));
                break;
            case R.id.iv_back:
            case R.id.iv_picture:
                setResultDataBeforeFinish();
                finish();
                break;
            default:
                break;
        }
    }

    class SimpleViewPagerAdapter extends PagerAdapter {

        private final int MAX_CACHE_COUNT = 3;

        private List<String> mList;
        private List<View> mViewCache;
        private boolean mIsLocalFile;

        public SimpleViewPagerAdapter(List<String> list, boolean isLoacalFile) {
            this.mIsLocalFile = isLoacalFile;
            this.mList = list;
            this.mViewCache = new LinkedList<>();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view;
            if (!ListUtils.isEmpty(mViewCache)) {
                view = mViewCache.remove(0);
            } else {
                SimpleDraweeView imageView = new SimpleDraweeView(container.getContext());
                imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
                ViewGroup.LayoutParams params = new ViewPager.LayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                imageView.setLayoutParams(params);
                imageView.setId(R.id.iv_picture);
                view = imageView;
            }
            if (mIsLocalFile) {
                ((SimpleDraweeView)view).setImageURI(Uri.fromFile(new File(mUrls.get(position))));
            } else {
                ((SimpleDraweeView)view).setImageURI(mUrls.get(position));
            }
            view.setOnClickListener(PictureBrowseActivity.this);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            SimpleDraweeView view = (SimpleDraweeView) object;
            container.removeView(view);
            if (mViewCache.size() < MAX_CACHE_COUNT) {
                view.setImageURI(Uri.EMPTY);
                mViewCache.add(view);
            }
        }

        @Override
        public int getCount() {
            return ListUtils.getSize(mList);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
