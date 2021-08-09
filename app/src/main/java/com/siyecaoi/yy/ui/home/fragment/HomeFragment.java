package com.siyecaoi.yy.ui.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.BannerListBean;
import com.siyecaoi.yy.bean.HomeItemData;
import com.siyecaoi.yy.bean.HomeRankBean;
import com.siyecaoi.yy.bean.HomeTuijianBean;
import com.siyecaoi.yy.bean.RankBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.MainActivity;
import com.siyecaoi.yy.ui.home.RankActivity;
import com.siyecaoi.yy.ui.home.RoomListActivity;
import com.siyecaoi.yy.ui.home.SearchActivity;
import com.siyecaoi.yy.ui.other.WebActivity;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.siyecaoi.yy.utils.ViewPagerIndicator;
import com.siyecaoi.yy.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/12/19.
 */

public class HomeFragment extends MyBaseFragment {

    @BindView(R.id.banner)
    ViewPager banner;
    @BindView(R.id.ll_indicator)
    LinearLayout llIndicator;
    @BindView(R.id.cl_tuijian)
    ConstraintLayout cl_tuijian;
    @BindView(R.id.ll_hot_room)
    LinearLayout ll_hot_room;
    FragPagerAdapter fragPagerAdapter;
    @BindView(R.id.mTabLayout_home)
    SlidingTabLayout mTabLayoutHome;
    @BindView(R.id.mViewPager_home)
    ViewPager mViewPagerHome;
    @BindView(R.id.mSwipeRefreshLayout_homehot)
    public SwipeRefreshLayout mSwipeRefreshLayoutHomehot;

    @BindView(R.id.iv_crown)
    ImageView ivCrown;
    @BindView(R.id.iv_crown_22)
    ImageView ivCrown22;
    @BindView(R.id.iv_crown_3)
    ImageView ivCrown3;
    @BindView(R.id.iv_crown_2)
    ImageView ivCrown2;
    @BindView(R.id.iv_crown_5)
    ImageView ivCrown5;
    @BindView(R.id.iv_crown_6)
    ImageView ivCrown6;
    @BindView(R.id.iv_tui_1)
    SimpleDraweeView iv_tui_1;
    @BindView(R.id.iv_tui_2)
    SimpleDraweeView iv_tui_2;
    @BindView(R.id.iv_tui_3)
    SimpleDraweeView iv_tui_3;
    @BindView(R.id.tv_tui_1)
    TextView tv_tui_1;
    @BindView(R.id.tv_tui_2)
    TextView tv_tui_2;
    @BindView(R.id.tv_tui_3)
    TextView tv_tui_3;
    @BindView(R.id.tv_count_1)
    TextView tv_count_1;
    @BindView(R.id.tv_count_2)
    TextView tv_count_2;
    @BindView(R.id.tv_count_3)
    TextView tv_count_3;
    @BindView(R.id.tv_tag_1)
    TextView tv_tag_1;
    @BindView(R.id.tv_tag_2)
    TextView tv_tag_2;
    @BindView(R.id.tv_tag_3)
    TextView tv_tag_3;
    @BindView(R.id.iv_rank_1)
    SimpleDraweeView iv_1;
    @BindView(R.id.iv_rank_2)
    SimpleDraweeView iv_2;
    @BindView(R.id.iv_rank_3)
    SimpleDraweeView iv_3;
    @BindView(R.id.iv_rank_4)
    SimpleDraweeView iv_4;
    @BindView(R.id.iv_rank_5)
    SimpleDraweeView iv_5;
    @BindView(R.id.iv_rank_6)
    SimpleDraweeView iv_6;

    @BindView(R.id.root_view)
    LinearLayout rootView;

    private Boolean isDestroyed = false;


    Unbinder unbinder;
    ArrayList<String> titles;
    ArrayList<Fragment> fragments;
    HomeHotFragment hotHomeFragment;//热门
//    MatchHomeFragment matchHomeFragment;//速配
    private int selectTab;
    private MainActivity activity;

