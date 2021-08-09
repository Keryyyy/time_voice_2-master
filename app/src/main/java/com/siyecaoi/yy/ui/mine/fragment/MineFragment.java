package com.siyecaoi.yy.ui.mine.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.GiftGetBean;
import com.siyecaoi.yy.bean.ImgEntity;
import com.siyecaoi.yy.bean.PersonalHomeBean;
import com.siyecaoi.yy.bean.UpdateOneBean;
import com.siyecaoi.yy.bean.UserBean;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.MainActivity;
import com.siyecaoi.yy.ui.message.GlideBlurTransformation;
import com.siyecaoi.yy.ui.mine.MyAttentionActivity;
import com.siyecaoi.yy.ui.mine.MyFansActivity;
import com.siyecaoi.yy.ui.mine.PersonDataActivity;
import com.siyecaoi.yy.ui.mine.PersonHomeActivity;
import com.siyecaoi.yy.ui.mine.ShowPhotoActivity;
import com.siyecaoi.yy.ui.mine.adapter.PersonHomeGiftListAdapter;
import com.siyecaoi.yy.ui.mine.adapter.PersonHomePropListAdapter;
import com.siyecaoi.yy.ui.other.SelectPhotoActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.ObsUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sinata.xldutils.activitys.SelectPhotoDialog;
import cn.sinata.xldutils.utils.StringUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/12/19.
 */

