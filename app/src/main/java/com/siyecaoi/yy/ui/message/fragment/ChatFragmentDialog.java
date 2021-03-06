package com.siyecaoi.yy.ui.message.fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.dialog.MyBottomPersonDialog;
import com.siyecaoi.yy.dialog.MyBottomShowDialog;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.model.CustomOneModel;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.TopupActivity;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.face.FaceManager;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class ChatFragmentDialog extends DialogFragment {
    Unbinder unbinder;

    String chatId;
    String userName;
    //    MyGiftDialog giftDialog;
    MyBottomPersonDialog myBottomPersonDialog;

    MyBottomShowDialog myBottomShowDialog;
    GiftDialogFragment giftDialogFragment;
    boolean isRoom = true;
    private ChatInfo mChatInfo;
    private ArrayList<String> bottomList;//??????????????????
    protected int userToken;

    View rootView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
//    @BindView(R.id.chat_layout)
//    ChatLayout chatLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chat, container);
        unbinder = ButterKnife.bind(this, view);
        rootView = view.getRootView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        assert bundle != null;
        chatId = bundle.getString(Const.ShowIntent.ID);
        userName = bundle.getString(Const.ShowIntent.NAME);
        mChatInfo = (ChatInfo) bundle.getSerializable(Const.ShowIntent.CHAT_INFO);
        userToken = (int) SharedPreferenceUtils.get(getActivity(), Const.User.USER_TOKEN, 0);
        tvTitle.setText(userName);
//        setView();
    }

