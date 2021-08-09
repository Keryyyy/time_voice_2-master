package com.siyecaoi.yy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.siyecaoi.yy.bean.StarBean;
import com.siyecaoi.yy.utils.ImageUtils;

import java.util.List;

/**
 * 自动垂直滚动的ImageView
 */
public class AutoVerticalScrollImageView extends ImageSwitcher implements ViewSwitcher.ViewFactory {

    private Context mContext;

    //mInUp,mOutUp分别构成向下翻页的进出动画
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;

    public AutoVerticalScrollImageView(Context context) {
        this(context, null);
    }

    public AutoVerticalScrollImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();

    }

    private void init() {

        setFactory(this);

        mInUp = createAnim(true, true);
        mOutUp = createAnim(false, true);

        setInAnimation(mInUp);//当View显示时动画资源ID
        setOutAnimation(mOutUp);//当View隐藏是动画资源ID。

    }

    private Rotate3dAnimation createAnim(boolean turnIn, boolean turnUp) {

        Rotate3dAnimation rotation = new Rotate3dAnimation(turnIn, turnUp);
        rotation.setDuration(animTime);//执行动画的时间
        rotation.setFillAfter(false);//是否保持动画完毕之后的状态
        rotation.setInterpolator(new AccelerateInterpolator());//设置加速模式
        return rotation;
    }

    private boolean isRunning = true;
    private int number = 0;
    private int target = 0; //摇奖目标
    private List<Drawable> list;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 199) {
                next();
                number++;
                setImageDrawable(list.get(number % list.size()));
                if ((number >= 15 && number % list.size() == target)||number>=40){
                    stopAutoScroll();
                    callback.onFinish();
                }
            }
        }
    };

    public void setList(List<Drawable> list,int target,OnFinishCallback callback) {
        this.list = list;
        this.target = target;
        this.callback = callback;
    }

    private OnFinishCallback callback;

    public interface OnFinishCallback{
        void onFinish();
    }

    public void startAutoScroll() {
        number = 0;
        isRunning = true;
        new Thread(() -> {
            while (isRunning) {
                handler.sendEmptyMessage(199);

                SystemClock.sleep(imageStillTime);
            }
        }).start();
    }


    public void stopAutoScroll() {
        isRunning = false;
    }

    private int imageStillTime = 200;//停留时长间隔
    private int animTime = 200;//执行动画的时间

    /**
     * 设置停留时长间隔
     */
    public void setImageStillTime(int textStillTime) {
        this.imageStillTime = textStillTime;
    }

    /**
     * 设置进入和退出的时间间隔
     */
    public void setAnimTime(int animTime) {
        this.animTime = animTime;
    }

    //这里返回的ImageView，就是我们看到的View,可以设置自己想要的效果
    public View makeView() {
        RoundImageView imageView = new RoundImageView(mContext);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(lp);
        return imageView;
    }

    //定义动作，向上滚动翻页
    public void next() {
        //显示动画
        if (getInAnimation() != mInUp) {
            setInAnimation(mInUp);
        }
        //隐藏动画
        if (getOutAnimation() != mOutUp) {
            setOutAnimation(mOutUp);
        }
    }

    class Rotate3dAnimation extends Animation {
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(boolean turnIn, boolean turnUp) {
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight();
            mCenterX = getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

}