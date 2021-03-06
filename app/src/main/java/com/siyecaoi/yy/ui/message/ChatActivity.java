package com.siyecaoi.yy.ui.message;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.dialog.MyBottomPersonDialog;
import com.siyecaoi.yy.dialog.MyBottomShowDialog;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.model.CustomOneModel;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.message.fragment.GiftDialogFragment;
import com.siyecaoi.yy.ui.mine.PersonHomeActivity;
import com.siyecaoi.yy.ui.other.SelectPhotoActivity;
import com.siyecaoi.yy.ui.room.TopupActivity;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.component.NoticeLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.face.FaceManager;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.inputmore.InputMoreActionUnit;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.activitys.SelectPhotoDialog;
import io.reactivex.functions.Consumer;


/**
 * ????????????
 */
public class ChatActivity extends MyBaseActivity {

    String chatId;
    String userName;
    //    MyGiftDialog giftDialog;
    MyBottomPersonDialog myBottomPersonDialog;

    MyBottomShowDialog myBottomShowDialog;
    GiftDialogFragment giftDialogFragment;
    boolean isRoom;
    @BindView(R.id.chat_layout)
    ChatLayout chatLayout;
    private ChatInfo mChatInfo;
    private ArrayList<String> bottomList;//??????????????????

    @Override
    public void initData() {
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void initView() {
        chatId = getBundleString(Const.ShowIntent.ID);
        userName = getBundleString(Const.ShowIntent.NAME);
        isRoom = getBundleBoolean("isRoom", false);
        mChatInfo = (ChatInfo) getBundleSerializable(Const.ShowIntent.CHAT_INFO);

        setTitleText(userName);
        setRightImg(getResources().getDrawable(R.drawable.icon_msg_more));

        setView();
//        getUserData();
        bottomList = new ArrayList<>();
        bottomList.add(getString(R.string.tv_ju_data));
        bottomList.add(getString(R.string.tv_hei_data));
        bottomList.add(getString(R.string.tv_cancel));



        tv_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMybottomDialog(bottomList);
            }
        });
    }


    private void showMybottomDialog(ArrayList<String> bottomList) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(ChatActivity.this, bottomList);
        myBottomShowDialog.show();
        BottomShowRecyclerAdapter adapter = myBottomShowDialog.getAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                    myBottomShowDialog.dismiss();
                }
                switch (position) {
                    case 0:
                        ArrayList<String> bottomListJu = new ArrayList<>();
                        bottomListJu.add(getString(R.string.tv_sensitivity_report));
                        bottomListJu.add(getString(R.string.tv_se_report));
                        bottomListJu.add(getString(R.string.tv_ad_report));
                        bottomListJu.add(getString(R.string.tv_at_report));
                        bottomListJu.add(getString(R.string.tv_cancel));
                        showMybottomDialogJu(bottomListJu);
                        break;
                    case 1:
                        showMyDialog(Const.IntShow.TWO, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (myDialog != null && myDialog.isShowing()) {
                                    myDialog.dismiss();
                                }
                                getHeiCall();
                            }
                        });

                        break;
                }
            }
        });
    }


    /**
     * ???????????????
     */
    private void getHeiCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", chatId);
        HttpManager.getInstance().post(Api.getAddUserblock, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("???????????????????????????????????????Ta???????????????");
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void showMybottomDialogJu(ArrayList<String> bottomList) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(ChatActivity.this, bottomList);
        myBottomShowDialog.show();
        BottomShowRecyclerAdapter adapter = myBottomShowDialog.getAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                    myBottomShowDialog.dismiss();
                }
                switch (position) {
                    case 4:

                        break;
                    default:
                        String updateString = (String) adapter.getItem(position);
                        updateCall(updateString);
                        break;
                }
            }
        });
    }

    //?????????
    private void updateCall(String updateString) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", Const.IntShow.ONE);
        map.put("buid", chatId);
        map.put("content", updateString);
        HttpManager.getInstance().post(Api.ReportSave, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("??????????????????????????????????????????");
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }


