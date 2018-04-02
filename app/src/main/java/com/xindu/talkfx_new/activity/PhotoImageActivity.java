package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.utils.Utils;

/**
 * Created by LeeBoo on 2018/4/2.
 */

public class PhotoImageActivity extends BaseActivity {
    String img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_img);
        img = getIntent().getStringExtra("img");
        Log.d("Ok==img==", img + "");
        if (TextUtils.isEmpty(img)) {
            return;
        }
        final PhotoView photoView = findViewById(R.id.photo_view);
        if (img.startsWith("http://") || img.startsWith("https://")) {
            Glide.with(mContext).load(img).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    photoView.setImageDrawable(resource);
                }
            });
        } else {
            photoView.setImageBitmap(Utils.base64ToBitmap(img));
        }
    }
}
