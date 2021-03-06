package com.siyecaoi.yy.ui.room;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.opensource.svgaplayer.SVGACallback;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.agora.AGEventHandler;
import com.siyecaoi.yy.agora.YySignaling;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.base.MyBaseMVPActivity;
import com.siyecaoi.yy.bean.AllmsgBean;
import com.siyecaoi.yy.bean.AlltopicBean;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.GiveGiftResultBean;
import com.siyecaoi.yy.bean.MicXgfjBean;
import com.siyecaoi.yy.bean.OnlineUserBean;
import com.siyecaoi.yy.bean.OpenPacketBean;
import com.siyecaoi.yy.bean.PkSetBean;
import com.siyecaoi.yy.bean.RandomNumberBean;
import com.siyecaoi.yy.bean.RoomCacheBean;
import com.siyecaoi.yy.bean.VoiceHomeBean;
import com.siyecaoi.yy.bean.VoiceMicBean;
import com.siyecaoi.yy.bean.VoiceUserBean;
import com.siyecaoi.yy.control.ChatMessage;
import com.siyecaoi.yy.control.ChatRoomMessage;
import com.siyecaoi.yy.dialog.MessageDialogFragment;
import com.siyecaoi.yy.dialog.MyBottomPersonDialog;
import com.siyecaoi.yy.dialog.MyBottomWheatDialog;
import com.siyecaoi.yy.dialog.MyBottomauctionDialog;
import com.siyecaoi.yy.dialog.MyChestsOneDialog;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.dialog.MyExpressionDialog;
import com.siyecaoi.yy.dialog.MyGiftAllshowDialog;
import com.siyecaoi.yy.dialog.MyGiftDialog;
import com.siyecaoi.yy.dialog.MyGoldSendDialog;
import com.siyecaoi.yy.dialog.MyHintDialog;
import com.siyecaoi.yy.dialog.MyMusicDialog;
import com.siyecaoi.yy.dialog.MyOnlineUserDialog;
import com.siyecaoi.yy.dialog.MyPacketDialog;
import com.siyecaoi.yy.dialog.MyPkDialog;
import com.siyecaoi.yy.dialog.MyRankingDialog;
import com.siyecaoi.yy.dialog.MyRewardDialog;
import com.siyecaoi.yy.dialog.MyRoomPassDialog;
import com.siyecaoi.yy.dialog.MyTopicshowDialog;
import com.siyecaoi.yy.dialog.RoomRankDialogFragment;
import com.siyecaoi.yy.dialog.SetRandomDialog;
import com.siyecaoi.yy.dialog.WinPrizeGiftAllDialog;
import com.siyecaoi.yy.model.AllMsgModel;
import com.siyecaoi.yy.model.CarShowMessageBean;
import com.siyecaoi.yy.model.ChatMessageBean;
import com.siyecaoi.yy.model.EmojiList;
import com.siyecaoi.yy.model.EmojiMessageBean;
import com.siyecaoi.yy.model.GetOutBean;
import com.siyecaoi.yy.model.GiftAllModel;
import com.siyecaoi.yy.model.GiftSendMessage;
import com.siyecaoi.yy.model.MessageBean;
import com.siyecaoi.yy.model.StarAllBean;
import com.siyecaoi.yy.model.VoiceTypeModel;
import com.siyecaoi.yy.model.WinPrizeGiftModel;
import com.siyecaoi.yy.mvp.model.VoiceModel;
import com.siyecaoi.yy.mvp.presenter.VoicePresenter;
import com.siyecaoi.yy.mvp.view.VoiceView;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.message.ChatActivity;
import com.siyecaoi.yy.ui.message.OtherHomeActivity;
import com.siyecaoi.yy.ui.message.fragment.ChatFragmentDialog;
import com.siyecaoi.yy.ui.mine.PersonHomeActivity;
import com.siyecaoi.yy.ui.room.adapter.ChatRecyclerAdapter;
import com.siyecaoi.yy.ui.room.adapter.PlaceRecyclerAdapter;
import com.siyecaoi.yy.ui.room.dialog.OtherShowDialog;
import com.siyecaoi.yy.ui.room.dialog.SendRecordDialog;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.MessageUtils;
import com.siyecaoi.yy.utils.MyUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.activitys.BaseActivity;
import cn.sinata.xldutils.utils.StringUtils;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.siyecaoi.yy.utils.Const.isOpenMicrophone;
import static com.siyecaoi.yy.utils.Const.isOpenReceiver;

/**
 * ??????????????????
 * Created by Administrator on 2018/12/20.
 */

public class VoiceActivity extends MyBaseMVPActivity implements VoiceView,
        ConversationManagerKit.MessageUnreadWatcher {
    private boolean isVD = false;
    @BindView(R.id.iv_topname_voice)
    SimpleDraweeView ivTopnameVoice;
    @BindView(R.id.iv_topname_headwear)
    SVGAImageView ivTopnameHW;
    @BindView(R.id.tv_leave_voice)
    TextView tvLeaveVoice;
    @BindView(R.id.rl_topimg_voice)
    RelativeLayout rlTopimgVoice;
    @BindView(R.id.tv_homename_voice)
    TextView tvHomenameVoice;
    @BindView(R.id.tv_homeid_voice)
    TextView tvHomeidVoice;
    @BindView(R.id.tv_homenum_voice)
    TextView tvHomenumVoice;
    @BindView(R.id.iv_homeset_voice)
    ImageView ivHomesetVoice;
    @BindView(R.id.tv_homecon_voice)
    TextView tvHomeconVoice;
    @BindView(R.id.iv_auction_voice)
    ImageView ivAuctionVoice;
    @BindView(R.id.tv_music_voice)
    TextView tvMusicVoice;//????????????
    @BindView(R.id.tv_hometype_voice)
    TextView tvHometypeVoice;
    @BindView(R.id.tv_homedetails_voice)
    TextView tvHomedetailsVoice;
    @BindView(R.id.mRecyclerView_place_voice)
    RecyclerView mRecyclerViewPlaceVoice;
    @BindView(R.id.mRecyclerView_chat_voice)
    RecyclerView mRecyclerViewChatVoice;
    @BindView(R.id.iv_message_voice)
    ImageView ivMessageVoice;
    @BindView(R.id.iv_mic_voice)
    ImageView ivMicVoice;
    @BindView(R.id.iv_receiver_voice)
    ImageView ivReceiverVoice;
    @BindView(R.id.iv_express_voice)
    ImageView ivExpressVoice;
    @BindView(R.id.iv_envelope_voice)
    ImageView ivEnvelopeVoice;
    @BindView(R.id.iv_packet_voice)
    ImageView ivPacketVoice;
    @BindView(R.id.iv_gift_voice)
    ImageView ivGiftVoice;
    @BindView(R.id.edt_input_mychat)
    EditText edtInputMychat;
    @BindView(R.id.btn_send_mychat)
    Button btnSendMychat;
    @BindView(R.id.rl_chat_back)
    RelativeLayout rlChatBack;
    @BindView(R.id.tv_has_message_voice)
    TextView tvHasMessageVoice;
    @BindView(R.id.iv_topnameback_voice)
    ImageView ivTopnamebackVoice;
    @BindView(R.id.iv_input_back_voice)
    ImageView ivInputBackVoice;
    @BindView(R.id.iv_act_gold)
    ImageView iv_act_gold;
    @BindView(R.id.iv_chests_voice)
    ImageView ivChestsVoice;
    @BindView(R.id.iv_act_star)
    ImageView iv_act_star;
    @BindView(R.id.tv_smallpk_show_voice)
    TextView tvSmallpkShowVoice;
    @BindView(R.id.tv_onename_smallpk_voice)
    TextView tvOnenameSmallpkVoice;
    @BindView(R.id.tv_twoname_smallpk_voice)
    TextView tvTwonameSmallpkVoice;
    @BindView(R.id.progress_smallpk_voice)
    ProgressBar progressSmallpkVoice;

    @BindView(R.id.ll_chat_voice)
    LinearLayout llChatVoice;
    boolean isCanOpen;//?????????????????????????????????
    /**
     * ???????????????????????????????????????
     */
//    boolean isCanSetMargin;
    boolean isOpenChat;//???????????????????????????
    @BindView(R.id.tv_endtime_show_voice)
    TextView tvEndtimeShowVoice;
    @BindView(R.id.tv_onenumber_voice)
    TextView tvOnenumberVoice;
    @BindView(R.id.tv_twonumber_voice)
    TextView tvTwonumberVoice;
    @BindView(R.id.ll_bottom_voice)
    LinearLayout llBottomVoice;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.rl_smallpk_voice)
    RelativeLayout rlSmallpkVoice;
    @BindView(R.id.tv_showimg_voice)
    TextView tvShowimgVoice;
    @BindView(R.id.ll_smallpk_voice)
    LinearLayout llSmallpkVoice;
    @BindView(R.id.iv_voiceback_voice)
    SimpleDraweeView ivVoicebackVoice;
    @BindView(R.id.iv_getpacket_voice)
    ImageView ivGetpacketVoice;
    @BindView(R.id.iv_music_voice)
    ImageView ivMusicVoice;
    @BindView(R.id.rl_music_voice)
    RelativeLayout rlMusicVoice;
    @BindView(R.id.rl_back_voice)
    RelativeLayout rlBackVoice;
    @BindView(R.id.iv_showimg_voice)
    SimpleDraweeView ivShowimgVoice;
    @BindView(R.id.tv_access_voice)
    TextView tvAccessVoice;

    String chatInputShow;//?????????????????????
    @BindView(R.id.iv_showsvga_voice)
    SVGAImageView ivShowsvgaVoice;
    @BindView(R.id.tv_giftpoint_voice)
    TextView tvGiftpointVoice;
    @BindView(R.id.tv_alltopic_voice)
    TextView tvAlltopicVoice;

    @BindView(R.id.alltopic_voice_ll)
    LinearLayout alltopic_voice_ll;

    @BindView(R.id.tongzhi_img)
    SimpleDraweeView tongzhi_img;

    @BindView(R.id.tv_chattype_voice)
    TextView tvChattypeVoice;
    @BindView(R.id.tv_chat2_voice)
    TextView tvChat2Voice;
    @BindView(R.id.tv_chat1_voice)
    TextView tvChat1Voice;
    @BindView(R.id.back_iv)
    ImageView back_iv;
    @BindView(R.id.ll_chattype_voice)
    LinearLayout llChattypeVoice;

    @BindView(R.id.liang_iv)
    ImageView liang_iv;

    @BindView(R.id.ll_giftall_voice)
    LinearLayout ll_giftall_voice;

    @BindView(R.id.ll_starall_voice)
    LinearLayout ll_starall_voice;

    @BindView(R.id.ll_timeall_voice)
    LinearLayout ll_timeall_voice;

    @BindView(R.id.ll_rotate)
    LinearLayout ll_rotate;

    @BindView(R.id.iv_number_1)
    ImageView iv_number_1;

    @BindView(R.id.iv_number_2)
    ImageView iv_number_2;

    @BindView(R.id.iv_number_3)
    ImageView iv_number_3;

    /**
     * ?????????????????????102
     */
    PlaceRecyclerAdapter placeRecyclerAdapter;
    /**
     * ?????????????????????
     */
    ChatRecyclerAdapter chatRecyclerAdapter;


    private String roomId;//??????id
    private String roomImg;//??????????????????
    /**
     * ??????id
     */
    private int roomUserId;
    /**
     * ????????????
     */
    private String roomUserName;
    /**
     * ??????????????????  1??????  2?????????  3????????????
     */
    private int userRoomType;
    //    /**
//     * ??????????????????(?????????) ???????????????????????????
//     */
////    boolean isOpenReceiver;
    Observable<Boolean> openReceiverObservable;
    Consumer<Boolean> receiverConsumer;
    /**
     * ?????????????????????
     */
//    boolean isOpenMicrophone;
    /**
     * ???????????????????????????
     */
    boolean isMicShow;
    /**
     * ?????????????????????????????????
     */
    int userMicOne;
    /**
     * ????????????????????????
     */
    boolean isOpenRecord;

    /**
     * ??????????????????
     */
    boolean isOpenGp;

    /**
     * ???????????????????????????????????????
     */
    boolean isMicCan;


//    MyBottomShowDialog myBottomShowDialog;
//    private ArrayList<String> bottomList;//??????????????????

    private ArrayList<String> chatShowChat;//????????????

    private List<VoiceMicBean.DataBean> chatUserList;//????????????

    VoicePresenter voicePresenter;
    //????????????
    private Consumer<String> consumer;
    Observable<String> observable;
    Disposable subscribe;

    VoiceHomeBean.DataBean.RoomBean roomBean;//????????????

    MyOnlineUserDialog myOnlineUserDialog;//??????????????????

    String roomTopic, topicCount;//?????????????????????
    String roomPass;//????????????
    MyTopicshowDialog myTopicshowDialog;
    MyRankingDialog myRankingDialog;
    MyBottomPersonDialog myBottomPersonDialog;

    MyBottomWheatDialog bottomWheatDialog;

    MyBottomauctionDialog bottomauctionDialog;

    MyRoomPassDialog roomPassDialog;

    MyDialog myDialog;

    MyGiftDialog giftDialog;
    FragPagerAdapter fragPagerAdapter;

    MyExpressionDialog myExpressionDialog;

    MyRewardDialog rewardDialog;

    SetRandomDialog randomDialog;

    MyPacketDialog packetDialog;

    MyMusicDialog musicDialog;

    CountDownTimer countDownTimer;

    MyPkDialog pkDialog;
    PkSetBean.DataBean pkData;
    int roomPid;//??????pk??????id

    MyChestsOneDialog chestsDialog;
    String userName;//????????????
    int goldNum;//????????????

