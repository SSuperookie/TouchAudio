package com.a200888.ailisten;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.a200888.ailisten.data.BleDevice;
import com.huawei.hms.mlsdk.sounddect.MLSoundDetectListener;
import com.huawei.hms.mlsdk.sounddect.MLSoundDetector;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private TextView tvMore;
    private RelativeLayout rlListen;

    private MLSoundDetector soundDetector;
    private ImageView ivMore;
    private TextView tvStatus;
    private TextView tvBle;
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPUtils.setApplication(this);
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance().enableLog(true);

        setContentView(R.layout.activity_main);
        initView();
        tvBle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBLEPermissions();
            }
        });
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryListActivity.class));
            }
        });
        rlListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status = tvStatus.getText().toString();

                if ("聆听中...".equals(status)) {
                    try {
                        if (soundDetector != null) {
                            stop();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tvStatus.setText("停止聆听了");
                } else  {
                    try {
                        if (soundDetector != null) {
                            stop();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        startDetector();
                        boolean start = soundDetector.start(MainActivity.this);
                        if (start) {
                            Toast.makeText(MainActivity.this, "声音检测引擎初始化成功，开始监听..." + start, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "声音检测引擎初始化失败，请联系开发者..." + start, Toast.LENGTH_LONG).show();
                        }

                        tvStatus.setText("聆听中...");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }




            }
        });
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MoreSettingActivity.class));
            }
        });


        requestPermission();
    }


    private void checkBLEPermissions() {
        // 请求权限的逻辑
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, getString(R.string.please_open_blue), Toast.LENGTH_LONG).show();
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                "android.permission.BLUETOOTH_SCAN",
                "android.permission.BLUETOOTH_CONNECT"};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = checkSelfPermission( permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            requestPermissions(deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    startActivity(new Intent(MainActivity.this,BleActivity.class));
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }


    private void startDetector() {
        soundDetector = MLSoundDetector.createSoundDetector();
        soundDetector.setSoundDetectListener(listener);
    }

    private static final int REQUEST_CODE_RECORD_AUDIO = 0x13;

    public void requestPermission() {
        // 检查是否已经授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // 如果未授权，申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授权成功
                // 进行相应的操作，例如启动录音功能
                Toast.makeText(this, "应用启动成功！", Toast.LENGTH_LONG).show();
            } else {
                // 用户拒绝授权
                // 可以给出提示，或者进行其他处理
                Toast.makeText(this, "应用启动失败！", Toast.LENGTH_LONG).show();
            }
        }
    }

    private MLSoundDetectListener listener = new MLSoundDetectListener() {
        @Override
        public void onSoundSuccessResult(Bundle result) {
            //识别成功的处理逻辑，识别结果为：0-12（对应MLSoundDetectConstants.java中定义的以SOUND_EVENT_TYPE开头命名的13种声音类型）。
//            笑声、婴儿或小孩哭声、打鼾声、喷嚏声、叫喊声、猫叫声、狗叫声、流水声（包括水龙头流水声、溪流声、海浪声）、
//            汽车喇叭声、门铃声、敲门声、火灾报警声（包括火灾报警器警报声、烟雾报警器警报声）、警报声（包括消防车警报声、
//            救护车警报声、警车警报声、防空警报声）。

            int soundType = result.getInt(MLSoundDetector.RESULTS_RECOGNIZED);
            Log.e("hkj", "soundType: " + soundType);
            String strType = "";
//            婴儿哭声 : 1
//            打鼾声 : 2
//            喷嚏声 : 3
//            尖叫声 : 4
//            猫叫声 : 5
//            狗叫声 : 6
//            流水声 : 7
//            汽车警报声 : 8
//            门铃声 : 9
//            敲门声: 10
//            警报声 : 11
//            汽笛声 : 12

            boolean isTrueSb1 = SPUtils.getBoolean("sb1");
            boolean isTrueSb2 = SPUtils.getBoolean("sb2");
            boolean isTrueSb3 = SPUtils.getBoolean("sb3");
            boolean isTrueSb4 = SPUtils.getBoolean("sb4");
            boolean isTrueSb5 = SPUtils.getBoolean("sb5");
            boolean isTrueSb6 = SPUtils.getBoolean("sb6");
            boolean isTrueSb7 = SPUtils.getBoolean("sb7");
            boolean isTrueSb8 = SPUtils.getBoolean("sb8");
            boolean isTrueSb9 = SPUtils.getBoolean("sb9");
            boolean isTrueSb10 = SPUtils.getBoolean("sb10");
            boolean isTrueSb11 = SPUtils.getBoolean("sb11");
            boolean isTrueSb12 = SPUtils.getBoolean("sb12");
            if (soundType == 1&&isTrueSb1) {
                strType = "婴儿哭声";
            } else if (soundType == 2&&isTrueSb2) {
                strType = "打鼾声";
            } else if (soundType == 3&&isTrueSb3) {
                strType = "喷嚏声";
            } else if (soundType == 4&&isTrueSb4) {
                strType = "尖叫声";
            } else if (soundType == 5&&isTrueSb5) {
                strType = "猫叫声";
            } else if (soundType == 6&&isTrueSb6) {
                strType = "狗叫声";
            } else if (soundType == 7&&isTrueSb7) {
                strType = "流水声";
            } else if (soundType == 8&&isTrueSb8) {
                strType = "汽车警报声";
            } else if (soundType == 9&&isTrueSb9) {
                strType = "门铃声";
            } else if (soundType == 10&&isTrueSb10) {
                strType = "敲门声";
            } else if (soundType == 11&&isTrueSb11) {
                strType = "警报声";
            } else if (soundType == 12&&isTrueSb12) {
                strType = "汽笛声";
            }
            stop();
            if (TextUtils.isEmpty(strType)) return;

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("检测到");
            builder.setMessage("" + strType);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 点击“确定”按钮后的操作
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            String cache = SPUtils.getString("cache");
            if (TextUtils.isEmpty(cache)) {
                cache = strType;
            } else {
                cache = cache + "-" + strType;
            }
            SPUtils.pushString("cache", cache);
        }

        @Override
        public void onSoundFailResult(int errCode) {
            //识别失败，可能没有授予麦克风权限（Manifest.permission.RECORD_AUDIO）等异常情况。
            Log.e("hkj", "errCode: " + errCode);
            stop();
        }
    };

    private void stop() {
        soundDetector.destroy();
        tvStatus.setText("停止聆听了");
    }

    private void initView() {
        tvMore = (TextView) findViewById(R.id.tv_more);
        rlListen = (RelativeLayout) findViewById(R.id.rl_listen);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        ivMore = (ImageView) findViewById(R.id.iv_more);
        tvBle = (TextView) findViewById(R.id.tv_ble);
    }
}
