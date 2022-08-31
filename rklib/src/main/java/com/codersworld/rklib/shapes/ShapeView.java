package com.codersworld.rklib.shapes;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;

import com.codersworld.rklib.R;
import com.codersworld.rklib.shapes.manager.ClipManager;
import com.codersworld.rklib.shapes.manager.ClipPathManager;


public class ShapeView extends FrameLayout {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();

    protected PorterDuffXfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    @Nullable
    protected Drawable drawable = null;
    private ClipManager mClipManager = new ClipPathManager();
    private boolean requiersShapeUpdate = true;
    private Bitmap mBitmap;

    final Path rectView = new Path();

    public ShapeView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ShapeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public void setBackground(Drawable background) {
        //disabled here, please set a background to to this view child
        //super.setBackground(background);
    }

    @Override
    public void setBackgroundResource(int resid) {
        //disabled here, please set a background to to this view child
        //super.setBackgroundResource(resid);
    }

    @Override
    public void setBackgroundColor(int color) {
        //disabled here, please set a background to to this view child
        //super.setBackgroundColor(color);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint.setAntiAlias(true);

        setDrawingCacheEnabled(true);

        setWillNotDraw(false);

        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1){
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            setLayerType(LAYER_TYPE_SOFTWARE, mPaint); //Only works for software layers
        } else {
            mPaint.setXfermode(pdMode);
            setLayerType(LAYER_TYPE_SOFTWARE, null); //Only works for software layers
        }

        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);

            if (attributes.hasValue(R.styleable.ShapeView_shape_clip_drawable)) {
                final int resourceId = attributes.getResourceId(R.styleable.ShapeView_shape_clip_drawable, -1);
                if (-1 != resourceId) {
                    setDrawable(resourceId);
                }
            }

            attributes.recycle();
        }
    }

    protected float dpToPx(float dp) {
        return dp * this.getContext().getResources().getDisplayMetrics().density;
    }

    protected float pxToDp(float px) {
        return px / this.getContext().getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            requiresShapeUpdate();
        }
    }

    private boolean requiresBitmap() {
        return isInEditMode() || (mClipManager != null && mClipManager.requiresBitmap()) || drawable != null;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        requiresShapeUpdate();
    }

    public void setDrawable(int redId) {
        setDrawable(AppCompatResources.getDrawable(getContext(), redId));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (requiersShapeUpdate) {
            calculateLayout(canvas.getWidth(), canvas.getHeight());
            requiersShapeUpdate = false;
        }
        if (requiresBitmap()) {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        } else {
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1){
                canvas.drawPath(mPath, mPaint);
            } else {
                canvas.drawPath(rectView, mPaint);
            }
        }

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            setLayerType(LAYER_TYPE_HARDWARE, null);
        }
    }

    private void calculateLayout(int width, int height) {
        rectView.reset();
        rectView.addRect(0f, 0f, 1f * getWidth(), 1f * getHeight(), Path.Direction.CW);

        if (mClipManager != null) {
            if (width > 0 && height > 0) {
                mClipManager.setupClipLayout(width, height);
                mPath.reset();
                mPath.set(mClipManager.createMask(width, height));

                if (requiresBitmap()) {
                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                    mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    final Canvas canvas = new Canvas(mBitmap);

                    if (drawable != null) {
                        drawable.setBounds(0, 0, width, height);
                        drawable.draw(canvas);
                    } else {
                        canvas.drawPath(mPath, mClipManager.getPaint());
                    }
                }

                //invert the path for android P
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                    final boolean success = rectView.op(mPath, Path.Op.DIFFERENCE);
                }

                //this needs to be fixed for 25.4.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ViewCompat.getElevation(this) > 0f) {
                    try {
                        setOutlineProvider(getOutlineProvider());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        postInvalidate();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ViewOutlineProvider getOutlineProvider() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                if (mClipManager != null && !isInEditMode()) {
                    final Path shadowConvexPath = mClipManager.getShadowConvexPath();
                    if (shadowConvexPath != null) {
                        try {
                            outline.setConvexPath(shadowConvexPath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        };
    }

    public void setClipPathCreator(ClipPathManager.ClipPathCreator createClipPath) {
        ((ClipPathManager) mClipManager).setClipPathCreator(createClipPath);
        requiresShapeUpdate();
    }

    public void requiresShapeUpdate() {
        this.requiersShapeUpdate = true;
        postInvalidate();
    }

}