//    MyGiftShowDialog giftShowDialog;

    private boolean isHavePacket;//???????????????

    private int packetNumber;//????????????
    private int giftMinShow;//????????????????????????

    MessageDialogFragment messageDialogFragment;
    ArrayList<ChatMessageBean.DataBean> accessList;//??????????????????
    boolean accessRoomShow;//???????????????????????????????????????

    ArrayList<GiftSendMessage.DataBean> giftList;//??????????????????
    int giftNumberShow;//???????????????????????????

    List<String> noShowString;//????????????

    List<VoiceTypeModel> typeList;
    List<VoiceTypeModel> typeOneList;
    Timer timerType;
    TimerTask taskType;

    private boolean isAllChat;//????????????????????????
    private SVGAParser parser;
    private boolean roll_bottom = false;

    ArrayList<GiftAllModel.DataBean> giftAllList = new ArrayList<>();
    ArrayList<StarAllBean.DataBean> starAllList = new ArrayList<>();
    ArrayList<StarAllBean.DataBean> timeAllList = new ArrayList<>();


    @Override
    public void initData() {
        roomId = getBundleString(Const.ShowIntent.ROOMID);
        roll_bottom = getBundleBoolean(Const.ShowIntent.ROLL_BOTTOM, false);
//        uid = getBundleInt(Const.ShowIntent.ID, 0);R
//        bottomList = new ArrayList<>();
        userName = (String) SharedPreferenceUtils.get(this, Const.User.NICKNAME, "");
        goldNum = (int) SharedPreferenceUtils.get(this, Const.User.GRADE_T, 0);

        typeList = new ArrayList<>();
        typeOneList = new ArrayList<>();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_voice);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        }
        reqVoice();

        initShow();

        initBroadCast();

        initCall();

        initTimer();

        edtInputMychat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mRecyclerViewChatVoice != null && chatRecyclerAdapter != null && chatRecyclerAdapter.getItemCount() > 0) {
            tvHasMessageVoice.setVisibility(View.INVISIBLE);
            mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
        }


    }

    private void initTimer() {
        timerType = new Timer();
        taskType = new TimerTask() {
            @SuppressLint("CheckResult")
            @Override
            public void run() {
                typeOneList.clear();
                for (VoiceTypeModel typeModel : typeList) {
                    if (typeModel.getTime() > 0) {
                        LogUtils.e("type" + typeModel.toString());
                        typeModel.setTime(typeModel.getTime() - 1);
                    } else if (typeModel.getTime() <= 0) {
                        typeModel.setTime(-1);
                        typeOneList.add(typeModel);
                        LogUtils.e("type" + typeModel.toString());
                        Observable
                                .just(typeModel)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<VoiceTypeModel>() {
                                    @Override
                                    public void accept(VoiceTypeModel voiceTypeModel) throws Exception {
                                        switch (voiceTypeModel.getType()) { //?????????????????????
                                            case 1://1????????????(??????????????????)
                                                //??????????????????????????????
                                                voicePresenter.getUserAttention(userToken, roomBean.getUid());
                                                break;
                                            case 2://2 ????????????????????????????????????
                                                if (voiceTypeModel.getPosition() == 8) {
                                                    if (tvShowimgVoice != null) {
                                                        tvShowimgVoice.setVisibility(View.GONE);
                                                    }
                                                    if (ivShowimgVoice != null) {
                                                        ivShowimgVoice.setVisibility(View.GONE);
                                                    }
                                                    if (ivShowsvgaVoice != null) {
                                                        ivShowsvgaVoice.setVisibility(View.GONE);
                                                    }
                                                } else {
                                                    VoiceMicBean.DataBean item = placeRecyclerAdapter.getItem(voiceTypeModel.getPosition());
                                                    if (item.getUserModel() != null) {
                                                        item.getUserModel().setShowImg(0);
                                                        item.getUserModel().setImgOver(false);
                                                    }
                                                    if (placeRecyclerAdapter.getItem(voiceTypeModel.getPosition()) != null) {
                                                        placeRecyclerAdapter.setData(voiceTypeModel.getPosition(), item);
                                                    }
                                                }
                                                break;
                                            case 3://3??????????????????????????????????????????
                                                if (voiceTypeModel.getPosition() == 8) {
                                                    setHomeAnimation(false);
                                                } else {
                                                    VoiceMicBean.DataBean item = placeRecyclerAdapter.getItem(voiceTypeModel.getPosition());
                                                    if (item.getUserModel() != null) {
                                                        item.getUserModel().setSpeak(false);
                                                    }
                                                    if (placeRecyclerAdapter.getItem(voiceTypeModel.getPosition()) != null) {
                                                        placeRecyclerAdapter.setData(voiceTypeModel.getPosition(), item);
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                });
                    }
                }
                typeList.removeAll(typeOneList);
            }
        };
        timerType.schedule(taskType, 1000, 1000);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param typeModelOne
     */
    private void addTypeList(VoiceTypeModel typeModelOne) {
        for (VoiceTypeModel typeModel : typeList) {
            if (typeModel.equals(typeModelOne)) {
                typeModel.setTime(typeModelOne.getTime());
                return;
            }
        }
        typeList.add(typeModelOne);
    }

    private void initCall() {
//        voicePresenter.getCall(userToken, roomId);
        voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.ONE);
//        voicePresenter.getAllTopic(userToken);
        voicePresenter.getRoomCahce();
    }

    private long nextGoRoomTime;

    @Override
    protected void onResume() {
        super.onResume();
        nextGoRoomTime = System.currentTimeMillis();
        if (voicePresenter != null)
            voicePresenter.getChatShow(userToken, roomId);
    }

    //?????????????????????????????????
    private void updateChatShow() {
        placeRecyclerAdapter.setNewData(chatUserList);
        updateUserData(chatUserList);
    }

    private void updateUserData(List<VoiceMicBean.DataBean> chatUserList) {
        //???????????????????????????????????????
        if (userRoomType != 1) {
            isMicShow = false;
        }
        userMicOne = -1;
        for (VoiceMicBean.DataBean dataBean : chatUserList) {
            userMicOne++;
            if (dataBean.getUserModel() != null) {
                if (dataBean.getUserModel().getId() == userToken) {
                    if (dataBean.getState() == 1) { //?????????
                        isMicCan = true;
                        setMicisOpen();
                    } else if (dataBean.getState() == 2) { //??????
                        isMicCan = false;
                        setMicisOpen();
                    }
                    userRoomType = dataBean.getUserModel().getType();
                    //??????????????????????????????
                    isMicShow = true;
                    break;
                }
            }
        }
        //????????????????????????????????????????????????
        if (!isMicShow) {
            isOpenMicrophone = false;
            setUpdateMic(Const.IntShow.TWO);
            BroadcastManager.getInstance(this).sendBroadcast(Const.BroadCast.MIC_DOWN, true);

            if (musicDialog != null && musicDialog.isShowing()) { //??????????????????
                musicDialog.dismiss();
            }
        }
    }

    @SuppressLint("CheckResult")
    private void reqVoice() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            isOpenRecord = true;
                        } else {
                            showToast("????????????????????????????????????????????????");
                            ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                        }
                    }
                });
    }

    private void initBroadCast() {
        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MUSIC_PLAY, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String filePath = intent.getStringExtra(Const.ShowIntent.DATA);
                MyApplication.getInstance().getWorkerThread().musicPlay(filePath);
                if (musicDialog != null) {
                    musicDialog.updateShow();
                }
                showMusicAnimation(true);
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MUSIC_PAUSE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MyApplication.getInstance().getWorkerThread().musicPause();
                if (musicDialog != null) {
                    musicDialog.updateShow();
                }
                showMusicAnimation(false);
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MUSIC_REPLAY, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MyApplication.getInstance().getWorkerThread().musicReplay();
                if (musicDialog != null) {
                    musicDialog.updateShow();
                }
                showMusicAnimation(true);
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.PACKET_OVER, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                packetNumber = intent.getIntExtra(Const.ShowIntent.DATA, 0);
                setPacketShow();
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MSG_COUN, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String topicBean = intent.getStringExtra(Const.ShowIntent.DATA);
                AllmsgBean.DataEntity dataEntity = new Gson().fromJson(topicBean, AllmsgBean.DataEntity.class);
                setCountMsg(dataEntity);
            }
        });
    }

    /**
     * ????????????
     */
    ObjectAnimator musicAnimator;
    long mCurrentPlayTime;//????????????

    private void showMusicAnimation(boolean isShow) {
        if (musicAnimator == null) {
            musicAnimator = ObjectAnimator.ofFloat(ivMusicVoice, "rotation", 0, 360);
            musicAnimator.setDuration(3000);
            musicAnimator.setRepeatCount(-1);
            //??????????????????
            musicAnimator.setInterpolator(new LinearInterpolator());
        }
        if (isShow) {
            musicAnimator.start();
            if (0 != mCurrentPlayTime) {
                musicAnimator.setCurrentPlayTime(mCurrentPlayTime);
            }
        } else {
            //??????????????????
            mCurrentPlayTime = musicAnimator.getCurrentPlayTime();
            musicAnimator.cancel();
        }
    }

    /**
     * ????????????PK?????????
     *
     * @param pkShow
     */
    float xdown, ydown;
    int Xmove, Ymove;
    int kleft, ktop;

    @SuppressLint("SetTextI18n")
    private void setPkShow() {
        LogUtils.e("msg", "????????????");
        if (rlSmallpkVoice.getVisibility() == View.GONE) {
            rlSmallpkVoice.setVisibility(View.VISIBLE);
        }

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/impact.ttf");
        tvSmallpkShowVoice.setTypeface(typeface);
//        String endTime = data.getTime();
        tvOnenameSmallpkVoice.setText(pkData.getUser().getName());
        tvTwonameSmallpkVoice.setText(pkData.getUser1().getName());
        progressSmallpkVoice.setMax(pkData.getUser().getNum() + pkData.getUser1().getNum());
        progressSmallpkVoice.setProgress(pkData.getUser().getNum());
        tvOnenumberVoice.setText(pkData.getUser().getNum() + "");
        tvTwonumberVoice.setText(pkData.getUser1().getNum() + "");
        long endTimeLong = pkData.getTime();
        long nowTime = System.currentTimeMillis();
        long endTimeShow = endTimeLong - nowTime;
        if (pkDialog != null && pkDialog.isShowing()) {
            pkDialog.updateData(pkData);
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (endTimeShow > 0) {
            tvEndtimeShowVoice.setText((endTimeShow / 1000) + "s");
            countDownTimer = new CountDownTimer(endTimeShow, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvEndtimeShowVoice.setText(millisUntilFinished / 1000 + "s");
                }

                @Override
                public void onFinish() {
                    if (rlSmallpkVoice.getVisibility() == View.VISIBLE) {
                        rlSmallpkVoice.setVisibility(View.GONE);
                        showMyPkDialog(pkData);
                    }
                }
            };
            countDownTimer.start();
        } else {
            rlSmallpkVoice.setVisibility(View.GONE);
//            showToast("Pk?????????");
        }
    }

    private int lastX, lastY;
    int dx, dy;

    @SuppressLint("ClickableViewAccessibility")
    private void setRoomTouch() {
        rlSmallpkVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getRawX() - lastX;
                        dy = (int) event.getRawY() - lastY;

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                        int l = layoutParams.leftMargin + dx;
                        int t = layoutParams.topMargin + dy;

                        if (l < 0) { //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                            l = 0;
//                            r = rlBackVoice.getWidth() - v.getWidth();
                        }
                        if (t < 100) {
                            t = 100;
//                            b = rlBackVoice.getHeight() - v.getHeight();
                        }
                        if (l > rlBackVoice.getWidth() - v.getWidth()) {
                            l = rlBackVoice.getWidth() - v.getWidth();
                        }
                        if (t > rlBackVoice.getHeight() - v.getHeight() - 80) {
                            t = rlBackVoice.getHeight() - v.getHeight() - 80;
                        }

                        layoutParams.leftMargin = l;
                        layoutParams.topMargin = t;
                        v.setLayoutParams(layoutParams);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        v.postInvalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                if (Math.abs(dx) > 2 || Math.abs(dy) > 2) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        rlSmallpkVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSmallpkVoice.setVisibility(View.GONE);
                LogUtils.e("msg", "????????????");
                showMyPkDialog(pkData);
            }
        });
    }

    private void initList() {
        if (!StringUtils.isEmpty(roomBean.getMark())) {
            MessageBean messageBean = new MessageBean(102, roomBean.getMark());
            String messageString = JSON.toJSONString(messageBean);
            chatShowChat.add(messageString);
        }
    }

    @SuppressLint("CheckResult")
    private void initVoice(String token) {
        MyApplication.getInstance().getWorkerThread().eventHandler().addEventHandler(agEventHandler);
        switch (userRoomType) {
            case 1:
                //???????????????????????????
//                MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_BROADCASTER);
//                isOpenMicrophone = true;
//                isOpenReceiver = true;
                isMicShow = true;
                isMicCan = true;
                Observable.just(0).delay(1000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Integer>() {
                                       @Override
                                       public void accept(Integer aLong) throws Exception {
                                           setMicisOpen();//???????????????????????????
                                       }
                                   }
                        );
//                ivMicVoice.setImageResource(R.drawable.selector_mic);
//                ivMicVoice.setSelected(true);
//                ivReceiverVoice.setSelected(true);
                ivExpressVoice.setVisibility(View.VISIBLE);
                setHomeAnimation(isOpenMicrophone);
                break;
            case 2:
                //???????????????????????????(??????????????????????????????)
//                MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_AUDIENCE);
//                isOpenMicrophone = false;
//                isOpenReceiver = true;
                isMicShow = false;
                ivMicVoice.setImageResource(R.drawable.bottom_mic_add);
//                ivReceiverVoice.setSelected(true);
                break;
            case 3:
                //???????????????????????????
//                MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_AUDIENCE);
//                isOpenMicrophone = false;
//                isOpenReceiver = true;
                isMicShow = false;
                ivMicVoice.setImageResource(R.drawable.bottom_mic_add);
//                ivReceiverVoice.setSelected(true);
                break;
        }
        //?????? Agora ????????????
//        token = "00635e826bd63df4998823c36aa5c62bf8cIACSW8Yqg8IxG/XUQzwz+pM7NSzDoKPhW8RRYvJhMrk1B/9BTooAAAAAEACyCuRRl3lOXQEAAQCPeU5d";
        YySignaling.getInstans().loginSignaling(String.valueOf(userToken), token);
        //??????????????????
        YySignaling.getInstans().joinChannel(roomId);

        if (!Const.RoomId.equals(roomId)) {  //???????????????????????????????????????????????????????????????
            if (!StringUtils.isEmpty(Const.RoomId)) {
                //????????????????????????????????????????????????
                voicePresenter.getChatRoom(userToken, Const.RoomId);
                MyApplication.getInstance().getWorkerThread().leaveChannel(Const.RoomId);
                Const.RoomId = "";
            }
            //??????????????????
            MyApplication.getInstance().getWorkerThread().joinChannel(roomId, userToken);

            //??????????????????
            Const.RoomName = roomBean.getRoomName();
            Const.RoomId = roomId;
            Const.RoomIdLiang = roomBean.getLiang();
            Const.RoomImg = roomImg;
            Const.RoomNum = roomBean.getLineNum() + "";
            isOpenReceiver = true;//???????????????????????????

            //????????????????????????
            initList();
            if (!StringUtils.isEmpty(roomBean.getRoomHint())) {  //??????
                //?????????????????????
                MessageBean messageBean = new MessageBean(103, roomBean.getRoomHint());
                String mesString = JSON.toJSONString(messageBean);
                if (!StringUtils.isEmpty(mesString)) {
                    chatShowChat.add(mesString);
//                    chatRecyclerAdapter.addData(mesString);
                }
            }

            if (!isVD) {
                //??????????????????
                setCarShow();
                //??????????????????
                setGradeShow();
            }


        } else { //????????????????????????
            if (Const.MusicShow.isHave && Const.MusicShow.musicPlayState == Const.IntShow.TWO) { //????????????????????????
                // add by 20201106 for ?????????????????????????????????????????????????????????????????????????????????????????????
                if (!TextUtils.isEmpty(Const.MusicShow.musicPath)) {
                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.MUSIC_PLAY, Const.MusicShow.musicPath);
                }
                showMusicAnimation(true);
            }
        }

        //?????????????????????
        openReceiverObservable = Observable.just(isOpenReceiver);
        receiverConsumer = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                ivReceiverVoice.setSelected(aBoolean);
            }
        };

        openReceiverObservable.subscribe(receiverConsumer);


        //???????????????????????????3???????????????
        MyApplication.getInstance().getWorkerThread().getRtcEngine().enableAudioVolumeIndication(3000, 3);

        MessageUtils.getInstans().addChatShows(chatMessage);

        voicePresenter.getChatShow(userToken, roomId);//???????????????????????????????????????????????????????????????


        if (roll_bottom) {
            tvHasMessageVoice.setVisibility(View.INVISIBLE);
            mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
        }
    }

    private void setGradeShow() {
        if (goldNum > 0) {  //1?????????
            ChatMessageBean.DataBean dataBean = new ChatMessageBean.DataBean(goldNum, userName, userToken);
            ChatMessageBean chatMessageBean = new ChatMessageBean(117, dataBean);
            String carShowString = JSON.toJSONString(chatMessageBean);
            setSendMessage(carShowString);
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    private void setCarShow() {
        String carHeader = (String) SharedPreferenceUtils.get(this, Const.User.CAR_H, "");
        String carUrl = (String) SharedPreferenceUtils.get(this, Const.User.CAR, "");
        if (!StringUtils.isEmpty(carHeader) && !carHeader.equals("x")) {
            CarShowMessageBean.DataBean dataBean = new CarShowMessageBean.DataBean(userName, carHeader, carUrl, goldNum, userToken);
            CarShowMessageBean carShowMessageBean = new CarShowMessageBean();
            carShowMessageBean.setCode(116);
            carShowMessageBean.setData(dataBean);
            String carShowString = JSON.toJSONString(carShowMessageBean);
            setSendMessage(carShowString);
        }
    }

    //??????????????????(????????????)
    ChatMessage chatMessage = new ChatMessage() {
        @SuppressLint("CheckResult")
        @Override
        public void setMessageShow(String channelID, String account, int uid, final String msg) {
//            chatRecyclerAdapter.addData(msg);
            if (channelID.equals(roomId)) {
                if (consumer == null) {
                    initConsumer();
                }
                observable = Observable.just(msg);
                subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumer);
            }
        }

        /**
         * ??????????????????
         * @param s  ?????????
         * @param s1 ?????????
         * @param s2 ?????????
         * @param s3 ????????????:
         */
        @Override
        public void setChannelAttrUpdated(String s, String s1, String s2, String s3) {
            if (s.equals(roomId)) {
                if (s1.equals(Const.Agora.ATTR_MICS)) {
                    observable = Observable.just(s2);
                    subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    chatUserList = JSON.parseArray(s, VoiceMicBean.DataBean.class);
                                    updateChatShow();
                                }
                            });
                } else if (s1.equals(Const.Agora.ATTR_XGFJ)) {
                    observable = Observable.just(s2);
                    subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    roomBean = JSON.parseObject(s, VoiceHomeBean.DataBean.RoomBean.class);
                                    // add by 20201117 ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                                    if (TextUtils.isEmpty(roomBean.getMark())){
                                    setRoomShow();
//                                    }
                                }
                            });
                } else if (s1.equals(Const.Agora.ATTR_PK) || s1.equals(Const.Agora.ATTR_PKTP)) {
                    observable = Observable.just(s2);
                    subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    pkData = JSON.parseObject(s, PkSetBean.DataBean.class);
                                    roomPid = pkData.getId();
                                    setPkShow();
                                }
                            });
                } else if (s1.equals(Const.Agora.ATTR_PACKET)) {
                    observable = Observable.just(s2);
                    subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    LogUtils.e("msg", s);
                                    OpenPacketBean openPacketBean = JSON.parseObject(s, OpenPacketBean.class);
                                    setPacketList(openPacketBean);
                                }
                            });
                }

            }
        }
    };

    private void setPacketList(OpenPacketBean openPacketBean) {
        for (OpenPacketBean.RedNumBean openPacketNow :
                openPacketBean.getRedNum()) {
            if (openPacketNow.getUid() == userToken) {
                isHavePacket = true;
                packetNumber++;
                setPacketShow();
                showPacketDialog(openPacketNow.getId(), openPacketNow.getRedId(),
                        openPacketBean.getImg(), openPacketBean.getName(), openPacketBean.getRedNum().size());
                break;
            }
        }
    }

    /**
     * ??????????????????
     */
    private void initConsumer() {
        consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (TextUtils.isEmpty(s)) {//??????????????????
                    return;
                }
                MessageBean messageBean = new Gson().fromJson(s, MessageBean.class);
                if (messageBean.getData() == null || TextUtils.isEmpty(messageBean.getData().toString())) {//??????????????????
                    return;
                }
                if (messageBean.getCode() == 118) { //??????????????????
                    voicePresenter.getChatShow(userToken, roomId);
                    return;
                } else if (messageBean.getCode() == 119) {   //???????????????
                    LogUtils.i("????????????");
                    AlltopicBean alltopicBean = JSON.parseObject(s, AlltopicBean.class);
//                    List<AlltopicBean.DataEntity> data = alltopicBean.getData();
                    topicList.addAll(alltopicBean.getData());
                    setTopicAnimation();

//                        if (data.size() > 0&& tvAlltopicVoice.getText().toString().length()>0)
//                        {
//                            tvAlltopicVoice.setText(data.get(0).getUserName() + "???" + data.get(0).getContent() + "  ");
//                            tvAlltopicVoice.requestFocus();
//                            tvAlltopicVoice.setVisibility(View.VISIBLE);
//                        }else {
//                            tvAlltopicVoice.setText(" ");
//                        }

//                    voicePresenter.getAllTopic(userToken);
                    return;
                } else if (messageBean.getCode() == 121) { //??????????????????
                    GiftAllModel giftAllModel = new Gson().fromJson(s, GiftAllModel.class);
                    ArrayList<GiftAllModel.DataBean> ral = new ArrayList<>();
                    ral.add(giftAllModel.getData());
//                    showGiftAllDialog(ral);
                    LogUtils.i("????????????????????????");
                    giftAllList.addAll(ral);
                    if (!isVD)
                        showGiftAllAnimation();
                    return;
                } else if (messageBean.getCode() == 2021) { //?????????????????????
                    StarAllBean starAllBean = new Gson().fromJson(s, StarAllBean.class);
                    LogUtils.i("?????????????????????");
                    starAllList.add(starAllBean.data);
                    if (!isVD)
                        showStarAllAnimation();
                    return;
                } else if (messageBean.getCode() == 2022) { //????????????????????????
                    StarAllBean starAllBean = new Gson().fromJson(s, StarAllBean.class);
                    Log.e("mmp", "?????????????????????????????????" + "\n" + s);
                    if (starAllBean.data.rids.contains(roomId)) {
                        timeAllList.add(starAllBean.data);
                        if (!isVD) {
                            GiftSendMessage.DataBean dataBean = new GiftSendMessage.DataBean(starAllBean.data.effect, 0);
                            setGiftDialogShow(dataBean);
                            showTimeAllAnimation();
                        }
                    }
                    return;
                } else if (messageBean.getCode() == 122) { //????????????????????????
//                    WinPrizeGiftModel giftAllModel = new Gson().fromJson(s, WinPrizeGiftModel.class);
//                    //     ??????????????????1000?????????
//                    if (giftAllModel != null && giftAllModel.getData().getCost() >= 10000) {
//                        //                        showWinPrizeGiftAllDialog(giftAllModel);
//                        LogUtils.i("??????????????????");
//                        prizeAllList.add(giftAllModel.getData());
//                        showPrizeGiftAllAnimation();
//                    }

                }

                chatShowChat.add(s);
                chatRecyclerAdapter.notifyDataSetChanged();
//                chatRecyclerAdapter.addData(s);
                if (chatRecyclerAdapter.getData().size() > 1000) {
                    chatRecyclerAdapter.remove(0);
                    chatShowChat.remove(0);
                }
                ChatMessageBean chatMessageBean;
                switch (messageBean.getCode()) {
                    case 101: //101 ????????????
                        GiftSendMessage giftSendMessage = new Gson().fromJson(s, GiftSendMessage.class);
                        //???????????????????????????????????????
                        if (roomBean.getIsState() == 2 && giftMinShow > giftSendMessage.getData().getGoodGold()) {
                            return;
                        }
                        if (!isVD)
                            setGiftDialogShow(giftSendMessage.getData());

//                        String sendIds = giftSendMessage.getData().getSendId();
//                        String[] allIds = sendIds.split(",");
//                        for (int i = 0; i < allIds.length; i++) {
//                            int nowId = Integer.parseInt(allIds[i]);
//                            if (nowId == userToken) {
//                                showMyGiftShowDialog(giftSendMessage.getData().getShowImg(), giftSendMessage.getData().getNum());
//                            }
//                        }
                        break;
                    case 105: //105 ???????????????(???????????????)
                        chatMessageBean = new Gson().fromJson(s, ChatMessageBean.class);
                        if (userToken == chatMessageBean.getData().getUid()) {
                            userRoomType = Const.IntShow.TWO;
                        }
                        break;
                    case 106: //106 ????????????
                        chatMessageBean = new Gson().fromJson(s, ChatMessageBean.class);
                        if (chatMessageBean.getData().getUid() == userToken) { //?????????????????????????????????
                            int grade = chatMessageBean.getData().getGrade();
                            if (grade > goldNum) {
                                goldNum = grade;
                                SharedPreferenceUtils.put(VoiceActivity.this, Const.User.GRADE_T, goldNum);
                            }
                        }
                        break;
                    case 107:  //????????????
                        GetOutBean getOutBean = new Gson().fromJson(s, GetOutBean.class);
                        if (getOutBean.getData().getBuid() == userToken) { //???????????????????????????
                            showToast("???????????????????????????????????????");
                            voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
                        }
                        break;
                    case 108:  //108 ????????????
                        EmojiMessageBean emojiMessageBean = new Gson().fromJson(s, EmojiMessageBean.class);
                        if (emojiMessageBean.getData().getUid() == roomUserId) { //????????????
                            if (!isVD)
                                setEmojiShow(emojiMessageBean);
                        } else {
                            for (int i = 0; i < chatUserList.size(); i++) {
                                VoiceMicBean.DataBean usetListOne = chatUserList.get(i);
                                if (usetListOne.getUserModel() != null) {
                                    if (usetListOne.getUserModel().getId() == emojiMessageBean.getData().getUid()) {
                                        usetListOne.getUserModel().setShowImg(emojiMessageBean.getData().getEmojiCode());
                                        usetListOne.getUserModel().setImgOver(true);
                                        usetListOne.getUserModel().setNumberShow(emojiMessageBean.getData().getNumberShow());
                                        usetListOne.getUserModel().setRandomNumbers(emojiMessageBean.getData().getRandomNumbers());
                                        if (placeRecyclerAdapter != null) {
                                            if (placeRecyclerAdapter.getItem(i) != null) {
                                                if (!isVD)
                                                    placeRecyclerAdapter.setData(i, usetListOne);
                                            }
                                            if (!isVD)
                                                addTypeList(new VoiceTypeModel(2, i, 3));
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case 110://??????????????????
                        voicePresenter.getChatShow(userToken, roomId);
                        break;
                    case 112://??????????????????(???????????????????????????)
                        chatMessageBean = new Gson().fromJson(s, ChatMessageBean.class);
                        int blackId = chatMessageBean.getData().getUid();
                        if (blackId == userToken) {
                            showToast("??????????????????????????????????????????");
                            voicePresenter.getChatRoom(blackId, roomId, Const.IntShow.TWO);
                        }
                        break;
                    case 115://?????????????????????

                        break;
                    case 116://????????????
                        CarShowMessageBean carShowMessageBean = new Gson().fromJson(s, CarShowMessageBean.class);
                        String carUrl = carShowMessageBean.getData().getCarUrl();
//                        showMyGiftShowDialog(carUrl, 0);
                        GiftSendMessage.DataBean dataBean = new GiftSendMessage.DataBean(carUrl, 0);
                        if (!isVD)
                            setGiftDialogShow(dataBean);
                        break;
                    case 117://??????????????????
                        chatMessageBean = new Gson().fromJson(s, ChatMessageBean.class);
                        if (accessList == null) {
                            accessList = new ArrayList<>();
                        }
                        accessList.add(chatMessageBean.getData());
                        if (!accessRoomShow) {
                            tvAccessVoice.setVisibility(View.VISIBLE);
                            chooseOneAccess = 0;
                            openAccessShow();
                        }
                        break;
                }

                if (mRecyclerViewChatVoice.canScrollVertically(1)) {
                    tvHasMessageVoice.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
                }
            }
        };
    }

    ArrayList<AlltopicBean.DataEntity> topicList = new ArrayList<>();
    Disposable subTopic;

    private void setTopicAnimation() {
        if (subTopic != null && !subTopic.isDisposed()) {
            return;
        }
        subTopic = Observable.interval(0, 10000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   topicShow();
                               }
                           }
                );
    }

    private void topicShow() {
        if (topicList.size() == 0) {
            alltopic_voice_ll.setVisibility(View.GONE);
            if (!subTopic.isDisposed()) {
                subTopic.dispose();
            }
            return;
        }

        AlltopicBean.DataEntity dataEntity = topicList.get(0);
        tvAlltopicVoice.setTag(dataEntity.getRid());
        tvAlltopicVoice.setText("" + dataEntity.getMessageShow() + "  ");
        ImageUtils.loadUri(tongzhi_img, dataEntity.getUserImg());

        tvAlltopicVoice.requestFocus();
        alltopic_voice_ll.setVisibility(View.VISIBLE);

        topicList.remove(dataEntity);

        Animation animation = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_right_enter10);
        alltopic_voice_ll.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //???????????????????????????
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation1 = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_left_out);
                alltopic_voice_ll.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    Disposable subPrize;
    ArrayList<WinPrizeGiftModel.DataBean> prizeAllList = new ArrayList<>();

    private void showPrizeGiftAllAnimation() {
        if (subPrize != null && !subPrize.isDisposed()) {
            return;
        }
        ll_giftall_voice.removeAllViews();
        ll_giftall_voice.setVisibility(View.GONE);
        subPrize = Observable.interval(0, 4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   loadPrizeView();
                               }
                           }
                );
    }

    private void loadPrizeView() {
        if (prizeAllList.size() == 0) {
            ll_giftall_voice.setVisibility(View.GONE);
            if (!subPrize.isDisposed()) {
                subPrize.dispose();
            }
            return;
        }
        ll_giftall_voice.removeAllViews();
        ll_giftall_voice.setVisibility(View.VISIBLE);

        WinPrizeGiftModel.DataBean giftAllModel = prizeAllList.get(0);
        Log.e("mmp", "????????????????????????");
        View giftViewAnimator = View.inflate(this, R.layout.dialog_winprize_giftallshow, null);
        SimpleDraweeView iv_one_giftall = giftViewAnimator.findViewById(R.id.iv_one_giftall);
        SimpleDraweeView iv_gift_giftall = giftViewAnimator.findViewById(R.id.iv_gift_giftall);
        TextView tv_one_giftall = giftViewAnimator.findViewById(R.id.tv_one_giftall);
        TextView tv_number_giftall = giftViewAnimator.findViewById(R.id.tv_number_giftall);
        TextView gif_name = giftViewAnimator.findViewById(R.id.gif_name);
        TextView gif_jiazhi = giftViewAnimator.findViewById(R.id.gif_jiazhi);

        ImageUtils.loadUri(iv_one_giftall, giftAllModel.getUserImg());
        tv_one_giftall.setText(giftAllModel.getNickname());
        ImageUtils.loadUri(iv_gift_giftall, giftAllModel.getGiftImg());
        tv_number_giftall.setText("X" + giftAllModel.getGiftNum());
        gif_name.setText(giftAllModel.getGiftName());
        gif_jiazhi.setText("(" + giftAllModel.getCost() * giftAllModel.getGiftNum() + ")");


        prizeAllList.remove(giftAllModel);
        ll_giftall_voice.addView(giftViewAnimator);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_right_enter);
        giftViewAnimator.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //???????????????????????????
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation1 = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_left_out);
                giftViewAnimator.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    Disposable subGift;

    @SuppressLint("CheckResult")
    private void showGiftAllAnimation() {
        if (subGift != null && !subGift.isDisposed()) {
            return;
        }
        ll_giftall_voice.removeAllViews();
        ll_giftall_voice.setVisibility(View.GONE);
        subGift = Observable.interval(0, 4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   loadGiftView();
                               }
                           }
                );
    }


    Disposable subStar;

    @SuppressLint("CheckResult")
    private void showStarAllAnimation() {
        if (subStar != null && !subStar.isDisposed()) {
            return;
        }
        ll_starall_voice.removeAllViews();
        ll_starall_voice.setVisibility(View.GONE);
        subStar = Observable.interval(0, 4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   loadStarView();
                               }
                           }
                );
    }


    Disposable subTime;

    @SuppressLint("CheckResult")
    private void showTimeAllAnimation() {
        if (subTime != null && !subTime.isDisposed()) {
            return;
        }
        ll_timeall_voice.removeAllViews();
        ll_timeall_voice.setVisibility(View.GONE);
        subTime = Observable.interval(0, 4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   loadTimeView();
                               }
                           }
                );
    }

    private void loadGiftView() {
        if (giftAllList.size() == 0) {
            ll_giftall_voice.setVisibility(View.GONE);
            if (!subGift.isDisposed()) {
                subGift.dispose();
            }
            return;
        }
        ll_giftall_voice.removeAllViews();
        ll_giftall_voice.setVisibility(View.VISIBLE);
        Log.e("mmp", "????????????????????????");

        View giftViewAnimator = View.inflate(this, R.layout.dialog_giftallshow, null);
        SimpleDraweeView iv_one_giftall = giftViewAnimator.findViewById(R.id.iv_one_giftall);
        TextView tv_one_giftall = giftViewAnimator.findViewById(R.id.tv_one_giftall);
        TextView tv_two_giftall = giftViewAnimator.findViewById(R.id.tv_two_giftall);
        SimpleDraweeView iv_two_giftall = giftViewAnimator.findViewById(R.id.iv_two_giftall);
        SimpleDraweeView iv_gift_giftall = giftViewAnimator.findViewById(R.id.iv_gift_giftall);
        TextView tv_number_giftall = giftViewAnimator.findViewById(R.id.tv_number_giftall);
        GiftAllModel.DataBean giftAllModel = giftAllList.get(0);
//        String roomNow = giftAllModel.getRid();
        ImageUtils.loadUri(iv_one_giftall, giftAllModel.getSimg());
        tv_one_giftall.setText(giftAllModel.getSnickname());
        ImageUtils.loadUri(iv_two_giftall, giftAllModel.getBimg());
        tv_two_giftall.setText(giftAllModel.getBnickname());
        ImageUtils.loadUri(iv_gift_giftall, giftAllModel.getImg());
        tv_number_giftall.setText("X" + giftAllModel.getNum());

        giftAllList.remove(giftAllModel);
        ll_giftall_voice.addView(giftViewAnimator);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_right_enter);
        giftViewAnimator.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //???????????????????????????
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation1 = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_left_out);
                giftViewAnimator.startAnimation(animation1);
                Log.e("mmp", "??????????????????");
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("mmp", "??????????????????");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.e("mmp", "??????????????????");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void loadStarView() {
        if (starAllList.size() == 0) {
            ll_starall_voice.setVisibility(View.GONE);
            if (!subStar.isDisposed()) {
                subStar.dispose();
            }
            return;
        }
        ll_starall_voice.removeAllViews();
        ll_starall_voice.setVisibility(View.VISIBLE);
        Log.e("mmp", "???????????????????????????");

        View giftViewAnimator = View.inflate(this, R.layout.dialog_star_all_show, null);
        SimpleDraweeView iv_one_giftall = giftViewAnimator.findViewById(R.id.iv_one_giftall);
        TextView tv_one_giftall = giftViewAnimator.findViewById(R.id.tv_one_giftall);
        TextView tv_number_giftall = giftViewAnimator.findViewById(R.id.tv_number_giftall);
        StarAllBean.DataBean giftAllModel = starAllList.get(0);
        ImageUtils.loadUri(iv_one_giftall, giftAllModel.imgTx);
        tv_one_giftall.setText(giftAllModel.nickname);
        tv_number_giftall.setText(giftAllModel.num);

        starAllList.remove(giftAllModel);
        ll_starall_voice.addView(giftViewAnimator);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_right_enter);
        giftViewAnimator.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //???????????????????????????
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation1 = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_left_out);
                giftViewAnimator.startAnimation(animation1);
                Log.e("mmp", "??????????????????");
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("mmp", "??????????????????");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.e("mmp", "??????????????????");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void loadTimeView() {
        if (timeAllList.size() == 0) {
            ll_timeall_voice.setVisibility(View.GONE);
            if (!subTime.isDisposed()) {
                subTime.dispose();
            }
            return;
        }
        ll_timeall_voice.removeAllViews();
        ll_timeall_voice.setVisibility(View.VISIBLE);
        Log.e("mmp", "??????????????????????????????");

        View giftViewAnimator = View.inflate(this, R.layout.dialog_time_all_show, null);
        SimpleDraweeView iv_one_giftall = giftViewAnimator.findViewById(R.id.iv_one_giftall);
        TextView tv_one_giftall = giftViewAnimator.findViewById(R.id.tv_one_giftall);
        TextView tv_number_giftall = giftViewAnimator.findViewById(R.id.tv_number_giftall);
        StarAllBean.DataBean giftAllModel = timeAllList.get(0);
        ImageUtils.loadUri(iv_one_giftall, giftAllModel.imgTx);
        tv_one_giftall.setText(giftAllModel.nickname);
        tv_number_giftall.setText(giftAllModel.num);

        timeAllList.remove(giftAllModel);
        ll_timeall_voice.addView(giftViewAnimator);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_right_enter);
        giftViewAnimator.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //???????????????????????????
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation1 = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_left_out);
                giftViewAnimator.startAnimation(animation1);
                Log.e("mmp", "??????????????????");
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("mmp", "??????????????????");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.e("mmp", "??????????????????");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    /**
     * ????????????????????????
     */
    WinPrizeGiftAllDialog prizeGiftAllDialog;

    private void showWinPrizeGiftAllDialog(WinPrizeGiftModel giftAllModel) {
        if (prizeGiftAllDialog != null && prizeGiftAllDialog.isShowing()) {
            prizeGiftAllDialog.setGiftAllModel(giftAllModel);
        } else {
            if (prizeGiftAllDialog != null) {
                prizeGiftAllDialog.dismiss();
            }
            prizeGiftAllDialog = new WinPrizeGiftAllDialog(this, giftAllModel);
            prizeGiftAllDialog.show();
            prizeGiftAllDialog.getButton().setOnClickListener(v -> {
                toOtherRoom(myGiftAllshowDialog.getRoomId());
            });
        }
    }


    private Integer getNumberResId(Integer number) {
        switch (number) {
            case 0: {
                return R.mipmap.voice_0;
            }
            case 1: {
                return R.mipmap.voice_1;
            }
            case 2: {
                return R.mipmap.voice_2;
            }
            case 3: {
                return R.mipmap.voice_3;
            }
            case 4: {
                return R.mipmap.voice_4;
            }
            case 5: {
                return R.mipmap.voice_5;
            }
            case 6: {
                return R.mipmap.voice_6;
            }
            case 7: {
                return R.mipmap.voice_7;
            }
            case 8: {
                return R.mipmap.voice_8;
            }
            case 9: {
                return R.mipmap.voice_9;
            }
        }
        return R.mipmap.voice_0;
    }

    /**
     * ????????????
     *
     * @param
     */
    private void setEmojiShow(EmojiMessageBean emojiMessageBean) {
        int emojiCode = emojiMessageBean.getData().getEmojiCode();
        int numberShow = emojiMessageBean.getData().getNumberShow();
        AnimationDrawable animationDrawable;
        if (emojiCode >= 128604) {
            if (emojiCode == 128629 || emojiCode == 128630) {
                Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.view_rotate);
                rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ll_rotate.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                ArrayList<Integer> randomNumbers = emojiMessageBean.getData().getRandomNumbers();
                iv_number_1.setImageResource(getNumberResId(randomNumbers.get(0)));
                iv_number_2.setImageResource(getNumberResId(randomNumbers.get(1)));
                iv_number_3.setImageResource(getNumberResId(randomNumbers.get(2)));
                ll_rotate.setVisibility(View.VISIBLE);

                iv_number_1.setAnimation(rotateAnimation);
                iv_number_2.setAnimation(rotateAnimation);
                iv_number_3.setAnimation(rotateAnimation);
                iv_number_1.startAnimation(rotateAnimation);
                iv_number_2.startAnimation(rotateAnimation);
                iv_number_3.startAnimation(rotateAnimation);
            } else {
                ivShowsvgaVoice.setVisibility(View.VISIBLE);
//            SVGAParser parser = new SVGAParser(mContext);
//            parser.decodeFromAssets(ImageShowUtils.getInstans().getAssetsName(emojiCode), new SVGAParser.ParseCompletion() {
//                @Override
//                public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
//                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
//                    ivShowsvgaVoice.setImageDrawable(drawable);
//                    ivShowsvgaVoice.startAnimation();
//                }
//
//                @Override
//                public void onError() {
//                    ivShowsvgaVoice.setVisibility(View.GONE);
//                    LogUtils.e("svga", "??????????????????");
//                }
//            });
                ivShowimgVoice.setVisibility(View.VISIBLE);
                ImageUtils.loadGif(ivShowsvgaVoice, ImageShowUtils.getInstans().getResId(emojiCode, numberShow), this);
            }
        } else if (emojiCode >= 128564) { //gif??????
            ivShowimgVoice.setVisibility(View.VISIBLE);
            ImageUtils.loadDrawable(ivShowimgVoice, ImageShowUtils.getInstans().getResId(emojiCode, numberShow));
        } else if (emojiCode >= 128552) {
            ivShowimgVoice.setVisibility(View.VISIBLE);
            if (emojiCode == 128552) { //?????????
                ivShowimgVoice.setImageResource(R.drawable.pai_voice);
                // 1. ????????????
                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                int maiDrawable = getMaiDrawable(emojiMessageBean.getData().getNumberShow());
                if (maiDrawable != 0) {
                    animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                }
                // 2. ??????????????????
                animationDrawable.start();
            } else if (emojiCode == 128553) { //??????
                ivShowimgVoice.setImageResource(R.drawable.deng_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                animationDrawable.start();
            } else if (emojiCode == 128554) { //??????
                ivShowimgVoice.setImageResource(R.drawable.hand_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                animationDrawable.start();
            } else if (emojiCode == 128555) { //???????????????
                ivShowimgVoice.setImageResource(R.drawable.cai_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, numberShow);
                if (maiDrawable != 0) {
                    animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                }
                animationDrawable.start();
            } else if (emojiCode == 128562) { //??????
                ivShowimgVoice.setImageResource(R.drawable.zhi_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, numberShow);
                if (maiDrawable != 0) {
                    animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                }
                animationDrawable.start();
            } else if (emojiCode == 128563) { //??????
                ivShowimgVoice.setImageResource(R.drawable.yingbi_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, numberShow);
                if (maiDrawable != 0) {
                    animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                }
                animationDrawable.start();
            } else {
                ivShowimgVoice.setImageResource(ImageShowUtils.getInstans().getResId(emojiCode, numberShow));
            }
        } else {
            tvShowimgVoice.setText(new String(Character.toChars(emojiMessageBean.getData().getEmojiCode())));
            tvShowimgVoice.setVisibility(View.VISIBLE);
        }
        addTypeList(new VoiceTypeModel(2, 8, 3));
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                observable = Observable.just("0");
//                subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                if (tvShowimgVoice != null) {
//                                    tvShowimgVoice.setVisibility(View.GONE);
//                                }
//                                if (ivShowimgVoice != null) {
//                                    ivShowimgVoice.setVisibility(View.GONE);
//                                }
//                            }
//                        });
//            }
//        };
//        timer.schedule(timerTask, 3000);
    }

    /**
     * ?????????????????????
     *
     * @param dataBean
     */
    private void setGiftDialogShow(GiftSendMessage.DataBean dataBean) {
        if (TextUtils.isEmpty(dataBean.getShowImg())) {
            return;
        }
        if (giftList == null) {
            giftList = new ArrayList<>();
        }
        giftList.add(dataBean);
        initShowGift(giftList.get(giftNumberShow).getNum(), giftList.get(giftNumberShow).getShowImg());
    }

    /**
     * ????????????????????????
     */
    int chooseOneAccess;
    SpannableStringBuilder stringBuilder;

    private void openAccessShow() {
        if (chooseOneAccess < accessList.size()) {
            ChatMessageBean.DataBean dataBean = accessList.get(chooseOneAccess);
            accessRoomShow = true;
            chooseOneAccess++;
            stringBuilder = new SpannableStringBuilder("?????? ");
//            stringBuilder.append("img");
//            int resShowid = ImageShowUtils.getGrade(dataBean.getGrade());
//            ImageSpan imageSpan = new ImageSpan(mContext, resShowid);
//            stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            stringBuilder.append(" ");
            String accessName = dataBean.getName();
            stringBuilder.append(accessName);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
            stringBuilder.setSpan(colorSpan, stringBuilder.length() - accessName.length(),
                    stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvAccessVoice.setText(stringBuilder);
            tvAccessVoice.requestFocus();

            AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(VoiceActivity.this, R.animator.view_access_enter);
            animatorSet.setTarget(tvAccessVoice);
            animatorSet.setInterpolator(new LinearInterpolator());
            animatorSet.start();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    openAccessShow();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            accessList.clear();
            accessRoomShow = false;
            chooseOneAccess = 0;
            tvAccessVoice.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * ????????????????????????
     *
     * @param nuberShow
     * @return
     */
    private int getMaiDrawable(int nuberShow) {
        switch (nuberShow) {
            case 1:
                return R.drawable.s_number_1;
            case 2:
                return R.drawable.s_number_2;
            case 3:
                return R.drawable.s_number_3;
            case 4:
                return R.drawable.s_number_4;
            case 5:
                return R.drawable.s_number_5;
            case 6:
                return R.drawable.s_number_6;
            case 7:
                return R.drawable.s_number_7;
            case 8:
                return R.drawable.s_number_8;
        }
        return 0;
    }

    @BindView(R.id.lay_SVGAImageView_gift)
    RelativeLayout laySVGAImageView_gift;
    //    @BindView(R.id.mSVGAImageView_gift)
    SVGAImageView mSVGAImageViewGift;
    @BindView(R.id.tv_show_gift)
    TextView tvShowGift;
    @BindView(R.id.lay_gift_svga)
    LinearLayout lay_gift_svga;
    @BindView(R.id.iv_show_gift)
    SimpleDraweeView ivShowGift;
    @BindView(R.id.iv_show_png)
    SimpleDraweeView ivShowPng;

    private Timer svgaTimer;
    private SVGADrawable svgaDrawable;

    /**
     * ??????????????????
     *
     * @param imgShow
     * @param number
     */
    private void initShowGift(int number, String imgShow) {
        if (laySVGAImageView_gift.getVisibility() == View.VISIBLE) {
            return;
        }
        if (mSVGAImageViewGift != null && mSVGAImageViewGift.isAnimating()) {
            return;
        }
        if (ivShowGift == null || ivShowGift.getVisibility() == View.VISIBLE) {
            return;
        }
        laySVGAImageView_gift.setVisibility(View.VISIBLE);
        if (number != 0) {
            tvShowGift.setText("X" + number);
        } else {
            tvShowGift.setText("");
        }
        if (imgShow.endsWith(".svga")) {
            if (svgaTimer != null) {
                svgaTimer.cancel();
            }
            ImageUtils.loadDrawable(ivShowGift, R.drawable.trans_bg);
            ivShowGift.setVisibility(View.GONE);
            ImageUtils.loadDrawable(ivShowPng, R.drawable.trans_bg);
            ivShowPng.setVisibility(View.GONE);
            lay_gift_svga.setVisibility(View.VISIBLE);

            mSVGAImageViewGift = new SVGAImageView(VoiceActivity.this);
            mSVGAImageViewGift.setLoops(1);
            mSVGAImageViewGift.setClearsAfterStop(true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mSVGAImageViewGift.setLayoutParams(params);
            lay_gift_svga.addView(mSVGAImageViewGift);

            parser = new SVGAParser(VoiceActivity.this);
            try {
                parser.parse(new URL(imgShow), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                        if (mSVGAImageViewGift != null) {
                            svgaDrawable = new SVGADrawable(svgaVideoEntity);
                            mSVGAImageViewGift.setImageDrawable(svgaDrawable);
                            mSVGAImageViewGift.startAnimation();
                        }
                    }

                    @Override
                    public void onError() {
                        LogUtils.e("svga", "??????????????????");
                        handler.sendEmptyMessage(101);
                    }
                });
            } catch (MalformedURLException e) {
                LogUtils.e("svga", "??????????????????");
                handler.sendEmptyMessage(101);
            }

            mSVGAImageViewGift.setCallback(new SVGACallback() {
                @Override
                public void onPause() {
                    LogUtils.e("svga", "onPause");
                }

                @Override
                public void onFinished() {
                    LogUtils.e("msg", "svga??????");
                    handler.sendEmptyMessageDelayed(101, 1000);
                }

                @Override
                public void onRepeat() {

                }

                @Override
                public void onStep(int i, double v) {

                }
            });
        } else if (imgShow.endsWith(".gif")) {
            lay_gift_svga.removeAllViews();
            lay_gift_svga.setVisibility(View.GONE);
            ImageUtils.loadDrawable(ivShowPng, R.drawable.trans_bg);
            ivShowPng.setVisibility(View.GONE);
            ivShowGift.setVisibility(View.VISIBLE);

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(imgShow)
                    .setAutoPlayAnimations(true)
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);

                            if (svgaTimer != null) {
                                svgaTimer.cancel();
                            }
                            svgaTimer = new Timer();
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(101);
                                }
                            };
                            svgaTimer.schedule(timerTask, 5000);
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            super.onFailure(id, throwable);
                            handler.sendEmptyMessage(101);
                        }
                    })
                    .build();
            ivShowGift.setController(controller);
        } else {
            lay_gift_svga.removeAllViews();
            lay_gift_svga.setVisibility(View.GONE);
            ImageUtils.loadDrawable(ivShowGift, R.drawable.trans_bg);
            ivShowGift.setVisibility(View.GONE);
            ivShowPng.setVisibility(View.VISIBLE);

            ImageUtils.loadUri(ivShowPng, imgShow);

            if (svgaTimer != null) {
                svgaTimer.cancel();
            }
            svgaTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(101);
                }
            };
            svgaTimer.schedule(timerTask, 2000);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:

                    if (isFinishing()) return;

                    clearGiftShow(true);

                    if (giftNumberShow < giftList.size() - 1) {
                        handler.sendEmptyMessageDelayed(102, 1000);
                    } else {
                        giftNumberShow = 0;
                        giftList.clear();
                    }
                    break;
                case 102:
                    giftNumberShow++;
                    initShowGift(giftList.get(giftNumberShow).getNum(), giftList.get(giftNumberShow).getShowImg());
                    break;
            }
        }
    };

    /**
     * ??????????????????
     *
     * @param isContinue
     */
    private void clearGiftShow(boolean isContinue) {
        if (mSVGAImageViewGift != null) {
            mSVGAImageViewGift.stopAnimation();
            mSVGAImageViewGift.clearAnimation();
        }
        if (lay_gift_svga != null) {
            lay_gift_svga.removeAllViews();
            lay_gift_svga.setVisibility(View.GONE);
        }
        if (ivShowGift != null) {
            ImageUtils.loadDrawable(ivShowGift, R.drawable.trans_bg);
            ivShowGift.setVisibility(View.GONE);
        }
        if (ivShowPng != null) {
            ImageUtils.loadDrawable(ivShowPng, R.drawable.trans_bg);
            ivShowPng.setVisibility(View.GONE);
        }
        laySVGAImageView_gift.setVisibility(View.GONE);

        if (svgaTimer != null) {
            svgaTimer.cancel();
        }
        parser = null;
        svgaDrawable = null;
        mSVGAImageViewGift = null;

        if (!isContinue) {
            giftNumberShow = 0;
            if (giftList != null) {
                giftList.clear();
            }
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        }
    }

    private void initShow() {

        MyUtils.getInstans().keepScreenLongLight(this, true);
        showHeader(false);
        showTitle(false);
        setBlcakShow(false);
//        isCanSetMargin = true;

        voicePresenter = new VoicePresenter(this, new VoiceModel(this));
        //?????????manner??????
        addToPresenterManager(voicePresenter);

        if (Const.chatShow == null) {
            chatShowChat = new ArrayList<>();
        } else if (Const.RoomId.equals(String.valueOf(roomId))) {//???????????????????????????????????????????????????
            chatShowChat = Const.chatShow;
        } else {
            chatShowChat = new ArrayList<>();
            Const.chatShow.clear();
        }


        setPlaceRecycler();
        setChatRecycler();
        setIsOpen();
//        setShow();

        setRoomTouch();
        setMsgShow();
        //??????????????????????????????
        packetNumber = Const.packetNumber;
        if (packetNumber > 0) {
            isHavePacket = true;
        }
        setPacketShow();


    }


    private void setMsgShow() {
        // ?????????????????????
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
    }


    @SuppressLint("SetTextI18n")
    private void setShow(VoiceUserBean.DataBean dataBean) {
        roomImg = dataBean.getImg();
        roomUserId = dataBean.getId();
        roomUserName = dataBean.getName();
        ImageUtils.loadUri(ivTopnameVoice, roomImg);
        if (!TextUtils.isEmpty(dataBean.getUserTh()) && dataBean.getUserTh().endsWith(".svga")) {
            ivTopnameHW.setVisibility(View.VISIBLE);
//            ImageUtils.loadUri(ivTopnameHW, dataBean.getUserTh());
            SVGAParser parser = new SVGAParser(VoiceActivity.this);
            try {
                parser.parse(new URL(dataBean.getUserTh()), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                        SVGADrawable svgaDrawable = new SVGADrawable(svgaVideoEntity);
                        ivTopnameHW.setImageDrawable(svgaDrawable);
                        ivTopnameHW.startAnimation();
                    }

                    @Override
                    public void onError() {
                        LogUtils.e("svga", "??????????????????");
                        ivTopnameHW.setVisibility(View.GONE);
                    }
                });
            } catch (MalformedURLException e) {
                LogUtils.e("svga", "??????????????????");
                ivTopnameHW.setVisibility(View.GONE);
            }
        } else {
//            ivTopnameHW.setVisibility(View.GONE);
        }
    }

    /**
     * ??????????????????
     */
    private void setRoomShow() {

        tvHomenameVoice.setText(roomBean.getName());
        if (StringUtils.isEmpty(roomBean.getLiang())) { //??????????????????
            tvHomeidVoice.setText("ID:" + roomId);
        } else {
            tvHomeidVoice.setText("ID:" + roomBean.getLiang());
        }


        if (TextUtils.isEmpty(roomBean.getLiang())) {
            liang_iv.setVisibility(View.GONE);
        } else {
            liang_iv.setVisibility(View.VISIBLE);
        }

        tvHomenumVoice.setText("?????????" + roomBean.getLineNum() + "??? >");//????????????
        tvHometypeVoice.setText(roomBean.getRoomLabel());//????????????

        if (roomBean.getStatus() == 2) {  //???????????????(??????????????????)
            showToast("??????????????????");
            //??????????????????
            Const.MusicShow.isHave = false;
//            Const.RoomId = "";
            packetNumber = 0;//??????????????????????????????
            isOpenMicrophone = false;
            isOpenReceiver = false;
            //????????????
            MyApplication.getInstance().getWorkerThread().musicStop();
            //????????????????????????
            MyApplication.getInstance().getWorkerThread().leaveChannel(roomId);
            Const.RoomId = "";
            //????????????????????????
            YySignaling.getInstans().leaveChannel(roomId);
            ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
            return;
        }

        if (roomBean.getIsfz() == 1) {
            tvLeaveVoice.setVisibility(View.INVISIBLE);
        } else if (roomBean.getIsfz() == 2) {
            tvLeaveVoice.setVisibility(View.VISIBLE);
        }

        roomTopic = roomBean.getRoomTopic();
        topicCount = roomBean.getRoomCount();
        tvHomedetailsVoice.setText(roomBean.getRoomName());//????????????

        tvGiftpointVoice.setText(roomBean.getRoomNum() + "");//???????????????

        int roomState = roomBean.getState();
//        if (roomState == 1) { //?????????????????????????????????
////            ivPacketVoice.setVisibility(View.GONE);
//        } else if (roomState == 2) {
////            ivPacketVoice.setVisibility(View.VISIBLE);
//            String roomBackImg = roomBean.getBjImg();
//            if (!StringUtils.isEmpty(roomBackImg)) {
//                ImageUtils.loadUri(ivVoicebackVoice, roomBackImg);//????????????
//            }
//        }
        String roomBackImg = roomBean.getBjImg();
        if (!StringUtils.isEmpty(roomBackImg)) {
            ImageUtils.loadUri(ivVoicebackVoice, roomBackImg);//????????????
        }

        if (userRoomType == 1) { //?????????????????????
            rlMusicVoice.setVisibility(View.VISIBLE);
        }

        if (roomBean.getIsJp() == 1) {
            ivAuctionVoice.setVisibility(View.GONE);
        } else if (roomBean.getIsJp() == 2) { //????????????
            ivAuctionVoice.setVisibility(View.VISIBLE);
        }

//        if (roomBean.getIsPk()==2){//??????????????????pk
//
//        }

        if (roomBean.getIsGp() == 1) {
            isOpenGp = true;
            ivMessageVoice.setImageResource(R.drawable.bottom_message);
        } else if (roomBean.getIsGp() == 2) {
            isOpenGp = false;
            ivMessageVoice.setImageResource(R.drawable.bottom_message_close);
        }

//        if (roomBean.getIsPk() == Const.IntShow.TWO && roomBean.getIsPkState() == Const.IntShow.TWO) {
//            voicePresenter.pkData(roomPid);
//        }
    }

    /**
     * ????????????
     */
    private void showMyPasswordDialog() {
        if (roomPassDialog != null && roomPassDialog.isShowing()) {
            roomPassDialog.dismiss();
        }
        roomPassDialog = new MyRoomPassDialog(VoiceActivity.this);
        roomPassDialog.show();
        roomPassDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPass = roomPassDialog.getPassword();
                if (inputPass.equals(roomPass)) { //?????????????????????????????????
                    roomPassDialog.dismiss();
//                    voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.ONE);
                    openRoomAnimation();
                } else {
                    showToast("??????????????????");
                }
            }
        });
        roomPassDialog.setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomPassDialog.dismiss();
                voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
            }
        });
    }


    AGEventHandler agEventHandler = new AGEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            LogUtils.e(TAG, channel + "onJoinChannelSuccess" + uid + "  " + elapsed);
