package com.codersworld.rklib.progressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.codersworld.rklib.R;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class RKProgress {
    Context mContext;

    int iconSide = Constant.CENTER;
    Boolean isIcon = false;
    Boolean isCancelable = true;
    int icon = R.drawable.ic_launcher_round;
    int theme = R.style.progressTheme;

    public RKProgress(Context ctx) {
        this.mContext = ctx;
    }

    public void setIconSide(int mIconSide) {
        iconSide = mIconSide;
    }

    public void setIcon(int mIcon) {
        icon = mIcon;
    }

    public void setTheme(int mTheme) {
        theme = mTheme;
    }

    public void isIconShow(Boolean isShow) {
        isIcon = isShow;
    }

    public void isCancelable(Boolean isShow) {
        isCancelable = isShow;
    }

    ImageView imgLeft, imgRight, imgTop, imgBottom, imgCenter;
    private static ProgressDialog progressDialog;

    public void showProgressDialog() {
        if (!((Activity) mContext).isFinishing()) {
            WeakReference<Context> weakActivity = new WeakReference<>(mContext);
            try {
                progressDialog = new ProgressDialog(mContext, theme);
            } catch (Exception ex) {
                ex.printStackTrace();
                theme = R.style.progressTheme;
                progressDialog = new ProgressDialog(mContext, theme);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            progressDialog.setCanceledOnTouchOutside(isCancelable);
            progressDialog.setCancelable(isCancelable);
            progressDialog.setContentView(R.layout.progress_bar);
            imgLeft = progressDialog.findViewById(R.id.imgLeft);
            imgRight = progressDialog.findViewById(R.id.imgRight);
            imgTop = progressDialog.findViewById(R.id.imgTop);
            imgBottom = progressDialog.findViewById(R.id.imgBottom);
            imgCenter = progressDialog.findViewById(R.id.imgCenter);
            if (isIcon) {
                if (iconSide == Constant.LEFT_SIDE) {
                    imgLeft.setVisibility(View.VISIBLE);
                    imgRight.setVisibility(View.GONE);
                    imgTop.setVisibility(View.GONE);
                    imgBottom.setVisibility(View.GONE);
                    imgCenter.setVisibility(View.GONE);
                } else if (iconSide == Constant.RIGHT_SIDE) {
                    imgLeft.setVisibility(View.GONE);
                    imgRight.setVisibility(View.VISIBLE);
                    imgTop.setVisibility(View.GONE);
                    imgBottom.setVisibility(View.GONE);
                    imgCenter.setVisibility(View.GONE);
                } else if (iconSide == Constant.TOP_SIDE) {
                    imgLeft.setVisibility(View.GONE);
                    imgRight.setVisibility(View.GONE);
                    imgTop.setVisibility(View.VISIBLE);
                    imgBottom.setVisibility(View.GONE);
                    imgCenter.setVisibility(View.GONE);
                } else if (iconSide == Constant.BOTTOM_SIDE) {
                    imgLeft.setVisibility(View.GONE);
                    imgRight.setVisibility(View.GONE);
                    imgTop.setVisibility(View.GONE);
                    imgBottom.setVisibility(View.VISIBLE);
                    imgCenter.setVisibility(View.GONE);
                } else if (iconSide == Constant.CENTER) {
                    imgLeft.setVisibility(View.GONE);
                    imgRight.setVisibility(View.GONE);
                    imgTop.setVisibility(View.GONE);
                    imgBottom.setVisibility(View.GONE);
                    imgCenter.setVisibility(View.VISIBLE);
                } else {
                    imgLeft.setVisibility(View.GONE);
                    imgRight.setVisibility(View.GONE);
                    imgTop.setVisibility(View.GONE);
                    imgBottom.setVisibility(View.GONE);
                    imgCenter.setVisibility(View.GONE);
                }
                try {
                    imgLeft.setImageResource(icon);
                    imgRight.setImageResource(icon);
                    imgBottom.setImageResource(icon);
                    imgTop.setImageResource(icon);
                    imgCenter.setImageResource(icon);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    icon = R.drawable.ic_launcher_round;
                    imgLeft.setImageResource(icon);
                    imgRight.setImageResource(icon);
                    imgBottom.setImageResource(icon);
                    imgTop.setImageResource(icon);
                    imgCenter.setImageResource(icon);
                }
            } else {
                imgLeft.setVisibility(View.GONE);
                imgRight.setVisibility(View.GONE);
                imgTop.setVisibility(View.GONE);
                imgBottom.setVisibility(View.GONE);
                imgCenter.setVisibility(View.GONE);
            }
            progressDialog.show();
        }
    }


    public static void hideProgressDialog(Context context) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
