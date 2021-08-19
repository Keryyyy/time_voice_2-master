package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siyecaoi.yy.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.VoiceMicBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import cn.sinata.xldutils.utils.StringUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class PlaceRecyclerAdapter extends BaseQuickAdapter<VoiceMicBean.DataBean, BaseViewHolder> {

    private SVGAParser parser;

    public PlaceRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void convert(BaseViewHolder helper, VoiceMicBean.DataBean item) {
//        helper.setIsRecyclable(false);
        SimpleDraweeView iv_headershow_place = helper.getView(R.id.iv_headershow_place);//头像
        SVGAImageView iv_headwear_place = helper.getView(R.id.iv_headwear_place);//头饰
        ImageView iv_back_place = helper.getView(R.id.iv_back_place);
        ImageView iv_anim_place = helper.getView(R.id.iv_anim_place);//头像圆圈图片和说话声音动图
        RelativeLayout iv_mute_place_rl = helper.getView(R.id.iv_mute_place_rl);
        SimpleDraweeView iv_img_place = helper.getView(R.id.iv_img_place);//爆灯、举手、排麦序动画 gif动画
        TextView tv_number_place = helper.getView(R.id.tv_number_place);
        TextView tv_img_place = helper.getView(R.id.tv_img_place);
        SVGAImageView mSVGAImageView_place = helper.getView(R.id.mSVGAImageView_place);
        LinearLayout llRotate = helper.getView(R.id.ll_rotate);
        ImageView ivNumber1 = helper.getView(R.id.iv_number_1);
        ImageView ivNumber2 = helper.getView(R.id.iv_number_2);
        ImageView ivNumber3 = helper.getView(R.id.iv_number_3);

        ivNumber1.clearAnimation();
        ivNumber2.clearAnimation();
        ivNumber3.clearAnimation();
        llRotate.setVisibility(View.GONE);


        tv_number_place.setText(helper.getAdapterPosition() + 1+"");
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/impact.ttf");
//        tv_number_place.setTypeface(typeface);
//        tv_number_place.setBackgroundResource(R.drawable.bg_round_gray_white1);

        //判断麦位是否被禁言
        if (item.getState() == 1) {
            iv_mute_place_rl.setVisibility(View.INVISIBLE);
        } else if (item.getState() == 2) {
            iv_mute_place_rl.setVisibility(View.VISIBLE);
        }

        //判断麦位是否被封锁
        if (item.getStatus() == 2) { //封锁
            iv_headershow_place.setVisibility(View.INVISIBLE);
            iv_back_place.setImageResource(R.drawable.block);
        } else if (item.getStatus() == 1) {
            iv_headershow_place.setVisibility(View.VISIBLE);
            if (helper.getAdapterPosition() == 7) {  //最后一个麦位
                iv_back_place.setImageResource(R.drawable.boss_set);
            } else {
                iv_back_place.setImageResource(R.drawable.micseat);
            }
        }

        RelativeLayout rl_points_place = helper.getView(R.id.rl_points_place);
        ImageView iv_sexShow_place = helper.getView(R.id.iv_sexShow_place);
        TextView tv_points_place = helper.getView(R.id.tv_points_place);

        //判断麦位是否有用户
        if (item.getUserModel() == null || item.getUserModel().getId() == 0) { //麦位没有用户
            iv_headershow_place.setVisibility(View.INVISIBLE);
            iv_headwear_place.setVisibility(View.INVISIBLE);
            helper.setText(R.id.tv_name_place, mContext.getString(R.string.tv_micshow));
            rl_points_place.setVisibility(View.INVISIBLE);
            iv_anim_place.setVisibility(View.INVISIBLE);
            tv_img_place.setVisibility(View.INVISIBLE);
            iv_img_place.setVisibility(View.INVISIBLE);
            mSVGAImageView_place.setVisibility(View.INVISIBLE);
            tv_number_place.setVisibility(View.VISIBLE);
        } else {  //麦位有用户
            iv_headershow_place.setVisibility(View.VISIBLE);
            rl_points_place.setVisibility(View.VISIBLE);
            tv_number_place.setVisibility(View.GONE);
            helper.setText(R.id.tv_name_place, item.getUserModel().getName());
            ImageUtils.loadUri(iv_headershow_place, item.getUserModel().getImg());

            String headwearShow = item.getUserModel().getUserTh();
            if (StringUtils.isEmpty(headwearShow)) {
                iv_headwear_place.setVisibility(View.INVISIBLE);
            } else if (headwearShow.endsWith(".svga")){
                iv_headwear_place.setVisibility(View.VISIBLE);
//                ImageUtils.loadUri(iv_headwear_place, headwearShow);
                if (!iv_headwear_place.isAnimating()){
                    try {
                        if (parser == null){
                            parser = new SVGAParser(mContext);
                        }
                        parser.parse(new URL(headwearShow), new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                                SVGADrawable svgaDrawable = new SVGADrawable(svgaVideoEntity);
                                iv_headwear_place.setImageDrawable(svgaDrawable);
                                iv_headwear_place.startAnimation();
                            }

                            @Override
                            public void onError() {
                                LogUtils.e("svga", "图片加载出错");
                                iv_headwear_place.setVisibility(View.INVISIBLE);
                            }
                        });
                    } catch (MalformedURLException e) {
                        LogUtils.e("svga", "图片加载出错");
                        iv_headwear_place.setVisibility(View.INVISIBLE);
                    }
                }
            }
            if (item.getUserModel().getSex() == 1) {
//                tv_number_place.setVisibility(View.GONE);
//                tv_number_place.setBackgroundResource(R.drawable.bg_round_blue_white1);
                iv_back_place.setImageResource(R.drawable.bg_round_blue);
                iv_anim_place.setImageResource(R.drawable.bg_round_blue);
                iv_sexShow_place.setImageResource(R.drawable.icon_charm_value);
            } else if (item.getUserModel().getSex() == 2) {
//                tv_number_place.setVisibility(View.GONE);
//                tv_number_place.setBackgroundResource(R.drawable.bg_round_red_white1);
                iv_back_place.setImageResource(R.drawable.bg_round_red);
                iv_anim_place.setImageResource(R.drawable.bg_round_red);
//                iv_sexShow_place.setImageResource(R.drawable.heart_female);
                iv_sexShow_place.setImageResource(R.drawable.icon_charm_value);
            }

            if (item.getUserModel().isSpeak()) { //动画
                iv_anim_place.setVisibility(View.VISIBLE);
                item.getUserModel().setSpeak(false);
                //缩放动画
//                ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv_anim_place, "scaleY", 1, 1.3f, 1, 1.3f, 1);
//                ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv_anim_place, "scaleX", 1, 1.3f, 1, 1.3f, 1);
//                ObjectAnimator alpha = ObjectAnimator.ofFloat(iv_anim_place, "alpha", 1, 0.3f);
//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.setDuration(3000);
//                animatorSet.playTogether(scaleX, scaleY, alpha);
//                animatorSet.start();

                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.view_room);
                animation.setRepeatCount(3);
                iv_anim_place.startAnimation(animation);
//                Timer timer = new Timer();
//                TimerTask timerTask = new TimerTask() {
//                    @SuppressLint("CheckResult")
//                    @Override
//                    public void run() {
//                        Observable.just("0").observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Consumer<String>() {
//                                    @Override
//                                    public void accept(String s) throws Exception {
//                                        animation.cancel();
//                                        iv_anim_place.setVisibility(View.INVISIBLE);
//                                    }
//                                });
//                    }
//                };
//                timer.schedule(timerTask, 3000);
            } else {
                iv_anim_place.setVisibility(View.INVISIBLE);
            }

            tv_points_place.setText(item.getUserModel().getNum() + "");
