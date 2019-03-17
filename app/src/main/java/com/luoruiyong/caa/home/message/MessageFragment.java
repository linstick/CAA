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
import com.luoruiyong.caa.bean.Function;
import com.luoruiyong.caa.widget.UniversalFunctionContainer;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements UniversalFunctionContainer.OnFunctionClickListener{

    private UniversalFunctionContainer mFunctionContainer;
    private List<Function> mFunctionList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFunctionContainer = (UniversalFunctionContainer) inflater.inflate(R.layout.fragment_message, container, false);
        initView();
        return mFunctionContainer;
    }

    private void initView() {
        mFunctionList = new ArrayList<>();
        mFunctionList.add(new Function(Function.TYPE_WITH_RED_POINT_AND_RIGHT_SIGN, "Reply Activity"));
        mFunctionList.add(new Function(Function.TYPE_WITH_RED_POINT_AND_RIGHT_SIGN, "Reply Discover"));
        mFunctionList.add(new Function(Function.TYPE_WITH_RED_POINT_AND_RIGHT_SIGN, "Join Topic"));
        mFunctionList.add(new Function(Function.TYPE_WITH_RED_POINT_AND_RIGHT_SIGN, "Collect Activity"));
        mFunctionList.add(new Function(Function.TYPE_WITH_RED_POINT_AND_RIGHT_SIGN, "Like Discover"));

        List<Integer> list = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < mFunctionList.size(); i++) {
            list.add(count);
            count = count * 10 + 9;
        }
        mFunctionContainer.setFunctionList(mFunctionList);
        mFunctionContainer.showRedPoints(list);
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
        mFunctionContainer.showRedPoint(position, -1);
    }
}
