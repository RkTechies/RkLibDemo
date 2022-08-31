package com.codersworld.rklib.ratings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

class RatingUtils {
    static int DEFAULT_BAR_COLOR = Color.parseColor("#333333");
    static int DEFAULT_BAR_SPACE = 5;
    static int DEFAULT_BAR_TEXT_COLOR = Color.parseColor("#333333");

    RatingUtils() {
    }

    @SuppressLint("NewApi")
    static float convertDpToPixel(float dp, Context context) {
        return (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f) * dp;
    }

    @SuppressLint("NewApi")
    static float convertPixelsToDp(float px, Context context) {
        return px / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    static Drawable getRoundedBarDrawable(int bgColor, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{(float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius});
        drawable.setColor(bgColor);
        return drawable;
    }

    static GradientDrawable getRoundedBarGradientDrawable(int startColor, int endColor, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{startColor, endColor});
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(new float[]{(float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius});
        return gradientDrawable;
    }
}
