package com.a200888.ailisten;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

public class HistoryListActivity extends Activity {

    private HistoryChatAdapter mAdapter;
    private ListView list;
    private ImageView ivClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String cache = SPUtils.getString("cache");
        String[] split = cache.split("-");
        if (split == null || split.length == 0) {
            Toast.makeText(this, "暂无识别历史哦，请先识别~~", Toast.LENGTH_LONG).show();
            finish();
        }

        setContentView(R.layout.activity_history);
        initView();
        mAdapter = new HistoryChatAdapter(this);
        list.setAdapter(mAdapter);

        // 倒序
        Arrays.sort(split, Collections.reverseOrder());
        for (String s : split) {
            mAdapter.addData(s);
        }

        mAdapter.notifyDataSetChanged();
        mAdapter.setOnChatClickListener(new HistoryChatAdapter.OnChatClickListener() {
            @Override
            public void onClick(String str) {
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        list = (ListView) findViewById(R.id.list);
        ivClose = (ImageView) findViewById(R.id.iv_close);
    }
}