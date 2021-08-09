package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.ChestOpenBean;
import com.siyecaoi.yy.ui.room.adapter.ChestsShowAdapter;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MyChestsShowDialog extends Dialog {

    @BindView(R.id.iv_close_chestsshow)
    ImageView ivCloseChestsshow;
    @BindView(R.id.iv_border)
    ImageView iv_border;
    @BindView(R.id.iv_border_normal)
    ImageView iv_border_normal;
    @BindView(R.id.iv_showone_chestsshow)
    SimpleDraweeView ivShowoneChestsshow;
    @BindView(R.id.tv_showone_chestsshow)
    TextView tvShowoneChestsshow;
    @BindView(R.id.tv_gold)
    TextView tvGold;
    @BindView(R.id.mRecyclerView_chestsshow)
    RecyclerView mRecyclerViewChestsshow;
    @BindView(R.id.btn_sure_chestsshow)
    Button btnSureChestsshow;

    ChestOpenBean.DataBean data;
    ChestsShowAdapter adapter;
    Context mContext;
    @BindView(R.id.ll_one_chestsshow)
    RelativeLayout llOneChestsshow;
    @BindView(R.id.btn_cancel_chestsshow)
    Button btnCancelChestsshow;
    int chooseOne;//为4则为自动寻宝
    @BindView(R.id.view_one_chests)
    View viewOneChests;

    @BindView(R.id.xingxing_iv)
    SimpleDraweeView xingxing_iv;

    public MyChestsShowDialog(Context context, ChestOpenBean.DataBean data, int chooseOne) {
        super(context, R.style.CustomDialogStyle);
        this.data = data;
        this.mContext = context;
        this.chooseOne = chooseOne;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_chestsshow);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.CenterDialogAnimation2);

        if (chooseOne == 4) {
            btnSureChestsshow.setVisibility(View.GONE);
            viewOneChests.setVisibility(View.GONE);
            btnCancelChestsshow.setVisibility(View.GONE);
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @SuppressLint("CheckResult")
                @Override
                public void run() {
                    Observable.just(chooseOne)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(integer -> {
                                if (integer == 4) {
                                    dismiss();
                                }
                            });
                }
            };
            timer.schedule(timerTask, 2000);
        }


        if (data.getLottery().size() == 0) {
            dismiss();
        } else if (data.getLottery().size() == 1) {
            llOneChestsshow.setVisibility(View.VISIBLE);
            mRecyclerViewChestsshow.setVisibility(View.GONE);
            ImageUtils.loadUri(ivShowoneChestsshow, data.getLottery().get(0).getImg());
            tvShowoneChestsshow.setText(data.getLottery().get(0).getName() + "×" + data.getLottery().get(0).getNum());
            tvGold.setText(data.getLottery().get(0).getGold()+"");
            // 礼物价值1-520（绿色）1000-10000（紫色-带闪亮）20000-52000（橙色带闪亮）

            if (data.getLottery().get(0).getCost()<10000){
                iv_border_normal.setVisibility(View.VISIBLE);
                iv_border_normal.setImageResource(R.drawable.dan_back1);
//            }else if (data.getLottery().get(0).getCost()<10000){
//                iv_border.setVisibility(View.VISIBLE);
//                ImageUtils.loadFrameAnimation(iv_border, R.array.bg_frame_mid,true, Const.giftBorderFrame,null);
            }else {
                iv_border.setVisibility(View.VISIBLE);
                ImageUtils.loadFrameAnimation(iv_border, R.array.bg_frame_red,true,Const.giftBorderFrame,null);
            }


        } else {
            llOneChestsshow.setVisibility(View.GONE);
            mRecyclerViewChestsshow.setVisibility(View.VISIBLE);
            setRecycler();
        }
    }

    private void setRecycler() {
        adapter = new ChestsShowAdapter(data.getLottery());
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        mRecyclerViewChestsshow.setLayoutManager(layoutManager);
        mRecyclerViewChestsshow.setAdapter(adapter);
    }

    public void setButton(View.OnClickListener onClickListener) {
        btnSureChestsshow.setOnClickListener(onClickListener);
    }

    public void setCancel(View.OnClickListener onClickListener) {
        btnCancelChestsshow.setOnClickListener(onClickListener);
    }


}