//    private void getUserData() {
//
//        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
//            @Override
//            public void onError(int i, String s) {
//                LogUtils.e(LogUtils.TAG, i + "  " + s);
//            }
//
//            @Override
//            public void onSuccess(TIMUserProfile timUserProfile) {
//                LogUtils.e(LogUtils.TAG, timUserProfile.getNickName() + " " + timUserProfile.getFaceUrl());
////                chatPanel.setSelfImg(timUserProfile.getFaceUrl());
//            }
//        });
//
//        //????????????????????????????????????
//        List<String> users = new ArrayList<String>();
//        users.add(chatId);
//        //??????????????????
//        TIMFriendshipManager.getInstance().getUsersProfile(users, false, new TIMValueCallBack<List<TIMUserProfile>>() {
//            @Override
//            public void onError(int code, String desc) {
//                //????????? code ??????????????? desc????????????????????????????????????
//                //????????? code ???????????????????????????
//                LogUtils.e(LogUtils.TAG, "getUsersProfile failed: " + code + " desc");
//            }
//
//            @Override
//            public void onSuccess(List<TIMUserProfile> result) {
//                LogUtils.e(LogUtils.TAG, "getUsersProfile succ");
//                for (TIMUserProfile res : result) {
//                    if (res.getIdentifier().equals(chatId)) {
//                        userName = res.getNickName();
//                        setTitleText(userName);
////                        chatPanel.setOtherImg(res.getFaceUrl());
//                    }
//                }
//            }
//        });
//    }

    private void setView() {

        // ????????????????????? UI ??????????????????
        chatLayout.initDefault();

        // ?????? ChatInfo ????????????????????????????????????????????????????????????????????????????????????
        chatLayout.setChatInfo(mChatInfo);

        //??????????????????????????????
        TitleBarLayout mTitleBar = chatLayout.getTitleBar();
        mTitleBar.setVisibility(View.GONE);

        // ??? ChatLayout ????????? NoticeLayout
        NoticeLayout noticeLayout = chatLayout.getNoticeLayout();
        noticeLayout.setVisibility(View.GONE);


        // ??? ChatLayout ????????? MessageLayout
        MessageLayout messageLayout = chatLayout.getMessageLayout();
        ////// ???????????? //////
        messageLayout.setLeftChatContentFontColor(Color.parseColor("#000103"));
        messageLayout.setRightChatContentFontColor(Color.parseColor("#FFFFFF"));
        messageLayout.setChatContextFontSize(12);
        // ?????????????????????????????????????????????????????????
        messageLayout.setAvatarRadius(24);
        // ??????????????????
        messageLayout.setAvatarSize(new int[]{48, 48});
        // ?????????????????????????????????
//        messageLayout.setRightBubble(getResources().getDrawable(R.drawable.frame2));
        // ?????????????????????????????????
//        messageLayout.setLeftBubble(getResources().getDrawable(R.drawable.frame1));

        /**
         * ???????????????????????????????????????
         */
        messageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //??????adapter??????????????????????????????????????????1
                chatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
                if (null == messageInfo || null == messageInfo.getTIMMessage()) {
                    return;
                }
                Bundle bundle = new Bundle();
                if (messageInfo.isSelf()) {
                    bundle.putInt(Const.ShowIntent.ID, userToken);
                    ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
                } else {
                    bundle.putInt(Const.ShowIntent.ID, Integer.parseInt(messageInfo.getTIMMessage().getSender()));
                    ActivityCollector.getActivityCollector().finishActivity(OtherHomeActivity.class);
                    ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
                }
            }
        });

        messageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw());

        // ??? ChatLayout ????????? InputLayout
        InputLayout inputLayout = chatLayout.getInputLayout();
        // ?????????????????????
        inputLayout.disableCaptureAction(true);
        // ??????????????????
        inputLayout.disableSendFileAction(true);
        // ??????????????????
        inputLayout.disableSendPhotoAction(true);
        // ?????????????????????
        inputLayout.disableVideoRecordAction(true);

        inputLayout.setBackgroundColor(getResources().getColor(R.color.white));

        // ????????????????????????
        InputMoreActionUnit unit = new InputMoreActionUnit();
        unit.setIconResId(R.drawable.image); // ?????????????????????
        unit.setTitleId(R.string.action_nothing); // ???????????????????????????
        unit.setOnClickListener(new View.OnClickListener() { // ??????????????????
            @Override
            public void onClick(View v) {
                openSelectPhoto();
            }
        });
        if (!isRoom) {
            // ??????????????????????????????????????????
            inputLayout.addAction(unit);
        }else {
            inputLayout.disableMoreInput(true);
        }

        // ????????????????????????
        InputMoreActionUnit unit1 = new InputMoreActionUnit();
        unit1.setIconResId(R.drawable.gift); // ?????????????????????
        unit1.setTitleId(R.string.action_nothing); // ???????????????????????????
        unit1.setOnClickListener(new View.OnClickListener() { // ??????????????????
            @Override
            public void onClick(View v) {
                showGiftDialog();
            }
        });
        // ??????????????????????????????????????????
