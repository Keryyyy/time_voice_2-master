package com.siyecaoi.yy.ui.room.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.DanRankBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.DanRankAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 手气榜
 * Created by Administrator on 2018/3/9.
 */

public class DanRankDialog extends Dialog {
    @BindView(R.id.rv_bg)
    RelativeLayout rv_bg;
    @BindView(R.id.mRecyclerView_dialog)
    RecyclerView mRecyclerViewDialog;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private DanRankAdapter danRankAdapter;

    private Activity activity;
    private int page = 1;
    private int type;
    private int userId;

    public DanRankDialog(Activity activity, int userId,int type) {
        super(activity, R.style.CustomDialogStyle);
        this.activity = activity;
        this.userId = userId;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_bottom_show1);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        rv_bg.setBackgroundResource(R.drawable.bg_rank_dan);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        setRecycler();
        getCall();
    }

    private void setRecycler() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCall();
            }
        });

        danRankAdapter = new DanRankAdapter(R.layout.item_danrank);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerViewDialog.setAdapter(danRankAdapter);
        mRecyclerViewDialog.setLayoutManager(layoutManager);

        danRankAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerViewDialog);

    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        String reqString = Api.luckList;
        map.put("uid", userId);
        map.put("pageSize", 10);
        map.put("type", type);
        map.put("pageNum", page);
        HttpManager.getInstance().post(reqString, map, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);

                DanRankBean danRankBean = JSON.parseObject(responseString, DanRankBean.class);
                setData(danRankBean.getData(), danRankAdapter);
            }
        });
    }

    private void setData(List<DanRankBean.DataEntity> data, DanRankAdapter adapter) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            adapter.setNewData(data);
        } else {
            if (size > 0) {
                adapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < 10) {
            adapter.loadMoreEnd(false);
        } else {
            adapter.loadMoreComplete();
        }
    }

}