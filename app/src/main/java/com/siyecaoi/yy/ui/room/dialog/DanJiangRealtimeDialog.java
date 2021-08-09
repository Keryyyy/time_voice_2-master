package com.siyecaoi.yy.ui.room.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.RealtimeListBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.DanJiangRealtimeAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实时奖池
 */
public class DanJiangRealtimeDialog extends Dialog {

    Context mContext;

    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.rv_gift)
    RecyclerView rvGift;
    DanJiangRealtimeAdapter danJiangAdapter;


    public DanJiangRealtimeDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        this.mContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_danjiang_realtime);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.CenterDialogAnimation);
        danJiangAdapter = new DanJiangRealtimeAdapter(R.layout.item_realtime_gift);
        rvGift.setLayoutManager(new GridLayoutManager(mContext,3));
        rvGift.setAdapter(danJiangAdapter);
        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        String reqString = Api.jackpotPage;
        HttpManager.getInstance().post(reqString, map, new MyObserver(mContext) {
            @Override
            public void success(String responseString) {
                RealtimeListBean giftShowBean = JSON.parseObject(responseString, RealtimeListBean.class);
                if (giftShowBean.code == 0){
                    tvCount.setText(((int)giftShowBean.data.num)+"");
                    ArrayList<RealtimeListBean.GiftBean> list = new ArrayList<>();
                    if (giftShowBean.data.list.size()>5){
                        list.addAll(giftShowBean.data.list.subList(0,5));
                    }else
                        list.addAll(giftShowBean.data.list);
                    while (list.size()<5){
                        list.add(RealtimeListBean.GiftBean.newEmpty());
                    }
                    if (list.size() == 5)//添加占位数据
                        list.add(2,RealtimeListBean.GiftBean.newEmpty());
                    danJiangAdapter.setNewData(list);
                }else {
                    showToast(giftShowBean.msg);
                }
            }
        });
    }


    @OnClick({R.id.iv_fortune, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fortune:
                DanRankRealtimeDialog danRankDialog = new DanRankRealtimeDialog(mContext);
                danRankDialog.show();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
