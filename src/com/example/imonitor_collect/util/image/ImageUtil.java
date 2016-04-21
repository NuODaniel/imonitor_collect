/**
 * Name        : ImageUtilEngine.java
 * Description : TODO
 */

package com.example.imonitor_collect.util.image;

public class ImageUtil {

    static {
        System.loadLibrary("DecodeJni");
    }

    public static native int[] decodeYUV420SP(byte[] buf, int width, int heigth);
}