//        inputLayout.addAction(unit1);


        //?????????????????????UI??????????????????
//        chatPanel.initDefault();
//        chatPanel.setRoom(isRoom);
        /*
         * ??????????????????ID?????????????????????identify??????????????????IMSDK??????????????????????????????????????????????????????SessionClickListener????????????????????????SessionInfo????????????????????????????????????ID???????????????????????????????????????????????????ID???
         * ????????????????????????????????????????????????????????????????????????????????????????????????????????????ID?????????
         */
//        chatPanel.setBaseChatId(chatId);
        //??????????????????????????????
//        PageTitleBar chatTitleBar = chatPanel.getTitleBar();
//        chatTitleBar.setVisibility(View.GONE);

//        /**
//         *?????????????????????
//         */
//        chatPanel.setOnMyViewClicker(new ChatPanel.OnMyViewClicker() {
//            @Override
//            public void giftOnclicker() {
////                showMyGiftDialog(userToken, Integer.parseInt(chatId), Const.IntShow.THREE, userName);
//                showGiftDialog();
//            }
//
//            /**
//             * ??????????????????
//             * @param messageInfo
//             */
//            @Override
//            public void onUserIconClicker(MessageInfo messageInfo) {
//                Bundle bundle = new Bundle();
//                if (messageInfo.isSelf()) {
//                    bundle.putInt(Const.ShowIntent.ID, userToken);
//                    ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
//                } else {
//                    bundle.putInt(Const.ShowIntent.ID, Integer.parseInt(messageInfo.getPeer()));
//                    ActivityCollector.getActivityCollector().finishActivity(OtherHomeActivity.class);
//                    ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
//                }
//            }
//
//            @Override
//            public void onShareClicker(String roomId) {
//                if (Const.RoomId.equals(roomId)) {
//                    showToast("?????????????????????");
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Const.ShowIntent.ROOMID, roomId);
//                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
//                    ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                }
//            }
//
//            @Override
//            public void onCopyStr(String copyStr) {
//                MyUtils.getInstans().copyStr(ChatActivity.this, copyStr);
//                showToast("????????????");
//            }
//        });
    }

    public class CustomMessageDraw implements IOnCustomMessageDrawListener {

        @Override
        public void onDraw(ICustomMessageViewGroup parent, MessageInfo info) {
            // ???????????????????????????json??????
            TIMCustomElem elem = (TIMCustomElem) info.getTIMMessage().getElement(0);
            // ????????????json????????????????????????bean??????
            try {
                JSONObject jsonObject = new JSONObject(new String(elem.getData(), "UTF-8"));
                int state = jsonObject.getInt("state");
                String showMsg = jsonObject.getString("showMsg");
                String showUrl = jsonObject.getString("showUrl");
                if (state == 1) { //????????????
                    // ??????????????????view?????????TUIKit?????????????????????
                    View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_adapter_share, null, false);
                    parent.addMessageContentView(view);

                    final String roomId = jsonObject.getString("roomId");
                    TextView tv_user_msg = view.findViewById(R.id.tv_user_msg);
                    TextView tv_room_msg = view.findViewById(R.id.tv_room_msg);
                    ImageView iv_meg_data_gropu = view.findViewById(R.id.iv_meg_data_gropu);
                    LinearLayout ll_share_data_group = view.findViewById(R.id.ll_share_data_group);

                    Glide.with(TUIKit.getAppContext()).load(showUrl).into(iv_meg_data_gropu);
                    FaceManager.handlerEmojiText(tv_user_msg, showMsg);
                    if (info.isSelf()) {
                        tv_user_msg.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.white));
                        tv_room_msg.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.white));
                    } else {
                        tv_user_msg.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.black));
                        tv_room_msg.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.text_ff0));
                    }
                    ll_share_data_group.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Const.RoomId.equals(roomId)) {
                                showToast("?????????????????????");
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                                ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                            }
                        }
                    });

                } else if (state == 2) { //????????????
                    // ??????????????????view?????????TUIKit?????????????????????
                    View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_adapter_image, null, false);
                    parent.addMessageContentView(view);

                    TextView tv_gift_data_group = view.findViewById(R.id.tv_gift_data_group);
                    ImageView iv_gift_data_gropu = view.findViewById(R.id.iv_gift_data_gropu);

                    Glide.with(TUIKit.getAppContext()).load(showUrl).into(iv_gift_data_gropu);
                    tv_gift_data_group.setText(showMsg);
                    if (info.isSelf()) {
                        tv_gift_data_group.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.white));
                    } else {
                        tv_gift_data_group.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.black));
                    }

                } else if (state == 3 || state == 4) {

                }
            } catch (Exception e) {
                LogUtils.e("?????????????????????");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Const.RequestCode.SELECTPHOTO_CODE:
                    if (data != null) {
                        String filePath = data.getStringExtra(SelectPhotoDialog.DATA);
                        MessageInfo info = MessageInfoUtil.buildImageMessage(Uri.fromFile(new File(filePath)), true);
                        chatLayout.sendMessage(info, false);
//                        if (chatLayout.getInputLayout().getmMessageHandler() != null) {
//                            chatLayout.getInputLayout().getmMessageHandler().sendMessage(info);
//                            chatLayout.getInputLayout().hideSoftInput();
//                        }
                    }
                    break;
            }
        }
    }

    @SuppressLint("CheckResult")
    private void openSelectPhoto() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ActivityCollector.getActivityCollector()
                                    .toOtherActivity(SelectPhotoActivity.class, Const.RequestCode.SELECTPHOTO_CODE);
                        } else {
                            showToast("??????????????????????????????????????????");
                        }
                    }
                });
    }

    private void showGiftDialog() {
        giftDialogFragment = new GiftDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.User.USER_TOKEN, userToken);
        bundle.putInt(Const.ShowIntent.OTHRE_ID, Integer.parseInt(chatId));
        bundle.putString(Const.ShowIntent.NAME, userName);
        giftDialogFragment.setArguments(bundle);
        giftDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        giftDialogFragment.show(getSupportFragmentManager(), "giftDialogFragment");

        giftDialogFragment.setSendGift(new GiftDialogFragment.SendGift() {
            @Override
            public void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold) {
                int userGold = (int) SharedPreferenceUtils.get(ChatActivity.this, Const.User.GOLD, 0);
                if (userGold < goodGold * num * sum) {
                    showMyDialog();
                } else {
                    getGiftCall(ids, gid, img, showImg, num, sum);
                }
            }

            @Override
            public void onUserClicker() {
                if (giftDialogFragment != null) {
                    giftDialogFragment.dismiss();
                    int sendId = giftDialogFragment.getOtherId();
                    showMyPseronDialog(userToken, sendId);
                }
            }
        });
    }


    /**
     * ????????????
     *
     * @param userId
     * @param otherId  //??????????????????id
     * @param type     //1?????????  2pk?????????  3???????????????
     * @param userName ???????????????????????????
     */
