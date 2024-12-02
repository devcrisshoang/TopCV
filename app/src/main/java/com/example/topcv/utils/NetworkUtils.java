package com.example.topcv.utils;

import android.os.Build;

public class NetworkUtils {
    public static String getBaseUrl() {
        // Kiểm tra xem có phải emulator hay không
        if (isEmulator()) {
            // Sử dụng IP 10.0.2.2 cho Emulator
            return "https://10.0.2.2:7200/";
        } else {
            // Dùng IP máy thật trong mạng nội bộ
            return "http://192.168.10.106:7200/";  // Chắc chắn rằng IP này là đúng của máy chủ của bạn
        }
    }

    // Kiểm tra xem thiết bị có phải là emulator không
    private static boolean isEmulator() {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.BOARD.contains("unknown")
                || Build.SERIAL.equals("Android")
                || Build.SERIAL.equals("unknown"));
    }
}
