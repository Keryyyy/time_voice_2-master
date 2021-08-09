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
import com.siyecaoi.yy.bean.TimeChangeBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.TimeRecordAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 手气榜
 * Created by Administrator on 2018/3/9.
 */

public class TimeRecordDialog extends Dialog {
    @BindView(R.id.rv_bg)
    RelativeLayout rv_bg;
    @BindView(R.id.mRecyclerView_dialog)
    RecyclerView mRecyclerViewDialog;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private TimeRecordAdapter danRankAdapter;

    private Activity activity;
    private int page = 1;
    private int type;
    private int userId;

    public TimeRecordDialog(Activity activity, int userId, int type) {
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
        rv_bg.setBackgroundResource(R.drawable.bg_time_record);
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

        danRankAdapter = new TimeRecordAdapter(R.layout.item_time_record);
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
        map.put("pageSize", 10);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.shuttleTime, map, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);

                TimeChangeBean danRankBean = JSON.parseObject(responseString, TimeChangeBean.class);
                setData(danRankBean.data, danRankAdapter);
            }
        });
    }

    private void setData(List<TimeChangeBean.RecordBean> data, TimeRecordAdapter adapter) {
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