//            if (consumer == null || subscribe.isDisposed()) {
//                consumer = null;
//                initConsumer();
//            }
//            observable = Observable.just(uid + "????????????");
//            subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(consumer);
        }

        @Override
        public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
            LogUtils.e(TAG, "??????????????????");
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            LogUtils.e(TAG, "onUserOffline" + uid + "  " + reason);
//            if (consumer == null || subscribe.isDisposed()) {
//                consumer = null;
//                initConsumer();
//            }
//            String msgShow = null;
//            if (reason == 0) {
//                msgShow = uid + "????????????";
//            } else if (reason == 2) {
//                msgShow = uid + "????????????";
//            }
//            observable = Observable.just(msgShow);
//            subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(consumer);

//            if (reason == USER_OFFLINE_DROPPED || reason == USER_OFFLINE_QUIT) { //??????????????????????????????????????????????????????
//                voicePresenter.getChatRoom(uid, roomId, Const.IntShow.TWO);
//            }
        }

        @Override
        public void onExtraCallback(final int type, final Object... data) {
//            LogUtils.d(TAG, "onExtraCallback" + type + "  " + data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        return;
                    }
                    doHandleExtraCallback(type, data);
                }
            });
        }
    };


    private void doHandleExtraCallback(int type, Object... data) {
        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_MIC://true?????????????????????* false?????????????????????

                break;
            case AGEventHandler.EVENT_TYPE_ON_USER_JOIN://????????????/?????????????????????????????????
                LogUtils.e("????????????/??????????????????????????????");
                break;
            case AGEventHandler.EVENT_TYPE_USERMUTEAUDIO://????????????????????????

                break;
            case AGEventHandler.EVENT_TYPE_VOLUMEINDICATION://????????????????????????????????????????????????????????????
                IRtcEngineEventHandler.AudioVolumeInfo[] speakers = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];
                showMicPersonAnimation(speakers);
                break;
            case AGEventHandler.EVENT_TYPE_AUDIOFINISH://????????????????????????
                if (musicDialog != null) {
                    musicDialog.nextPlay();
                }
                break;
            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR://??????????????????
