package com.siyecaoi.yy.ui.home.adapter;

import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.HomeItemData;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class HomeZBAdapter extends BaseMultiItemQuickAdapter<HomeItemData, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HomeZBAdapter(List<HomeItemData> data) {
        super(data);
        addItemType(HomeItemData.TYPE_ITEM1, R.layout.item_xh_item1);//小布局
        addItemType(HomeItemData.TYPE_ITEM2, R.layout.item_xh_item_linear);//大布局
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