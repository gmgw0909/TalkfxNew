package com.xindu.talkfx_new.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.cache.CacheManager;
import com.xindu.talkfx_new.utils.PermissionUtil;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.widget.CircleImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class PersonalInfoActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.userIcon)
    CircleImageView userIcon;

    private final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    public static final String AVATAR_FILE_NAME = "talk_app_avatar.png";
    public static final int REQUEST_CODE_AVATAR = 100;
    private final int REQUEST_CODE_PERMISSIONS = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        initTopBar();
    }

    private void initTopBar() {
        title.setText("个人资料");
    }

    @OnClick({R.id.btn_back, R.id.logout, R.id.rl_userIcon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.rl_userIcon:
                PermissionUtil.checkMorePermissions(mContext, PERMISSIONS,
                        new PermissionUtil.PermissionCheckCallBack() {
                            @Override
                            public void onHasPermission() {
                                // 已授予权限
                                toImagePicker();
                            }

                            @Override
                            public void onUserHasAlreadyTurnedDown(String... permission) {
                                // 上一次申请权限被拒绝，可用于向用户说明权限原因，然后调用权限申请方法。
                                new QMUIDialog.MessageDialogBuilder(mContext)
                                        .setTitle("权限申请")
                                        .setMessage("需要读写手机存储和相机权限")
                                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                PermissionUtil.requestMorePermissions(mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                                // 第一次申请权限或被禁止申请权限，建议直接调用申请权限方法。
                                PermissionUtil.requestMorePermissions(mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                            }
                        });
                break;
            case R.id.logout:
                new QMUIDialog.MessageDialogBuilder(mContext)
                        .setTitle("退出登录")
                        .setMessage("确定要退出登录吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "退出", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                SPUtil.put(Constants.IS_LOGIN, false);
                                SPUtil.put(Constants.USERNAME, "");
                                SPUtil.put(Constants.TOKEN, "");
                                //登陆成功网络请求头设置token
                                HttpHeaders headers = new HttpHeaders();
                                headers.put("token", SPUtil.getString(Constants.TOKEN));
                                OkGo.getInstance().addCommonHeaders(headers);
                                App.clearActivity();
                                startActivity(new Intent(PersonalInfoActivity.this, LoginActivity.class)
                                        .putExtra("goMain", true));
                            }
                        })
                        .show();
//                MessageBox.promptTwoDialog("取消", "确定", mContext, "确定要退出登录吗？", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        App.clearActivity();
//                        startActivity(LoginActivity.class, false);
//                        ((Dialog) view.getTag()).dismiss();
//                    }
//                });
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestMorePermissionsResult(mContext, PERMISSIONS, new PermissionUtil.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                toImagePicker();
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                showToast("需要打开读写手机存储和相机权限");
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                new QMUIDialog.MessageDialogBuilder(mContext)
                        .setTitle("权限申请")
                        .setMessage("需要读写手机存储和相机权限，点击前往，将转到应用的设置界面，开启应用的相关权限")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("前往", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                PermissionUtil.toAppSetting(mContext);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void toImagePicker() {
        SImagePicker
                .from(PersonalInfoActivity.this)
                .pickMode(SImagePicker.MODE_AVATAR)
                .showCamera(true)
                .cropFilePath(
                        CacheManager.getInstance().getImageInnerCache()
                                .getAbsolutePath(System.currentTimeMillis() + AVATAR_FILE_NAME))
                .forResult(REQUEST_CODE_AVATAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            Glide.with(App.getInstance().getApplicationContext()).load(pathList.get(0)).into(userIcon);
        }
    }

}