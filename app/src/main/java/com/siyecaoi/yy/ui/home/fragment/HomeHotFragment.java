package com.siyecaoi.yy.ui.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orient.tea.barragephoto.adapter.BarrageAdapter;
import com.orient.tea.barragephoto.ui.BarrageView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.HomeItemData;
import com.siyecaoi.yy.bean.HomeTuijianBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.home.BarrageData;
import com.siyecaoi.yy.ui.home.adapter.HomeZBAdapter;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 首页热门页面
 */

public class HomeHotFragment extends MyBaseFragment {

    @BindView(R.id.mRecyclerView_header_hot)
    RecyclerView mRecyclerViewHeaderHot;
    @BindView(R.id.mRecyclerView_hot)
    RecyclerView mRecyclerViewHot;
    @BindView(R.id.dan_mu_rl)
    RelativeLayout dan_mu_rl;


    Unbinder unbinder;

    HomeZBAdapter hotAdapter;



    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homehot, container, false);
    }

    private final int VIDEO_DURATION = 100 * 1000;

    @Override
    public void initView() {

        setHotRecycler();

    }

    private BarrageView barrageView;
    private BarrageAdapter<BarrageData> mAdapter;


    class ViewHolder extends BarrageAdapter.BarrageViewHolder<BarrageData> {

        private ImageView mHeadView;
        private TextView mContent;

        ViewHolder(View itemView) {
            super(itemView);

            mHeadView = itemView.findViewById(R.id.image);
            mContent = itemView.findViewById(R.id.content);
        }

        @Override
        protected void onBind(BarrageData data) {
            Glide.with(getActivity()).load(data.getImgTx())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mHeadView);
            mContent.setText(data.getContent());
        }
    }


    public void getData() {
        SwipeRefreshLayout mSwipeRefreshLayoutHomehot = ((HomeFragment) getParentFragment()).mSwipeRefreshLayoutHomehot;
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("state", 4);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.HOME_DATA, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayoutHomehot != null && mSwipeRefreshLayoutHomehot.isRefreshing()) {
                    mSwipeRefreshLayoutHomehot.setRefreshing(false);
                    hotAdapter.setEnableLoadMore(true);
                }
                HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {

                    setData(baseBean.getData(), hotAdapter);
                } else {
                    showToast(baseBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                if (mSwipeRefreshLayoutHomehot != null && mSwipeRefreshLayoutHomehot.isRefreshing()) {
                    mSwipeRefreshLayoutHomehot.setRefreshing(false);
                    hotAdapter.setEnableLoadMore(true);
                }
            }
        });
    }

    List<HomeItemData> listData = new ArrayList<>();

    private void setData(List<HomeItemData> data, HomeZBAdapter adapter) {

        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            listData.clear();
            listData.addAll(data);
            handlData();
            adapter.setNewData(listData);
        } else {
            if (size > 0) {

                listData.addAll(data);
                handlData();
                adapter.notifyDataSetChanged();
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            if (page == 1) { //GridLayoutManager不显示加载更多
                adapter.loadMoreEnd(true);
            } else {
                adapter.loadMoreEnd(true);
            }
        } else {
            adapter.loadMoreComplete();
        }
    }


    public void handlData() {
        for (int i = 0; i < listData.size(); i++) {
            listData.get(i).setItemType(HomeItemData.TYPE_ITEM2);
        }
    }

    private void gotoRoom(String roomId) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ROOMID, roomId);
        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
    }

    private void setHotRecycler() {
        hotAdapter = new HomeZBAdapter(listData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        mRecyclerViewHot.setLayoutManager(layoutManager);
        mRecyclerViewHot.setAdapter(hotAdapter);

        hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeItemData homeItemData = (HomeItemData) adapter.getItem(position);
                assert homeItemData != null;
                gotoRoom(homeItemData.getUsercoding());
            }
        });

        hotAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getData();
            }
        }, mRecyclerViewHot);
    }

    public void refresh(){
        page = 1;
        getData();
        hotAdapter.setEnableLoadMore(false);
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
