package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.TopupListBean;
import com.siyecaoi.yy.utils.MyUtils;

public class TopupListAdapter extends BaseQuickAdapter<TopupListBean.DataBean.SetRechargeBean, BaseViewHolder> {

    int choosePostion;

    int sendGold;//赠送的星球币

    public int getSendGold() {
        return sendGold;
    }

    public void setSendGold(int sendGold) {
        this.sendGold = sendGold;
    }


    public int getChoosePostion() {
        return choosePostion;
    }

    public void setChoosePostion(int choosePostion) {
        this.choosePostion = choosePostion;
        notifyDataSetChanged();
    }

    public TopupListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, TopupListBean.DataBean.SetRechargeBean item) {
        TextView tv_show_topup = helper.getView(R.id.tv_show_topup);
        TextView tv_price_topup = helper.getView(R.id.tv_price_topup);
        RelativeLayout rl_back_topup = helper.getView(R.id.rl_back_topup);

        tv_show_topup.setText(item.getX() + "");
        tv_price_topup.setText(mContext.getString(R.string.tv_money) + MyUtils.getInstans().doubleTwo(item.getY()));
        if (choosePostion == helper.getAdapterPosition()) {
            tv_price_topup.setAlpha(1f);
        } else {
            tv_price_topup.setAlpha(0.6f);
        }
    }
}