    private int itemPosition = 0;
    private boolean isFirst = true;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;
        }
    });

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void initView() {

        activity = (MainActivity) getActivity();
        initFrag();
        getBanner();
        mSwipeRefreshLayoutHomehot.post(new Runnable() {
            @Override
            public void run() {
                getRankData();
                getTuijian();
                hotHomeFragment.refresh();
            }
        });
        mSwipeRefreshLayoutHomehot.setOnRefreshListener(() -> {
            if (selectTab == 0){
                hotHomeFragment.refresh();
            }else {
                ((OtherFragment)fragments.get(selectTab)).refresh();
            }
            getRankData();
            getTuijian();
        });
    }

    private void getBanner() {
        banner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                itemPosition = position;
            }
        });
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("type", Const.IntShow.ONE);
        HttpManager.getInstance().post(Api.Banner, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BannerListBean baseBean = JSON.parseObject(responseString, BannerListBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    List<BannerListBean.DataBean> data = baseBean.getData();
                    if (data != null && !data.isEmpty()) {
                        if (data.size() == 1) {
                            data.add(data.get(0));
                        }
                        ArrayList<BannerFragment> fragments = new ArrayList<>();
                        for (BannerListBean.DataBean item : data) {
                            BannerFragment fragment = BannerFragment.newInstance(item.getImgUrl());
                            fragment.setListener(() -> {
                                clickBanner(item.getId());
                                switch (item.getUrlType()) {
                                    case 2: {
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse(item.getUrlHtml());
                                        intent.setData(content_url);
                                        startActivity(intent);
                                        break;
                                    }
                                    case 3: {
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Const.ShowIntent.URL, item.getUrlHtml());
                                        bundle.putString(Const.ShowIntent.TITLE, item.getTitle());
                                        ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                                        break;
                                    }
                                    case 4: {
                                        gotoRoom(item.getUrlHtml());
                                        break;
                                    }
                                }
                            });
                            fragments.add(fragment);
                        }
                        banner.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                            @Override
                            public Fragment getItem(int i) {
                                return fragments.get(i);
                            }

                            @Override
                            public int getCount() {
                                return fragments.size();
                            }
                        });
                        ViewPagerIndicator viewPagerIndicator = new ViewPagerIndicator(getActivity(), llIndicator, R.drawable.indicator_normal, R.drawable.indicator_select, fragments.size());
                        viewPagerIndicator.setupWithViewPager(banner);
                        if (isFirst)
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isDestroyed)
                                        try {
                                            itemPosition++;
                                            handler.postDelayed(this, 3000);
                                            banner.setCurrentItem(itemPosition % fragments.size());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                }
                            }, 3000);
                        isFirst = false;
                    }
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * banner点击调用
     *
     * @param id
     */
    private void clickBanner(int id) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", id);
        HttpManager.getInstance().post(Api.Banner_Click, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {

            }
        });
    }

    private void getTuijian(){
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.HOME_TUIJIAN, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayoutHomehot != null && mSwipeRefreshLayoutHomehot.isRefreshing()) {
                    mSwipeRefreshLayoutHomehot.setRefreshing(false);
                }
                HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    List<HomeItemData> data = baseBean.getData();
                    if (data != null&&data.size()!=0) {
                        cl_tuijian.setVisibility(View.VISIBLE);
                        ll_hot_room.removeAllViews();
                        for (HomeItemData item:data){
                            View view = LayoutInflater.from(activity).inflate(R.layout.item_hot_room, null);
                            SimpleDraweeView iv = view.findViewById(R.id.iv_tui_1);
                            TextView tvName = view.findViewById(R.id.tv_tui_1);
                            TextView tvTag = view.findViewById(R.id.tv_tag_1);
                            TextView tvCount = view.findViewById(R.id.tv_count_1);
                            iv.setImageURI(item.getImg());
                            tvName.setText(item.getRoomName());
                            tvTag.setText(item.getRoomLabel());
                            tvCount.setText(item.getNum()+"");
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    gotoRoom(item.getUsercoding());
                                }
                            });
                            ll_hot_room.addView(view);
                        }
