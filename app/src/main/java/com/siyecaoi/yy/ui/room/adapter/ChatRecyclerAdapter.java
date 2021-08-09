package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.model.CarShowMessageBean;
import com.siyecaoi.yy.model.EmojiMessageBean;
import com.siyecaoi.yy.model.GetOutBean;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.model.ChatMessageBean;
import com.siyecaoi.yy.model.GiftSendMessage;
import com.siyecaoi.yy.model.MessageBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/1/2.
 */

public class ChatRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    SpannableStringBuilder stringBuilder;

    public ChatRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    public interface OnAttentionClicker {
        void onClicker(int userId);
        void onFollowUser(int userId);

        void onOtherRoomClicker(String roomId);
    }

    OnAttentionClicker onAttentionClicker;

    public OnAttentionClicker getOnAttentionClicker() {
        return onAttentionClicker;
    }

    public void setOnAttentionClicker(OnAttentionClicker onAttentionClicker) {
        this.onAttentionClicker = onAttentionClicker;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        RelativeLayout rl_item = helper.getView(R.id.rl_item);
        RelativeLayout rl_simple = helper.getView(R.id.rl_simple);
        RelativeLayout rl_back_voice = helper.getView(R.id.rl_back_voice);
        TextView tv_show_chat = helper.getView(R.id.tv_show_chat);
        TextView tv_end_chat = helper.getView(R.id.tv_end_chat);
        SimpleDraweeView iv_show_chat = helper.getView(R.id.iv_show_chat);
        SimpleDraweeView iv_emoji = helper.getView(R.id.iv_emoji);
        TextView tv_nickname_radioda = helper.getView(R.id.tv_nickname_radioda);
        TextView tv_show_radioda = helper.getView(R.id.tv_show_radioda);
        SimpleDraweeView iv_header_radioda = helper.getView(R.id.iv_header_radioda);
        ImageView iv_grade_allmsg = helper.getView(R.id.iv_grade_radioda);//财富等级显示
        ImageView iv_charm_allmsg = helper.getView(R.id.iv_charm_allmsg);//魅力等级显示
        LinearLayout llRotate = helper.getView(R.id.ll_rotate);
        ImageView ivNumber1 = helper.getView(R.id.iv_number_1);
        ImageView ivNumber2 = helper.getView(R.id.iv_number_2);
        ImageView ivNumber3 = helper.getView(R.id.iv_number_3);

        tv_end_chat.setVisibility(View.GONE);
        iv_show_chat.setVisibility(View.GONE);
        rl_back_voice.setBackgroundResource(R.drawable.bg_round5_chat);
        MessageBean messageBean = new Gson().fromJson(item, MessageBean.class);
        String messageShow;//显示信息
        int resShowid;
        ImageSpan imageSpan;
        String userName;
        ForegroundColorSpan colorSpan;
        ClickableSpan clickableSpan;

        if (messageBean.getCode() == 100){
            rl_item.setVisibility(View.VISIBLE);
            tv_show_radioda.setVisibility(View.VISIBLE);
            rl_simple.setVisibility(View.GONE);
            iv_emoji.setVisibility(View.GONE);
            rl_back_voice.setBackgroundResource(0);
            llRotate.setVisibility(View.GONE);
        }else if(messageBean.getCode() == 108){
            rl_item.setVisibility(View.VISIBLE);
            iv_emoji.setVisibility(View.VISIBLE);
            tv_show_radioda.setVisibility(View.GONE);
            rl_simple.setVisibility(View.GONE);
            rl_back_voice.setBackgroundResource(0);
            llRotate.setVisibility(View.GONE);
            EmojiMessageBean emojiMessageBean = new Gson().fromJson(item, EmojiMessageBean.class);
            if (emojiMessageBean.getData().getEmojiCode() == 128629 || emojiMessageBean.getData().getEmojiCode() == 128630){
                llRotate.setVisibility(View.VISIBLE);
                iv_emoji.setVisibility(View.GONE);
            }
        }else {
            rl_item.setVisibility(View.GONE);
            rl_simple.setVisibility(View.VISIBLE);
            rl_back_voice.setBackgroundResource(R.drawable.bg_round5_chat);
            llRotate.setVisibility(View.GONE);
        }

            switch (messageBean.getCode()) {
            case 100:
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                LogUtils.e("msg", item);
                ChatMessageBean chatMessageBean1 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean1.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean1.getData().getName();

                iv_header_radioda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAttentionClicker.onClicker(chatMessageBean1.getData().getUid());
                    }
                });
                tv_nickname_radioda.setText(userName);
                tv_show_radioda.setText(chatMessageBean1.getData().getMessageShow());

                iv_grade_allmsg.setImageResource(ImageShowUtils.getGrade(chatMessageBean1.getData().getGrade()));

                iv_charm_allmsg.setImageResource(ImageShowUtils.getCharm(chatMessageBean1.getData().charm));
                ImageUtils.loadUri(iv_header_radioda, chatMessageBean1.getData().avatar);


                break;
            case 101://礼物消息
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                GiftSendMessage giftSendMessage = new Gson().fromJson(item, GiftSendMessage.class);
//                String[] idsShow = giftSendMessage.getData().getSendId().split(",");
                String[] namesShow = giftSendMessage.getData().getNames().split(",");
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(giftSendMessage.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = giftSendMessage.getData().getName();
                if (userName.length() > 6) {
                    userName = userName.substring(0, 6) + "...";
                }
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(giftSendMessage.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" 送给 ");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.xt_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 4,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                String nameStr;
                if (namesShow.length > 1) {
                    nameStr = namesShow.length + "位嘉宾";

                } else {
                    nameStr = namesShow[0];
                    if (nameStr.length() > 6) {
                        nameStr = nameStr.substring(0, 6) + "...";
                    }
                    clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (onAttentionClicker != null) {
                                    onAttentionClicker.onClicker(Integer.valueOf(giftSendMessage.getData().getSendId()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtils.e("点击失效,id为空");
                            }
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                            /**Remove the underline**/
                            ds.setUnderlineText(false);
                        }
                    };

                }

                stringBuilder.append(nameStr);
                stringBuilder.setSpan(clickableSpan, stringBuilder.length() - nameStr.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - nameStr.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//                for (int i = 0; i < namesShow.length; i++) {
//                    String othername = namesShow[i];
//                    stringBuilder.append(othername);
//                    colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
//                    stringBuilder.setSpan(colorSpan, stringBuilder.length() - othername.length(),
//                            stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    if (i < namesShow.length - 1) {
//                        stringBuilder.append(",");
//                    }
//                }
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());

                String urlImg = giftSendMessage.getData().getImg();
                iv_show_chat.setVisibility(View.VISIBLE);
                tv_end_chat.setVisibility(View.VISIBLE);
                ImageUtils.loadUri(iv_show_chat, urlImg);
                tv_end_chat.setText(" x" + giftSendMessage.getData().getNum());

                break;
            case 102://系统消息
                messageShow = (String) messageBean.getData();
                tv_show_chat.setText(messageShow);
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                break;
            case 103:
                messageShow = (String) messageBean.getData();
                tv_show_chat.setText(messageShow);
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                break;
            case 104:
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                LogUtils.e("msg", item);
                ChatMessageBean chatMessageBean2 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean2.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean2.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean2.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append("：");
                int nowLenght = stringBuilder.length();
                stringBuilder.append(chatMessageBean2.getData().getMessageShow());
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FF3333));
                stringBuilder.setSpan(colorSpan, nowLenght,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 105://设置管理员
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean3 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean3.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean3.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean3.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (chatMessageBean3.getData().getState() == 1) {
                    stringBuilder.append("被设置为管理员");
                } else if (chatMessageBean3.getData().getState() == 2) {
                    stringBuilder.append("被移除管理员");
                }
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 106:
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean4 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
                userName = chatMessageBean4.getData().getName();
                if (userName != null){
                    stringBuilder.append(userName);
                    colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
                    stringBuilder.setSpan(colorSpan, 0,
                            stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                stringBuilder.append("等级提升为 ");
                stringBuilder.append("img");
                Bitmap bitmap = ImageShowUtils.getGradeBitmap(mContext,chatMessageBean4.getData().getGrade());
                imageSpan = new ImageSpan(mContext, bitmap);
                stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean4.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 107://踢出房间
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.FFE795));
                GetOutBean getOutBean = new Gson().fromJson(item, GetOutBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(getOutBean.getData().getBgrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(getOutBean.getData().getBname());
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(getOutBean.getData().getBuid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" 被 ");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.white));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 3,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (getOutBean.getData().getState() == 1) {
                    stringBuilder.append("房主");
                } else if (getOutBean.getData().getState() == 2) {
                    stringBuilder.append("管理员");
                }
                stringBuilder.append(" 踢出房间");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.white));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 4,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 108:// 表情消息
                EmojiMessageBean emojiMessageBean = new Gson().fromJson(item, EmojiMessageBean.class);
                userName = emojiMessageBean.getData().getName();



                iv_grade_allmsg.setImageResource(ImageShowUtils.getGrade(emojiMessageBean.getData().getGrade()));
                iv_charm_allmsg.setImageResource(ImageShowUtils.getCharm(emojiMessageBean.getData().charm));
                ImageUtils.loadUri(iv_header_radioda, emojiMessageBean.getData().avatar);
                tv_nickname_radioda.setText(userName);
                iv_header_radioda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAttentionClicker.onClicker(emojiMessageBean.getData().getUid());

                    }
                });

                int emojiCode = emojiMessageBean.getData().getEmojiCode();

                if (emojiCode >= 128564) {
                    if (emojiCode == 128629 || emojiCode == 128630){ //随机数
                        ArrayList<Integer> randomNumbers= emojiMessageBean.getData().getRandomNumbers();
                        ivNumber1.setImageResource(ImageShowUtils.getInstans().getNumberResId(randomNumbers.get(0)));
                        ivNumber2.setImageResource(ImageShowUtils.getInstans().getNumberResId(randomNumbers.get(1)));
                        ivNumber3.setImageResource(ImageShowUtils.getInstans().getNumberResId(randomNumbers.get(2)));
                    }else {
                        iv_show_chat.setVisibility(View.VISIBLE);
                        ImageUtils.loadDrawableStatic(iv_emoji, ImageShowUtils.getInstans().getResId(emojiCode, emojiMessageBean.getData().getNumberShow()));
                    }

                } else if (emojiCode >= 128552) { //特殊表情
                    stringBuilder.append("img");
                    imageSpan = new ImageSpan(mContext, ImageShowUtils.getInstans().getResId(emojiCode, emojiMessageBean.getData().getNumberShow()));
                    stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    String emoji = new String(Character.toChars(emojiMessageBean.getData().getEmojiCode()));
                    stringBuilder.append(emoji);
                }
                break;
            case 109:
                stringBuilder = new SpannableStringBuilder();
                ChatMessageBean chatMessageBean5 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder.append(chatMessageBean5.getData().getMessageShow());
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.C11FFD8));
                stringBuilder.setSpan(colorSpan, 0,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append("img");
                imageSpan = new ImageSpan(mContext, R.drawable.attention);
                stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onFollowUser(chatMessageBean5.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, stringBuilder.length() - 3, stringBuilder.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 110://下麦(上麦)
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean6 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean6.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean6.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean6.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (chatMessageBean6.getData().getMessageShow() != null) {
                    stringBuilder.append(" ").append(chatMessageBean6.getData().getMessageShow());
                }
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 111://禁麦
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean111 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();

                if (chatMessageBean111.getData().getMessageShow() != null) {
                    stringBuilder.append(chatMessageBean111.getData().getMessageShow());
                    colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                    stringBuilder.setSpan(colorSpan, 0,
                            stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tv_show_chat.setText(stringBuilder);
                break;
            case 112://加入黑名单
                ChatMessageBean chatMessageBean112 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
                stringBuilder.append("img ");
                resShowid = ImageShowUtils.getGrade(chatMessageBean112.getData().getGrade());
                imageSpan = new ImageSpan(mContext, resShowid);
                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = chatMessageBean112.getData().getName();
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean112.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" 被 ");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.white));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 3,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                stringBuilder.append("加入黑名单");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.white));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 4,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 114://抱他上麦
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean114 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();

                if (chatMessageBean114.getData().getMessageShow() != null) {
                    stringBuilder.append(chatMessageBean114.getData().getMessageShow());
                    colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                    stringBuilder.setSpan(colorSpan, 0,
                            stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tv_show_chat.setText(stringBuilder);
                break;
            case 115://全频道通知消息(寻宝？)
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean115 = new Gson().fromJson(item, ChatMessageBean.class);

                stringBuilder = new SpannableStringBuilder();

                stringBuilder.append("恭喜 ");
//                stringBuilder.append("img ");
//                imageSpan = new ImageSpan(mContext, R.drawable.gifts);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String nickName = chatMessageBean115.getData().getNickname();
                stringBuilder.append(nickName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - nickName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean115.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" ");

                stringBuilder.append(chatMessageBean115.getData().getMessageShow());
//                stringBuilder.append("  ");
//                stringBuilder.append("点击这里快速前往");
//                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FF3333));
//                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 8,
//                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                String roomId = chatMessageBean115.getData().getRid();
//                ClickableSpan clickableSpan1 = new ClickableSpan() {
//                    @Override
//                    public void onClick(View widget) {
//                        if (onAttentionClicker != null) {
//                            onAttentionClicker.onOtherRoomClicker(roomId);
//                        }
//                    }
//
//                    @Override
//                    public void updateDrawState(TextPaint ds) {
////                        super.updateDrawState(ds);
//                        /**Remove the underline**/
//                        ds.setUnderlineText(false);
//                    }
//                };
//
//                stringBuilder.setSpan(clickableSpan1, stringBuilder.length() - 8, stringBuilder.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 116://座驾消息
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                CarShowMessageBean carShowMessageBean = new Gson().fromJson(item, CarShowMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img ");
//                resShowid = ImageShowUtils.getGrade(carShowMessageBean.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                userName = carShowMessageBean.getData().getUserName();
                if (userName.length() > 6) {
                    userName = userName.substring(0, 6) + "...";
                }
                stringBuilder.append(userName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - userName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(carShowMessageBean.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" 驾着 ");
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.xt_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - 4,
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tv_show_chat.setText(stringBuilder);

                String carCover = carShowMessageBean.getData().getCarCover();
                iv_show_chat.setVisibility(View.VISIBLE);
                tv_end_chat.setVisibility(View.VISIBLE);
                ImageUtils.loadUri(iv_show_chat, carCover);
                tv_end_chat.setText(" 进入房间");
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 117://117 进房通知消息（1级及以上）
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean117 = new Gson().fromJson(item, ChatMessageBean.class);
                stringBuilder = new SpannableStringBuilder();
//                stringBuilder.append("img");
//                resShowid = ImageShowUtils.getGrade(chatMessageBean117.getData().getGrade());
//                imageSpan = new ImageSpan(mContext, resShowid);
//                stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String accessName = chatMessageBean117.getData().getName();
                stringBuilder.append(accessName);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.jrfj_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - accessName.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean117.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(" ");
                stringBuilder.append("进入房间");

                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case 122://全频道通知消息(寻宝)
                tv_show_chat.setTextColor(ContextCompat.getColor(mContext, R.color.xt_color));
                ChatMessageBean chatMessageBean122 = new Gson().fromJson(item, ChatMessageBean.class);

                stringBuilder = new SpannableStringBuilder();
                stringBuilder.append("恭喜  ");

                String nickName1 = chatMessageBean122.getData().getNickname();
                stringBuilder.append(nickName1);
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.egg_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - nickName1.length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        if (onAttentionClicker != null) {
                            onAttentionClicker.onClicker(chatMessageBean122.getData().getUid());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
//                        super.updateDrawState(ds);
                        /**Remove the underline**/
                        ds.setUnderlineText(false);
                    }
                };
                stringBuilder.setSpan(clickableSpan, 0, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                stringBuilder.append(" 寻宝获得了 ");

                stringBuilder.append(chatMessageBean122.getData().getMessageShow());
                colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.egg_color));
                stringBuilder.setSpan(colorSpan, stringBuilder.length() - chatMessageBean122.getData().getMessageShow().length(),
                        stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tv_show_chat.setText(stringBuilder);
                tv_show_chat.setMovementMethod(LinkMovementMethod.getInstance());
                if (chatMessageBean122.getData().getCost() <= 520) {
                    rl_back_voice.setBackgroundResource(R.drawable.bg_egg_3);
                } else if (chatMessageBean122.getData().getCost() <= 4999) {
                    rl_back_voice.setBackgroundResource(R.drawable.bg_egg_2);
                } else {
                    rl_back_voice.setBackgroundResource(R.drawable.bg_egg_1);
                }

                break;
        }

    }


//    else if (emojiCode == 128553) { //爆灯
//        stringBuilder.append("img");
//        imageSpan = new ImageSpan(mContext, R.drawable.deng_1);
//        stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    } else if (emojiCode == 128554) { //举手
//        stringBuilder.append("img");
//        imageSpan = new ImageSpan(mContext, R.drawable.hand_4);
//        stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    }


}
