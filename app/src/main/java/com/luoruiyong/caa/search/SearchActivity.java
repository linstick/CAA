package com.luoruiyong.caa.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.search.adapter.SearchResultAdapter;
import com.luoruiyong.caa.search.adapter.SearchTipsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements View.OnClickListener, SearchTipsAdapter.OnItemClickListener{

    private ImageView mBackIv;
    private TextView mSearchTv;
    private EditText mSearchInputEt;

    private RecyclerView mSearchTipsRv;
    private RecyclerView mSearchResultRv;
    private ProgressBar mProgressBar;

    private List<String> mTipsList;
    private List<String> mResultList;
    private SearchTipsAdapter mTipsAdapter;
    private SearchResultAdapter mResultAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mSearchInputEt = findViewById(R.id.et_search_input);
        mSearchTv = findViewById(R.id.tv_search);
        mSearchTipsRv = findViewById(R.id.rv_search_tips);
        mSearchResultRv = findViewById(R.id.rv_search_result);
        mProgressBar = findViewById(R.id.progress_bar);

        mBackIv.setOnClickListener(this);
        mSearchInputEt.setOnClickListener(this);
        mSearchTv.setOnClickListener(this);

        mTipsList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mTipsList.add("Search Tip " + (i + 1));
        }
        mTipsAdapter = new SearchTipsAdapter(mTipsList);
        mTipsAdapter.setOnItemClickListener(this);
        mSearchTipsRv.setAdapter(mTipsAdapter);
        mSearchTipsRv.setLayoutManager(new LinearLayoutManager(this));

        mResultList = new ArrayList<>();
        mResultAdapter = new SearchResultAdapter(mResultList);
        mSearchResultRv.setAdapter(mResultAdapter);
        mSearchResultRv.setLayoutManager(new LinearLayoutManager(this));

        mSearchInputEt.requestFocus();
    }

    private void checkAndSearch(String data) {
        if (TextUtils.isEmpty(data)) {
            Toast.makeText(this, getString(R.string.search_tip_empty_search_input), Toast.LENGTH_SHORT).show();
            return;
        }
        // do search
        mSearchTipsRv.setVisibility(View.GONE);
        mSearchInputEt.clearFocus();
        mProgressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "search for " + data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_search_input:
                mSearchTipsRv.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_search:
                String data = mSearchInputEt.getText().toString().trim();
                checkAndSearch(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(String data) {
        checkAndSearch(data);
    }
}