//                int errorCode = (int) data[0];
//                showToast("?????????????????????" + errorCode);
                break;
            case AGEventHandler.EVENT_TYPE_ON_LOST_ERROR://????????????????????????
//                showToast("??????????????????????????????...");
                break;
        }
    }

    //??????????????????????????????
    private void showMicPersonAnimation(IRtcEngineEventHandler.AudioVolumeInfo[] speakers) {
        chatUserList = placeRecyclerAdapter.getData();
        for (int i = 0; i < speakers.length; i++) {
            LogUtils.e("?????????id???" + speakers[i].uid);
            if (speakers[i].uid == roomUserId) { //???????????????????????????????????????
                setHomeAnimation(true);
                addTypeList(new VoiceTypeModel(3, 8, 3));
                continue;
            } else if (speakers[i].uid == 0) { //??????0?????????
                if (userRoomType != 1 && isOpenMicrophone) { //?????????????????????????????????????????????
                    speakers[i].uid = userToken;
                }
            }
            for (int j = 0; j < chatUserList.size(); j++) {
                VoiceMicBean.DataBean usetListOne = chatUserList.get(j);
                if (usetListOne.getUserModel() != null) { //??????????????????
                    if (usetListOne.getUserModel().getId() == speakers[i].uid) {
                        usetListOne.getUserModel().setSpeak(true);
                        if (placeRecyclerAdapter != null) {
                            if (placeRecyclerAdapter.getItem(j) != null) {
                                placeRecyclerAdapter.setData(j, usetListOne);
                            }
                            addTypeList(new VoiceTypeModel(3, j, 3));
                        }
                        break;
                    }
                }
            }

//            for (VoiceMicBean.DataBean dataBean : chatUserList) {
//                if (dataBean.getUserModel() == null) {
//                    continue;
//                } else if (dataBean.getUserModel().getId() == speakers[i].uid) {
//                    dataBean.getUserModel().setSpeak(true);
//                }
//            }

        }
    }

