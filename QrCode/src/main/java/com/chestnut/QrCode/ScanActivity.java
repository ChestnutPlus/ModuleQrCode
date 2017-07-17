package com.chestnut.QrCode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chestnut.Common.utils.LogUtils;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import static com.chestnut.QrCode.QrCodeUtils.QrCodeUtils_RESULT_CODE;
import static com.chestnut.QrCode.QrCodeUtils.QrCodeUtils_SCAN_RESULT;
import static com.chestnut.QrCode.QrCodeUtils.QrCodeUtils_SCAN_SETTING_BTN_TXT;
import static com.chestnut.QrCode.QrCodeUtils.QrCodeUtils_SCAN_SETTING_NEED_VIBRATE;

public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate{

    private boolean OpenLog = true;
    private QRCodeView mQRCodeView;
    private boolean isNeedVibrate = false;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Button button = (Button) findViewById(R.id.btn_back);
        TextView textView = (TextView) findViewById(R.id.txt_title);
        button.setOnClickListener(onClickListener);
        if (getIntent().getExtras()!=null) {
            button.setText(getIntent().getExtras().getString(QrCodeUtils_SCAN_SETTING_BTN_TXT, "返回"));
            textView.setText(getIntent().getExtras().getString(QrCodeUtils.QrCodeUtils_SCAN_SETTING_TITLE,"扫描"));
            isNeedVibrate = getIntent().getExtras().getBoolean(QrCodeUtils_SCAN_SETTING_NEED_VIBRATE,false);
        }
        findViewById(R.id.img_arrow_back).setOnClickListener(onClickListener);
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startCamera();
        mQRCodeView.startSpotAndShowRect();
    }

    @Override
    protected void onPause() {
        try {
            mQRCodeView.stopSpot();
        } catch (Exception ignored) {}
        try {
            mQRCodeView.stopCamera();
        } catch (Exception ignored) {}
        super.onPause();
    }

    private void vibrate() {
        if (isNeedVibrate)
            try {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(200);
            }catch (Exception ignored) {}
    }

    @Override
    public void onScanQRCodeSuccess(String s) {
        vibrate();
        LogUtils.e(OpenLog,"onScanQRCodeSuccess:"+s);
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(QrCodeUtils.QrCodeUtils_SCAN_SUCCESS,true);
        bundle.putString(QrCodeUtils_SCAN_RESULT, s);
        resultIntent.putExtras(bundle);
        this.setResult(QrCodeUtils_RESULT_CODE, resultIntent);
        this.finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        vibrate();
        LogUtils.e(OpenLog,"onScanQRCodeOpenCameraError");
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(QrCodeUtils.QrCodeUtils_SCAN_SUCCESS, false);
        bundle.putString(QrCodeUtils_SCAN_RESULT, "");
        resultIntent.putExtras(bundle);
        this.setResult(QrCodeUtils_RESULT_CODE, resultIntent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
