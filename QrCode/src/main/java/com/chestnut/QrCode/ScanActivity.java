package com.chestnut.QrCode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.bingoogolapple.qrcode.core.QRCodeView;

public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    @Override
    public void onScanQRCodeSuccess(String s) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }
}
