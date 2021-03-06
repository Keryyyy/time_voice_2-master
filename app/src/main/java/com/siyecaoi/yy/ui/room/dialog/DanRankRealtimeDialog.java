package com.siyecaoi.yy.ui.room.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.DanRankBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.DanRankRealtimeAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 手气榜
 * Created by Administrator on 2018/3/9.
 */

public class DanRankRealtimeDialog extends Dialog {

    @BindView(R.id.mRecyclerView_dialog)
    RecyclerView mRecyclerViewDialog;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private DanRankRealtimeAdapter danRankAdapter;

    private Context activity;
    private int page = 1;

    public DanRankRealtimeDialog(Context activity) {
        super(activity, R.style.CustomDialogStyle);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_rank_realtime);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
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

        danRankAdapter = new DanRankRealtimeAdapter(R.layout.item_danrank_realtime);
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
        String reqString = Api.jackpot;
        map.put("pageSize", 15);
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

    private void setData(List<DanRankBean.DataEntity> data, DanRankRealtimeAdapter adapter) {
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