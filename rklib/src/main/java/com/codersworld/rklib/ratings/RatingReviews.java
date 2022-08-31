package com.codersworld.rklib.ratings;

import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;


import com.codersworld.rklib.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RatingReviews extends FrameLayout {
    private boolean isBarAdded = false;
    /* access modifiers changed from: private */
    public boolean isRoundCorner;
    private boolean isShowAnimation = true;
    private boolean isShowLabel = true;
    private boolean isShowRaters = true;
    private int mBarColor;
    private int mBarDimension;
    private int mBarMaxValue;
    private int mBarSpaces;
    private int mBarTextColor;
    private int mBarTextSize;
    private List<Bar> mBars = new ArrayList();
    private Context mCtx;
    private LinearLayout mLinearParentLayout;
    private int mNumOfBars;
    private int mStyle;
    /* access modifiers changed from: private */
    public OnBarClickListener onBarClickListener;

    private interface DimensionReceivedCallback {
        void onDimensionReceived(int i);
    }

    public interface OnBarClickListener {
        void onBarClick(Bar bar);
    }

    public RatingReviews(Context mCtx2) {
        super(mCtx2);
        initLayout();
    }

    public RatingReviews(Context mCtx2, AttributeSet attrs) {
        super(mCtx2, attrs);
        this.mCtx = mCtx2;
        TypedArray a = mCtx2.obtainStyledAttributes(attrs, R.styleable.RatingReviews, 0, 0);
        this.mStyle = a.getInt(R.styleable.RatingReviews_style, 1);
        this.mBarDimension = a.getDimensionPixelSize(R.styleable.RatingReviews_width, (int) RatingUtils.convertDpToPixel(20.0f, mCtx2));
        this.mBarColor = a.getColor(R.styleable.RatingReviews_color, RatingUtils.DEFAULT_BAR_COLOR);
        this.mBarTextSize = (int) RatingUtils.convertPixelsToDp((float) a.getDimensionPixelSize(R.styleable.RatingReviews_text_size, (int) RatingUtils.convertDpToPixel(15.0f, mCtx2)), mCtx2);
        this.mBarTextColor = a.getColor(R.styleable.RatingReviews_text_color, RatingUtils.DEFAULT_BAR_TEXT_COLOR);
        this.mBarMaxValue = a.getInt(R.styleable.RatingReviews_max_value, 0);
        this.mBarSpaces = a.getDimensionPixelSize(R.styleable.RatingReviews_spaces, (int) RatingUtils.convertDpToPixel((float) RatingUtils.DEFAULT_BAR_SPACE, mCtx2));
        this.isRoundCorner = a.getBoolean(R.styleable.RatingReviews_rounded, false);
        this.isShowLabel = a.getBoolean(R.styleable.RatingReviews_show_label, true);
        this.isShowRaters = a.getBoolean(R.styleable.RatingReviews_show_raters, true);
        this.isShowAnimation = a.getBoolean(R.styleable.RatingReviews_animation, true);
        this.mNumOfBars = 5;
        a.recycle();
        initLayout();
    }

    private void initLayout() {
        LinearLayout linearLayout = new LinearLayout(this.mCtx);
        this.mLinearParentLayout = linearLayout;
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        this.mLinearParentLayout.setLayoutParams(new LayoutParams(-1, -2));
        this.mLinearParentLayout.setGravity(GravityCompat.START);
        if (this.isShowAnimation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                this.mLinearParentLayout.setLayoutTransition(new LayoutTransition());
            }
        }
        addView(this.mLinearParentLayout);
    }

    public void setMaxBarValue(int mBarMaxValue2) {
        this.mBarMaxValue = mBarMaxValue2 + 20;
        clearAll();
    }

    public void createRatingBars(int maxBarValue, String[] labels, int[] colorsArr, int[] raters) {
        setMaxBarValue(maxBarValue);
        for (int i = 0; i < this.mNumOfBars; i++) {
            Bar bar = new Bar();
            bar.setRaters(raters[i]);
            bar.setColor(colorsArr[i]);
            bar.setStarLabel(labels[i]);
            addBar(bar);
        }
    }

    public void createRatingBars(int maxBarValue, String[] labels, Pair[] colors, int[] raters) {
        setMaxBarValue(maxBarValue);
        for (int i = 0; i < this.mNumOfBars; i++) {
            Bar bar = new Bar();
            bar.setRaters(raters[i]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                bar.setStartColor(((Integer) colors[i].first).intValue());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                bar.setEndColor(((Integer) colors[i].second).intValue());
            }
            bar.setStarLabel(labels[i]);
            addBar(bar);
        }
    }

    public void createRatingBars(int maxBarValue, String[] labels, int color, int[] raters) {
        setMaxBarValue(maxBarValue);
        for (int i = 0; i < this.mNumOfBars; i++) {
            Bar bar = new Bar();
            bar.setRaters(raters[i]);
            bar.setColor(color);
            bar.setStarLabel(labels[i]);
            addBar(bar);
        }
    }

    /* access modifiers changed from: private */
    public void createBar(int dimension, Bar bar) {
        int styleLayout;
        if (dimension != 0 && this.mBarMaxValue != 0) {
            int i = this.mStyle;
            if (i == 1) {
                styleLayout = R.layout.bar;
            } else if (i != 2) {
                styleLayout = R.layout.bar;
            } else {
                styleLayout = R.layout.bar_two;
            }
            draw(dimension, (Bar) null, bar, LayoutInflater.from(this.mCtx).inflate(styleLayout, this.mLinearParentLayout, false));
        }
    }

    private void draw(int dimension, Bar initialBar, final Bar bar, final View view) {
        int i;
        final int bgColor = bar.getColor() != 0 ? bar.getColor() : this.mBarColor;
        view.post(new Runnable() {
            public void run() {
                int radius = view.getHeight() / 2;
                if (bar.isGradientBar()) {
                    if (bar.getStartColor() == 0 || bar.getEndColor() == 0) {
                        throw new RuntimeException("Gradient colors were not provided.");
                    } else if (RatingReviews.this.isRoundCorner) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.findViewById(R.id.linear_bar).setBackground(RatingUtils.getRoundedBarGradientDrawable(bar.getStartColor(), bar.getEndColor(), radius));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.findViewById(R.id.linear_bar).setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{bar.getStartColor(), bar.getEndColor()}));
                        }
                    }
                } else if (RatingReviews.this.isRoundCorner) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.findViewById(R.id.linear_bar).setBackground(RatingUtils.getRoundedBarDrawable(bgColor, radius));
                    }
                } else {
                    view.findViewById(R.id.linear_bar).setBackgroundColor(bgColor);
                }
            }
        });
        int dimensionBar = (bar.getRaters() * dimension) / this.mBarMaxValue;
        MarginLayoutParams layoutParamsBar = (MarginLayoutParams) view.getLayoutParams();
        if (this.isShowLabel) {
            TextView textView = (TextView) view.findViewById(R.id.tvBarLabel);
            if (bar.getStarLabel() != null) {
                textView.setText(String.format(Locale.getDefault(), "%s", new Object[]{bar.getStarLabel()}));
            }
            textView.setTextSize((float) this.mBarTextSize);
            textView.setTextColor(this.mBarTextColor);
        } else {
            view.findViewById(R.id.tvBarLabel).setVisibility(View.GONE);
        }
        if (this.isShowRaters) {
            TextView raters = (TextView) view.findViewById(R.id.tvRaters);
            if (bar.getStarLabel() != null) {
                raters.setText(String.format(Locale.getDefault(), "%s", new Object[]{Integer.valueOf(bar.getRaters())}));
            }
        }
        final LinearLayout linearLayoutBar = (LinearLayout) view.findViewById(R.id.linear_bar);
        int[] iArr = new int[2];
        if (initialBar == null) {
            i = 0;
        } else {
            i = (initialBar.getRaters() * dimension) / this.mBarMaxValue;
        }
        iArr[0] = i;
        iArr[1] = dimensionBar;
        ValueAnimator anim = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            anim = ValueAnimator.ofInt(iArr);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewGroup.LayoutParams layoutParams = linearLayoutBar.getLayoutParams();
                    layoutParams.width = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    linearLayoutBar.setLayoutParams(layoutParams);
                }
            });
        }
        if (this.isShowAnimation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                anim.setDuration(500);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                anim.setDuration(0);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            anim.start();
        }
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (RatingReviews.this.onBarClickListener != null) {
                    RatingReviews.this.onBarClickListener.onBarClick(bar);
                }
            }
        });
        view.setTag(bar);
        view.getLayoutParams().height = this.mBarDimension;
        if (initialBar == null) {
            if (this.isBarAdded) {
                layoutParamsBar.topMargin = this.mBarSpaces;
            }
            this.mLinearParentLayout.addView(view);
        }
        this.isBarAdded = true;
    }

    private void getDimension(final View view, final DimensionReceivedCallback listener) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                listener.onDimensionReceived(view.getWidth());
            }
        });
    }

    public void setOnBarClickListener(OnBarClickListener onBarClickListener2) {
        this.onBarClickListener = onBarClickListener2;
    }

    public void addBar(Bar bar) {
        addBar(this.mBars.size(), bar);
    }

    public void addBar(int position, final Bar bar) {
        if (position <= this.mNumOfBars) {
            this.mBars.add(bar);
            if (bar == null) {
                return;
            }
            if (this.mLinearParentLayout.getHeight() == 0) {
                getDimension(this.mLinearParentLayout, new DimensionReceivedCallback() {
                    public void onDimensionReceived(int dimension) {
                        RatingReviews.this.createBar(dimension, bar);
                    }
                });
            } else {
                createBar(this.mLinearParentLayout.getWidth(), bar);
            }
        }
    }

    public void clearAll() {
        this.mBars.clear();
        this.mLinearParentLayout.removeAllViews();
    }
}
