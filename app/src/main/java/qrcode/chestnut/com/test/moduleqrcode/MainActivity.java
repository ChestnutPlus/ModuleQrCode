package qrcode.chestnut.com.test.moduleqrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.chestnut.Common.ui.Toastc;
import com.chestnut.Common.utils.LogUtils;
import com.chestnut.QrCode.QrCodeUtils;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private Toastc toastc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QrCodeUtils.detectQrCodeFromCamera(MainActivity.this,null,null,true);
            }
        });
        toastc = new Toastc(this, Toast.LENGTH_LONG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QrCodeUtils.analyseResultFromCamera(requestCode, resultCode, data)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtils.e(true,"识别结果："+s);
                        toastc.setText("识别结果："+s).show();
                    }
                });
    }
}