//    private void setView() {
//
//        // ????????????????????? UI ??????????????????
//        chatLayout.initDefault();
//
//        // ?????? ChatInfo ????????????????????????????????????????????????????????????????????????????????????
//        chatLayout.setChatInfo(mChatInfo);
//
//        //??????????????????????????????
//        TitleBarLayout mTitleBar = chatLayout.getTitleBar();
//        mTitleBar.setVisibility(View.GONE);
//
//        // ??? ChatLayout ????????? NoticeLayout
//        NoticeLayout noticeLayout = chatLayout.getNoticeLayout();
//        noticeLayout.setVisibility(View.GONE);
//
//
//        // ??? ChatLayout ????????? MessageLayout
//        MessageLayout messageLayout = chatLayout.getMessageLayout();
//        ////// ???????????? //////
//        messageLayout.setLeftChatContentFontColor(Color.parseColor("#000103"));
//        messageLayout.setRightChatContentFontColor(Color.parseColor("#FFFFFF"));
//        messageLayout.setChatContextFontSize(12);
//        // ?????????????????????????????????????????????????????????
//        messageLayout.setAvatarRadius(24);
//        // ??????????????????
//        messageLayout.setAvatarSize(new int[]{48, 48});
//        // ?????????????????????????????????
////        messageLayout.setRightBubble(getResources().getDrawable(R.drawable.frame2));
//        // ?????????????????????????????????
////        messageLayout.setLeftBubble(getResources().getDrawable(R.drawable.frame1));
//
//        /**
//         * ???????????????????????????????????????
//         */
//        messageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {
//            @Override
//            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
//                //??????adapter??????????????????????????????????????????1
//                chatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
//            }
//
//            @Override
//            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
//                if (null == messageInfo || null == messageInfo.getTIMMessage()) {
//                    return;
//                }
//                Bundle bundle = new Bundle();
//                if (messageInfo.isSelf()) {
//                    bundle.putInt(Const.ShowIntent.ID, userToken);
//                    ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
//                } else {
//                    bundle.putInt(Const.ShowIntent.ID, Integer.parseInt(messageInfo.getTIMMessage().getSender()));
//                    ActivityCollector.getActivityCollector().finishActivity(OtherHomeActivity.class);
//                    ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
//                }
//            }
//        });
//
//        messageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw());
//
//        // ??? ChatLayout ????????? InputLayout
//        InputLayout inputLayout = chatLayout.getInputLayout();
//        // ?????????????????????
//        inputLayout.disableCaptureAction(true);
//        // ??????????????????
//        inputLayout.disableSendFileAction(true);
//        // ??????????????????
//        inputLayout.disableSendPhotoAction(true);
//        // ?????????????????????
//        inputLayout.disableVideoRecordAction(true);
//
//        inputLayout.setBackgroundColor(getResources().getColor(R.color.white));
//
//        // ????????????????????????
//        InputMoreActionUnit unit = new InputMoreActionUnit();
//        unit.setIconResId(R.drawable.image); // ?????????????????????
//        unit.setTitleId(R.string.action_nothing); // ???????????????????????????
//        unit.setOnClickListener(new View.OnClickListener() { // ??????????????????
//            @Override
//            public void onClick(View v) {
////                openSelectPhoto();
//            }
//        });
//        if (!isRoom) {
//            // ??????????????????????????????????????????
//            inputLayout.addAction(unit);
//        }
//
//        // ????????????????????????
//        InputMoreActionUnit unit1 = new InputMoreActionUnit();
//        unit1.setIconResId(R.drawable.gift); // ?????????????????????
//        unit1.setTitleId(R.string.action_nothing); // ???????????????????????????
//        unit1.setOnClickListener(new View.OnClickListener() { // ??????????????????
//            @Override
//            public void onClick(View v) {
//                showGiftDialog();
//            }
//        });
//        // ??????????????????????????????????????????
//        inputLayout.addAction(unit1);
//
//    }

    private void showGiftDialog() {
        giftDialogFragment = new GiftDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.User.USER_TOKEN, userToken);
        bundle.putInt(Const.ShowIntent.OTHRE_ID, Integer.parseInt(chatId));
        bundle.putString(Const.ShowIntent.NAME, userName);
        giftDialogFragment.setArguments(bundle);
        giftDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        giftDialogFragment.show(getChildFragmentManager(), "giftDialogFragment");

        giftDialogFragment.setSendGift(new GiftDialogFragment.SendGift() {
            @Override
            public void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold) {
                int userGold = (int) SharedPreferenceUtils.get(getActivity(), Const.User.GOLD, 0);
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
     * @param userId  ??????id
     * @param otherId ????????????id
     */
    private void showMyPseronDialog(int userId, int otherId) {
        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
            myBottomPersonDialog.dismiss();
        }
        myBottomPersonDialog = new MyBottomPersonDialog(getActivity(), userId, otherId);
        myBottomPersonDialog.show();

    }

    MyDialog myDialog;

    private void showMyDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(getActivity());
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
        HttpManager.getInstance().post(Api.userSaveGift, map, new MyObserver(getActivity()) {
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
//        chatLayout.sendMessage(messageInfo, false);

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
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.chat_adapter_share, null, false);
                    parent.addMessageContentView(view);

                    final String roomId = jsonObject.getString("roomId");
                    TextView tv_user_msg = view.findViewById(R.id.tv_user_msg);
                    TextView tv_room_msg = view.findViewById(R.id.tv_room_msg);
                    ImageView iv_meg_data_gropu = view.findViewById(R.id.iv_meg_data_gropu);
                    LinearLayout ll_share_data_group = view.findViewById(R.id.ll_share_data_group);

                    Glide.with(TUIKit.getAppContext()).load(showUrl).into(iv_meg_data_gropu);
                    FaceManager.handlerEmojiText(tv_user_msg, showMsg);
                    if (info.isSelf()) {
                        tv_user_msg.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        tv_room_msg.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    } else {
                        tv_user_msg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        tv_room_msg.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_ff0));
                    }
                    ll_share_data_group.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Const.RoomId.equals(roomId)) {
//                                showToast("?????????????????????");
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
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.chat_adapter_image, null, false);
                    parent.addMessageContentView(view);

                    TextView tv_gift_data_group = view.findViewById(R.id.tv_gift_data_group);
                    ImageView iv_gift_data_gropu = view.findViewById(R.id.iv_gift_data_gropu);

                    Glide.with(TUIKit.getAppContext()).load(showUrl).into(iv_gift_data_gropu);
                    tv_gift_data_group.setText(showMsg);
                    if (info.isSelf()) {
                        tv_gift_data_group.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    } else {
                        tv_gift_data_group.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.view_message_d, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_message_d:
            case R.id.iv_back:
                dismissAllowingStateLoss();
                break;
        }
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        super.onResume();
    }
}
