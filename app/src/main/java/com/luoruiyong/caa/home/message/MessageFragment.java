package com.luoruiyong.caa.home.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.widget.UniversalFunctionContainer;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements UniversalFunctionContainer.OnFunctionClickListener{

    private UniversalFunctionContainer mFunctionContainer;
    private List<String> mFunctionList;
    private List<Integer> mRedPointCountList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFunctionContainer = (UniversalFunctionContainer) inflater.inflate(R.layout.fragment_message, container, false);
        initView();
        return mFunctionContainer;
    }

    private void initView() {
        mFunctionList = new ArrayList<>();
        mFunctionList.add("Reply Activity");
        mFunctionList.add("Reply Discover");
        mFunctionList.add("Join Topic");
        mFunctionList.add("Collect Activity");
        mFunctionList.add("Like Discover");

        mRedPointCountList = new ArrayList<>();
        mRedPointCountList.add(0);
        mRedPointCountList.add(1);
        mRedPointCountList.add(9);
        mRedPointCountList.add(99);
        mRedPointCountList.add(999);
        mFunctionContainer.setFunctionList(mFunctionList, mRedPointCountList);
        mFunctionContainer.setOnFunctionClickListener(this);
    }

    @Override
    public void onFunctionClick(int position) {
        switch (position) {
            case 0:
                Toast.makeText(getContext(), "replay activity", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getContext(), "replay discover", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getContext(), "join topic", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getContext(), "collect activity", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getContext(), "like discover", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        mFunctionContainer.updateRedPoint(position, -1);
    }
}
