package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.bean.VoiceUserBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.Toast;


/**
 * Created by Administrator on 2018/3/9.
 */

public class MyGoldSendDialog extends Dialog {


    @BindView(R.id.tv_hinttitle)
    TextView tvHinttitle;
    @BindView(R.id.tv_name_goldsend)
    TextView tvNameGoldsend;
    @BindView(R.id.tv_hintshow)
    TextView tvHintshow;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.tv_numbershow)
    TextView tvNumbershow;
    @BindView(R.id.rl_cen)
    LinearLayout rlCen;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.ll_show)
    LinearLayout llShow;
    @BindView(R.id.rl_center)
    RelativeLayout rlCenter;
    VoiceUserBean.DataBean dataBean;
    int userId;
    Context context;
    @BindView(R.id.tv_id_goldsend)
    TextView tvIdGoldsend;
    @BindView(R.id.tv_record)
    TextView tv_record;

    public MyGoldSendDialog(Context context, VoiceUserBean.DataBean dataBean, int userId) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.userId = userId;
        this.dataBean = dataBean;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // ??????????????????
        setContentView(R.layout.dialog_goldsend);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        tvNameGoldsend.setText("?????????" + dataBean.getName());
        tvIdGoldsend.setText("ID???" + dataBean.getUsercoding());

        setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getGoldCall();
    }

    private void getGoldCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        HttpManager.getInstance().post(Api.getUserMoney, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    if (getOneBean.getData().getState() == 2) {
                        Toast.create(MyApplication.getInstance()).show("???????????????????????????????????????");
                        BroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(Const.BroadCast.EXIT);
                        return;
                    }
                    tv_balance.setText("" + getOneBean.getData().getGold());

                    tvNumbershow.setText("????????????" + getOneBean.getData().getGold() + "???");
                } else {
                    showToast(getOneBean.getMsg());
                }

            }
        });
    }

    public void setRecordButton(View.OnClickListener clickListener) {
        tv_record.setOnClickListener(clickListener);
    }

    /**
     * ?????????????????????????????????
     *
     * @param rightStr      ??????
     * @param clickListener ????????????
     */
    public void setRightButton(String rightStr, View.OnClickListener clickListener) {
        tvSure.setOnClickListener(clickListener);
        tvSure.setText(rightStr);
    }

    public void setRightButton(View.OnClickListener clickListener) {
        tvSure.setOnClickListener(clickListener);
    }

    /**
     * ?????????????????????????????????
     *
     * @param leftStr       ??????
     * @param clickListener ????????????
     */
    public void setLeftButton(String leftStr, View.OnClickListener clickListener) {
        tvCancel.setOnClickListener(clickListener);
        tvCancel.setText(leftStr);
    }

    public void setLeftButton(View.OnClickListener clickListener) {
        tvCancel.setOnClickListener(clickListener);
    }

    /**
     * ??????title
     *
     * @param str ??????
     */
    public void setHintTitle(String str) {
        tvHinttitle.setText(str);
    }


    /**
     * ??????????????????
     *
     * @param str ??????
     */
    public void setHintText(String str) {
        tvHintshow.setText(str);
    }

    /**
     * ??????????????? ????????????
     *
     * @param leftStr  ???????????????
     * @param rightStr ???????????????
     */
    public void setBtnText(String leftStr, String rightStr) {
        tvCancel.setText(leftStr);
        tvSure.setText(rightStr);
    }

    public void setRightText(String rightStr) {
        tvSure.setText(rightStr);
    }

    public void setLeftText(String rightStr) {
        tvCancel.setText(rightStr);
    }

    public String getNumber() {
        return edtNumber.getText().toString();
    }

}