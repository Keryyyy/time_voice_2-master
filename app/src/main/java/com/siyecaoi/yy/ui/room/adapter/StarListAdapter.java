package com.siyecaoi.yy.ui.room.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.StarBean;

import cn.sinata.xldutils.utils.StringUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class StarListAdapter extends BaseQuickAdapter<StarBean.LuckBean, BaseViewHolder> {


    public StarListAdapter() {
        super(R.layout.item_star_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, StarBean.LuckBean item) {
        SimpleDraweeView iv_head = helper.getView(R.id.iv_head);
        TextView tv_code = helper.getView(R.id.tv_code);
        iv_head.setImageURI(item.imgTx);
        helper.setText(R.id.tv_name,item.nickname);
        if (item.liang!=null&&item.liang.length()!=0){
            tv_code.setText("ID:"+item.liang);
            tv_code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id,0,0,0);
        }else {
            tv_code.setText("ID:"+item.usercoding);
            tv_code.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }
        helper.setText(R.id.tv_turn,"第"+ StringUtils.formatChineseNum(item.coherence)+"轮幸运星");
        helper.setText(R.id.tv_gold,"总价值："+item.logGoldTotal);
    }

}
