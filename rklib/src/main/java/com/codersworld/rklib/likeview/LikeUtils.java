package com.codersworld.rklib.likeview;

import android.content.Context;

/**
 * Created by Ram.
 */
public class LikeUtils {
    public static double mapValueFromRangeToRange(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return toLow + ((value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow));
    }

    public static double clamp(double value, double low, double high) {
        return Math.min(Math.max(value, low), high);
    }

    public static int dp2px(Context context, float dp) {
        return Math.round(context.getResources().getDisplayMetrics().density * dp);
    }
}