//    ValueAnimator valueAnimator;

    //???????????????????????????
    Animation animation;
    Animation animationOne;

    private void setHomeAnimation(boolean isOpenMicrophone) {
        if (isOpenMicrophone) {
            ivTopnamebackVoice.setImageResource(R.drawable.bg_round_green);
            if (animation == null) {
                animation = AnimationUtils.loadAnimation(this, R.anim.view_room);
                animation.setRepeatCount(Animation.INFINITE);
                ivTopnamebackVoice.startAnimation(animation);
            } else {
                ivTopnamebackVoice.startAnimation(animation);
            }
//            if (valueAnimator == null) {
//                valueAnimator = ValueAnimator.ofInt(ivTopnamebackVoice.getLayoutParams().width
//                        , rlTopimgVoice.getLayoutParams().width);
//                valueAnimator.setDuration(800);
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        int currentValue = (Integer) animation.getAnimatedValue();
//                        ivTopnamebackVoice.getLayoutParams().width = currentValue;
//                        ivTopnamebackVoice.getLayoutParams().height = currentValue;
//                        // ?????????????????????????????????????????????????????????
//                        ivTopnamebackVoice.requestLayout();
//                    }
//                });
//
//                valueAnimator.setRepeatCount(Animation.INFINITE);//??????????????????
//                valueAnimator.setRepeatMode(ObjectAnimator.RESTART);// ????????????
//                valueAnimator.setInterpolator(new DecelerateInterpolator());
//                valueAnimator.start();
//            } else {
//                valueAnimator.resume();
//            }

        } else {
            ivTopnamebackVoice.clearAnimation();
            ivTopnamebackVoice.setImageResource(R.drawable.bg_round_blue);

        }
    }

//    //??????????????????????????????????????????
//    private void setHomeOneAnimation(boolean isShow) {
//        if (isShow) {
//            if (animationOne == null) {
//                animationOne = AnimationUtils.loadAnimation(this, R.anim.view_room);
//                animationOne.setRepeatCount(3);
//                ivTopnamebackVoice.startAnimation(animationOne);
//            } else {
//                ivTopnamebackVoice.startAnimation(animationOne);
//            }
//        } else {
//            ivTopnamebackVoice.clearAnimation();
//        }
//    }

    //???????????????????????????????????????

    private void setIsOpen() {
        edtInputMychat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
//                //??????????????????????????????
                VoiceActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                //?????????????????????
                int screenHeight = 0;
//                if (NavigationBarUtil.checkDeviceHasNavigationBar(VoiceActivity.this)) {
//                    if (NavigationBarUtil.checkNavigationBarShow(VoiceActivity.this, VoiceActivity.this.getWindow())) {
//                        screenHeight = VoiceActivity.this.getWindow().getDecorView().getRootView().getHeight();
//                    } else {
//                        screenHeight = NavigationBarUtil.getRealHeight(VoiceActivity.this);
//                    }
//                } else {
//                    screenHeight = VoiceActivity.this.getWindow().getDecorView().getRootView().getHeight();
//                }
                screenHeight = VoiceActivity.this.getWindow().getDecorView().getRootView().getHeight();
                setVIewShow(screenHeight, rect);
            }
        });
    }

    private void setVIewShow(int screenHeight, Rect rect) {
        //????????????????????????????????????????????? ?????????????????????????????? ????????????0 ????????????????????????????????????
        int keyboardHeight = screenHeight - rect.bottom;

        LogUtils.e("msg", keyboardHeight + " " + screenHeight);
//                float pmmd = getResources().getDisplayMetrics().density;
        if (keyboardHeight == 0) {
            if (!isOpenChat && isCanOpen) {
                rlChatBack.setVisibility(View.VISIBLE);
                isOpenChat = true;
            } else if (isOpenChat && !isCanOpen) {
                rlChatBack.setVisibility(View.GONE);
                isOpenChat = false;
            }
        } else if (keyboardHeight <= screenHeight / 4) {
            rlChatBack.setVisibility(View.GONE);
            isOpenChat = false;
        } else {
            if (isCanOpen) {
                isCanOpen = false;
                rlChatBack.setVisibility(View.VISIBLE);
                isOpenChat = true;
                int keyboardNow = (int) (screenHeight - keyboardHeight);//??????????????????????????????
                setBoottomMargin(keyboardNow);
                if (noShowString == null) {
                    try {
                        noShowString = readFile02("ReservedWords-utf8.txt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * ???????????????
     *
     * @param path
     * @return
     * @throws IOException
     */
    public List<String> readFile02(String path) throws IOException {
        // ?????????????????????????????????????????????????????? ????????????String []??????
        List<String> list = new ArrayList<String>();
//        FileInputStream fis = new FileInputStream(path);
        InputStream in = getResources().getAssets().open(path);
        // ??????????????????   ??????utf-8 ??????  ???GBK     eclipse????????????txt  ???UTF-8??????????????????????????????txt  ???GBK
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            // ?????? t x t?????????????????? ?????????---?????????       ?????????????????????????????????????????????
            if (line.lastIndexOf("---") < 0) {
                list.add(line);
            }
        }
        br.close();
        isr.close();
        in.close();
        return list;
    }


    private void setBoottomMargin(int keyboardNow) {
        if (keyboardNow > 100) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(llChatVoice.getLayoutParams());
            int llvoiceHighe = lp.height;//?????????????????????
            //????????????????????????????????????
            LogUtils.e("??????" + (keyboardNow - llvoiceHighe));
            lp.setMargins(0, keyboardNow - llvoiceHighe, 0, 0);
            llChatVoice.setLayoutParams(lp);

            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(llChattypeVoice.getLayoutParams());
            int llvoiceHighe1 = lp1.height;//?????????????????????
            //????????????????????????????????????
            LogUtils.e("??????lp1" + (llvoiceHighe1));
            lp1.setMargins(0, keyboardNow - llvoiceHighe - llvoiceHighe1, 0, 0);
            llChattypeVoice.setLayoutParams(lp1);
        }
    }

    /**
     * ?????????????????????
     */
    private void setChatRecycler() {
        chatRecyclerAdapter = new ChatRecyclerAdapter(R.layout.item_chat_voice);
        chatRecyclerAdapter.setOnAttentionClicker(new ChatRecyclerAdapter.OnAttentionClicker() {
            @Override
            public void onClicker(int userId) {
                if (userId != 0) {
                    showOtherDialog(userToken, userId);
//                    showMyPseronDialog(userToken, userId);

//                    getAttentionCall();
                }
            }

            @Override
            public void onFollowUser(int userId) {
                getAttentionCall(userToken);
            }

            @Override
            public void onOtherRoomClicker(String RoomId) {
                toOtherRoom(RoomId);
//                if (RoomId.equals(roomId)) {
//                    showToast("??????????????????");
//                    return;
//                }
//                Bundle bundle = new Bundle();
//                bundle.putString(Const.ShowIntent.ROOMID, RoomId);
//                ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
//                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewChatVoice.setLayoutManager(layoutManager);
        mRecyclerViewChatVoice.setAdapter(chatRecyclerAdapter);


        chatRecyclerAdapter.setNewData(chatShowChat);

        mRecyclerViewChatVoice.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    tvHasMessageVoice.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (Const.RoomId.equals(String.valueOf(roomId))) {//???????????????????????????????????????????????????
            mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
        }
    }

    // ????????????????????????
    private void getAttentionCall(int uid) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("buid", roomUserId);
        map.put("type", 1);
        HttpManager.getInstance().post(Api.addAttention, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("????????????");
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }


    //????????????????????????
    OtherShowDialog otherShowDialog;

    private void showOtherDialog(int userId, int otherId) {
        if (otherShowDialog != null && otherShowDialog.isShowing()) {
            otherShowDialog.dismiss();
        }
        ArrayList<String> seatList = new ArrayList<>();
        seatList.add(getString(R.string.tv_sendgift_bottomshow));
        seatList.add(getString(R.string.tv_message_bottomshow));
        seatList.add(getString(R.string.tv_sixin));
        otherShowDialog = new OtherShowDialog(this, seatList, userId, otherId);
        otherShowDialog.show();
        otherShowDialog.setOnItemClickListener((position, user) -> {
            voicePresenter.showOtherDialog(position, userId, user);
        });
    }

    /**
     * ????????????????????????adapter
     */
    private void setPlaceRecycler() {
        placeRecyclerAdapter = new PlaceRecyclerAdapter(R.layout.item_place_voice);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerViewPlaceVoice.setLayoutManager(layoutManager);
        mRecyclerViewPlaceVoice.setAdapter(placeRecyclerAdapter);

        placeRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (roomBean == null)
                    return;
                VoiceMicBean.DataBean dataBean = (VoiceMicBean.DataBean) adapter.getItem(position);
                assert dataBean != null;
                voicePresenter.PlaceClicker(dataBean, position, userToken, userRoomType, roomId, roomBean.getState());
            }
        });
    }

    @Override
    public void setResume() {
//        LogUtils.e("msg", "????????????");

        //????????????????????????(??????)
        String userRoomImg = (String) SharedPreferenceUtils.get(this, Const.User.IMG, "");
        if (!userRoomImg.equals(roomImg) && userToken == roomUserId) {
            roomImg = userRoomImg;
            ImageUtils.loadUri(ivTopnameVoice, roomImg);
        }
    }

    @Override
    public void setOnclick() {

    }

    @Override
    public void onBackPressed() {
//        if (Const.chatShowChat != null) {
//            Const.chatShowChat.clear();
//        }
        if (System.currentTimeMillis() - nextGoRoomTime < 1000) {
            return;
        }
        //?????????????????????????????????????????????????????????
        if (laySVGAImageView_gift.getVisibility() == View.VISIBLE) {
            clearGiftShow(false);
            return;
        }
        Const.chatShow = chatShowChat;
        BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.MUSIC_REPLAY);
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.MUSIC_PAUSE);
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.MUSIC_PLAY);
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.PACKET_OVER);
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.MSG_COUN);
//        if (valueAnimator != null) {
//            valueAnimator.cancel();
//            valueAnimator.end();
//        }

        handler.removeCallbacksAndMessages(null);

        MyApplication.getInstance().delChatRoomMessage(chatRoomMessage1);

        if (animation != null) {
            animation.cancel();
        }
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }
        if (timerType != null) {
            timerType.cancel();
        }

        if (subscribe != null) {
            subscribe.dispose();//????????????
        }
        MyApplication.getInstance().getWorkerThread().eventHandler().removeEventHandler(agEventHandler);
        MessageUtils.getInstans().removeChatShows(chatMessage);
        Const.packetNumber = packetNumber;//????????????????????????

        if (chestsDialog != null) {
            chestsDialog.dismiss();
        }

        mSVGAImageViewGift = null;
        parser = null;
        svgaDrawable = null;
        ivShowGift = null;

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        clearGiftShow(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_topimg_voice, R.id.tv_homename_voice, R.id.tv_homeid_voice,
            R.id.tv_homenum_voice, R.id.iv_homeset_voice,
            R.id.tv_homecon_voice, R.id.iv_auction_voice, R.id.rl_music_voice,
            R.id.tv_hometype_voice, R.id.tv_homedetails_voice_rl, R.id.rl_chat_back,
            R.id.chart_ll, R.id.iv_mic_voice, R.id.iv_receiver_voice,
            R.id.iv_express_voice, R.id.iv_envelope_voice, R.id.iv_packet_voice,
            R.id.iv_gift_voice, R.id.btn_send_mychat, R.id.rl_back_voice,
            R.id.edt_input_mychat, R.id.tv_has_message_voice, R.id.iv_chests_voice,
            R.id.iv_getpacket_voice, R.id.tv_chattype_voice, R.id.tv_chat2_voice,
            R.id.tv_chat1_voice, R.id.tv_alltopic_voice, R.id.back_iv, R.id.iv_act_gold, R.id.iv_act_star})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_topimg_voice://????????????
                VoiceMicBean.DataBean homeData = new VoiceMicBean.DataBean();
                homeData.setPid(roomId);
                VoiceUserBean.DataBean userShowBean = new VoiceUserBean.DataBean();
                userShowBean.setImg(roomImg);
                userShowBean.setId(roomUserId);
                userShowBean.setName(roomUserName);
                userShowBean.setUsercoding(roomId);
                homeData.setUserModel(userShowBean);
                voicePresenter.onUserClicker(homeData, userToken, userRoomType, roomId);
//                if (userRoomType == 1) {
//                    showMyPseronDialog(userToken, roomBean.getUid());
//                } else {

//                    showMyGiftDialog(userToken, roomBean.getUid(), Const.IntShow.ONE, roomBean.getName(), roomBean.getState(), false);
//                    if (giftDialog != null) {
//                        giftDialog.setSendGift(new MyGiftDialog.SendGift() {
//                            @Override
//                            public void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
//                                int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
//                                if (sendType == 1) { //1 ???????????? 2?????????
//                                    if (userGold < goodGold * num * sum) {
//                                        showMyDialog(getString(R.string.hint_nogold_gift),
//                                                Const.IntShow.THREE, getString(R.string.tv_topup_packet));
//                                        if (myDialog != null) {
//                                            myDialog.setRightButton(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    if (myDialog != null && myDialog.isShowing()) {
//                                                        myDialog.dismiss();
//                                                    }
//                                                    ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
//                                                }
//                                            });
//                                        }
//                                    } else {
//                                        voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
//                                    }
//                                } else if (sendType == 2) {
//                                    voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
//                                }
//                            }
//                        });
//                    }
//                }
                break;
            case R.id.tv_homename_voice:
                showMyPseronDialog(userToken, roomBean.getUid());
                break;
            case R.id.tv_homeid_voice:
                break;
            case R.id.tv_homenum_voice://????????????
                setUserDialogShow();
                break;
            case R.id.iv_homeset_voice://????????????

//                ??????????????????
//                YySignaling.getInstans().iCallBack.onMessageChannelReceive("52768034","1",369657133,
//                        "{\"code\":2022,\"data\":{\"effect\":\"https://hw29115726.obs.cn-east-3.myhuaweicloud.com/admin/b1df352c155a470f8d209a85f1ac800a.gif\",\"imgTx\":\"https://hw29115726.obs.cn-east-3.myhuaweicloud.com/admin/8c923fbd5613470283d277aa8e93bf37.jpg\",\"nickname\":\"????????????\",\"num\":8888}}");


                voicePresenter.SetClicker(userRoomType, roomBean);


//                initExitClicker();
//                setExitClicker(showMybottomDialog(bottomList));
                break;
            case R.id.tv_homecon_voice:
//                showMyRankingDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString("roomId", roomId);
                bundle2.putInt("userRoomType", userRoomType);
                RoomRankDialogFragment rankDialogFragment = new RoomRankDialogFragment();
                rankDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                rankDialogFragment.setArguments(bundle2);
                rankDialogFragment.show(getSupportFragmentManager(), "rank");
//                ActivityCollector.getActivityCollector().toOtherActivity(RoomRankActivity.class, bundle2);
                break;
            case R.id.iv_auction_voice:
                showAuction();
                break;
            case R.id.rl_music_voice:
                showMyMusicDialog();
                break;
            case R.id.tv_hometype_voice:
                break;
            case R.id.tv_homedetails_voice_rl://????????????
                if (userRoomType == 1 || userRoomType == 2) { //??????????????????
                    Bundle bundle = new Bundle();
                    bundle.putString(Const.ShowIntent.TOPIC, roomTopic);
                    bundle.putString(Const.ShowIntent.DATA, topicCount);
                    bundle.putString(Const.ShowIntent.ROOMID, roomId);
                    ActivityCollector.getActivityCollector().toOtherActivity(SetTopicActivity.class, bundle);
                } else {
//                    showTopicDialog();
                }
                break;
            case R.id.chart_ll://????????????