//            tv_points_place.setTypeface(typeface);

            if (item.getUserModel().getShowImg() != 0) { //展示表情


                int emojiCode = item.getUserModel().getShowImg();
                AnimationDrawable animationDrawable;
                if (emojiCode >= 128604) { //svga表情

                    if (emojiCode == 128629 || emojiCode == 128630){ //随机数

                        Animation rotateAnimation= AnimationUtils.loadAnimation(mContext,R.anim.view_rotate);
                        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                llRotate.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        ArrayList<Integer> randomNumbers= item.getUserModel().getRandomNumbers();
                        ivNumber1.setImageResource(ImageShowUtils.getInstans().getNumberResId(randomNumbers.get(0)));
                        ivNumber2.setImageResource(ImageShowUtils.getInstans().getNumberResId(randomNumbers.get(1)));
                        ivNumber3.setImageResource(ImageShowUtils.getInstans().getNumberResId(randomNumbers.get(2)));
                        llRotate.setVisibility(View.VISIBLE);


                        ivNumber1.setAnimation(rotateAnimation);
                        ivNumber2.setAnimation(rotateAnimation);
                        ivNumber3.setAnimation(rotateAnimation);
                        ivNumber1.startAnimation(rotateAnimation);
                        ivNumber2.startAnimation(rotateAnimation);
                        ivNumber3.startAnimation(rotateAnimation);
                    }else {
                        mSVGAImageView_place.setVisibility(View.VISIBLE);
//                    SVGAParser parser = new SVGAParser(mContext);
//                    parser.decodeFromAssets(ImageShowUtils.getInstans().getAssetsName(emojiCode), new SVGAParser.ParseCompletion() {
//                        @Override
//                        public void onComplete(SVGAVideoEntity svgaVideoEntity) {
//                            SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
//                            mSVGAImageView_place.setImageDrawable(drawable);
//                            mSVGAImageView_place.startAnimation();
//                        }
//
//                        @Override
//                        public void onError() {
//                            mSVGAImageView_place.setVisibility(View.INVISIBLE);
//                            LogUtils.e("svga", "图片加载出错");
//                        }
//                    });
                        ImageUtils.loadGif(mSVGAImageView_place, ImageShowUtils.getInstans().getResId(emojiCode, 0),mContext);
                    }

                } else if (emojiCode >= 128564) { //gif表情

                    iv_img_place.setVisibility(View.VISIBLE);
                    ImageUtils.loadDrawable(iv_img_place, ImageShowUtils.getInstans().getResId(emojiCode, 0));
                } else if (emojiCode >= 128552) {
                    iv_img_place.setVisibility(View.VISIBLE);
                    if (emojiCode == 128552) { //排麦序
                        iv_img_place.setImageResource(R.drawable.pai_voice);
                        // 1. 设置动画
                        animationDrawable = (AnimationDrawable) iv_img_place.getDrawable();
                        int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, item.getUserModel().getNumberShow());
                        if (maiDrawable != 0) {
                            animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                        }
                        // 2. 获取动画对象
                        animationDrawable.start();
                    } else if (emojiCode == 128553) { //爆灯
                        iv_img_place.setImageResource(R.drawable.deng_voice);

                        animationDrawable = (AnimationDrawable) iv_img_place.getDrawable();
                        animationDrawable.start();
                    } else if (emojiCode == 128554) { //举手
                        iv_img_place.setImageResource(R.drawable.hand_voice);

                        animationDrawable = (AnimationDrawable) iv_img_place.getDrawable();
                        animationDrawable.start();
                    } else if (emojiCode == 128555) { //石头剪刀布
                        iv_img_place.setImageResource(R.drawable.cai_voice);

                        animationDrawable = (AnimationDrawable) iv_img_place.getDrawable();
                        int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, item.getUserModel().getNumberShow());
                        if (maiDrawable != 0) {
                            animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                        }
                        animationDrawable.start();
                    } else if (emojiCode == 128562) { //骰子
                        iv_img_place.setImageResource(R.drawable.zhi_voice);

                        animationDrawable = (AnimationDrawable) iv_img_place.getDrawable();
                        int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, item.getUserModel().getNumberShow());
                        if (maiDrawable != 0) {
                            animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                        }
                        animationDrawable.start();
                    } else if (emojiCode == 128563) { //硬币
                        iv_img_place.setImageResource(R.drawable.yingbi_voice);

                        animationDrawable = (AnimationDrawable) iv_img_place.getDrawable();
                        int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, item.getUserModel().getNumberShow());
                        if (maiDrawable != 0) {
                            animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                        }
                        animationDrawable.start();
                    } else {
                        iv_img_place.setImageResource(ImageShowUtils.getInstans().getResId(emojiCode, item.getUserModel().getNumberShow()));
                    }
                } else {

                    tv_img_place.setVisibility(View.VISIBLE);
                    tv_img_place.setText(new String(Character.toChars(item.getUserModel().getShowImg())));
                }
                item.getUserModel().setShowImg(0);
