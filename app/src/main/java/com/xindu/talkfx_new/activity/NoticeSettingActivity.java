package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseActivity;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class NoticeSettingActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_icon)
    TextView rightIcon;
    @Bind(R.id.tv_up_price)
    QMUIRoundButton tvUpPrice;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.tv_notice)
    TextView tvNotice;
    double upPrice;
    DecimalFormat df = new DecimalFormat("######0.00000");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_setting);
        ButterKnife.bind(this);
        initTopBar();
        upPrice = Double.parseDouble(tvUpPrice.getText().toString());
        tvUpPrice.setText(df.format(upPrice * 1.25) + "");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvUpPrice.setText(df.format(upPrice * ((double) i / 100 + 1)) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initTopBar() {
        title.setText("设置提醒");
        rightIcon.setText("完成");
        rightIcon.setTextColor(getResources().getColor(R.color.blue));
    }

    @OnClick({R.id.btn_back, R.id.rl_notice, R.id.right_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.right_icon:
                finish();
                break;
            case R.id.rl_notice:
                showSimpleBottomSheetList();
                break;
        }
    }

    /**
     * 提醒频率
     */
    private void showSimpleBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(mContext)
                .addItem("仅提醒一次")
                .addItem("每天提醒")
                .addItem("持续提醒")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        tvNotice.setText(tag);
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }
}
