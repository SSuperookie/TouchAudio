package com.a200888.ailisten.callback;

import com.a200888.ailisten.data.BleDevice;

public interface BleScanPresenterImp {

    void onScanStarted(boolean success);

    void onScanning(BleDevice bleDevice);

}