//                Timer timer = new Timer();
//                TimerTask timerTask = new TimerTask() {
//                    @SuppressLint("CheckResult")
//                    @Override
//                    public void run() {
//                        Observable.just("0").observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Consumer<String>() {
//                                    @Override
//                                    public void accept(String s) throws Exception {
//                                        tv_img_place.setVisibility(View.INVISIBLE);
//                                        iv_img_place.setVisibility(View.INVISIBLE);
//                                    }
//                                });
//                    }
//                };
//                timer.schedule(timerTask, 3000);
            } else {
                ivNumber1.clearAnimation();
                ivNumber2.clearAnimation();
                ivNumber3.clearAnimation();
                llRotate.setVisibility(View.GONE);

                item.getUserModel().setImgOver(true);
                item.getUserModel().setShowImg(0);
                tv_img_place.setVisibility(View.INVISIBLE);
                iv_img_place.setVisibility(View.INVISIBLE);
                mSVGAImageView_place.setVisibility(View.INVISIBLE);
            }
        }
    }




    private int getMaiDrawable(int nuberShow) {
        switch (nuberShow) {
            case 1:
                return R.drawable.s_number_1;
            case 2:
                return R.drawable.s_number_2;
            case 3:
                return R.drawable.s_number_3;
            case 4:
                return R.drawable.s_number_4;
            case 5:
                return R.drawable.s_number_5;
            case 6:
                return R.drawable.s_number_6;
            case 7:
                return R.drawable.s_number_7;
            case 8:
                return R.drawable.s_number_8;
        }
        return 0;
    }
}