//                showMyChatDialog();
                if (!isOpenChat && isOpenGp) { //????????????????????????????????????
                    rlChatBack.setVisibility(View.VISIBLE);
                    edtInputMychat.setFocusable(true);
                    edtInputMychat.setFocusableInTouchMode(true);
                    edtInputMychat.requestFocus();
                    isCanOpen = true;
                    MyUtils.getInstans().showOrHahe(this);
                } else {
                    isOpenChat = false;
                }
                break;
            case R.id.iv_mic_voice://?????????
                if (isMicShow) { //?????????????????????????????????
                    if (isMicCan) { //??????????????????????????????
                        if (isOpenMicrophone) {
                            isOpenMicrophone = false;
                        } else {
                            isOpenMicrophone = true;
                        }
                        setMicisOpen();
                    }
                } else { //????????????????????????
                    if (userRoomType == 1) { //?????????????????????
                        isMicShow = true;
                        setMicisOpen();
                        return;
                    }
                    showBottomSheat();
                }
                break;
            case R.id.iv_receiver_voice://?????????
                if (isOpenReceiver) {
                    int isSuccess = MyApplication.getInstance().getWorkerThread().getRtcEngine().muteAllRemoteAudioStreams(true);
                    if (isSuccess == 0) {
                        isOpenReceiver = false;
                        openReceiverObservable = Observable.just(isOpenReceiver);
                        openReceiverObservable.subscribe(receiverConsumer);
//                        ivReceiverVoice.setSelected(false);
                    } else {
                        showToast("????????????????????????????????????");
                    }
                } else {
                    int isSuccess = MyApplication.getInstance().getWorkerThread().getRtcEngine().muteAllRemoteAudioStreams(false);
                    if (isSuccess == 0) {
                        isOpenReceiver = true;
                        openReceiverObservable = Observable.just(isOpenReceiver);
                        openReceiverObservable.subscribe(receiverConsumer);
//                        ivReceiverVoice.setSelected(true);
                    } else {
                        showToast("????????????????????????????????????");
                    }
                }
                break;
            case R.id.iv_express_voice://??????
                showMyEmojiDialog();
                break;
            case R.id.iv_envelope_voice://??????
                showMessageDialog();
                break;
            case R.id.iv_packet_voice://?????????
                showRewardDialog();
                break;
            case R.id.iv_gift_voice://?????????
                if (roomBean == null) { //??????
                    showMyGiftDialog(userToken, userToken, Const.IntShow.ONE, roomUserName, 1, true);
                } else {
                    showMyGiftDialog(userToken, userToken, Const.IntShow.ONE, roomUserName, roomBean.getState(), true);
                }
                if (giftDialog != null) {
                    giftDialog.setSendGift(new MyGiftDialog.SendGift() {
                        @Override
                        public void getSendGift(String ids, String names, int gid, String img,
                                                String showImg, int num, int sum, int goodGold, int sendType) {
                            int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
                            if (sendType == 1) { //1 ???????????? 2?????????
                                if (userGold < goodGold * num * sum) {
                                    showMyDialog(getString(R.string.hint_nogold_gift),
                                            Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                                    if (myDialog != null) {
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
                                } else {
                                    voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                                }
                            } else if (sendType == 2) {
                                voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                            }

                        }

                        @Override
                        public void getSendAllGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
                            onGiftSendSuccess(userToken, ids, names, gid, img, showImg, num, goodGold);
                        }
                    });
                }
                break;
            case R.id.btn_send_mychat://?????????????????????????????????
                if (isOpenChat) {
                    String chatInputShow = edtInputMychat.getText().toString();
                    if (StringUtils.isEmpty(chatInputShow)) {
                        showToast("?????????????????????");
                        return;
                    }
                    if (noShowString != null) {
                        for (String noShow : noShowString) {
                            if (noShow.equals(chatInputShow)) {
                                showToast("????????????????????????????????????");
                                return;
                            }
                        }
                    }
//                    ChatMessageBean.DataBean dataBean = new ChatMessageBean.DataBean(userToken, userName, goldNum, chatInputShow);
                    llChattypeVoice.setVisibility(View.INVISIBLE);
                    if (isAllChat) { //???????????????
                        voicePresenter.getAllChat(userToken, roomId, chatInputShow);
                        edtInputMychat.setText("");
                    } else {
                        int charmGrade = (int) SharedPreferenceUtils.get(this, Const.User.CharmGrade, 0);
                        String header = (String) SharedPreferenceUtils.get(this, Const.User.IMG, "");

                        ChatMessageBean.DataBean dataBean = new ChatMessageBean.DataBean(goldNum, charmGrade, header, chatInputShow, userName, userToken);
                        ChatMessageBean chatMessageBean = new ChatMessageBean(100, dataBean);
                        String chatString = new Gson().toJson(chatMessageBean);
                        setSendMessage(chatString);
                        edtInputMychat.setText("");
                    }
                    if (isOpenChat) {
                        MyUtils.getInstans().hideKeyboard(llChatVoice);
                        rlChatBack.setVisibility(View.GONE);
                        isOpenChat = false;
                    }
                }
                break;
            case R.id.rl_back_voice:

                break;
            case R.id.iv_chests_voice://??????
                showChestsDialog();
                break;
            case R.id.tv_has_message_voice://??????????????????
                tvHasMessageVoice.setVisibility(View.INVISIBLE);
                mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
                break;
            case R.id.rl_chat_back://??????????????????
                if (isOpenChat) {
                    MyUtils.getInstans().hideKeyboard(llChatVoice);
                    rlChatBack.setVisibility(View.GONE);
                    isOpenChat = false;
                    isCanOpen = false;
                }
                break;
            case R.id.iv_getpacket_voice://??????????????????
                Bundle bundle = new Bundle();
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().toOtherActivity(ReceivePacketActivity.class, bundle);
                break;
            case R.id.tv_chattype_voice://??????????????????
                if (llChattypeVoice.getVisibility() == View.VISIBLE) {
                    llChattypeVoice.setVisibility(View.INVISIBLE);
                } else {
                    llChattypeVoice.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.tv_chat2_voice://?????????
                isAllChat = true;
                llChattypeVoice.setVisibility(View.INVISIBLE);
                setAllChatShow();
                break;
            case R.id.tv_chat1_voice://??????
                isAllChat = false;
                llChattypeVoice.setVisibility(View.INVISIBLE);
                setAllChatShow();
                break;
            case R.id.tv_alltopic_voice://go ??????
                String rid = (String) tvAlltopicVoice.getTag();
                toOtherRoom(rid);
                break;
            case R.id.back_iv://??????
                onBackPressed();
                break;
            case R.id.iv_act_star://?????????????????????
                ActivityCollector.getActivityCollector().toOtherActivity(StarActivityActivity.class);
                break;
            case R.id.iv_act_gold://??????????????????
                ActivityCollector.getActivityCollector().toOtherActivity(RankActivityActivity.class);
                break;
        }
    }

    /**
     * ????????????????????????
     */
    MyGiftAllshowDialog myGiftAllshowDialog;

    private void showGiftAllDialog(ArrayList<GiftAllModel.DataBean> giftAllModels) {
        if (myGiftAllshowDialog != null && myGiftAllshowDialog.isShowing()) {
            myGiftAllshowDialog.setGiftAllModel(giftAllModels);
        } else {
            if (myGiftAllshowDialog != null) {
                myGiftAllshowDialog.dismiss();
            }
            myGiftAllshowDialog = new MyGiftAllshowDialog(this, giftAllModels);
            myGiftAllshowDialog.show();
            myGiftAllshowDialog.getButton().setOnClickListener(v -> {
                toOtherRoom(myGiftAllshowDialog.getRoomId());
            });
        }
    }

    /**
     * ??????????????????
     *
     * @param RoomId ????????????id
     */
    private void toOtherRoom(String RoomId) {
        if (RoomId.equals(roomId)) {
            showToast("??????????????????");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ROOMID, RoomId);
        ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
    }

    /**
     * ?????????????????????????????????
     */
    private void setAllChatShow() {
        if (isAllChat) {
            tvChattypeVoice.setText(R.string.hint_chat2_voice);
        } else {
            tvChattypeVoice.setText(R.string.hint_chat1_voice);
        }
    }

    /**
     * ????????????
     */
    private void showMessageDialog() {
//        if (bottomMessageDialog != null && bottomMessageDialog.isShowing()) {
//            bottomMessageDialog.dismiss();
//        }
//        bottomMessageDialog = new BottomMessageDialog(VoiceActivity.this);
//        bottomMessageDialog.show();
        messageDialogFragment = new MessageDialogFragment();
        messageDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        messageDialogFragment.show(getSupportFragmentManager(), "messageDialogFragment");
    }

    /**
     * Pk??????
     *
     * @param data
     */
    private void showMyPkDialog(PkSetBean.DataBean data) {
        if (pkDialog != null && pkDialog.isShowing()) {
            pkDialog.updateData(data);
        } else {
            if (this.isFinishing() || data == null) {
                return;
            }
            pkDialog = new MyPkDialog(VoiceActivity.this, userToken, data);
            pkDialog.show();
            pkDialog.setOneClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voicePresenter.setPkClicker(data.getState(), userToken, data.getId(),
                            data.getUser().getId(), data.getUser().getName());
                }
            });

            pkDialog.setTwoClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voicePresenter.setPkClicker(data.getState(), userToken, data.getId(),
                            data.getUser1().getId(), data.getUser1().getName());
                }
            });

            pkDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setPkShow();
                }
            });
        }
    }


    /**
     * ????????????
     */
    private void showChestsDialog() {
        clearGiftShow(false);

        if (chestsDialog != null && chestsDialog.isShowing()) {
            chestsDialog.dismiss();
        }
        chestsDialog = new MyChestsOneDialog(VoiceActivity.this, userToken, roomId);
        chestsDialog.setSendChestsMsg(new MyChestsOneDialog.SendChestsMsg() {
            @Override
            public void onSendChestsMsg(String msg) {
                sendTencentMsg(msg);
            }
        });
        chestsDialog.show();
        chestsDialog.getTopup().setOnClickListener(v -> {
            if (chestsDialog != null && chestsDialog.isShowing()) {
                chestsDialog.dismiss();
            }
            showToast("????????????");
//            ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
        });
        //            if (chestsDialog != null && chestsDialog.isShowing()) {
//                chestsDialog.dismiss();
//            }
//        chestsDialog.setChestsShow(this::showChestssShowDialog);
    }

    private long lastClickTime;

    /**
     * ???????????????
     *
     * @param id           ??????id
     * @param packetId     ??????id
     * @param packetNumber ????????????
     */
    @SuppressLint("CheckResult")
    private void showPacketDialog(int id, int packetId, String img, String nickName, int packetNumber) {
        if (packetDialog != null && packetDialog.isShowing()) {
            packetDialog.dismiss();
        }
        packetDialog = new MyPacketDialog(VoiceActivity.this, id, packetId, userToken, img, nickName, packetNumber);
        if (!isFinishing()) {
            packetDialog.show();
            packetDialog.setOpenOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voicePresenter.openPacket(id, packetId);
                }
            });
        }

