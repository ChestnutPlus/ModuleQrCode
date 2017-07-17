package com.chestnut.QrCode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.Subscriber;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/7/17 14:21
 *     desc  :  对ScanActivity的封装调用
 *     thanks To:
 *     dependent on:
 *     update log:
 *              1.  库的使用：http://blog.csdn.net/fan7983377/article/details/51499508
 *              2.  库的地址：https://github.com/bingoogolapple/BGAQRCode-Android
 * </pre>
 */
public class QrCodeUtils {

    /*常量*/
    final static String QrCodeUtils_SCAN_SETTING_TITLE = "SCAN_TITLE";
    final static String QrCodeUtils_SCAN_SETTING_BTN_TXT = "SCAN_BTN_TXT";
    final static String QrCodeUtils_SCAN_SETTING_NEED_VIBRATE = "SCAN_NEED_VIBRATE";

    final static int QrCodeUtils_REQUEST_CODE = 121;
    final static int QrCodeUtils_RESULT_CODE = 0x12;

    final static String QrCodeUtils_SCAN_SUCCESS = "SCAN_SUCCESS";   //key：在onActivityResult中取得Bundle的结果，扫描是否成功，boolean
    final static String QrCodeUtils_SCAN_RESULT = "SCAN_RESULT";     //key：在在onActivityResult中取得Bundle的结果，扫描的结果集，String

    /**
     * 集成直接调用ScanActivity
     * @param activity  activity
     * @param title     title
     * @param btnTxt    txt
     * @param needVibrate   是否需要震动
     */
    public static void detectQrCodeFromCamera(Activity activity, String title, String btnTxt, boolean needVibrate) {
        Bundle bundle = new Bundle();
        bundle.putString(QrCodeUtils_SCAN_SETTING_TITLE,title);
        bundle.putString(QrCodeUtils_SCAN_SETTING_BTN_TXT,btnTxt);
        bundle.putBoolean(QrCodeUtils_SCAN_SETTING_NEED_VIBRATE,needVibrate);
        Intent intent = new Intent(activity,ScanActivity.class);
        activity.startActivityForResult(intent,QrCodeUtils_REQUEST_CODE);
    }

    /**
     * 分析结果，需要在 onActivityResult 中调用
     * @param requestCode   onActivityResult - requestCode
     * @param resultCode    onActivityResult - resultCode
     * @param data  onActivityResult - data
     * @return  识别的结果
     */
    public static Observable<String> analyseResultFromCamera(int requestCode, int resultCode, Intent data) {
        if (QrCodeUtils.QrCodeUtils_RESULT_CODE == resultCode && data.getExtras()!=null && QrCodeUtils_REQUEST_CODE == requestCode) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString(QrCodeUtils_SCAN_RESULT,"");
            boolean success = bundle.getBoolean(QrCodeUtils_SCAN_SUCCESS,false);
            if (!success)
                return Observable.just(null);
            else
                return Observable.just(result);
        }
        else {
            return Observable.just("");
        }
    }

    /**
     * 生成二维码Bitmap
     * @param content   内容，文本
     * @param size      大小，像素
     * @param foregroundColor    二维码的颜色
     * @param backgroundColor   背景颜色
     * @param logo  logo
     * @return  bitmap
     */
    public static Observable<Bitmap> createQrCode(@NonNull final String content, final int size, final int foregroundColor, final int backgroundColor, @Nullable final Bitmap logo) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                subscriber.onNext(QRCodeEncoder.syncEncodeQRCode(content, size, foregroundColor, backgroundColor, logo));
                subscriber.onCompleted();
            }
        });
    }

    public static Observable<Bitmap> createQrCode(@NonNull final String content, final int size, final int foregroundColor, @Nullable final Bitmap logo) {
        return createQrCode(content, size, foregroundColor, -1, logo);
    }

    /**
     * 检测图片上的二维码
     * @param bitmap    bitmap
     * @return  string
     */
    public static Observable<String> detectQrCodeFromPhoto(final Bitmap bitmap) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(QRCodeDecoder.syncDecodeQRCode(bitmap));
                subscriber.onCompleted();
            }
        });
    }

    public static Observable<String> detectQrCodeFromPhoto(final String filePath) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(QRCodeDecoder.syncDecodeQRCode(filePath));
                subscriber.onCompleted();
            }
        });
    }
}