public class MineFragment extends MyBaseFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_back_personhome)
    ImageView ivBackPersonhome;
    @BindView(R.id.tv_data_personhome)
    TextView tvDataPersonhome;
    @BindView(R.id.iv_show_personhome)
    SimpleDraweeView ivShowPersonhome;
    @BindView(R.id.iv_sex_personhome)
    ImageView ivSexPersonhome;
    @BindView(R.id.tv_name_personhome)
    TextView tvNamePersonhome;

    @BindView(R.id.iv_grade_personhome)
    ImageView ivGradePersonhome;


    @BindView(R.id.iv_gapsi)
    ImageView iv_gapsi;

    @BindView(R.id.iv_charm_personhome)
    ImageView ivCharmPersonhome;

    @BindView(R.id.ll_back_personshow)
    LinearLayout ll_back_personshow;

    @BindView(R.id.ll_mine)
    LinearLayout ll_mine;
    @BindView(R.id.tv_isattention_personhome)
    TextView tvIsattentionPersonhome;
    @BindView(R.id.tv_find_personhome)
    TextView tvFindPersonhome;
    @BindView(R.id.tv_room_personhome)
    TextView tvRoomPersonhome;
    @BindView(R.id.tv_attention_personhome)
    TextView tvAttentionPersonhome;
    @BindView(R.id.rl_attention_personhome)
    LinearLayout rlAttentionPersonhome;
    @BindView(R.id.tv_fans_personhome)
    TextView tvFansPersonhome;
    @BindView(R.id.rl_fans_personhome)
    LinearLayout rlFansPersonhome;
    @BindView(R.id.tv_roomid_personhome)
    TextView tvRoomidPersonhome;
    @BindView(R.id.mRecyclerView_personhome)
    ConstraintLayout mRecyclerViewPersonhome;

    @BindView(R.id.ll_add)
    LinearLayout ll_add;
    @BindView(R.id.iv_1)
    SimpleDraweeView iv_1;
    @BindView(R.id.iv_2)
    SimpleDraweeView iv_2;
    @BindView(R.id.iv_3)
    SimpleDraweeView iv_3;
    @BindView(R.id.iv_4)
    SimpleDraweeView iv_4;
    @BindView(R.id.iv_5)
    SimpleDraweeView iv_5;
    @BindView(R.id.iv_6)
    SimpleDraweeView iv_6;
    @BindView(R.id.liang_iv)
    ImageView liang_iv;
    @BindView(R.id.tv_signer_personhome)
    TextView tvSignerPersonhome;
    @BindView(R.id.tv_gold)
    TextView tv_gold;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.iv_signers_personhome)
    ImageView ivSignersPersonhome;
    @BindView(R.id.mRecyclerView_gift_personhome)
    RecyclerView mRecyclerViewGiftPersonhome;

    @BindView(R.id.mRecyclerView_prop_personhome)
    RecyclerView mRecyclerViewPropPersonhome;
    @BindView(R.id.rl_chat_personhome)
    RelativeLayout rlChatPersonhome;
    @BindView(R.id.rl_addattention_personhome)
    RelativeLayout rlAddattentionPersonhome;

    @BindView(R.id.tv_nomal_gift)
    TextView tvNomalGift;

    @BindView(R.id.tv_packet_gift)
    TextView tvPacketGift;

    @BindView(R.id.tv_photo)
    TextView tv_photo;

    private ArrayList<SimpleDraweeView> imgViews = new ArrayList<>();

    private ArrayList<ImgEntity> imgs = new ArrayList<>();
    private int chooseOne;
    int pageNumber;

    PersonHomeGiftListAdapter giftListAdapter;
    PersonHomePropListAdapter propListAdapter;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_personhome, container, false);
    }

    @Override
    public void initView() {
        ll_mine.setVisibility(View.VISIBLE);
        ll_back_personshow.setVisibility(View.GONE);
        initShow();
        getUserCall();
        getCall();
        setRecycler();
        setGiftRecycler();
        setPropRecycler();
    }

    @Override
    protected void onVisibleToUser() {
        super.onVisibleToUser();
        getUserCall();
        getCall();
        getGiftCall();
    }

    private void getUserCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.getUserInfo, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    initShared(userBean.getData());
                }
            }
        });
    }

    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.PersonageUser, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                PersonalHomeBean personalHomeBean = JSON.parseObject(responseString, PersonalHomeBean.class);
                if (personalHomeBean.getCode() == Api.SUCCESS) {
                    initShow1(personalHomeBean.getData());
                } else {
                    showToast(personalHomeBean.getMsg());
                }
            }
        });
    }

    /**
     * 更新显示
     *
     * @param data
     */
    @SuppressLint("SetTextI18n")
    private void initShow1(PersonalHomeBean.DataEntity data) {
        if (data.getUser() != null) {
            PersonalHomeBean.DataEntity.UserEntity userEntity = data.getUser();
            ivGradePersonhome.setImageResource(ImageShowUtils.getGrade(userEntity.getTreasureGrade()));
            ivCharmPersonhome.setImageResource(ImageShowUtils.getCharm(userEntity.getCharmGrade()));

            data.getImg().add(new ImgEntity());
            pageNumber = data.getImg().size();

            imgs.clear();
            imgs.addAll(data.getImg());
            for (SimpleDraweeView iv:imgViews){
                iv.setVisibility(View.INVISIBLE);
            }
            for (int i = 0;i< imgs.size();i++){
                if (i<6){
                    imgViews.get(i).setVisibility(View.VISIBLE);
                    imgViews.get(i).setImageURI(imgs.get(i).getUrl());
                }
            }

            if (pageNumber == 7){
                ll_add.setVisibility(View.GONE);
            }else {
                ll_add.setVisibility(View.VISIBLE);
                changeAddPosition();
            }


            tvSignerPersonhome.setText(userEntity.getIndividuation());


            if (TextUtils.isEmpty( userEntity.getLiang())){
                liang_iv.setVisibility(View.GONE);
            }else {
                liang_iv.setVisibility(View.VISIBLE);
            }


            propListAdapter.setNewData(data.getScene());
        }
    }

    private void changeAddPosition() {
        int target = imgViews.get(pageNumber-1).getId();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mRecyclerViewPersonhome);
        constraintSet.connect(R.id.ll_add,ConstraintSet.TOP,target,ConstraintSet.TOP);
        constraintSet.connect(R.id.ll_add,ConstraintSet.BOTTOM,target,ConstraintSet.BOTTOM);
        constraintSet.connect(R.id.ll_add,ConstraintSet.START,target,ConstraintSet.START);
        constraintSet.connect(R.id.ll_add,ConstraintSet.END,target,ConstraintSet.END);
        constraintSet.applyTo(mRecyclerViewPersonhome);

    }

    /**
     * 道具列表
     */
    private void setPropRecycler() {
        propListAdapter = new PersonHomePropListAdapter(R.layout.item_prop_presonhome);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        mRecyclerViewPropPersonhome.setLayoutManager(layoutManager);
        mRecyclerViewPropPersonhome.setAdapter(propListAdapter);

        View view = View.inflate(getActivity(), R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        ivNodata.setImageResource(R.drawable.empty_prop);
        tvNodata.setText(getString(R.string.hint_prop_personhome));
        propListAdapter.setEmptyView(view);

        propListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PersonalHomeBean.DataEntity.SceneEntity sceneEntity = (PersonalHomeBean.DataEntity.SceneEntity) adapter.getItem(position);
                assert sceneEntity != null;
                showMyUpdialog(sceneEntity);
            }
        });
    }

    /**
     * 判断是否使用该座驾或头饰
     *
     * @param sceneEntity
     */
    MyDialog myDialog;

    private void showMyUpdialog(PersonalHomeBean.DataEntity.SceneEntity sceneEntity) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(getActivity());
        myDialog.show();
        int state = sceneEntity.getState();
        if (sceneEntity.getType() == 1) {  //座驾
            if (state == 1) { //未使用
                myDialog.setHintText("是否使用此座驾？");
            } else if (state == 2) {
                myDialog.setHintText("是否取消使用此座驾？");
            }
        } else if (sceneEntity.getType() == 2) {
            if (state == 1) { //未使用
                myDialog.setHintText("是否使用此头饰？");
            } else if (state == 2) {
                myDialog.setHintText("是否取消使用此头饰？");
            }
        }
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                if (state == 1) {
                    getUpCall(sceneEntity.getType(), sceneEntity.getId(), Const.IntShow.ONE);
                } else if (state == 2) {
                    getUpCall(sceneEntity.getType(), 0, Const.IntShow.TWO);
                }
            }
        });
    }

    private void getUpCall(int type, int id, int isCancel) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", userToken);
        map.put("seId", id);
        if (isCancel == 2) {
            map.put("isSeId", type);
        }
        HttpManager.getInstance().post(Api.updateUser, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UpdateOneBean baseBean = JSON.parseObject(responseString, UpdateOneBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (type == 1) {//座驾
                        SharedPreferenceUtils.put(getActivity(), Const.User.CAR, baseBean.getData().getUserZj());
                        SharedPreferenceUtils.put(getActivity(), Const.User.CAR_H, baseBean.getData().getUserZjfm());
                    } else if (type == 2) {
                        SharedPreferenceUtils.put(getActivity(), Const.User.HEADWEAR, baseBean.getData().getUserTh());
                        SharedPreferenceUtils.put(getActivity(), Const.User.HEADWEAR_H, baseBean.getData().getUserZjfm());
                    }
                    getCall();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }


    private void setGiftRecycler() {
        giftListAdapter = new PersonHomeGiftListAdapter(R.layout.item_gift_presonhome);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        mRecyclerViewGiftPersonhome.setLayoutManager(layoutManager);
        mRecyclerViewGiftPersonhome.setAdapter(giftListAdapter);

        View view = View.inflate(getActivity(), R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        ivNodata.setImageResource(R.drawable.empty_gift);
        tvNodata.setText(getString(R.string.hint_gift_personhome));
        giftListAdapter.setEmptyView(view);
        getGiftCall();
        giftListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getGiftCall();
            }
        }, mRecyclerViewGiftPersonhome);
    }

    private void getGiftCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("state", 2);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.userScene, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GiftGetBean giftGetBean = JSON.parseObject(responseString, GiftGetBean.class);
                if (giftGetBean.getCode() == Api.SUCCESS) {
                    setData(giftGetBean.getData(), giftListAdapter);
                } else {
                    showToast(giftGetBean.getMsg());
                }
            }
        });
    }

    private void setData(List<GiftGetBean.DataEntity> data, PersonHomeGiftListAdapter adapter) {
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
        if (size < PAGE_SIZE) {
            //不显示没有更多
            adapter.loadMoreEnd(true);
        } else {
            adapter.loadMoreComplete();
        }
    }

    private void setRecycler() {
        imgViews.add(iv_1);
        imgViews.add(iv_2);
        imgViews.add(iv_3);
        imgViews.add(iv_4);
        imgViews.add(iv_5);
        imgViews.add(iv_6);
        for (int i = 0;i<6;i++){
            int finalI = i;
            imgViews.get(i).setOnClickListener(v->{
                if (finalI == imgs.size() - 1 ){
                    if (pageNumber < 7) {
                        openSelectPhoto();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Const.ShowIntent.POSITION, finalI);
                        ArrayList<ImgEntity> imgEntities1 = new ArrayList<>();
                        imgEntities1.addAll(imgs.subList(0, imgs.size() - 1));
                        bundle.putSerializable(Const.ShowIntent.DATA, (Serializable) imgEntities1);
                        Intent intent = new Intent(getActivity(), ShowPhotoActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, Const.RequestCode.CLEAR_PIC);
                    }
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Const.ShowIntent.POSITION, finalI);
                    ArrayList<ImgEntity> imgEntities1 = new ArrayList<>();
                    imgEntities1.addAll(imgs.subList(0, imgs.size() - 1));

                    bundle.putSerializable(Const.ShowIntent.DATA, (Serializable) imgEntities1);
                    Intent intent = new Intent(getActivity(), ShowPhotoActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Const.RequestCode.CLEAR_PIC);
                }
            });
        }

    }

    @SuppressLint("SetTextI18n")
    private void initShared(UserBean.DataBean dataBean) {

        SharedPreferenceUtils.put(Objects.requireNonNull(getContext()), Const.User.USER_TOKEN, dataBean.getId());
        SharedPreferenceUtils.put(getContext(), Const.User.AGE, dataBean.getAge());
        SharedPreferenceUtils.put(getContext(), Const.User.IMG, dataBean.getImgTx());
        SharedPreferenceUtils.put(getContext(), Const.User.SEX, dataBean.getSex());
        SharedPreferenceUtils.put(getContext(), Const.User.NICKNAME, dataBean.getNickname());
        SharedPreferenceUtils.put(getContext(), Const.User.ROOMID, dataBean.getUsercoding());
        SharedPreferenceUtils.put(getContext(), Const.User.CharmGrade, dataBean.getCharmGrade());
        SharedPreferenceUtils.put(getContext(), Const.User.DATEOFBIRTH, dataBean.getDateOfBirth());
        SharedPreferenceUtils.put(getContext(), Const.User.FansNum, dataBean.getFansNum());
        SharedPreferenceUtils.put(getContext(), Const.User.AttentionNum, dataBean.getAttentionNum());
        SharedPreferenceUtils.put(getContext(), Const.User.GOLD, dataBean.getGold());
        SharedPreferenceUtils.put(getContext(), Const.User.GoldNum, dataBean.getGoldNum());
        SharedPreferenceUtils.put(getContext(), Const.User.GRADE_T, dataBean.getTreasureGrade());
        SharedPreferenceUtils.put(getContext(), Const.User.PHONE, dataBean.getPhone());
        SharedPreferenceUtils.put(getContext(), Const.User.QQSID, dataBean.getQqSid());
        SharedPreferenceUtils.put(getContext(), Const.User.WECHATSID, dataBean.getWxSid());
        SharedPreferenceUtils.put(getContext(), Const.User.Ynum, dataBean.getYnum());
        SharedPreferenceUtils.put(getContext(), Const.User.Yuml, dataBean.getYuml());
        SharedPreferenceUtils.put(getContext(), Const.User.HEADWEAR_H, dataBean.getUserThfm());
        SharedPreferenceUtils.put(getContext(), Const.User.CAR_H, dataBean.getUserZjfm());
        SharedPreferenceUtils.put(getContext(), Const.User.HEADWEAR, dataBean.getUserTh());
        SharedPreferenceUtils.put(getContext(), Const.User.CAR, dataBean.getUserZj());
        SharedPreferenceUtils.put(getContext(), Const.User.SIGNER, dataBean.getIndividuation());
        SharedPreferenceUtils.put(getContext(), Const.User.USER_LiANG, dataBean.getLiang());
        SharedPreferenceUtils.put(getContext(), Const.User.PAY_PASS, dataBean.getPayPassword());
        SharedPreferenceUtils.put(getContext(), Const.User.IS_AGENT_GIVE, dataBean.getIsAgentGive());
//        SharedPreferenceUtils.put(getContext(), Const.User.USER_SIG, dataBean.getToken());
        initShow();
    }

    String imgShow;

    @SuppressLint("SetTextI18n")
    private void initShow() {
        String img = (String) SharedPreferenceUtils.get(getContext(), Const.User.IMG, "");
        String headwear = (String) SharedPreferenceUtils.get(getContext(), Const.User.HEADWEAR, "");
        String nickName = (String) SharedPreferenceUtils.get(getContext(), Const.User.NICKNAME, "");
        int attentionNumber = (int) SharedPreferenceUtils.get(getContext(), Const.User.AttentionNum, 0);
        int fansNumber = (int) SharedPreferenceUtils.get(getContext(), Const.User.FansNum, 0);
        int gold = (int) SharedPreferenceUtils.get(getContext(), Const.User.GOLD, 0);
        String ynum = (String) SharedPreferenceUtils.get(getContext(), Const.User.Ynum, "0");
        String roomId = (String) SharedPreferenceUtils.get(getContext(), Const.User.ROOMID, "");
        int sexShow = (int) SharedPreferenceUtils.get(getContext(), Const.User.SEX, 0);
        String userLiang = (String) SharedPreferenceUtils.get(getContext(), Const.User.USER_LiANG, "");

        if (TextUtils.isEmpty(userLiang)){
            liang_iv.setVisibility(View.GONE);
        }else {
            liang_iv.setVisibility(View.VISIBLE);
        }

        tv_gold.setText(gold+"");
        tv_money.setText(ynum);

        tvSignerPersonhome.setHint(getString(R.string.hint_signer_data));

        if (StringUtils.isEmpty(imgShow)) {
            imgShow = img;
            ImageUtils.loadUri(ivShowPersonhome, imgShow);
        } else {
            if (!imgShow.equals(img)) {
                imgShow = img;
                ImageUtils.loadUri(ivShowPersonhome, imgShow);
            }
        }



        if (sexShow == 1) {
            ivSexPersonhome.setSelected(true);

        } else if (sexShow == 2) {
            ivSexPersonhome.setSelected(false);
        }

        tvNamePersonhome.setText(nickName);
        Glide.with(this)
                .load(imgShow)
                .apply(RequestOptions.bitmapTransform(new GlideBlurTransformation(getActivity())))
                .into(new ViewTarget<ImageView, Drawable>(iv_gapsi) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Drawable current = resource.getCurrent();
                        iv_gapsi.setImageDrawable(current);
                    }
                });

        if (!StringUtils.isEmpty(userLiang)) {
            tvRoomidPersonhome.setText(userLiang);
        } else {
            tvRoomidPersonhome.setText(roomId);
        }

        tvAttentionPersonhome.setText(attentionNumber + "");
        tvFansPersonhome.setText(fansNumber + "");
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @SuppressLint("CheckResult")
    private void openSelectPhoto() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
                            startActivityForResult(intent,Const.RequestCode.SELECTPHOTO_CODE);
                        } else {
                            showToast("请在应用权限页面开启相机权限");
                        }
                    }
                });
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


    @OnClick({R.id.tv_data_personhome, R.id.rl_fans_personhome, R.id.iv_show_personhome, R.id.tv_name_personhome,
            R.id.iv_signers_personhome,  R.id.rl_chat_personhome,R.id.tv_photo,R.id.iv_menu,
            R.id.rl_addattention_personhome, R.id.rl_signers_personhome, R.id.tv_changesingers_personhome,R.id.tv_nomal_gift,R.id.tv_packet_gift})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_data_personhome:
                ActivityCollector.getActivityCollector().toOtherActivity(PersonDataActivity.class, Const.RequestCode.DATA_CHANGE);
                break;
            case R.id.iv_show_personhome:
            case R.id.tv_name_personhome:
                bundle.putInt(Const.ShowIntent.ID, userToken);
                ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
                break;
            case R.id.rl_attention_personhome:
                ActivityCollector.getActivityCollector().toOtherActivity(MyAttentionActivity.class);
                break;
            case R.id.rl_fans_personhome:
                ActivityCollector.getActivityCollector().toOtherActivity(MyFansActivity.class);
                break;
            case R.id.iv_signers_personhome:
                break;
            case R.id.iv_menu:
                ((MainActivity)getActivity()).switchDrawer();
                break;
            case R.id.tv_photo:

                if (chooseOne != 0) {
                    setNochoose();
                    chooseOne = 0;
                    setChoose();
                }
                break;
            case R.id.tv_nomal_gift:

                if (chooseOne != 1) {
                    setNochoose();
                    chooseOne = 1;
                    setChoose();
                }
                break;

            case R.id.tv_packet_gift:

                if (chooseOne != 2) {
                    setNochoose();
                    chooseOne = 2;
                    setChoose();
                }
                break;

        }
    }

    private void setChoose() {
        switch (chooseOne) {
            case 1:
                tvNomalGift.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvNomalGift.setTextSize(14);
                tvNomalGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);
                mRecyclerViewPropPersonhome.setVisibility(View.GONE);
                mRecyclerViewGiftPersonhome.setVisibility(View.VISIBLE);
                mRecyclerViewPersonhome.setVisibility(View.GONE);
                break;
            case 2:
                tvPacketGift.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvPacketGift.setTextSize(14);
                tvPacketGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);
                mRecyclerViewPropPersonhome.setVisibility(View.VISIBLE);
                mRecyclerViewGiftPersonhome.setVisibility(View.GONE);
                mRecyclerViewPersonhome.setVisibility(View.GONE);

                break;
            case 0:
                tv_photo.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tv_photo.setTextSize(14);
                tv_photo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);
                mRecyclerViewPersonhome.setVisibility(View.VISIBLE);
                mRecyclerViewPropPersonhome.setVisibility(View.GONE);
                mRecyclerViewGiftPersonhome.setVisibility(View.GONE);
                break;

        }
    }

    private void setNochoose() {
        switch (chooseOne) {
            case 1:
                tvNomalGift.setTextColor(ContextCompat.getColor(getActivity(), R.color.white8));
                tvNomalGift.setTextSize(12);
                tvNomalGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 2:
                tvPacketGift.setTextColor(ContextCompat.getColor(getActivity(), R.color.white8));
                tvPacketGift.setTextSize(12);
                tvPacketGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 0:
                tv_photo.setTextColor(ContextCompat.getColor(getActivity(), R.color.white8));
                tv_photo.setTextSize(12);
                tv_photo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && requestCode == Const.RequestCode.SELECTPHOTO_CODE) {
                String filePath = data.getStringExtra(SelectPhotoDialog.DATA);
                updateImgCallObs(filePath);
            } else if (requestCode == Const.RequestCode.CLEAR_PIC) {
                getCall();
            } else if (requestCode == Const.RequestCode.DATA_CHANGE) {
                getCall();
            }
        }
    }

    /**
     * 图片上传
     *
     * @param url
     */
    private void uploadImgCall(String url) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("imagess", url);
        HttpManager.getInstance().post(Api.addUserImg, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    getCall();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void updateImgCallObs(String filePath) {
        showDialog();
        ObsUtils obsUtils = new ObsUtils();
        obsUtils.uploadFile(filePath, new ObsUtils.ObsCallback() {
            @Override
            public void onUploadFileSuccess(String url) {
                dismissDialog();
//                dismissDialog();
                uploadImgCall(url);
            }

            @Override
            public void onUploadMoreFielSuccess() {

            }

            @Override
            public void onFail(String message) {
                dismissDialog();
                LogUtils.e("obs "+message);
                showToast("上传失败");
            }
        });
    }

}
