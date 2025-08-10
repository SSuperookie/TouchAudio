package com.a200888.ailisten;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

public class MoreSettingActivity extends Activity {

    private ImageView ivClose;
    private SwitchCompat sb1;
    private SwitchCompat sb2;
    private SwitchCompat sb3;
    private SwitchCompat sb4;
    private SwitchCompat sb5;
    private SwitchCompat sb6;
    private SwitchCompat sb7;
    private SwitchCompat sb8;
    private SwitchCompat sb9;
    private SwitchCompat sb10;
    private SwitchCompat sb11;
    private SwitchCompat sb12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sbOnChecked(sb1,"sb1");
        sbOnChecked(sb2,"sb3");
        sbOnChecked(sb3,"sb4");
        sbOnChecked(sb4,"sb4");
        sbOnChecked(sb5,"sb5");
        sbOnChecked(sb6,"sb6");
        sbOnChecked(sb7,"sb7");
        sbOnChecked(sb8,"sb8");
        sbOnChecked(sb9,"sb9");
        sbOnChecked(sb10,"sb10");
        sbOnChecked(sb11,"sb11");
        sbOnChecked(sb12,"sb12");
    }

    private void sbOnChecked(SwitchCompat sb,String key) {
        boolean isTrue = SPUtils.getBoolean(key);
        sb.setChecked(isTrue);
        sb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.pushBoolean(key,isChecked);
            }
        });
    }

    private void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        sb1 = (SwitchCompat) findViewById(R.id.sb_1);
        sb2 = (SwitchCompat) findViewById(R.id.sb_2);
        sb3 = (SwitchCompat) findViewById(R.id.sb_3);
        sb4 = (SwitchCompat) findViewById(R.id.sb_4);
        sb5 = (SwitchCompat) findViewById(R.id.sb_5);
        sb6 = (SwitchCompat) findViewById(R.id.sb_6);
        sb7 = (SwitchCompat) findViewById(R.id.sb_7);
        sb8 = (SwitchCompat) findViewById(R.id.sb_8);
        sb9 = (SwitchCompat) findViewById(R.id.sb_9);
        sb10 = (SwitchCompat) findViewById(R.id.sb_10);
        sb11 = (SwitchCompat) findViewById(R.id.sb_11);
        sb12 = (SwitchCompat) findViewById(R.id.sb_12);
    }
}