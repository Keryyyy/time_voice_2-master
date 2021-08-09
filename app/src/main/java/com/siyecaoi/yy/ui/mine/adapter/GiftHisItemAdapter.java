package com.siyecaoi.yy.ui.mine.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GiftHisBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.MyUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class GiftHisItemAdapter extends BaseQuickAdapter<GiftHisBean.GiftRecordEntity, BaseViewHolder> {


    public GiftHisItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftHisBean.GiftRecordEntity item) {
        TextView view = helper.getView(R.id.tv_name_gifthis);
        view.setTextColor(Color.parseColor("#81F6B4"));
        if (state == 0) {
            if (item.getType() == 1) {
                helper.setText(R.id.tv_name_gifthis, item.getName() );
                helper.setText(R.id.hint_tv, " 赠送您");
            } else if (item.getType() == 2) {
                helper.setText(R.id.tv_name_gifthis, item.getName());
                helper.setText(R.id.hint_tv, " 开宝箱");
            }
        } else if (state == 1) {
            if (item.getType() == 1) {
                helper.setText(R.id.tv_name_gifthis, item.getName());
                helper.setText(R.id.hint_tv, " 收到您");
            } else if (item.getType() == 2) {
                helper.setText(R.id.tv_name_gifthis, item.getName());
                helper.setText(R.id.hint_tv, " 开宝箱");
            }
        }

        helper.setText(R.id.tv_time_gifthis, MyUtils.getInstans().showTimeYMDHM(MyUtils.getInstans().getLongTime(item.getCreateDate())));
        SimpleDraweeView iv_show_gifthis = helper.getView(R.id.iv_show_gifthis);
        ImageUtils.loadUri(iv_show_gifthis, item.getImg());
        helper.setText(R.id.tv_money_gifthis, "*" + item.getNum());

    }
}
