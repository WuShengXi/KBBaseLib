package com.kbit.kbbaselib.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentProvider;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.kbit.kbbaselib.context.ContextProvider;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceUtil {

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) ContextProvider.getContext().getSystemService(Service.TELEPHONY_SERVICE);
        String imeiString = "";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            imeiString = Settings.System.getString(ContextProvider.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                assert telephonyManager != null;
                imeiString = telephonyManager.getImei();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("deviceId", "get IMEI error, you may need use read phone state permission");
            }
        } else {
            try {
                assert telephonyManager != null;
                imeiString = telephonyManager.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("deviceId", "get deviceId error, you may need use read phone state permission");
            }
        }
        return imeiString;
    }

    public static String getAndroidId() {
        return Settings.System.getString(ContextProvider.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getSerialNumber() {
        String serialString = "";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            serialString = Settings.System.getString(ContextProvider.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            serialString = Build.getSerial();
        } else {
            serialString = Build.SERIAL;
        }
        return serialString;
    }

    public static String getMac() {
        String macString = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return macString;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }

                macString = res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macString;
    }
}