//    private void showMyGiftDialog(int userId, int otherId, int type, String userName) {
//        if (giftDialog != null && giftDialog.isShowing()) {
//            giftDialog.dismiss();
//        }
//
//        giftDialog = new MyGiftDialog(ChatActivity.this, userId, otherId, type, userName, Const.IntShow.THREE);
//        giftDialog.show();
//        giftDialog.setDataShow(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int sendId = giftDialog.getOtherId();
//                if (giftDialog != null && giftDialog.isShowing()) {
//                    giftDialog.dismiss();
//                }
//                showMyPseronDialog(userId, sendId);
//            }
//        });
//        giftDialog.setSendGift(new MyGiftDialog.SendGift() {
//            @Override
//            public void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold) {
//                int userGold = (int) SharedPreferenceUtils.get(ChatActivity.this, Const.User.GOLD, 0);
//                if (userGold < goodGold * num * sum) {
//                    showMyDialog();
//                } else {
//                    getGiftCall(ids, gid, img, showImg, num, sum);
//                }
//            }
//        });
//    }

    MyDialog myDialog;


    private void showMyDialog(int showType, View.OnClickListener onClickListener) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(ChatActivity.this);
        myDialog.show();
        if (showType == 2) {
            myDialog.setHintText("????????????????????? ????????????????????????????????????");
        }
        myDialog.setRightButton(onClickListener);
    }



    private void showMyDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(ChatActivity.this);
        myDialog.show();
        myDialog.setHintText(getString(R.string.hint_nogold_gift));
        myDialog.setRightText(getString(R.string.tv_topup_packet));
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
            }
        });
    }

    private void getGiftCall(String ids, int gid, String img, String showImg, int num, int sum) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("ids", ids);
        map.put("gid", gid);
        map.put("num", num);
        map.put("sum", sum);
        HttpManager.getInstance().post(Api.userSaveGift, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    sendMessageShow(num, img, showImg);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void sendMessageShow(int num, String img, String showImg) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //?????????????????????
                chatId);

        CustomOneModel customOneModel = new CustomOneModel();
        customOneModel.setShowMsg("??" + num);
        customOneModel.setShowImg(showImg);
        customOneModel.setShowUrl(img);
        customOneModel.setState(Const.IntShow.TWO);
        //??????????????????
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("[??????]");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        String msgShow = JSON.toJSONString(customOneModel);
        //??? TIMMessage ????????????????????????
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgShow.getBytes());      //????????? byte[]
        elem.setDesc("[??????]"); //?????????????????????
        //??? elem ???????????????
        if (msg.addElement(elem) != 0) {
            LogUtils.d(LogUtils.TAG, "addElement failed");
            return;
        }

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTIMMessage(msg);
        messageInfo.setSelf(true);
        messageInfo.setMsgTime(System.currentTimeMillis());
        messageInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        messageInfo.setFromUser(TIMManager.getInstance().getLoginUser());
        chatLayout.sendMessage(messageInfo, false);

    }

    /**
     * ????????????
     *
     * @param userId  ??????id
     * @param otherId ????????????id
     */
    private void showMyPseronDialog(int userId, int otherId) {
        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
            myBottomPersonDialog.dismiss();
        }
        myBottomPersonDialog = new MyBottomPersonDialog(ChatActivity.this, userId, otherId);
        myBottomPersonDialog.show();

    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    public void onPause() {
        super.onPause();
        AudioPlayer.getInstance().stopPlayRecord();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        //??????Activity???????????????????????????
//        C2CChatManager.getInstance().destroyC2CChat();
        chatLayout.exitChat();
        super.onDestroy();
    }
}