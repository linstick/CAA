package com.luoruiyong.caa.widget.imageviewlayout.clickmanager;

import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/7/007
 * Description:
 **/
public class ChooseImageClickManager implements IClickManager {
    @Override
    public boolean onImageClick(ImageViewLayout view, int position) {
        boolean consumeClick = false;
        List<String> mPictureUrls = view.getPictureUrls();
        if (!ListUtils.isIndexBetween(mPictureUrls, position)) {
            return consumeClick;
        }
        if (position + 1 == mPictureUrls.size()) {
            // 添加图片
            // for test
            if (Enviroment.VAR_DEBUG) {
                consumeClick = true;
                Toast.makeText(view.getContext(), "选择图片添加", Toast.LENGTH_SHORT).show();
                mPictureUrls.add(mPictureUrls.size() - 1, "https://www.baidu.com/1.jpg");
                view.notifyChildViewChanged();
            }
        } else {
            // 查看图片
            consumeClick = true;
            List list = new ArrayList();
            // 需要移除最后的那一张添加
            for (int i = 0; i < mPictureUrls.size() - 1; i++) {
                list.add(mPictureUrls.get(i));
            }
            PictureBrowseActivity.startAction(view.getContext(), list, position);
        }
        return consumeClick;
    }

    @Override
    public void onImageLongClick(final DynamicInputView wrapper, final ImageViewLayout view, final int position) {
        final List<String> urls = view.getPictureUrls();
        if (position + 1 == urls.size()) {
            return;
        }
        DialogHelper.showListDialog(view.getContext(), ResourcesUtils.getString(R.string.common_str_delete), new CommonDialog.Builder.OnItemClickListener() {
            @Override
            public void onItemClick(int which) {
                urls.remove(position);
                if (wrapper != null) {
                    wrapper.notifyInputDataChanged();
                }
            }
        });
    }
}
