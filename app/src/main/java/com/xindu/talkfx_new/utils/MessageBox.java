package com.xindu.talkfx_new.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xindu.talkfx_new.R;

import java.util.Timer;
import java.util.TimerTask;

public class MessageBox {
    public static  boolean b     = true;
    private static Timer timer = new Timer();

    /**
     * 单纯的提示
     */
    public static void promptDialog(String msg, Context context) {
        try {
            promptDialog("确定", "提示", msg, View.VISIBLE, context,
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ((Dialog) v.getTag()).dismiss();
                        }
                    }, false);
        } catch (Exception e) {
        }
    }

    /**
     * 单纯的提示(全局性)
     *
     * @param isSystem 是否是全局性的弹出框
     */
    public static void promptDialog(String msg, Context context,
                                    boolean isSystem) {
        try {
            promptDialog("确定", "提示", msg, View.VISIBLE, context,
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ((Dialog) v.getTag()).dismiss();
                        }
                    }, isSystem);
        } catch (Exception e) {
        }
    }

    /**
     * 提示，点击确认执行某些动作
     */
    public static Dialog promptDialog(String msg, Context context,
                                      OnClickListener clicklistener) {

        return promptDialog("确定", "提示", msg, View.VISIBLE, context,
                clicklistener, false);
    }

    /**
     * 提示，点击确认执行某些动作
     */
    public static void promptDialog(String btnstr, String msg, Context context,
                                    OnClickListener clicklistener) {
        try {
            promptDialog(btnstr, "提示", msg, View.VISIBLE, context,
                    clicklistener, false);
        } catch (Exception e) {
        }
    }

    /**
     * 单个按钮弹出框,自定义按钮文字,自定义标题,自定义内容,自定义点击事件
     */
    public static Dialog promptDialog(String btnstr, String title, String msg,
                                      int titleVisible, Context context,
                                      OnClickListener clicklistener, boolean isSystem) {
        return promptTwoDialog(R.layout.layout_dialog_stop_track, "", btnstr,
                title, msg, "", titleVisible, context, View.GONE, null,
                clicklistener, isSystem);
    }

    /**
     * 双按钮,自定义标题,自定义内容,自定义确定事件
     */
    public static void promptTwoDialog(String btnStrNo, String btnStrYes,
                                       Context context, String title, String msg,
                                       OnClickListener clicklistener) {
        promptTwoDialog(R.layout.layout_dialog_stop_track, btnStrNo, btnStrYes,
                title, msg, "", View.VISIBLE, context, View.VISIBLE,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((Dialog) v.getTag()).dismiss();
                    }
                }, clicklistener, false);

    }
    /**
     * 单个按钮弹出框,自定义按钮文字,自定义标题,自定义内容,自定义点击事件 是否点击外部或返回键关闭
     */
    public static Dialog promptDialog(String btnstr, String title, String msg,
                                      int titleVisible, Context context,
                                      OnClickListener clicklistener, boolean isSystem, boolean cancelable) {
        return promptTwoDialog(R.layout.layout_dialog_stop_track, "", btnstr,
                title, msg, "", titleVisible, context, View.GONE, null,
                clicklistener, isSystem, cancelable);
    }

    /**
     * 自定义 按钮名称，标题，内容，显示，点击事件
     *
     * @param btnstr1
     * @param btnstr2
     * @param msg
     * @param context
     * @param clicklistener
     * @param clicklistener1
     */
    public static Dialog promptTwoDialog(int layout, String btnstr1,
                                         String btnstr2, String title, String msg, String strEt,
                                         int titleVisible, Context context, int btn1Visible,
                                         OnClickListener clicklistener,
                                         OnClickListener clicklistener1, boolean isSystem, boolean cancelable) {
        final Dialog dialog = new Dialog(context, R.style.sc_FullScreenDialog);
        LayoutInflater mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reNameView = mLayoutInflater.inflate(layout, null);
        LayoutParams params = new LayoutParams(
                (int) (Utils.getScreenWidth(context) * 0.8), LayoutParams.WRAP_CONTENT);
        dialog.addContentView(reNameView, params);
        dialog.setCancelable(cancelable);
        Button rb1 = (Button) reNameView.findViewById(R.id.rb1);
        Button rb2 = (Button) reNameView.findViewById(R.id.rb2);
        if (layout == R.layout.layout_dialog_stop_track) {
            TextView titleTv = (TextView) reNameView
                    .findViewById(R.id.tv_title);
            titleTv.setVisibility(titleVisible);
            titleTv.setText(title);
            TextView tellTv = (TextView) reNameView.findViewById(R.id.tellTv);
            tellTv.setText(msg);
            rb1.setTag(dialog);
            rb2.setTag(dialog);

        } else {
            TextView tellTv = (TextView) reNameView.findViewById(R.id.tellTv);
            tellTv.setText(msg);

            rb1.setTag(dialog);
            rb2.setTag(dialog);
        }

        rb1.setVisibility(btn1Visible);

        rb1.setText(btnstr1);
        rb1.setOnClickListener(clicklistener);

        rb2.setText(btnstr2);
        rb2.setOnClickListener(clicklistener1);
//        if (b) {
//            b = false;
//            timer.schedule(new TimerTask() {
//
//                @Override
//                public void run() {
//                    b = true;
//                }
//            }, 1000);
        if (isSystem) {
            dialog.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();

//        } else {
//            timer.schedule(new TimerTask() {
//
//                @Override
//                public void run() {
//                    b = true;
//                }
//            }, 1000);
//        }
        return dialog;

    }

    /**
     * 双按钮,自定义标题,自定义内容,自定义确定事件,自定义取消事件
     */
    public static void promptTwoDialog(String btnStrNo, String btnStrYes,
                                       Context context, String title, String msg,
                                       OnClickListener clicklistenerConfirm, OnClickListener clicklistenerCancel) {
        promptTwoDialog(R.layout.layout_dialog_stop_track, btnStrNo, btnStrYes,
                title, msg, "", View.VISIBLE, context, View.VISIBLE, clicklistenerCancel, clicklistenerConfirm, false);

    }

    /**
     * 另一个布局， 双按钮,自定义标题,自定义内容,自定义确定事件
     */
    public static void promptTwoDialog(String btnStrNo, String btnStrYes,
                                       Context context, String msg, OnClickListener clicklistener) {
        promptTwoDialog(R.layout.layout_dialog_stop_track1, btnStrNo,
                btnStrYes, "", msg, "", View.VISIBLE, context, View.VISIBLE,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((Dialog) v.getTag()).dismiss();
                    }
                }, clicklistener, false);

    }


    /**
     * 自定义 按钮名称，标题，内容，显示，点击事件
     *
     * @param btnstr1
     * @param btnstr2
     * @param msg
     * @param context
     * @param clicklistener
     * @param clicklistener1
     */
    public static Dialog promptTwoDialog(int layout, String btnstr1,
                                         String btnstr2, String title, String msg, String strEt,
                                         int titleVisible, Context context, int btn1Visible,
                                         OnClickListener clicklistener,
                                         OnClickListener clicklistener1, boolean isSystem) {
        final Dialog dialog = new Dialog(context, R.style.sc_FullScreenDialog);
        LayoutInflater mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reNameView = mLayoutInflater.inflate(layout, null);
        LayoutParams params = new LayoutParams(
                (int) (Utils.getScreenWidth(context) * 0.8), LayoutParams.WRAP_CONTENT);
        dialog.addContentView(reNameView, params);
        Button rb1 = (Button) reNameView.findViewById(R.id.rb1);
        Button rb2 = (Button) reNameView.findViewById(R.id.rb2);
        if (layout == R.layout.layout_dialog_stop_track) {
            TextView titleTv = (TextView) reNameView
                    .findViewById(R.id.tv_title);
            titleTv.setVisibility(titleVisible);
            titleTv.setText(title);
            TextView tellTv = (TextView) reNameView.findViewById(R.id.tellTv);
            tellTv.setText(msg);
            rb1.setTag(dialog);
            rb2.setTag(dialog);

        } else {
            TextView tellTv = (TextView) reNameView.findViewById(R.id.tellTv);
            tellTv.setText(msg);

            rb1.setTag(dialog);
            rb2.setTag(dialog);
        }

        rb1.setVisibility(btn1Visible);
        rb1.setText(btnstr1);
        rb1.setOnClickListener(clicklistener);

        rb2.setText(btnstr2);
        rb2.setOnClickListener(clicklistener1);
        if (b) {
            b = false;
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    b = true;
                }
            }, 1000);
            if (isSystem) {
                dialog.getWindow().setType(
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            dialog.show();

        } else {
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    b = true;
                }
            }, 1000);
        }
        return dialog;

    }

    public interface OnActionSheetSelected {
        void onClick(String name);
    }
}
