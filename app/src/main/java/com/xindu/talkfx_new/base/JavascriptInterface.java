package com.xindu.talkfx_new.base;

import android.content.Context;
import android.content.Intent;

import com.xindu.talkfx_new.activity.PhotoImageActivity;

/**
 * Created by LeeBoo on 2018/4/2.
 */

public class JavascriptInterface {
    private Context context;

    public JavascriptInterface(Context context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        Intent intent = new Intent();
        intent.putExtra("img", img);
        intent.setClass(context, PhotoImageActivity.class);
        context.startActivity(intent);
        System.out.println(img);
    }
}
