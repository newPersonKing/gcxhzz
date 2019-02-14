package com.whoami.gcxhzz.until;

/**
 * Created by emcc-pc on 2018/3/2.
 */

public class Untils {
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
