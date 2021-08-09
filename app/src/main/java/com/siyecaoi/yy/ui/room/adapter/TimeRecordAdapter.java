package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.DanRankBean;
import com.siyecaoi.yy.bean.TimeChangeBean;
import com.siyecaoi.yy.utils.ImageUtils;

import cn.sinata.xldutils.utils.TimeUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class TimeRecordAdapter extends BaseQuickAdapter<TimeChangeBean.RecordBean, BaseViewHolder> {


    public TimeRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, TimeChangeBean.RecordBean item) {
        SimpleDraweeView iv_header_danrank = helper.getView(R.id.iv_header_danrank);
        TextView tv_name_danrank = helper.getView(R.id.tv_name_danrank);
        TextView tv_id = helper.getView(R.id.tv_id);
        TextView tv_gold = helper.getView(R.id.tv_gold);
        iv_header_danrank.setImageURI(item.imgTx);
        tv_name_danrank.setText(item.nickname);
        tv_id.setText("ID:"+item.usercoding);
        if (item.isLiang == 1){
            tv_id.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id,0,0,0);
        }else
            tv_id.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        tv_gold.setText("总价值："+item.num);
    }
}
