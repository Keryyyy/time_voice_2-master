package com.siyecaoi.yy.ui.room;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.StarBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.StarListAdapter;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.siyecaoi.yy.view.AutoVerticalScrollImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import cn.sinata.xldutils.utils.StringUtils;

public class StarActivityActivity extends MyBaseActivity {

    private TextView tvTimer;
    private TextView tvBalance;
    private TextView tv_turn;
    private TextView tv_state;
    private TextView tv_turn_gold;
    private TextView tv_joined_count;
    private TextView tv_total_gold;
    private TextView tv_rule;
    private LinearLayout ll_opening;
    private ImageView iv_join;
    private AutoVerticalScrollImageView iv_anim;
    private SimpleDraweeView iv_end;
    private RecyclerView rv_star;
    private CountDownTimer timer;
    private StarListAdapter adapter;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_star_activity);
    }

    @Override
    public void initView() {
        tvTimer = findViewById(R.id.tv_timer);
        tvBalance = findViewById(R.id.tv_balance);
        tv_turn = findViewById(R.id.tv_turn);
        tv_state = findViewById(R.id.tv_state);
        tv_rule = findViewById(R.id.tv_rule);
        iv_anim = findViewById(R.id.iv_anim);
        iv_end = findViewById(R.id.iv_end);
        ll_opening = findViewById(R.id.ll_opening);
        tv_joined_count = findViewById(R.id.tv_joined_count);
        tv_total_gold = findViewById(R.id.tv_total_gold);
        iv_join = findViewById(R.id.iv_join);
        tv_turn_gold = findViewById(R.id.tv_turn_gold);
        rv_star = findViewById(R.id.rv_star);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
                stringBuilder.append("img ");
                ImageSpan imageSpan = new ImageSpan(mContext, R.drawable.ic_rule_star);
                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(getString(R.string.rule_star));
        tv_rule.setText(stringBuilder);

        setTitleText("???????????????");
        setBlcakShow(true);
        setTitleBackGroundColor(R.color.white);
        setTitleBarColor(R.color.white);
        setTitleColorRes(R.color.textColor);
        setLeftImg(getDrawable(R.drawable.icon_back1));
        tvBalance.setText(SharedPreferenceUtils.get(this,Const.User.GOLD,0)+"");
        rv_star.setLayoutManager(new LinearLayoutManager(this));
        adapter =new StarListAdapter();
        rv_star.setAdapter(adapter);
        getTotal(false);
    }


    private int target = 0;
    private void getTotal(boolean isRefresh) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid",userToken);
        HttpManager.getInstance().post(Api.raiseActivityPageData, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                StarBean starBean = JSON.parseObject(responseString, StarBean.class);
                if (starBean.code == Api.SUCCESS) {
                    tvBalance.setText(starBean.data.gold);
                    tv_turn.setText("???"+ StringUtils.formatChineseNum(starBean.data.coherence)+"???");
                    tv_turn_gold.setText(getString(R.string.turn_gold,starBean.data.logGold));
                    tv_turn_gold.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //?????????
                    adapter.setNewData(starBean.data.luckList);

                    long l = starBean.data.countdown0 * 1000 - System.currentTimeMillis();
                    if (l<0){ //?????????????????????
                        ll_opening.setVisibility(View.VISIBLE);
                        long l1 = starBean.data.countdown * 1000 - System.currentTimeMillis();
                        if (l1<0){//??????????????????????????????????????????????????????,?????????????????????????????????????????????????????????
                            iv_end.setVisibility(View.VISIBLE);
                            iv_anim.setVisibility(View.GONE);
                            for (StarBean.HeadImgBean img:starBean.data.headImgList){
                                if (img.isWin == 1){
                                    iv_end.setImageURI(img.lotteryHeadImg);
                                }
                            }
                            tv_state.setText("?????????");
                        }else {//????????????????????????????????????????????????????????????????????????????????????
                            if (isRefresh){//?????????????????????????????????????????????????????????????????????????????????????????????
                                tv_state.setText("?????????");
                                if (starBean.data.headImgList.isEmpty()) //???????????????????????????????????????
                                    ll_opening.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getTotal(true);
                                        }
                                    },6000);
                                else {//?????????????????????????????????????????????????????????
                                    if (starBean.data.headImgList.size() == 1){ //???????????????????????????????????????
                                        iv_end.setVisibility(View.VISIBLE);
                                        iv_anim.setVisibility(View.GONE);
                                        for (StarBean.HeadImgBean img:starBean.data.headImgList){
                                            if (img.isWin == 1){
                                                iv_end.setImageURI(img.lotteryHeadImg);
                                            }
                                        }
                                        tv_state.setText("?????????");
                                    }else {
                                        tv_state.setText("?????????");
                                        iv_end.setVisibility(View.GONE);
                                        iv_anim.setVisibility(View.VISIBLE);
                                        length = starBean.data.headImgList.size();
                                        for (int i =0;i<starBean.data.headImgList.size();i++){
                                            if (starBean.data.headImgList.get(i).isWin == 1){
                                                target = i;
                                                iv_end.setImageURI(starBean.data.headImgList.get(i).lotteryHeadImg);
                                            }
                                            addBmp(starBean.data.headImgList.get(i).lotteryHeadImg);
                                        }
                                    }
                                }
                            }else {//??????????????????????????????????????????
                                timer = new CountDownTimer(l1,1000){
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        tvTimer.setText(String.format("?????????????????????%02d:%02d:%02d",millisUntilFinished/(1000*60*60)
                                                ,millisUntilFinished%(1000*60*60)/(60*1000),millisUntilFinished%(1000*60)/1000));
                                    }

                                    @Override
                                    public void onFinish() {
                                        tvTimer.setText("?????????????????????");
                                        ll_opening.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getTotal(false);
                                            }
                                        },2000);
                                    }
                                };
                                timer.start();
                                iv_end.setVisibility(View.VISIBLE);
                                iv_anim.setVisibility(View.GONE);
                                for (StarBean.HeadImgBean img:starBean.data.headImgList){
                                    if (img.isWin == 1){
                                        iv_end.setImageURI(img.lotteryHeadImg);
                                    }
                                }
                            }
                        }
                    }else{//?????????????????????????????????????????????
                        ll_opening.setVisibility(View.GONE);
                        if (!isRefresh){
                            timer = new CountDownTimer(l,1000){
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    tvTimer.setText(String.format("??????????????????%02d:%02d:%02d",millisUntilFinished/(1000*60*60)
                                            ,millisUntilFinished%(1000*60*60)/(60*1000),millisUntilFinished%(1000*60)/1000));
                                }

                                @Override
                                public void onFinish() {
                                    showToast("???????????????????????????");

                                    ll_opening.setVisibility(View.VISIBLE);
                                    ll_opening.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getTotal(true);
                                        }
                                    },1000);
                                    long l1 = starBean.data.countdown * 1000 - System.currentTimeMillis();
                                    timer = new CountDownTimer(l1,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            tvTimer.setText(String.format("?????????????????????%02d:%02d:%02d",millisUntilFinished/(1000*60*60)
                                                    ,millisUntilFinished%(1000*60*60)/(60*1000),millisUntilFinished%(1000*60)/1000));
                                        }

                                        @Override
                                        public void onFinish() {
                                            tvTimer.setText("?????????????????????");
                                            ll_opening.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    getTotal(false);
                                                }
                                            },2000);
                                        }
                                    };
                                    timer.start();
                                }
                            };
                            timer.start();
                        }
                    }
                    iv_join.setEnabled(starBean.data.isTakePartIn != 1);
                    tv_joined_count.setText("??????????????????"+starBean.data.peopleNum);
                    tv_total_gold.setText("??????????????????"+starBean.data.logGoldTotal);
                } else {
                    showToast(starBean.msg);
                }
            }
        });
    }

    private int length = 0;
    private ArrayList<Drawable> bitmaps = new ArrayList<>();

    //???????????????????????????Bitmap
    public void addBmp(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    length--;
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Drawable drawable = Drawable.createFromStream(is, url);
                    bitmaps.add(drawable);
                    if (bitmaps.size() == length){
                        iv_anim.setList(bitmaps, target, new AutoVerticalScrollImageView.OnFinishCallback() {
                            @Override
                            public void onFinish() {
                                iv_end.setVisibility(View.VISIBLE);
                                iv_anim.setVisibility(View.GONE);
                                tv_state.setText("?????????");
                            }
                        });
                        iv_anim.startAutoScroll();
                    }
                    is.close();
                } catch (IOException e) {
                    length--;
                }
            }
        }).start();
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {
        iv_join.setOnClickListener(v -> {
            showDialog();
            HashMap<String, Object> map = HttpManager.getInstance().getMap();
            map.put("uid",userToken);
            HttpManager.getInstance().post(Api.raiseActivitySign, map, new MyObserver(this){
                @Override
                public void success(String responseString) {
                    BaseBean bean = JSON.parseObject(responseString, BaseBean.class);
                    if (bean.getCode() == Api.SUCCESS){
                        iv_join.setEnabled(false);
                        showToast("??????????????????????????????");
                        getTotal(true);
                    }else {
                        showToast(bean.getMsg());
                    }
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iv_anim.stopAutoScroll();
        if (timer!=null)
            timer.cancel();
    }
}
