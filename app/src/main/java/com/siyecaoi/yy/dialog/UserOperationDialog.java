package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.OnlineUserBean;
import com.siyecaoi.yy.bean.PersonalHomeBean;
import com.siyecaoi.yy.bean.VoiceMicBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserOperationDialog extends Dialog {
    private final VoiceMicBean.DataBean dataBean;
    private boolean isUserOpration = false;
    @BindView(R.id.iv_user_header)
    SimpleDraweeView iv_user_header;


    @BindView(R.id.user_name_tv)
    TextView user_name_tv;
    @BindView(R.id.tv_follow)
    TextView tv_follow;

    @BindView(R.id.liang_iv)
    ImageView liang_iv;
    @BindView(R.id.user_id_tv)
    TextView user_id_tv;

    @BindView(R.id.daichong_tv)
    TextView daichong_tv;
    @BindView(R.id.tv_sign)
    TextView tv_sign;

    @BindView(R.id.zilaio_ll)
    LinearLayout zilaio_ll;

    @BindView(R.id.liwu_ll)
    LinearLayout liwu_ll;

    @BindView(R.id.guanli_ll)
    LinearLayout guanli_ll;
    @BindView(R.id.fl_guanli)
    FrameLayout fl_guanli;


    @BindView(R.id.sixi_ll)
    LinearLayout sixi_ll;


    @BindView(R.id.xiamai_tv)
    TextView xiamai_tv;

    @BindView(R.id.jinmai_tv)
    TextView jinmai_tv;

    @BindView(R.id.lahei_tv)
    TextView lahei_tv;


    @BindView(R.id.tichu_tv)
    TextView tichu_tv;

    @BindView(R.id.guanli_tv)
    TextView guanli_tv;


    @BindView(R.id.caozuo_ll)
    LinearLayout caozuo_ll;

    @BindView(R.id.item_list)
    LinearLayout item_list;


    @BindView(R.id.user_cao_zuo_ll)
    LinearLayout user_cao_zuo_ll;

    @BindView(R.id.ziliao_tv)
    TextView ziliao_tv;

    @BindView(R.id.xmpt_tv)
    TextView xmpt_tv;
    @BindView(R.id.meili_tv)
    TextView meili_tv;
    @BindView(R.id.iv_grade_person)
    ImageView ivGradePerson;
    @BindView(R.id.iv_gradetwo_person)
    ImageView ivGradetwoPerson;

    private Context context;
    private ArrayList<String> bottomList;
    private OnItemClickListener listener;

    private int userId;

    public UserOperationDialog(Context context, ArrayList<String> bottomList, VoiceMicBean.DataBean dataBean, int userId) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.bottomList = bottomList;
        this.dataBean = dataBean;
        this.userId = userId;
    }

    public UserOperationDialog(Context context, ArrayList<String> bottomList, VoiceMicBean.DataBean dataBean, int userId, boolean isUserOpration) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.bottomList = bottomList;
        this.dataBean = dataBean;
        this.isUserOpration = isUserOpration;
        this.userId = userId;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_dialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        getCall(dataBean.getUserModel().getId());
        getSignCall(dataBean.getUserModel().getId());

        if (dataBean.getUserModel().getId() == userId){ //??????
            tv_follow.setVisibility(View.GONE);
        }

        //??????
        ImageUtils.loadUri(iv_user_header, dataBean.getUserModel().getImg());
        //??????
        user_name_tv.setText(dataBean.getUserModel().getName());
        //id
        user_id_tv.setText("ID???" + dataBean.getUserModel().getUsercoding());

        //??????????????????
//        ivGradePerson.setBackgroundResource(ImageShowUtils.getGrade(dataBean.getUserModel().getTreasureGrade()));
//        ivGradePerson.setImageResource(ImageShowUtils.getGrade(dataBean.getUserModel().getTreasureGrade()));
        //??????????????????
