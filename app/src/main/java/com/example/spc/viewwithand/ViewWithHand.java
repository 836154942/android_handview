package com.example.spc.viewwithand;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by spc on 16-7-16.
    跟随手指移动的iew
 */
public class ViewWithHand  {

    private Context mContext;
    protected static ViewWithHand mCreatView;
    private int[] location;
    private ImageView mImageView;
    public WindowManager mWm;
    public WindowManager.LayoutParams layoutParams;

    private ViewWithHand(Context context) {
        this.mContext = context;
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
    }

    public synchronized static ViewWithHand getCreatView(Context context) {
        if (mCreatView == null) {
            mCreatView = new ViewWithHand(context);
        }
        return mCreatView;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    /**
     * 创建view
     */
    private View createNewView() {
        ImageView mImageView = new ImageView(mContext);
        mImageView.setImageDrawable(mContext.getResources().getDrawable(
                R.mipmap.ic_launcher));
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        mImageView.setLayoutParams(layoutParams);
        mImageView.setBackgroundColor(0x00000000);
        return mImageView;
    }

    /**
     * 在屏幕上显示
     */
    public void addViewToScreen() {
        mImageView = (ImageView) createNewView();//
        mImageView.setOnTouchListener(new TouchListener());

        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x = location[0]; //偏移量x
        layoutParams.y = location[1]; //偏移量y
        layoutParams.width = 100;
        layoutParams.height = 100;
        layoutParams.alpha = 1.0f;
        mWm.addView(mImageView, layoutParams);
    }

    class TouchListener implements View.OnTouchListener {

        int lastX;
        int lastY;
        int screenWidth;
        int screenHeight;

        public TouchListener() {
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;
                    int left = v.getLeft() + dx;
                    int top = v.getTop() + dy;
                    int right = v.getRight() + dx;
                    int bottom = v.getBottom() + dy;
                    //边界处理
                    if (left < 0) {
                        left = 0;
                        right = left + v.getWidth();
                    }
                    if (right > screenWidth) {
                        right = screenWidth;
                        left = right - v.getWidth();
                    }
                    if (top < 0) {
                        top = 0;
                        bottom = top + v.getHeight();
                    }
                    if (bottom > screenHeight) {
                        bottom = screenHeight;
                        top = bottom - v.getHeight();
                    }

                    v.layout(left, top, right, bottom);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    layoutParams.x = lastX - v.getWidth() / 2;// 看起來在 中间效果处理
                    layoutParams.y = lastY - v.getHeight();
                    mWm.updateViewLayout(v, layoutParams);

                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    }
}
