package com.siyecaoi.yy.ui.room.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GiftShowBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.DanJiangAdapter;
import com.siyecaoi.yy.ui.room.adapter.ViewPagerAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 本期奖池
 */
public class DanJiangDialog extends Dialog {

    Context mContext;

    @BindView(R.id.mViewPager_danjiang)
    ViewPager mViewPager_danjiang;
    @BindView(R.id.ll_indicator_gift)
    LinearLayout ll_indicator_gift;
    private int type;

    public DanJiangDialog(Context context,int type) {
        super(context, R.style.CustomDialogStyle);
        this.mContext = context;
        this.type = type;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_danjiang);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.CenterDialogAnimation);


        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        String reqString = Api.JackpotInfoMark;
        map.put("type",type);
        HttpManager.getInstance().post(reqString, map, new MyObserver(mContext) {
            @Override
            public void success(String responseString) {
                GiftShowBean giftShowBean = JSON.parseObject(responseString, GiftShowBean.class);

                setFragment(giftShowBean.getData(), giftShowBean.getData().size());
            }
        });
    }

    DanJiangAdapter danJiangAdapter;
    GridLayoutManager layoutManager;
    List<DanJiangAdapter> groudAdapterList;
    private List<View> mPagerList;//页面集合
    private ViewPagerAdapter viewPagerAdapter;
    private int curIndex;

    private void setFragment(List<GiftShowBean.DataEntity> data, int size) {
        groudAdapterList = new ArrayList<>();
        mPagerList = new ArrayList<>();
        int fragNumber = size / 8;
        int fragEndNum = size % 8;
//        总的页数=总数/每页数量，并取整
//        int fragNumber = (int) Math.ceil(size / Const.IntShow.EIGHT);
        for (int i = 0; i <= fragNumber; i++) {
//            GiftFragment giftFragment = new GiftFragment();
            List<GiftShowBean.DataEntity> giftData;
            if (fragNumber == i) {
                giftData = data.subList(i * 8, i * 8 + fragEndNum);
            } else {
                giftData = data.subList(i * 8, (i + 1) * 8);
            }
            RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_recycler_gift, mViewPager_danjiang, false);
            danJiangAdapter = new DanJiangAdapter(R.layout.item_chestsshow);
            recyclerView.setAdapter(danJiangAdapter);

            layoutManager = new GridLayoutManager(mContext, 4);
            recyclerView.setLayoutManager(layoutManager);
            danJiangAdapter.setNewData(giftData);
            groudAdapterList.add(danJiangAdapter);
            mPagerList.add(recyclerView);
        }
        viewPagerAdapter = new ViewPagerAdapter(mPagerList, mContext);
        mViewPager_danjiang.setAdapter(viewPagerAdapter);
        mViewPager_danjiang.setCurrentItem(0);
//        for (int i = 0; i < groudAdapterList.size(); i++) {
//            groudAdapterList.get(i).setGid(gid);
//            groudAdapterList.get(i).notifyDataSetChanged();
//        }
        for (int i = 0; i <= fragNumber; i++) {
            ImageView iv_indicator = (ImageView) LayoutInflater.from(mContext)
                    .inflate(R.layout.iv_indicator_gift, ll_indicator_gift, false);
            if (i == 0) {
                iv_indicator.setSelected(true);
            }
            ll_indicator_gift.addView(iv_indicator);
        }


        mViewPager_danjiang.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ll_indicator_gift.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(false);
                curIndex = i;
                ll_indicator_gift.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(true);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

}