//        ivGradetwoPerson.setBackgroundResource(ImageShowUtils.getGrade(dataBean.getUserModel().getCharmGrade()));
//        ivGradetwoPerson.setImageResource(ImageShowUtils.getCharm(dataBean.getUserModel().getTreasureGrade()));

        if (isUserOpration) {
            daichong_tv.setVisibility(View.GONE);
        }
        setListListener();

        meili_tv.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(9);

            dismiss();

        });

        iv_user_header.setOnClickListener(v->{
            if (listener != null)
                listener.onItemClick(1);
        });

        tv_follow.setOnClickListener(v->{
            getAttentionCall(dataBean.getUserModel().getId());
        });

        int isChong = (int) SharedPreferenceUtils.get(context, Const.User.IS_AGENT_GIVE, 0);
        if (isChong == 2) {

            int  uuId= (int) SharedPreferenceUtils.get(context, Const.User.USER_TOKEN, 0);
            if (uuId==dataBean.getUserModel().getId()){
               daichong_tv.setVisibility(View.GONE);
               return;
           }
            daichong_tv.setVisibility(View.VISIBLE);
            daichong_tv.setOnClickListener(v -> {
                dismiss();
                if (listener != null)
                    listener.onItemClick(7);
            });
        }else {
            daichong_tv.setVisibility(View.GONE);
        }
    }

    private void getCall(int otherId) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("buid", otherId);
        HttpManager.getInstance().post(Api.UserAttention, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                OnlineUserBean onlineUserBean = JSON.parseObject(responseString, OnlineUserBean.class);
                if (onlineUserBean.getCode() == Api.SUCCESS) {
                    String strLiang = onlineUserBean.getData().getUser().getLiang();

                    if (TextUtils.isEmpty(strLiang)) {
                        liang_iv.setVisibility(View.GONE);
                    } else {
                        liang_iv.setVisibility(View.VISIBLE);
                        user_id_tv.setText("ID???" + strLiang);
                    }
                    int status = onlineUserBean.getData().getState();
                    tv_follow.setText(status == 1?"+??????":"?????????");
                }
            }
        });
    }

    private void setListListener() {
        for (int i = 0; i < bottomList.size(); i++) {

            if (bottomList.get(i).contains("????????????")) {
//                zilaio_ll.setVisibility(View.VISIBLE);
//                zilaio_ll.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (listener != null)
//                            listener.onItemClick(1);
//                    }
//                });

            } else if (bottomList.get(i).contains("??????")) {
                liwu_ll.setVisibility(View.VISIBLE);
                liwu_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.onItemClick(0);
                    }
                });

            } else if (bottomList.get(i).contains("?????????")) {
                guanli_ll.setVisibility(View.VISIBLE);
                guanli_tv.setText(bottomList.get(i));
                if (bottomList.get(i).startsWith("??????")){
                    fl_guanli.setBackgroundResource(R.drawable.bg_gradient_manager_20dp);
                    guanli_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_manager,0,0,0);
                }else {
                    fl_guanli.setBackgroundResource(R.drawable.bg_gradient_manager_20dp_disable);
                    guanli_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.manager_remove,0,0,0);

                }
                guanli_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.onItemClick(5);
                    }
                });

            } else if (bottomList.get(i).contains("??????")) {
                sixi_ll.setVisibility(View.VISIBLE);
                sixi_ll.setOnClickListener(v -> {
                    dismiss();
                    if (listener != null)
                        listener.onItemClick(8);
                });
            } else if (bottomList.get(i).contains("??????")) {
                xiamai_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.onItemClick(2);
                    }
                });


            } else if (bottomList.get(i).contains("??????")) {
                jinmai_tv.setText("??????");
                jinmai_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.onItemClick(3);
                    }
                });


            } else if (bottomList.get(i).contains("???????????????")) {
                jinmai_tv.setText("??????");
                jinmai_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.onItemClick(3);
                    }
                });


            } else if (bottomList.get(i).contains("?????????")) {
                lahei_tv.setText(bottomList.get(i));
                lahei_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.onItemClick(6);
                    }
                });


            } else if (bottomList.get(i).contains("??????")) {
                caozuo_ll.setVisibility(View.VISIBLE);
                tichu_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.onItemClick(4);
                    }
                });


            } else if (bottomList.get(i).contains("?????????")) {
                daichong_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.onItemClick(7);
                    }
                });

            }
        }

    }

    private void getSignCall(int id) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", id);
        HttpManager.getInstance().post(Api.PersonageUser, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                PersonalHomeBean personalHomeBean = JSON.parseObject(responseString, PersonalHomeBean.class);
                if (personalHomeBean.getCode() == Api.SUCCESS) {
                    String individuation = personalHomeBean.getData().getUser().getIndividuation();
                    tv_sign.setText(individuation.isEmpty()?"???????????????????????????????????????":individuation);
                    ivGradePerson.setImageResource(ImageShowUtils.getGrade(personalHomeBean.getData().getUser().getTreasureGrade()));
                    ivGradetwoPerson.setImageResource(ImageShowUtils.getCharm(personalHomeBean.getData().getUser().getCharmGrade()));
                }
            }
        });
    }

    private void getAttentionCall(int id) {
        String s = tv_follow.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("buid", id);
        map.put("type", s.equals("?????????")?2:1);
        HttpManager.getInstance().post(Api.addAttention, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("????????????");
                    getCall(id);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}