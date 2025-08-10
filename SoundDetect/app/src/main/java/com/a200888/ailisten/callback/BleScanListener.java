package com.a200888.ailisten.callback;

import com.a200888.ailisten.data.BleDevice;

public interface BleScanListener {

    void onScanStarted(boolean success);

    void onScanning(BleDevice bleDevice);

}
