package com.a200888.ailisten;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.a200888.ailisten.callback.BleScanCallback;
import com.a200888.ailisten.data.BleDevice;
import com.a200888.ailisten.scan.BleScanRuleConfig;

import java.util.List;

public class BleActivity extends Activity {

    private DeviceAdapter mAdapter;
    private ListView list;
    private ImageView ivClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        initView();
        mAdapter = new DeviceAdapter(this);
        list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                // BLE设备连接成功


                // 写入ble数据
//                BleManager.getInstance().write();
            }

            @Override
            public void onDisConnect(BleDevice bleDevice) {
                // BLE设备失去链接

            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                // BLE设备详情


            }

        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setScanRule();
        startScan();
    }

    private void initView() {
        list = (ListView) findViewById(R.id.list);
        ivClose = (ImageView) findViewById(R.id.iv_close);
    }

    private void setScanRule() {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setAutoConnect(false)
//                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
//                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
//                .setDeviceMac("mac")                  // 只扫描指定mac的设备，可选
//                .setAutoConnect(isAutoConnect)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }


    private void startScan() {
        Log.e("hkj","ble : "+BleManager.getInstance().isBlueEnable());
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mAdapter.clearScanDevice();
                mAdapter.notifyDataSetChanged();
//                img_loading.startAnimation(operatingAnim);
//                img_loading.setVisibility(View.VISIBLE);
//                btn_scan.setText(getString(R.string.stop_scan));
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Log.e("hkj","bleDevice: "+bleDevice.getName());
                mAdapter.addDevice(bleDevice);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
//                img_loading.clearAnimation();
//                img_loading.setVisibility(View.INVISIBLE);
//                btn_scan.setText(getString(R.string.start_scan));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().destroy();
    }
}
