package com.codersworld.rklib.shapes.manager;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.Nullable;

public class ClipPathManager implements ClipManager {

    protected final Path mPath = new Path();
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ClipPathCreator mClipPathCreator = null;

    public ClipPathManager() {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
    }

    public Paint getPaint() {
        return mPaint;
    }

    @Override
    public boolean requiresBitmap() {
        return mClipPathCreator != null && mClipPathCreator.requiresBitmap();
    }

    @Nullable
    protected final Path mClipPathCreator(int width, int height) {
        if (mClipPathCreator != null) {
            return mClipPathCreator.clipPathCreator(width, height);
        }
        return null;
    }

    public void setClipPathCreator(ClipPathCreator mClipPathCreator) {
        this.mClipPathCreator = mClipPathCreator;
    }

    @Override
    public Path createMask(int width, int height) {
        return mPath;
    }

    @Nullable
    @Override
    public Path getShadowConvexPath() {
        return mPath;
    }

    @Override
    public void setupClipLayout(int width, int height) {
        mPath.reset();
        final Path clipPath = mClipPathCreator(width, height);
        if (clipPath != null) {
            mPath.set(clipPath);
        }
    }

    public interface ClipPathCreator {
        Path clipPathCreator(int width, int height);
        boolean requiresBitmap();
    }
}