//        Observable.just("").observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//
//                    }
//                });
    }

    /**
     * ???????????????
     */
    private void showRewardDialog() {
        if (rewardDialog != null && rewardDialog.isShowing()) {
            rewardDialog.dismiss();
        }
        rewardDialog = new MyRewardDialog(VoiceActivity.this, roomId, userToken, Const.IntShow.ONE);
        rewardDialog.show();
        rewardDialog.setSendRedPacket(new MyRewardDialog.SendRedPacket() {
            @Override
            public void getRedPacket(int gold, String ids, int num) {
                setSendRedPacketShow(gold, ids, num);
            }
        });
    }

    /**
     * ???????????????
     */
    private void showRewardDialog(int otherId, String otherName) {
        if (rewardDialog != null && rewardDialog.isShowing()) {
            rewardDialog.dismiss();
        }
        rewardDialog = new MyRewardDialog(VoiceActivity.this, roomId, userToken, otherId, otherName, Const.IntShow.TWO);
        rewardDialog.show();
        rewardDialog.setSendRedPacket(new MyRewardDialog.SendRedPacket() {
            @Override
            public void getRedPacket(int gold, String ids, int num) {
                setSendRedPacketShow(gold, ids, num);
            }
        });
    }

    /**
     * ?????????????????????
     *
     * @param gold
     * @param ids
     * @param num
     */
    private void setSendRedPacketShow(int gold, String ids, int num) {
        int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
        if (userGold < gold) {
            showMyDialog(getString(R.string.hint_nogold_packet),
                    Const.IntShow.THREE, getString(R.string.tv_topup_packet));
            if (myDialog != null) {
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
        } else {
            voicePresenter.sendRedPacket(userToken, roomId, gold, ids, num);
        }
    }

    /**
     * ?????????????????????????????????
     */
    private void setMicisOpen() {
        if (userRoomType == 1) { //?????????????????????????????????????????????
            Log.e("mmp", "isOpenMicrophone:" + isOpenMicrophone);
            Log.e("mmp", "isMicCan:" + isMicCan);
            setHomeAnimation(isOpenMicrophone);
        }
        if (isOpenMicrophone && isMicCan) { //???????????????
            int i = MyApplication.getInstance().getWorkerThread().getRtcEngine().muteLocalAudioStream(false);
            Log.e("mmp", "?????????????????????:" + i);

            if (i == 0) {
                LogUtils.e(LogUtils.TAG, "?????????????????????");
                setUpdateMic(Const.IntShow.ZERO);
            } else {
                LogUtils.e(LogUtils.TAG, "???????????????????????????");
            }
        } else { //???????????????
            int i = MyApplication.getInstance().getWorkerThread().getRtcEngine().muteLocalAudioStream(true);
            Log.e("mmp", "?????????????????????:" + i);
            if (i == 0) {
                LogUtils.e(LogUtils.TAG, "???????????????");
                setUpdateMic(Const.IntShow.ONE);
            } else {
                LogUtils.e(LogUtils.TAG, "???????????????????????????");
            }
        }
    }

    /**
     * ?????????
     *
     * @param hintShow  ????????????
     * @param typeShow  ???????????? ????????????
     * @param rightShow ??????????????????????????????
     */
    private void showMyDialog(String hintShow, final int typeShow, String rightShow) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(VoiceActivity.this);
        myDialog.show();
        myDialog.setHintText(hintShow);
        if (!StringUtils.isEmpty(rightShow)) {
            myDialog.setRightText(rightShow);
        }
//        myDialog.setRightButton(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (myDialog != null && myDialog.isShowing()) {
//                    myDialog.dismiss();
//                }
//                switch (typeShow) {
//                    case 1:
//                        voicePresenter.pkClose(roomPid);
//                        break;
//                    case 2:
//
//                        break;
//                    case 3:
//                        ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
//                        break;
//                }
//            }
//        });
    }


    private void showMyMusicDialog() {
        if (musicDialog != null && musicDialog.isShowing()) {
            musicDialog.dismiss();
        } else {
            musicDialog = new MyMusicDialog(VoiceActivity.this);
        }
        musicDialog.show();
    }


    /**
     * ????????????
     *
     * @param userId
     * @param otherId  //??????????????????id
     * @param type     //1?????????  2pk?????????
     * @param userName ???????????????????????????
     */
    private void showMyGiftDialog(int userId, int otherId, int type, String userName, int roomState, boolean isBootom) {
        if (giftDialog != null && giftDialog.isShowing()) {
            giftDialog.dismiss();
        }

        giftDialog = new MyGiftDialog(VoiceActivity.this, userId, otherId, type, userName, roomId, roomState, isBootom);
        giftDialog.show();
        giftDialog.setDataShow(v -> {
            int sendId = giftDialog.getOtherId();
            if (giftDialog != null && giftDialog.isShowing()) {
                giftDialog.dismiss();
            }
            showMyPseronDialog(userId, sendId);
        });
        giftDialog.setPacketGiftListener(v -> {
            int sendId = giftDialog.getOtherId();
            String userNamenow = giftDialog.getUserName();
            if (giftDialog != null && giftDialog.isShowing()) {
                giftDialog.dismiss();
            }
            if (sendId == userToken) {
                showToast("????????????????????????");
                return;
            }
            showRewardDialog(sendId, userNamenow);
        });
        giftDialog.setSendAllPacketGift((userId1, tab, giftID) -> {
            if (giftDialog != null && giftDialog.isShowing()) {
                giftDialog.dismiss();
            }
            if (tab == 5) {// ????????????
                voicePresenter.getSendAllGift(roomId, userToken, userId1);
            } else {// ????????????
                voicePresenter.getSendPartyGift(roomId, userToken, userId1, giftID);
            }
        });

    }

    /**
     * ???????????????
     */
    private void showMyEmojiDialog() {
        if (myExpressionDialog != null && myExpressionDialog.isShowing()) {
            myExpressionDialog.dismiss();
        }
        myExpressionDialog = new MyExpressionDialog(VoiceActivity.this);
        myExpressionDialog.show();
        myExpressionDialog.setEmojiChoose(new MyExpressionDialog.EmojiChoose() {
            @Override
            public void chooseOne(EmojiList.DataBean dataBean) {
                if (dataBean != null) {
                    int numberShow = getNumberShow(dataBean.getUnicode());
                    int charmGrade = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.CharmGrade, 0);
                    String header = (String) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.IMG, "");

                    if (dataBean.getUnicode() != 128629 && dataBean.getUnicode() != 128630) {
                        EmojiMessageBean.DataBean emojiData =
                                new EmojiMessageBean.DataBean(goldNum, charmGrade,
                                        dataBean.getUnicode(), header, userName, userToken, numberShow, dataBean.getName(), new ArrayList<>());
                        EmojiMessageBean emojiMessageBean = new EmojiMessageBean(108, emojiData);
                        String chatString = new Gson().toJson(emojiMessageBean);
                        setSendMessage(chatString);
                    } else {
                        if (dataBean.getUnicode() == 128630) {
                            showRandomDialog(charmGrade,dataBean,header,numberShow);
                        } else {
                            voicePresenter.getRandomNumber(userToken, new MyObserver(getSelfActivity()) {
                                @Override
                                public void success(String responseString) {
                                    RandomNumberBean baseBean = JSON.parseObject(responseString, RandomNumberBean.class);
                                    try {
                                        ArrayList<Integer> list = baseBean.getData();
                                        EmojiMessageBean.DataBean emojiData =
                                                new EmojiMessageBean.DataBean(goldNum, charmGrade,
                                                        dataBean.getUnicode(), header, userName, userToken, numberShow, dataBean.getName(), list);
                                        EmojiMessageBean emojiMessageBean = new EmojiMessageBean(108, emojiData);
                                        String chatString = new Gson().toJson(emojiMessageBean);
                                        setSendMessage(chatString);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private int getNumberShow(int uniCode) {
        Random ra = new Random();//???1-8????????????
        int number = 0;
        switch (uniCode) {
            case 128552://?????????
                number = ra.nextInt(8) + 1;
                break;
            case 128555://???????????????
                number = ra.nextInt(3) + 1;
                break;
            case 128562://??????
                number = ra.nextInt(6) + 1;
                break;
            case 128563://?????????
                number = ra.nextInt(2) + 1;
                break;
            case 128606://???????????????
                number = ra.nextInt(3) + 1;
                break;
            case 128615://?????????
                number = ra.nextInt(8) + 1;
                break;
            case 128621://??????
                number = ra.nextInt(6) + 1;
                break;
        }
        return number;
    }

    /**
     * ????????????
     *
     * @param userId  ??????id
     * @param otherId ????????????id
     */
    private void showMyPseronDialog(int userId, int otherId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.ID, otherId);
        if (otherId == userId) {
            ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
        } else {
            ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
        }

//        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
//            myBottomPersonDialog.dismiss();
//        }
//        myBottomPersonDialog = new MyBottomPersonDialog(VoiceActivity.this, userId, otherId);
//        myBottomPersonDialog.show();
    }

    /**
     * ?????????????????????
     */
    private void showMyRankingDialog() {
        if (myRankingDialog != null && myRankingDialog.isShowing()) {
            myRankingDialog.dismiss();
        }
        myRankingDialog = new MyRankingDialog(VoiceActivity.this, roomId, userToken);
        myRankingDialog.show();
    }

    /**
     * ??????????????????
     */
    private void showTopicDialog() {
        if (myTopicshowDialog != null && myTopicshowDialog.isShowing()) {
            myTopicshowDialog.dismiss();
        }
        myTopicshowDialog = new MyTopicshowDialog(VoiceActivity.this, roomTopic, topicCount);
        myTopicshowDialog.show();
    }

    /**
     * ??????????????????
     */
    private void showBottomSheat() {
        if (bottomWheatDialog != null && bottomWheatDialog.isShowing()) {
            bottomWheatDialog.dismiss();
        }
        bottomWheatDialog = new MyBottomWheatDialog(VoiceActivity.this, roomId, userToken);
        bottomWheatDialog.show();
    }

    /**
     * ???????????????
     */
    private void showAuction() {
        if (bottomauctionDialog != null && bottomauctionDialog.isShowing()) {
            bottomauctionDialog.dismiss();
        }
        bottomauctionDialog = new MyBottomauctionDialog(VoiceActivity.this, roomId);
        bottomauctionDialog.show();
    }

    /**
     * ??????????????????
     */
    private void setUserDialogShow() {
        if (myOnlineUserDialog != null && myOnlineUserDialog.isShowing()) {
            myOnlineUserDialog.dismiss();
        }
        myOnlineUserDialog = new MyOnlineUserDialog(VoiceActivity.this, roomId, userToken, Const.IntShow.ONE);
        myOnlineUserDialog.show();
        myOnlineUserDialog.setOnlineClicer(new MyOnlineUserDialog.OnlineClicer() {
            @Override
            public void setOnlineClicer(VoiceUserBean.DataBean dataBean, int position) {
//                showMyGiftDialog(userToken, otherId, Const.IntShow.ONE, userName, roomBean.getState());
//                voicePresenter.onLineClicker(dataBean, userRoomType, userToken, roomId, position, roomBean.getState());
                if (dataBean != null) {
                    showOtherDialog(userToken, dataBean.getId());
                }
            }
        });
    }

    //????????????
    private void setSendMessage(String chatInputShow) {
        if (StringUtils.isEmpty(chatInputShow)) {
            showToast("??????????????????");
        } else {
            YySignaling.getInstans().sendChannelMessage(roomId, chatInputShow);
        }
    }

    /**
     * ????????????????????????
     *
     * @param chatUserList
     */
    @Override
    public void onGetChatUserCallSuccess(List<VoiceMicBean.DataBean> chatUserList) {
        this.chatUserList = chatUserList;
        updateChatShow();
    }

    /**
     * ??????????????????
     */
    @SuppressLint("CheckResult")
    private void openRoomAnimation() {
        if (ivInputBackVoice.getVisibility() == View.VISIBLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(ivInputBackVoice, "alpha", 1, 0);
            animator.setDuration(1500);
            animator.start();
//            voicePresenter.getToken(userToken);//????????????token
            initVoice("");//?????????????????????
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ivInputBackVoice.setVisibility(View.GONE);
                }
            });
        } else {
            initVoice("");//?????????????????????
        }
    }

    /**
     * ????????????????????????
     *
     * @param userList
     */
    @Override
    public void onUserCallSuccess(List<VoiceUserBean.DataBean> userList) {

    }


    private void showRandomDialog(int charmGrade, EmojiList.DataBean dataBean,String header,int numberShow) {
        randomDialog = new SetRandomDialog(VoiceActivity.this, new SetRandomDialog.CallBack() {
            @Override
            public void setNumber(String oldResult,String result) {
                voicePresenter.setRandomNumber(userToken, result, new MyObserver(getSelfActivity()) {
                    @Override
                    public void success(String responseString) {
                        BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                        showToast(baseBean.getMsg());
                        if (baseBean.getCode() == 0) {
                            //?????????????????????????????????
                            voicePresenter.getRandomNumber(userToken, new MyObserver(getSelfActivity()) {
                                @Override
                                public void success(String responseString) {
                                    RandomNumberBean baseBean = JSON.parseObject(responseString, RandomNumberBean.class);
                                    try {
                                        ArrayList<Integer> list = baseBean.getData();
                                        EmojiMessageBean.DataBean emojiData =
                                                new EmojiMessageBean.DataBean(goldNum, charmGrade,
                                                        dataBean.getUnicode(), header, userName, userToken, numberShow, dataBean.getName(), list);
                                        EmojiMessageBean emojiMessageBean = new EmojiMessageBean(108, emojiData);
                                        String chatString = new Gson().toJson(emojiMessageBean);
                                        setSendMessage(chatString);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
//                            try {
//                                //?????????????????????
//                                ArrayList<Integer> list = new ArrayList<>();
//                                for (int index=0;index <oldResult.length(); index++) {
//                                    list.add(Integer.parseInt(oldResult.substring(index,index+1)));
//                                }
//                                EmojiMessageBean.DataBean emojiData =
//                                        new EmojiMessageBean.DataBean(goldNum, charmGrade,
//                                                dataBean.getUnicode(), header, userName, userToken, numberShow, dataBean.getName(), list);
//                                EmojiMessageBean emojiMessageBean = new EmojiMessageBean(108, emojiData);
//                                String chatString = new Gson().toJson(emojiMessageBean);
//                                setSendMessage(chatString);
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
                        }
                    }
                });
            }
        });
        randomDialog.show();
    }

    /**
     * ?????????????????????????????????
     *
     * @param type 1 ???????????? 2?????????
     * @Param sequence ??????
     */
    @Override
    public void onMicStateChange(int uid, int type, int sequence) {
//        voicePresenter.getChatShow(userToken, roomId);
        //????????????????????????????????????????????????
        ChatMessageBean chatMessageBean = new ChatMessageBean(118, new ChatMessageBean.DataBean());
        String carShowString = JSON.toJSONString(chatMessageBean);
        setSendMessage(carShowString);
        if (uid == userToken) {
//            isOpenMicrophone = true;
            if (type == 1) { //??????
                if (isOpenMicrophone) {
                    setUpdateMic(Const.IntShow.ZERO);
                } else {
                    setUpdateMic(Const.IntShow.ONE);
                }
                voicePresenter.getOnline(userToken, roomId);
            } else {
                isOpenMicrophone = false;
                setUpdateMic(Const.IntShow.TWO);
            }
        }

//        if (type == 1) {
//            voicePresenter.getChatShow(userToken, roomId);
//            setUpdateMic(true);
//        } else if (type == 2) {
//            voicePresenter.getChatShow(userToken, roomId);
////            VoiceMicBean.DataBean item = placeRecyclerAdapter.getItem(sequence - 1);
////            assert item != null;
////            Objects.requireNonNull(item).getUserModel().setId(0);
////            placeRecyclerAdapter.setData(sequence - 1, item);
////            placeRecyclerAdapter.notifyItemChanged(sequence - 1, item);
//            setUpdateMic(false);
//        }
    }

    /**
     * ?????????????????????????????????
     */
    private void setUpdateMic(int isUpMic) {
        if (isUpMic == 0) { //?????????   ????????????
            MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_BROADCASTER);
            ivMicVoice.setImageResource(R.drawable.selector_mic);
            ivMicVoice.setSelected(true);
            isMicShow = true;
            ivExpressVoice.setVisibility(View.VISIBLE);
            if (userRoomType != 3)
                rlMusicVoice.setVisibility(View.VISIBLE);
        } else if (isUpMic == 1) { //?????????   ?????????????????????????????????????????????
            MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_BROADCASTER);
            ivMicVoice.setImageResource(R.drawable.selector_mic);
            ivMicVoice.setSelected(false);
            isMicShow = true;
            ivExpressVoice.setVisibility(View.VISIBLE);
            if (userRoomType != 3)
                rlMusicVoice.setVisibility(View.VISIBLE);
        } else if (isUpMic == 2) { //????????????
            //???????????????????????????
            MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_AUDIENCE);
            if (Const.MusicShow.isHave) { //???????????????,????????????
                MyApplication.getInstance().getWorkerThread().musicStop();
            }
            isMicShow = false;
            ivMicVoice.setImageResource(R.drawable.bottom_mic_add);
            ivExpressVoice.setVisibility(View.GONE);
            rlMusicVoice.setVisibility(View.GONE);
        }
    }

    /**
     * ????????????
     *
     * @param position ??????
     */
    @Override
    public void onUserSetClicker(int position) {
        Bundle bundle = new Bundle();
        if (userRoomType == 1) {
            switch (position) {
                case 0://??????????????????????????????
                    if (roomBean.getIsGp() == 1) { //?????? 1 ????????? 2 ??????
                        voicePresenter.addOpenGp(roomId, Const.IntShow.TWO);
                    } else if (roomBean.getIsGp() == 2) {
                        voicePresenter.addOpenGp(roomId, Const.IntShow.ONE);
                    }
                    break;
                case 1://????????????
                    clearGiftShow(false);

                    bundle.putString(Const.ShowIntent.ROOMID, roomId);
                    ActivityCollector.getActivityCollector().toOtherActivity(RoomSetActivity.class, bundle);
                    break;
//                case 2://????????????
//                    if (roomBean.getState() == 2) { //????????????????????????
//                        if (roomBean.getIsJp() == 1) { //?????????????????? 1?????????2 ?????????
//                            voicePresenter.IsOpenJp(roomId, Const.IntShow.ONE);
//                        } else {
//                            voicePresenter.IsOpenJp(roomId, Const.IntShow.TWO);
//                        }
//                    } else {
//                        showToast("????????????????????????????????????");
//                    }
//                    break;
//                case 3://??????pk
//                    if (roomBean.getIsPk() == 1) { //????????????pk 1?????????2 ?????????
//                        bundle.putString(Const.User.ROOMID, roomId);
//                        ActivityCollector.getActivityCollector().toOtherActivity(RoomPkSetActivity.class, bundle);
//                    } else if (roomBean.getIsPk() == 2) {
//                        showMyDialog("???????????????pk????????????", Const.IntShow.ONE, null);
//                        if (myDialog != null) {
//                            myDialog.setRightButton(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (myDialog != null && myDialog.isShowing()) {
//                                        myDialog.dismiss();
//                                    }
//                                    voicePresenter.pkClose(roomPid);
//                                }
//                            });
//                        }
//                    }
//                    break;
                case 2://?????????
                    onBackPressed();
//                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
//                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    break;
                case 3://????????????
                    voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
                    break;

            }
        } else if (userRoomType == 2) {
            switch (position) {
                case 0://??????????????????????????????
                    if (roomBean.getIsGp() == 1) { //?????? 1 ????????? 2 ??????
                        voicePresenter.addOpenGp(roomId, Const.IntShow.TWO);
                    } else if (roomBean.getIsGp() == 2) {
                        voicePresenter.addOpenGp(roomId, Const.IntShow.ONE);
                    }
                    break;
                case 1://????????????
                    bundle.putString(Const.ShowIntent.ROOMID, roomId);
                    ActivityCollector.getActivityCollector().toOtherActivity(RoomSetActivity.class, bundle);
                    break;
//                case 2://?????????????????????
//                    if (roomBean.getIsJp() == 1) { //?????????????????? 1?????????2 ?????????
//                        voicePresenter.IsOpenJp(roomId, Const.IntShow.ONE);
//                    } else {
//                        voicePresenter.IsOpenJp(roomId, Const.IntShow.TWO);
//                    }
//                    break;
//                case 3://??????pk
//                    if (roomBean.getIsPk() == 1) { //????????????pk 1?????????2 ?????????
//                        bundle.putString(Const.User.ROOMID, roomId);
//                        ActivityCollector.getActivityCollector().toOtherActivity(RoomPkSetActivity.class, bundle);
//                    } else if (roomBean.getIsPk() == 2) {
//                        showMyDialog("???????????????pk????????????", Const.IntShow.ONE, null);
//                        if (myDialog != null) {
//                            myDialog.setRightButton(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (myDialog != null && myDialog.isShowing()) {
//                                        myDialog.dismiss();
//                                    }
//                                    voicePresenter.pkClose(roomPid);
//                                }
//                            });
//                        }
//                    }
//                    break;
                case 2://?????????
                    onBackPressed();
//                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
//                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    break;
                case 3://????????????
                    voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
                    break;

            }
        } else if (userRoomType == 3) {
            switch (position) {
                case 0://????????????
                    voicePresenter.updateReport(userToken, roomId);
                    break;
                case 1://?????????
                    onBackPressed();
//                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
//                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    break;
                case 2:
                    voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
                    break;
                case 3:

                    break;
            }
        }

    }

    /**
     * ??????????????????????????????
     *
     * @param position ???????????????
     * @param sequence ??????
     */
    @Override
    public void onUserDownClicker(int position, int sequence, String name) {
        switch (position) {
            case 0://????????????
                showMyPseronDialog(userToken, userToken);
                break;
            case 1://????????????
                voicePresenter.getMicUpdateCall(userToken, roomId, sequence, Const.IntShow.TWO);
                break;
            case 6://??????
                onMicSendGift(userToken, name);
                break;
        }
    }

    /**
     * ?????????????????????
     *
     * @param type ??????
     */
    @Override
    public void onChatRoom(int userId, int type) {
        if (userId == userToken) {
            switch (type) {
                case 1://????????????
                    //???????????????????????????????????????????????????
                    MessageUtils.getInstans().removeAllChats();

                    voicePresenter.getCall(userToken, roomId);
//                    voicePresenter.getChatShow(userToken, roomId);
                    addTencentChatRoom();
                    break;
                case 2://????????????
                    //??????????????????
                    Const.MusicShow.isHave = false;
                    packetNumber = 0;//??????????????????????????????
                    isOpenMicrophone = false;
                    isOpenReceiver = false;
                    Const.RoomId = "";
                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
                    //????????????
                    MyApplication.getInstance().getWorkerThread().musicStop();
                    //????????????????????????
                    MyApplication.getInstance().getWorkerThread().leaveChannel(roomId);

                    //????????????????????????
                    YySignaling.getInstans().leaveChannel(roomId);
                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    break;
            }
        }
    }

    //???????????????????????????

    TIMConversation timConversation;

    private void addTencentChatRoom() {
        TIMGroupManager.getInstance().applyJoinGroup(Const.roomChannelMsg, "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (!Objects.requireNonNull(VoiceActivity.this).isFinishing()) {
                    LogUtils.e(i + s);
                    //????????????
                    if (i == 6014 && StringUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
                        String userSig = (String) SharedPreferenceUtils.get(VoiceActivity.this,
                                Const.User.USER_SIG, "");
                        onRecvUserSig(userToken + "", userSig);
                    } else {
                        showToast("???????????????????????????????????????");
                    }
                }
            }

            @Override
            public void onSuccess() {
                LogUtils.i("?????????????????????");
                timConversation = TIMManager.getInstance().getConversation(
                        TIMConversationType.Group, Const.roomChannelMsg);
                timConversation.setReadMessage(timConversation.getLastMsg(), null);
            }
        });
        MyApplication.getInstance().setChatRoomMessage(chatRoomMessage);
        MyApplication.getInstance().addChatRoomMessage(chatRoomMessage1);
    }

    ChatRoomMessage chatRoomMessage1 = new ChatRoomMessage() {
        @Override
        public void onNewMessage(TIMMessage timMsg) {
            if (timMsg.getElement(0) instanceof TIMCustomElem) {
                JSONObject jsonObject = null;
                try {
                    AllMsgModel allMsgModel = new Gson().fromJson(new String(((TIMCustomElem) timMsg.getElement(0)).getData(),
                            "UTF-8"), AllMsgModel.class);
                    if (allMsgModel.getState() == 4) {
                        setCountMsg(allMsgModel.getData());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private void setCountMsg(AllmsgBean.DataEntity data) {
        AlltopicBean alltopicBean = new AlltopicBean();
        alltopicBean.setCode(119);
        AlltopicBean.DataEntity dataEntity = new AlltopicBean.DataEntity();
        dataEntity.setUid(data.getUid());
        dataEntity.setMessageShow(data.getContent());
        dataEntity.setUserImg(data.getImgTx());
        dataEntity.setRid(data.getRid());
        List<AlltopicBean.DataEntity> list = new ArrayList<>();
        list.add(dataEntity);
        alltopicBean.setData(list);
//                        sendTencentMsg(new Gson().toJson(alltopicBean));
        setTIMMessageShow(new Gson().toJson(alltopicBean));
    }

    ChatRoomMessage chatRoomMessage = new ChatRoomMessage() {
        @Override
        public void onNewMessage(TIMMessage timMsg) {
            if (timMsg.getElement(0) instanceof TIMCustomElem) {
                JSONObject jsonObject = null;
                try {
                    String msg = new String(((TIMCustomElem) timMsg.getElement(0)).getData(), "UTF-8");
                    setTIMMessageShow(msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void setTIMMessageShow(String msg) {
        MessageBean messageBean = new Gson().fromJson(msg, MessageBean.class);
        if (messageBean.getCode() == 119) {
            LogUtils.i("????????????");
            AlltopicBean alltopicBean = JSON.parseObject(msg, AlltopicBean.class);
//                    List<AlltopicBean.DataEntity> data = alltopicBean.getData();
            topicList.addAll(alltopicBean.getData());
            setTopicAnimation();
        } else if (messageBean.getCode() == 121) {
            GiftAllModel giftAllModel = new Gson().fromJson(msg, GiftAllModel.class);
            ArrayList<GiftAllModel.DataBean> ral = new ArrayList<>();
            ral.add(giftAllModel.getData());
//                    showGiftAllDialog(ral);
            LogUtils.i("????????????????????????");
            giftAllList.addAll(ral);
            showGiftAllAnimation();
        } else if (messageBean.getCode() == 2022) {
            StarAllBean starAllBean = new Gson().fromJson(msg, StarAllBean.class);
            Log.e("mmp", "??????????????????????????????????????????id???" + roomId);
            if (starAllBean.data.rids.contains(roomId)) {
                timeAllList.add(starAllBean.data);
                if (!isVD) {
                    GiftSendMessage.DataBean dataBean = new GiftSendMessage.DataBean(starAllBean.data.effect, 0);
                    setGiftDialogShow(dataBean);
                    showTimeAllAnimation();
                }
            }
        } else if (messageBean.getCode() == 122) {
            WinPrizeGiftModel giftAllModel = new Gson().fromJson(msg, WinPrizeGiftModel.class);

            if (giftAllModel != null && giftAllModel.getData().getCost() >= 10000) {// ??????????????????10000?????????
                // showWinPrizeGiftAllDialog(giftAllModel);
                LogUtils.i("??????????????????");
                prizeAllList.add(giftAllModel.getData());
                showPrizeGiftAllAnimation();
            }

            if (giftAllModel != null) {
                List<String> msgRids = giftAllModel.getData().getRids();
                if (msgRids != null && msgRids.size() > 0 && msgRids.contains(roomId)) {
                    chatShowChat.add(msg);
                    if (mRecyclerViewChatVoice.canScrollVertically(1)) {
                        tvHasMessageVoice.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
                    }
                }
            }

        }
    }

    private void onRecvUserSig(String userId, String userSig) {
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                 */
                LogUtils.e(LogUtils.TAG, "?????????????????????");
                addTencentChatRoom();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e(LogUtils.TAG, errCode + "?????????????????????" + errMsg);
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param userId  ??????id
     * @param otherId ???????????????id
     */
    @Override
    public void onDataShow(int userId, int otherId) {
        showMyPseronDialog(userId, otherId);
    }

    /**
     * pk?????????????????????
     *
     * @param pid
     * @param buid
     */
    @Override
    public void onPkGiftSend(int pid, int buid, String userName) {
        if (userToken == buid) {
            showToast("????????????????????????");
            return;
        }
        showMyGiftDialog(userToken, userToken, Const.IntShow.TWO, userName, roomBean.getState(), false);
        if (giftDialog != null) {
            giftDialog.setSendGift(new MyGiftDialog.SendGift() {
                @Override
                public void getSendGift(String ids, String names, int gid, String img, String showImg,
                                        int num, int sum, int goodGold, int sendType) {
                    int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
                    if (userGold < goodGold * num * sum) {
                        showMyDialog(getString(R.string.hint_nogold_gift),
                                Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                        if (myDialog != null) {
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
                    } else {
                        voicePresenter.getTou(userToken, pid, buid, Const.IntShow.TWO, num, gid, img, showImg, names, goodGold);
                    }
                }

                @Override
                public void getSendAllGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
//                    onGiftSendSuccess(userToken, ids, names, gid, img, showImg, num, goodGold);
                }
            });
        }
    }

    /**
     * ????????????
     *
     * @param buid
     */
    @Override
    public void onPersonShow(int buid) {
        showMyPseronDialog(userToken, buid);
    }

    /**
     * ????????????????????????
     *
     * @param res  ??????
     * @param type ??????   1 pk????????????   2pk??????  3????????????????????????????????????,
     *             4????????????????????????  5??????????????????
     */
    @Override
    public void onCallbackShow(String res, int type) {
        switch (type) {
            case 1:
//                PkSetBean pkSetBean = JSON.parseObject(res, PkSetBean.class);
//                if (pkSetBean.getCode() == Api.SUCCESS) {
//                    pkData = pkSetBean.getData();
//                    roomPid = pkData.getId();
//                    if (pkDialog != null && pkDialog.isShowing()) {
//                        pkDialog.updateData(pkData);
//                    } else {
//                        showMyPkDialog(pkData);
//                    }
//                } else {
//                    showToast(pkSetBean.getMsg());
//                }
                break;
            case 2:
//                PkSetBean pkSetBean = JSON.parseObject(res, PkSetBean.class);
//                if (pkSetBean.getCode() == Api.SUCCESS) {
//                    pkData = pkSetBean.getData();
//                    roomPid = pkData.getId();
//                    if (pkDialog != null && pkDialog.isShowing()) {
//                        pkDialog.updateData(pkData);
//                    } else {
//                        showMyPkDialog(pkData);
//                    }
//                } else {
//                    showToast(pkSetBean.getMsg());
//                }
                break;
            case 3:
                OnlineUserBean onlineUserBean = JSON.parseObject(res, OnlineUserBean.class);
                if (onlineUserBean.getCode() == Api.SUCCESS) {
                    if (onlineUserBean.getData().getState() == 1) {
                        ChatMessageBean.DataBean dataBean = new ChatMessageBean.DataBean(goldNum, 0, "",
                                "????????????????????????Ta???  ", roomBean.getName(), roomBean.getUid());
                        ChatMessageBean chatMessageBean = new ChatMessageBean(109, dataBean);
                        String mesString = JSON.toJSONString(chatMessageBean);
                        if (!StringUtils.isEmpty(mesString)) {
                            chatShowChat.add(mesString);
//                            chatRecyclerAdapter.addData(mesString);
                        }
                    }
                }
                break;
            case 4:
                BaseBean baseBean = JSON.parseObject(res, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (myOnlineUserDialog != null && myOnlineUserDialog.isShowing()) {
                        myOnlineUserDialog.updateShow();
                    }
                } else {
                    showToast(baseBean.getMsg());
                }
                break;
            case 5:
                showToast("??????????????????");

                break;


        }
    }

    MyGoldSendDialog myGoldSendDialog;

    private void showSendGoldDialog(VoiceUserBean.DataBean dataBean) {
        if (myGoldSendDialog != null && myGoldSendDialog.isShowing()) {
            myGoldSendDialog.dismiss();
        }
        int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
        myGoldSendDialog = new MyGoldSendDialog(VoiceActivity.this, dataBean, userToken);
        myGoldSendDialog.show();
        myGoldSendDialog.setRightButton(v -> {
            String number = myGoldSendDialog.getNumber();
            if (StringUtils.isEmpty(number)) {
                showToast("??????????????????????????????");
                return;
            }
            try {
                int goldNum = Integer.parseInt(number);
                if (goldNum > userGold) {
                    showToast("??????????????????????????????????????????");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e("?????????????????????");
            }

            new TUIKitDialog(VoiceActivity.this)
                    .builder()
                    .setCancelable(true)
                    .setCancelOutside(true)
                    .setTitle("?????????????????????????")
                    .setDialogWidth(0.75f)
                    .setPositiveButton("??????", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            voicePresenter.getSendGoldCall(userToken, dataBean.getId(), number, roomId);
                        }
                    })
                    .setNegativeButton("??????", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();
        });
        myGoldSendDialog.setRecordButton(v -> {
//            if (myGoldSendDialog != null && myGoldSendDialog.isShowing()) {
//                myGoldSendDialog.dismiss();
//            }
            showRecordDialog();
        });
    }

    SendRecordDialog sendRecordDialog;

    private void showRecordDialog() {
        if (sendRecordDialog != null && sendRecordDialog.isShowing()) {
            sendRecordDialog.dismiss();
        }
        sendRecordDialog = new SendRecordDialog(VoiceActivity.this, userToken);
        sendRecordDialog.show();
    }

    /**
     * ??????????????????
     *
     * @param otherId  ??????????????????id
     * @param userName ????????????
     */
    @Override
    public void onMicSendGift(int otherId, String userName) {
        showMyGiftDialog(userToken, userToken, Const.IntShow.ONE, userName, roomBean.getState(), false);
        if (giftDialog != null) {
            giftDialog.setSendGift(new MyGiftDialog.SendGift() {
                @Override
                public void getSendGift(String ids, String names, int gid, String img, String showImg,
                                        int num, int sum, int goodGold, int sendType) {
                    int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
                    if (sendType == 1) { //1 ???????????? 2?????????
                        if (userGold < goodGold * num * sum) {
                            showMyDialog(getString(R.string.hint_nogold_gift),
                                    Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                            if (myDialog != null) {
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
                        } else {
                            voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                        }
                    } else if (sendType == 2) {
                        voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                    }

                }

                @Override
                public void getSendAllGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
                    onGiftSendSuccess(userToken, ids, names, gid, img, showImg, num, goodGold);
                }
            });
        }
    }

    @Override
    public void onPersonSendGift(int otherId, String userName) {
        showMyGiftDialog(userToken, userToken, Const.IntShow.FOUR, userName, roomBean.getState(), false);
        if (giftDialog != null) {
            giftDialog.setSendGift(new MyGiftDialog.SendGift() {
                @Override
                public void getSendGift(String ids, String names, int gid,
                                        String img, String showImg, int num,
                                        int sum, int goodGold, int sendType) {
                    int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
                    if (sendType == 1) {
                        if (userGold < goodGold * num * sum) {
                            showMyDialog(getString(R.string.hint_nogold_gift),
                                    Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                            if (myDialog != null) {
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
                        } else {
                            voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                        }
                    } else if (sendType == 2) {
                        voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                    }

                }

                @Override
                public void getSendAllGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
                    onGiftSendSuccess(userToken, ids, names, gid, img, showImg, num, goodGold);
                }
            });
        }
    }


    /**
     * ??????????????????
     *
     * @param packetId
     */
    @Override
    public void onPacketOpen(int packetId) {
        if (packetDialog != null && packetDialog.isShowing()) {
            packetDialog.dismiss();
        }
        packetNumber--;
        setPacketShow();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.DATA, packetId);
        ActivityCollector.getActivityCollector().toOtherActivity(PacketActivity.class, bundle);
    }

    /**
     * ????????????????????????????????????
     */
    private void setPacketShow() {
        if (isHavePacket) {
            if (packetNumber > 0) {
                ivGetpacketVoice.setVisibility(View.VISIBLE);
            } else {
                ivGetpacketVoice.setVisibility(View.GONE);
            }
        } else {
            ivGetpacketVoice.setVisibility(View.GONE);
        }
    }

    /**
     * ??????????????????
     *
     * @param uid
     * @param ids
     * @param gid
     * @param img
     * @param showImg
     * @param num
     */
    @Override
    public void onGiftSendSuccess(int uid, String ids, String names, int gid, String img, String showImg, int num, int goodGold) {
        GiftSendMessage.DataBean dataBean = new GiftSendMessage.DataBean(userToken, userName, goldNum,
                ids, names, gid, img, showImg, num, goodGold);
        GiftSendMessage giftSendMessage = new GiftSendMessage(101, dataBean);
        String giftString = JSON.toJSONString(giftSendMessage);
        setSendMessage(giftString);
        if (giftDialog != null && giftDialog.isShowing()) {
            giftDialog.getPacketRefresh();
        }
    }

    /**
     * ????????????????????????
     *
     * @param userId ??????????????????id
     * @param roomId ??????id
     * @param fgid   ?????????id
     * @param state  ???????????????????????????
     * @param type   1?????????2??????
     */
    @Override
    public void onBlackListAdd(int userId, String roomId, int fgid, int state, int type, String userName) {
        showMyDialog("?????????" + "<font color='#81F6B4'> " + userName + " </font>" + "?????????????????????????????????????????????????????????", 0, null);
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                voicePresenter.addRoomBlack(userId, roomId, fgid, state, type);
            }
        });
    }

    /**
     * ????????????
     *
     * @param msgShow
     */
    @Override
    public void onMsgShow(String msgShow) {
        showToast(msgShow);
    }

    /**
     * ????????????????????????
     *
     * @param msgShow
     */
    @Override
    public void onJoinRoomShow(String msgShow) {
        if (!isFinishing()) {
            showMyHintDialog(msgShow);
        }
    }

    /**
     * ?????????????????????
     */
    @Override
    public void onPacketSendSuccecss() {
        showToast(getString(R.string.hint_success_packet));
//        if (rewardDialog != null && rewardDialog.isShowing()) {
//            rewardDialog.dismiss();
//        }
    }

    /**
     * ??????????????????
     *
     * @param position
     */
    @Override
    public void onGetOutSuccecss(int position) {
        showToast("??????????????????");
        if (myOnlineUserDialog != null && myOnlineUserDialog.isShowing()) {
            myOnlineUserDialog.setUserGetOut(position);
        }
    }

    @Override
    public void onGetToken(String token) {
//        initVoice(token);////?????????????????????
    }

    /**
     * ???????????????
     *
     * @param dataBean
     */
    @Override
    public void onGoldSend(VoiceUserBean.DataBean dataBean) {
        showSendGoldDialog(dataBean);
    }

    ChatFragmentDialog chatDialog;

    @Override
    public void onChatTo(int userId, String userName) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ID, userId + "");
        bundle.putString(Const.ShowIntent.NAME, userName);

//        if (chatDialog!=null && chatDialog.isAdded())
//            chatDialog.dismissAllowingStateLoss();

        ActivityCollector.getActivityCollector().finishActivity(ChatActivity.class);
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(TIMConversationType.C2C);
        chatInfo.setId(String.valueOf(userId));
        chatInfo.setChatName(userName);
        bundle.putSerializable(Const.ShowIntent.CHAT_INFO, chatInfo);
        bundle.putBoolean("isRoom", true);
        ActivityCollector.getActivityCollector().toOtherActivity(ChatActivity.class, bundle);
//        chatDialog = new ChatFragmentDialog();
//        chatDialog.setArguments(bundle);
//        chatDialog.show(getSupportFragmentManager(),"chat");

    }

    /**
     * ????????????????????????
     *
     * @param micData
     */
    @Override
    public void onMicDataSend(String micData) {
        LogUtils.i("??????????????????????????????");
        YySignaling.getInstans().sendCHannelAttrUpdate(roomId, Const.Agora.ATTR_MICS, micData);
    }

    /**
     * ????????????????????????
     *
     * @param roomData
     */
    @Override
    public void onRoomDataSend(String roomData) {
        LogUtils.i("??????????????????????????????");
        YySignaling.getInstans().sendCHannelAttrUpdate(roomId, Const.Agora.ATTR_XGFJ, roomData);
    }

    /**
     * ??????????????????????????????
     *
     * @param giftData
     */
    @Override
    public void onGiftAllSend(List<GiftAllModel.DataBean> giftData) {
//        GiftAllModel giftAllModel = new GiftAllModel();
//        giftAllModel.setCode(121);
//        giftAllModel.setData(giftData);
//        YySignaling.getInstans().sendChannelMessage(roomId,JSON.toJSONString(giftAllModel));

    }

    /**
     * ????????????
     *
     * @param type           1 ????????????????????????
     *                       2????????????????????? 3,???????????????????????????????????????
     *                       5????????????
     *                       6????????????
     *                       7?????????????????????
     *                       8??????????????????
     * @param responseString
     */
    @Override
    public void onCallBack(int type, String responseString) {
        switch (type) {
            case 1:
//                AlltopicBean alltopicBean = JSON.parseObject(responseString, AlltopicBean.class);
//                if (alltopicBean.getCode() == Api.SUCCESS) {
//                    StringBuilder stringBuilder = new StringBuilder();
//                    List<AlltopicBean.DataEntity> data = alltopicBean.getData();
//                    for (AlltopicBean.DataEntity dataEntity : data) {
//                        stringBuilder.append(dataEntity.getUserName())
//                                .append("???")
//                                .append(dataEntity.getContent())
//                                .append("        ");
//                    }
//                    tvAlltopicVoice.setText(stringBuilder);
//
//                    if (data.size() > 0&& tvAlltopicVoice.getText().toString().length()>0)
//                    {
//                        tvAlltopicVoice.setText(data.get(0).getUserName() + "???" + data.get(0).getContent() + "  ");
//                        tvAlltopicVoice.requestFocus();
//                        tvAlltopicVoice.setVisibility(View.VISIBLE);
//                    }else {
//                        tvAlltopicVoice.setText(" ");
//                    }
//
//
//                } else {
//                    showToast(alltopicBean.getMsg());
//                }
                break;
            case 2:
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("??????????????????");
                    edtInputMychat.setText("");
                    isAllChat = false;
                    setAllChatShow();
                } else if (baseBean.getCode() == Const.IntShow.TWO) {
                    showMyDialog("???????????????????????????????????????????????????",
                            Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                } else {
                    showToast(baseBean.getMsg());
                }
                break;
            case 3:
                RoomCacheBean roomCacheBean = JSON.parseObject(responseString, RoomCacheBean.class);
                if (roomCacheBean.getCode() == Api.SUCCESS) {
                    if (roomCacheBean.getData().getX() == 1) {//???????????? 1??????2 ???
                        ivChestsVoice.setVisibility(View.GONE);
                        iv_act_gold.setVisibility(View.GONE);
                        iv_act_star.setVisibility(View.GONE);
                    } else if (roomCacheBean.getData().getX() == 2) {
                        if (goldNum >= roomCacheBean.getData().getY()) {
                            ivChestsVoice.setVisibility(View.VISIBLE);
//                            iv_act_gold.setVisibility(View.VISIBLE);
                            iv_act_star.setVisibility(View.VISIBLE);
                        } else {
                            ivChestsVoice.setVisibility(View.GONE);
                            iv_act_gold.setVisibility(View.GONE);
                            iv_act_star.setVisibility(View.GONE);
                        }
                    }
                } else {
                    showToast(roomCacheBean.getMsg());
                }
                break;
            case 4:
                BaseBean baseBean1 = JSON.parseObject(responseString, BaseBean.class);
//                if (myGoldSendDialog != null && myGoldSendDialog.isShowing()) {
//                    myGoldSendDialog.dismiss();
//                }
                if (baseBean1.getCode() == Api.SUCCESS) {
                    showToast("?????????????????????");
                    myGoldSendDialog.dismiss();
                } else if (baseBean1.getCode() == Const.IntShow.TWO) {
                    showMyDialog("???????????????????????????????????????????????????",
                            Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                } else {
                    showToast(baseBean1.getMsg());
                }
                break;
            case 5:
                MicXgfjBean baseBean2 = JSON.parseObject(responseString, MicXgfjBean.class);
                if (giftDialog != null) {
                    giftDialog.sendAllGiftAfter();
                    if (giftDialog.isShowing()) {
                        giftDialog.dismiss();
                    }
                }
                if (baseBean2.getCode() == Api.SUCCESS) {
                    for (MicXgfjBean.DataBean.ListDataBean listDataBean : baseBean2.getData().getMsgList()) {
                        sendTencentMsg(listDataBean.getMessageChannelSend());
                    }
                }
                break;
            case 6:
                MicXgfjBean baseBean3 = JSON.parseObject(responseString, MicXgfjBean.class);
                if (baseBean3.getCode() == Api.SUCCESS) {
                    for (MicXgfjBean.DataBean.ListDataBean listDataBean : baseBean3.getData().getMsgList()) {
                        sendTencentMsg(listDataBean.getMessageChannelSend());
                    }
                }
                break;
            case 7:
                MicXgfjBean baseBean4 = JSON.parseObject(responseString, MicXgfjBean.class);
                if (baseBean4.getCode() == Api.SUCCESS) {
                    if (StringUtils.isEmpty(baseBean4.getData().getAttr_xgfj())) {
                        return;
                    }
                    YySignaling.getInstans().sendCHannelAttrUpdate(roomId, Const.Agora.ATTR_XGFJ,
                            baseBean4.getData().getAttr_xgfj());
                }
                break;
            case 8:
                GiveGiftResultBean baseBean8 = JSON.parseObject(responseString, GiveGiftResultBean.class);
                showToast(baseBean8.getMsg());
                break;

        }
    }

    private void sendTencentMsg(String msgBean) {
        if (StringUtils.isEmpty(msgBean)) {
            return;
        }
        setTIMMessageShow(msgBean);
        LogUtils.i("??????????????????", msgBean);
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group,    //?????????????????????
                Const.roomChannelMsg);

        //??????????????????
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("[????????????]");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        //??? TIMMessage ????????????????????????
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgBean.getBytes());      //????????? byte[]
        elem.setDesc("[????????????]"); //?????????????????????
        //??? elem ???????????????
        if (msg.addElement(elem) != 0) {
            LogUtils.i(LogUtils.TAG, "addElement failed");
            return;
        }

        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                //????????? code ??????????????? desc????????????????????????????????????
                //????????? code ???????????????????????????
                LogUtils.e(i + s);
                showToast("??????????????????" + s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) { //??????????????????
                LogUtils.i("??????????????????");
            }
        });
    }

    MyHintDialog myHintDialog;

    private void showMyHintDialog(String msgShow) {
        if (myHintDialog != null && myHintDialog.isShowing()) {
            myHintDialog.dismiss();
        }
        myHintDialog = new MyHintDialog(VoiceActivity.this);
        myHintDialog.show();
        myHintDialog.setHintText(msgShow);
        myHintDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myHintDialog != null && myHintDialog.isShowing()) {
                    myHintDialog.dismiss();
                }
                ActivityCollector.getActivityCollector().finishActivity();
            }
        });
    }


    /**
     * ????????????????????????????????????????????????
     *
     * @param responseString
     */
    @Override
    public void onCallSuccess(String responseString) {
        VoiceHomeBean voiceUserBean = JSON.parseObject(responseString, VoiceHomeBean.class);
        if (voiceUserBean.getCode() == 0) {
            VoiceHomeBean.DataBean homeData = voiceUserBean.getData();
            roomBean = homeData.getRoom();
            giftMinShow = homeData.getMinGift();

            if (roomBean.getUid() != userToken) { //?????????????????????????????????
                addTypeList(new VoiceTypeModel(1, 8, 10));
//                Timer timer = new Timer();
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        observable = Observable.just("0");
//                        subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Consumer<String>() {
//                                    @Override
//                                    public void accept(String s) throws Exception {
//                                        if (!isFinishing()) {
//                                            //??????????????????????????????
//                                            voicePresenter.getUserAttention(userToken, roomBean.getUid());
//                                        }
//                                    }
//                                });
//                    }
//                };
//                timer.schedule(timerTask, 10000);
            }

            setRoomShow();
            List<VoiceUserBean.DataBean> data = homeData.getUserModel();
            setUpdateRoom(data);
        } else {
            showToast(voiceUserBean.getMsg());
        }
    }

    /**
     * ??????????????????
     *
     * @param data
     */
    private void setUpdateRoom(List<VoiceUserBean.DataBean> data) {

        for (VoiceUserBean.DataBean dataOne : data) {

            if (dataOne.getType() == 1) { //??????????????????????????????
                setShow(dataOne);
            }

            if (dataOne.getId() == userToken) { //????????????
                userRoomType = dataOne.getType();
                userName = dataOne.getName();
//                goldNum = dataOne.getTreasureGrade();
//                SharedPreferenceUtils.put(this, Const.User.GRADE_T, goldNum);
                if (isOpenRecord) { //??????????????????
                    roomPass = roomBean.getPassword();
                    if (userRoomType == 1) { //??????
//                        voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.ONE);
                        openRoomAnimation();
                    } else {
                        if (StringUtils.isEmpty(roomPass) || Const.RoomId.equals(roomId)) {
                            //????????????????????????????????????????????????
//                            voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.ONE);
                            openRoomAnimation();
                        } else {
                            showMyPasswordDialog();
                        }
                    }
                } else {
                    showToast("????????????????????????????????????????????????");
                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                }
            }
        }
    }

    /**
     * ????????????
     *
     * @param code
     * @param msg
     */
    @Override
    public void onError(int code, String msg) {

    }

    /**
     * ???????????????
     *
     * @param msg
     */
    @Override
    public void onNumberSuccess(ArrayList<Integer> msg) {

    }

    @Override
    public BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    public void updateUnread(int count) {
        if (count <= 0) {
            ivEnvelopeVoice.setImageResource(R.drawable.bottom_envelope);
        } else {
            ivEnvelopeVoice.setImageResource(R.drawable.bottom_envelope1);
        }
    }
}
