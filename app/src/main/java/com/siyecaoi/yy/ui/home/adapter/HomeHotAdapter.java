package com.siyecaoi.yy.ui.home.adapter;

import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.HomeItemData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Locale;

public class HomeHotAdapter extends BaseQuickAdapter<HomeItemData, BaseViewHolder> {

    public HomeHotAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItemData item) {
        TextView tag = helper.getView(R.id.tv_tag);
        tag.setText(item.getRoomLabel());

        helper.setText(R.id.tv_title,item.getRoomName());
        helper.setText(R.id.tv_name,item.getUserName());
        helper.setText(R.id.tv_count,item.getNum()+"");
        ((SimpleDraweeView)helper.getView(R.id.iv_head)).setImageURI(item.getImg());
    }
}
