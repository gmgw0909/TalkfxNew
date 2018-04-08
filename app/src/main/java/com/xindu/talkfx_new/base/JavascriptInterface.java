package com.xindu.talkfx_new.base;

import android.content.Context;
import android.content.Intent;

import com.xindu.talkfx_new.activity.PhotoBrowserActivity;
import com.xindu.talkfx_new.utils.SPUtil;

/**
 * Created by LeeBoo on 2018/4/2.
 */

public class JavascriptInterface {
    private Context context;
    private String[] imageUrls;

    public JavascriptInterface(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        SPUtil.put("imageUrls", getStr(imageUrls));
        Intent intent = new Intent();
//        intent.putExtra("imageUrls", imageUrls);
        intent.putExtra("img", img);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
    }

    public String getStr(String[] args) {
        String str = "";
        for (int i = 0; i < args.length; i++) {
            if (i==args.length-1){
                str += (String) args[i];
            }else {
                str += (String) args[i] + "==分割线==";
            }
        }
        return str;
    }
}
