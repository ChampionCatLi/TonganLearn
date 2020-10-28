package com.tongan.learn.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tongan.learn.R;

import static com.tongan.learn.TaConstant.themColor;
import static com.tongan.learn.util.BitmapUtils.dp2px;

/**
 * Date: 2020/10/25
 * User: LiChao
 */
class TaCameraPopupWindow extends PopupWindow {

    private Context mContext;
    private ImageView tonganOriginPhoto;
    private TextView tonganLaterLearnBtn;
    private TextView tonganTryAgainBtn;
    private View.OnClickListener mOnClickListener;

    public TaCameraPopupWindow(Activity activity, View.OnClickListener onClickListener) {
        super(activity);
        mContext = activity;
        mOnClickListener = onClickListener;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.tong_an_camera_popuop_window, null);
        setContentView(contentView);
        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        //获取popupwindow的高度与宽度
        this.setWidth((int) (screenWidth - 2 * dp2px(mContext, 35f)));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置背景透明度
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置动画
//        this.setAnimationStyle(R.style.Po);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 点击外部可取消
        this.setOutsideTouchable(false);
        initVew(contentView);

    }

    private void initVew(View view) {
        tonganOriginPhoto = view.findViewById(R.id.tong_an_origin_photo);
        tonganLaterLearnBtn = view.findViewById(R.id.tong_an_pop_later_learn_btn);
        tonganTryAgainBtn = view.findViewById(R.id.tong_an_try_again_btn);

        tonganLaterLearnBtn.setOnClickListener(mOnClickListener);
        tonganTryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaCameraPopupWindow.this.dismiss();
            }
        });
        setShapeBackColor((GradientDrawable) tonganLaterLearnBtn.getBackground());
    }

    private void setShapeBackColor(GradientDrawable drawable) {
        drawable.mutate();
        drawable.setColor(themColor);
    }
    public void setTonganOriginPhoto(Bitmap bitmap) {
        if (bitmap != null) {
            tonganOriginPhoto.setImageBitmap(bitmap);
        }
    }

    /**
     * 显示在屏幕的下方
     */
    public void showAtScreenBottom(View parent) {
        this.showAtLocation(parent, Gravity.CENTER, 0, 0);
//        popOutShadow();
    }

    /**
     * 让popupwindow以外区域阴影显示
     */
    private void popOutShadow() {
        final Window window = ((Activity) mContext).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;//设置阴影透明度
        window.setAttributes(lp);
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = 1f;
                window.setAttributes(lp);
            }
        });
    }


}