//                        switch (data.size()){
//                            case 1:
//                                iv_tui_1.setImageURI(data.get(0).getImg());
//                                iv_tui_2.setVisibility(View.INVISIBLE);
//                                iv_tui_3.setVisibility(View.INVISIBLE);
//                                tv_tui_1.setText(data.get(0).getRoomName());
//                                tv_tui_2.setVisibility(View.INVISIBLE);
//                                tv_tui_3.setVisibility(View.INVISIBLE);
//                                tv_tag_1.setText(data.get(0).getRoomLabel());
//                                tv_tag_2.setVisibility(View.INVISIBLE);
//                                tv_tag_3.setVisibility(View.INVISIBLE);
//                                tv_count_1.setText(data.get(0).getNum()+"");
//                                tv_count_2.setVisibility(View.INVISIBLE);
//                                tv_count_3.setVisibility(View.INVISIBLE);
//                                break;
//                            case 2:
//                                iv_tui_1.setImageURI(data.get(0).getImg());
//                                iv_tui_2.setImageURI(data.get(1).getImg());
//                                iv_tui_2.setVisibility(View.VISIBLE);
//                                iv_tui_3.setVisibility(View.INVISIBLE);
//
//                                tv_tui_1.setText(data.get(0).getRoomName());
//                                tv_tui_2.setText(data.get(1).getRoomName());
//                                tv_tui_2.setVisibility(View.VISIBLE);
//                                tv_tui_3.setVisibility(View.INVISIBLE);
//
//                                tv_tag_1.setText(data.get(0).getRoomLabel());
//                                tv_tag_2.setText(data.get(1).getRoomLabel());
//                                tv_tag_2.setVisibility(View.VISIBLE);
//                                tv_tag_3.setVisibility(View.INVISIBLE);
//
//                                tv_count_1.setText(data.get(0).getNum()+"");
//                                tv_count_2.setText(data.get(1).getNum()+"");
//                                tv_count_2.setVisibility(View.VISIBLE);
//                                tv_count_3.setVisibility(View.INVISIBLE);
//                                break;
//                            default:
//                                iv_tui_1.setImageURI(data.get(0).getImg());
//                                iv_tui_2.setImageURI(data.get(1).getImg());
//                                iv_tui_3.setImageURI(data.get(2).getImg());
//                                iv_tui_2.setVisibility(View.VISIBLE);
//                                iv_tui_3.setVisibility(View.VISIBLE);
//
//                                tv_tui_1.setText(data.get(0).getRoomName());
//                                tv_tui_2.setText(data.get(1).getRoomName());
//                                tv_tui_3.setText(data.get(2).getRoomName());
//                                tv_tui_2.setVisibility(View.VISIBLE);
//                                tv_tui_3.setVisibility(View.VISIBLE);
//
//                                tv_tag_1.setText(data.get(0).getRoomLabel());
//                                tv_tag_2.setText(data.get(1).getRoomLabel());
//                                tv_tag_3.setText(data.get(2).getRoomLabel());
//                                tv_tag_2.setVisibility(View.VISIBLE);
//                                tv_tag_3.setVisibility(View.VISIBLE);
//
//                                tv_count_1.setText(data.get(0).getNum()+"");
//                                tv_count_2.setText(data.get(1).getNum()+"");
//                                tv_count_3.setText(data.get(2).getNum()+"");
//                                tv_count_2.setVisibility(View.VISIBLE);
//                                tv_count_3.setVisibility(View.VISIBLE);
//                                break;
//                        }
                    }else {
                        cl_tuijian.setVisibility(View.GONE);
                    }
                } else {
                    showToast(baseBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });

    }


    /**
     * 首页排行榜预览
     */
    private void getRankData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.HOT_RANK, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {

                HomeRankBean baseBean = JSON.parseObject(responseString, HomeRankBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    iv_1.setVisibility(View.INVISIBLE);
                    iv_2.setVisibility(View.INVISIBLE);
                    iv_3.setVisibility(View.INVISIBLE);
                    iv_4.setVisibility(View.INVISIBLE);
                    iv_5.setVisibility(View.INVISIBLE);
                    iv_6.setVisibility(View.INVISIBLE);
                    ivCrown.setVisibility(View.INVISIBLE);
                    ivCrown22.setVisibility(View.INVISIBLE);
                    ivCrown3.setVisibility(View.INVISIBLE);
                    ivCrown2.setVisibility(View.INVISIBLE);
                    ivCrown5.setVisibility(View.INVISIBLE);
                    ivCrown6.setVisibility(View.INVISIBLE);
                    HomeRankBean.HotRankData data = baseBean.getData();
                    if (data != null) {
                        List<RankBean.DataBean> list = data.list;
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (i == 0) {
                                    iv_1.setVisibility(View.VISIBLE);
                                    ivCrown.setVisibility(View.VISIBLE);
                                    iv_1.setImageURI(list.get(0).getImgTx());
                                } else if (i == 1){
                                    iv_2.setVisibility(View.VISIBLE);
                                    ivCrown22.setVisibility(View.VISIBLE);
                                    iv_2.setImageURI(list.get(1).getImgTx());
                                }
                                else if (i == 2){
                                    iv_3.setImageURI(list.get(2).getImgTx());
                                    iv_3.setVisibility(View.VISIBLE);
                                    ivCrown3.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        List<RankBean.DataBean> roomList = data.roomList;
                        if (roomList != null) {
                            for (int i = 0; i < roomList.size(); i++) {
                                if (i == 0) {
                                    iv_4.setVisibility(View.VISIBLE);
                                    ivCrown2.setVisibility(View.VISIBLE);
                                    iv_4.setImageURI(roomList.get(0).getImgTx());
                                } else if (i == 1){
                                    iv_5.setVisibility(View.VISIBLE);
                                    ivCrown5.setVisibility(View.VISIBLE);
                                    iv_5.setImageURI(roomList.get(1).getImgTx());
                                }
                                else if (i == 2){
                                    iv_6.setImageURI(roomList.get(2).getImgTx());
                                    iv_6.setVisibility(View.VISIBLE);
                                    ivCrown6.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                } else {
                    showToast(baseBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
    }


    private void initFrag() {
        //1是热门，2是陪玩，4娱乐，5听歌，6相亲，7电台
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add(getString(R.string.tv_hot_home));

        titles.add(getString(R.string.tv_pei_home));
//        titles.add(getString(R.string.tv_match_home));

        titles.add(getString(R.string.tv_radio_home));
        titles.add("游戏");
        titles.add("个人");
//        titles.add(getString(R.string.tv_rec_home));

//        titles.add(getString(R.string.tv_rec_home));3
//        titles.add(getString(R.string.tv_music_home));4
//        titles.add(getString(R.string.tv_miai_home));5
//        titles.add(getString(R.string.tv_city_home));6
//        titles.add(getString(R.string.tv_radio_home));7
        //热门
        hotHomeFragment = new HomeHotFragment();
        fragments.add(hotHomeFragment);

        //陪玩
        Fragment otherFragment1 = new OtherFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(Const.ShowIntent.TYPE, 2);
        otherFragment1.setArguments(bundle1);
        fragments.add(otherFragment1);

//        速配
//        matchHomeFragment = new MatchHomeFragment();
//        fragments.add(matchHomeFragment);

        //电台
        Fragment otherFragment2 = new OtherFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(Const.ShowIntent.TYPE, 7);
        otherFragment2.setArguments(bundle2);
        fragments.add(otherFragment2);


        //游戏
        Fragment otherFragment = new OtherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.TYPE, 8);
        otherFragment.setArguments(bundle);
        fragments.add(otherFragment);



        //个人
        Fragment otherFragment3 = new OtherFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt(Const.ShowIntent.TYPE, 1);
        otherFragment3.setArguments(bundle3);
        fragments.add(otherFragment3);

//        Fragment personalFragment = new HomePersonalFragment();
//        fragments.add(personalFragment);

//        for (int i = 3; i < titles.size(); i++) {
//            Fragment otherFragment = new OtherFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt(Const.ShowIntent.TYPE, i);
//            otherFragment.setArguments(bundle);
//            fragments.add(otherFragment);
//        }

        setFrag();
    }

    private void setFrag() {
        fragPagerAdapter = new FragPagerAdapter(getChildFragmentManager());

        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPagerHome.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();
        mTabLayoutHome.setViewPager(mViewPagerHome);
        mViewPagerHome.setOffscreenPageLimit(5);
        mViewPagerHome.setCurrentItem(0);
        mViewPagerHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mTabLayoutHome.getTitleView(selectTab).setTextSize(16);
                
                mTabLayoutHome.getTitleView(i).setTextSize(18);
                selectTab=i;
                if (i!=0){
                    ((OtherFragment)fragments.get(i)).refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

//        mTabLayoutHome.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//
//            }
//        });

        selectTab = 0;
        mTabLayoutHome.getTitleView(0).setTextSize(18);
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }


    public void setMainTab(){
//        if (selectTab==0){
//            if (activity!=null)
//            activity.getHomeMainTab().setBackgroundResource(R.color.bottm_color1);
//        }else if (selectTab==1){
//            if (activity!=null)
//            activity.getHomeMainTab().setBackgroundResource(R.color.bottm_color2);
//        }else {
//            if (activity!=null)
//            activity.getHomeMainTab().setBackgroundResource(R.color.bottm_color1);
//        }
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
        handler.removeCallbacksAndMessages(null);
        unbinder.unbind();
    }

    @OnClick(R.id.search_ll)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }


    @OnClick(R.id.my_home_ll)
    public void onmyHomeViewClicked() {
        String roomId = (String) SharedPreferenceUtils.get(getContext(), Const.User.ROOMID, "");
        gotoRoom(roomId);
    }


    @OnClick({R.id.phb_rl, R.id.fjb_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //排行榜
            case R.id.phb_rl:
                startActivity(new Intent(getActivity(), RankActivity.class));
                break;
            case R.id.fjb_rl:
                //房间榜
                startActivity(new Intent(getActivity(), RoomListActivity.class));
                break;
        }
    }

    private void gotoRoom(String roomId) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ROOMID, roomId);
        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
    }

}
