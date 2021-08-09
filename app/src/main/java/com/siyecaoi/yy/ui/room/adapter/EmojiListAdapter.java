package com.siyecaoi.yy.ui.room.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.model.EmojiList;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

public class EmojiListAdapter extends BaseQuickAdapter<EmojiList.DataBean, BaseViewHolder> {


    public EmojiListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmojiList.DataBean item) {
        TextView tv_emoji_expression = helper.getView(R.id.tv_emoji_expression);
        SimpleDraweeView iv_emoji_expression = helper.getView(R.id.iv_emoji_expression);

        if (item.getUnicode() >= 128564) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            ImageUtils.loadDrawableStatic(iv_emoji_expression, getShow(item.getUnicode()));
        } else if (item.getUnicode() >= 128552 && item.getUnicode() <= 128563) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            iv_emoji_expression.setImageResource(getShow(item.getUnicode()));
        } else {
            tv_emoji_expression.setText(new String(Character.toChars(item.getUnicode())));
            iv_emoji_expression.setVisibility(View.INVISIBLE);
            tv_emoji_expression.setVisibility(View.VISIBLE);
        }
        if (item.getName().equals("排麦序")) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            iv_emoji_expression.setImageResource(R.drawable.s_number_1);
        } else if (item.getName().equals("爆灯")) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            iv_emoji_expression.setImageResource(R.drawable.deng_1);
        } else if (item.getName().equals("举手")) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            iv_emoji_expression.setImageResource(R.drawable.hand_4);
        }
    }

    private int getShow(int unicode) {
        int resInt = 0;
        switch (unicode) {
            case 128552:
                resInt = R.drawable.s_number_1;
                break;
            case 128553:
                resInt = R.drawable.deng_1;
                break;
            case 128554:
                resInt = R.drawable.hand_4;
                break;
            case 128555:
                resInt = R.drawable.cai1;
                break;
            case 128556:
                resInt = R.drawable.xiaoku;
                break;
            case 128557:
                resInt = R.drawable.sese;
                break;
            case 128558:
                resInt = R.drawable.songhua;
                break;
            case 128559:
                resInt = R.drawable.taoqi;
                break;
            case 128560:
                resInt = R.drawable.yaofan;
                break;
            case 128561:
                resInt = R.drawable.qinqin;
                break;
            case 128562:
                resInt = R.drawable.zhi6;
                break;
            case 128563:
                resInt = R.drawable.yingbi1;
                break;
            case 128564:
                resInt = R.drawable.gif128564;
                break;
            case 128565:
                resInt = R.drawable.gif128565;
                break;
            case 128566:
                resInt = R.drawable.gif128566;
                break;
            case 128567:
                resInt = R.drawable.gif128567;
                break;
            case 128568:
                resInt = R.drawable.gif128568;
                break;
            case 128569:
                resInt = R.drawable.gif128569;
                break;
            case 128570:
                resInt = R.drawable.gif128570;
                break;
            case 128571:
                resInt = R.drawable.gif128571;
                break;
            case 128572:
                resInt = R.drawable.gif128572;
                break;
            case 128573:
                resInt = R.drawable.gif128573;
                break;
            case 128574:
                resInt = R.drawable.gif128574;
                break;
            case 128575:
                resInt = R.drawable.gif128575;
                break;
            case 128576:
                resInt = R.drawable.gif128576;
                break;
            case 128577:
                resInt = R.drawable.gif128577;
                break;
            case 128578:
                resInt = R.drawable.gif128578;
                break;
            case 128579:
                resInt = R.drawable.gif128579;
                break;
            case 128580:
                resInt = R.drawable.gif128580;
                break;
            case 128581:
                resInt = R.drawable.gif128581;
                break;
            case 128582:
                resInt = R.drawable.gif128582;
                break;
            case 128583:
                resInt = R.drawable.gif128583;
                break;
            case 128584:
                resInt = R.drawable.gif128584;
                break;
            case 128585:
                resInt = R.drawable.gif128585;
                break;
            case 128586:
                resInt = R.drawable.gif128586;
                break;
            case 128587:
                resInt = R.drawable.gif128587;
                break;
            case 128588:
                resInt = R.drawable.gif128588;
                break;
            case 128589:
                resInt = R.drawable.gif128589;
                break;
            case 128590:
                resInt = R.drawable.gif128580;
                break;
            case 128591:
                resInt = R.drawable.gif128581;
                break;
            case 128592:
                resInt = R.drawable.gif128582;
                break;
            case 128593:
                resInt = R.drawable.gif128583;
                break;
            case 128594:
                resInt = R.drawable.gif128584;
                break;
            case 128595:
                resInt = R.drawable.gif128585;
                break;
            case 128596:
                resInt = R.drawable.gif128586;
                break;
            case 128597:
                resInt = R.drawable.gif128587;
                break;
            case 128598:
                resInt = R.drawable.gif128588;
                break;
            case 128599:
                resInt = R.drawable.gif128589;
                break;
            case 128600:
                resInt = R.drawable.gif128600;
                break;
            case 128601:
                resInt = R.drawable.gif128601;
                break;
            case 128602:
                resInt = R.drawable.gif128602;
                break;
            case 128603:
                resInt = R.drawable.gif128603;
                break;
            case 128604:
                resInt = R.drawable.png128604;
                break;
            case 128605:
                resInt = R.drawable.png128605;
                break;
            case 128606:
                resInt = R.drawable.png128606;
                break;
            case 128607:
                resInt = R.drawable.png128607;
                break;
            case 128608:
                resInt = R.drawable.png128608;
                break;
            case 128609:
                resInt = R.drawable.png128609;
                break;
            case 128610:
                resInt = R.drawable.png128610;
                break;
            case 128611:
                resInt = R.drawable.png128611;
                break;
            case 128612:
                resInt = R.drawable.png128612;
                break;
            case 128613:
                resInt = R.drawable.png128613;
                break;
            case 128614:
                resInt = R.drawable.png128614;
                break;
            case 128615:
                resInt = R.drawable.png128615;
                break;
            case 128616:
                resInt = R.drawable.png128616;
                break;
            case 128617:
                resInt = R.drawable.png128617;
                break;
            case 128618:
                resInt = R.drawable.png128618;
                break;
            case 128619:
                resInt = R.drawable.png128619;
                break;
            case 128620:
                resInt = R.drawable.png128620;
                break;
            case 128621:
                resInt = R.drawable.png128621;
                break;
            case 128622:
                resInt = R.drawable.png128622;
                break;
            case 128623:
                resInt = R.drawable.png128623;
                break;
            case 128624:
                resInt = R.drawable.png128624;
                break;
            case 128625:
                resInt = R.drawable.png128625;
                break;
            case 128626:
                resInt = R.drawable.png128626;
                break;
            case 128627:
                resInt = R.drawable.png128627;
                break;
            case 128628:
                resInt = R.drawable.png128628;
                break;
            case 128629:
                resInt = R.drawable.png128629;
                break;
            case 128630:
                resInt = R.drawable.png128630;
                break;
        }
        return resInt;
    }
}
