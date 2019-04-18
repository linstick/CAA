package com.luoruiyong.caa.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.luoruiyong.caa.MyApplication;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.ImageBean;
import com.luoruiyong.caa.utils.PictureUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

import java.io.File;

/**
 * Author: luoruiyong
 * Date: 2019/4/15/015
 * Description:
 **/
public class ImageLoader {

    public static void loadAndSave(String url, OnLoadAndSaveCallback callback) {
        loadAndSave(Uri.parse(url), callback);
    }

    public static void loadAndSave(Uri uri, OnLoadAndSaveCallback callback) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setProgressiveRenderingEnabled(true).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                //bitmap即为下载所得图片
                String path = PictureUtils.save(bitmap);
                if (TextUtils.isEmpty(path)) {
                    if (callback != null) {
                        callback.onFail(ResourcesUtils.getString(R.string.common_image_save_fail));
                    }
                } else {
                    if (callback != null) {
                        callback.onSuccess(bitmap, path);
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (callback != null) {
                    callback.onFail(ResourcesUtils.getString(R.string.common_image_load_fail));
                }
            }
        }, CallerThreadExecutor.getInstance());
    }

    public static void showUrlBlur(SimpleDraweeView draweeView, String url, int iterations, int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(6, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageSource(SimpleDraweeView draweeView, ImageBean bean) {
        if (draweeView == null || bean == null) {
            return;
        }
        switch (bean.getType()) {
            case ImageBean.TYPE_RESOURCE_ID:
                draweeView.setActualImageResource(bean.getResId());
                break;
            case ImageBean.TYPE_LOCAL_FILE:
                draweeView.setImageURI(Uri.fromFile(new File(bean.getPath())));
                break;
            case ImageBean.TYPE_REMOTE_FILE:
                draweeView.setImageURI(bean.getUrl());
                break;
            default:
                break;
        }
    }

    public interface OnLoadAndSaveCallback {
        void onSuccess(Bitmap bitmap, String path);
        void onFail(String error);
    }
}
