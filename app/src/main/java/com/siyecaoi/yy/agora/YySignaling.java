package com.siyecaoi.yy.agora;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.control.ChatMessage;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.MessageUtils;

import java.util.ArrayList;

import cn.sinata.xldutils.utils.StringUtils;
import io.agora.AgoraAPIOnlySignal;
import io.agora.IAgoraAPI;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 信令配置
 */
public class YySignaling {

    Context context;
    AgoraAPIOnlySignal m_agoraAPI;

    private static YySignaling yySignaling;
    String TAG = "agora";

    ArrayList<ChatMessage> fastConShows;

    public YySignaling() {

    }

    public static YySignaling getInstans() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (yySignaling == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (YySignaling.class) {
                //未初始化，则初始instance变量
                if (yySignaling == null) {
                    yySignaling = new YySignaling();
                }
            }
        }
        return yySignaling;
    }

    /**
     * 初始化信令
     *
     * @param context
     */
    public void initSignaling(Context context) {
        this.context = context;
        m_agoraAPI = AgoraAPIOnlySignal.getInstance(context, Const.AGORA_APP_ID);
        m_agoraAPI.callbackSet(iCallBack);
    }

    /**
     * 获取信令对象
     *
     * @return
     */
    public AgoraAPIOnlySignal getAgorasignaling() {
        return this.m_agoraAPI;
    }

    /**
     * 登录 Agora 信令系统
     *
     * @param account 用户id
     */
    public void loginSignaling(String account, String token) {

        m_agoraAPI.login2(Const.AGORA_APP_ID, account, "_no_need_token", 0, null, 30, 3);
//        m_agoraAPI.login2(context.getString(R.string.private_app_id), account, token, 0, null, 30, 3);
    }

    /**
     * 退出信令系统
     */
    public void logout() {
        m_agoraAPI.logout();
    }

    /**
     * 发送点对点消息
     *
     * @param account 接收方的用户id
     * @param msg     消息内容
     */
    public void sengMessage(String account, String msg) {
        m_agoraAPI.messageInstantSend(account, 0, msg, "");
    }

//    // 设置对端收到消息回调
//    m_agoraAPI.onMessageInstantReceive(account, uid, msg){
//        // Your code
//    }


    /**
     * 加入频道
     *
     * @param channelName 频道名称
     */
    public void joinChannel(String channelName) {
        m_agoraAPI.channelJoin(channelName);

    }

    /**
     * 退出频道
     *
     * @param channelName 频道名称
     */
    public void leaveChannel(String channelName) {
        m_agoraAPI.channelLeave(channelName);
    }

    /**
     * 发送频道消息
     *
     * @param channelName 频道名称
     * @param msg         消息内容
     */
    public void sendChannelMessage(String channelName, String msg) {
        m_agoraAPI.messageChannelSend(channelName, msg, "");
    }

    /**
     * 频道属性发生变化
     *
     * @param s  频道名
     * @param s1 属性名
     * @param s2 属性值
     */
    public void sendCHannelAttrUpdate(String s, String s1, String s2) {
        m_agoraAPI.channelSetAttr(s, s1, s2);
    }

//    // 设置对端接收到频道消息回调
//    m_agoraAPI.onMessageChannelReceive(channelID, account, uid, msg) {
//        // Your code
//    }


    //信令回调
    public IAgoraAPI.ICallBack iCallBack = new IAgoraAPI.ICallBack() {
        @Override
        public void onReconnecting(int i) {

        }

        @Override
        public void onReconnected(int i) {

        }

        // 设置登录成功回调
        @Override
        public void onLoginSuccess(int i, int i1) {
            LogUtils.d(TAG, "登录成功回调" + i + " " + i1);
        }

        @Override
        public void onLogout(int i) {

        }

        // 设置登录失败回调
        @Override
        public void onLoginFailed(int i) {
            LogUtils.d(TAG, "登录失败回调" + i + " ");
        }

        // 设置加入频道成功回调
        @Override
        public void onChannelJoined(String s) {
            LogUtils.d(TAG, "加入频道成功" + s);
        }

        // 设置加入频道失败回调
        @Override
        public void onChannelJoinFailed(String s, int i) {
            LogUtils.d(TAG, "加入频道失败" + s + i);
        }

        // 设置退出频道回调
        @Override
        public void onChannelLeaved(String s, int i) {
            LogUtils.d(TAG, "退出频道" + s + i);
        }

        @Override
        public void onChannelUserJoined(String s, int i) {

        }

        @Override
        public void onChannelUserLeaved(String s, int i) {

        }

        @Override
        public void onChannelUserList(String[] strings, int[] ints) {

        }

        @Override
        public void onChannelQueryUserNumResult(String s, int i, int i1) {

        }

        @Override
        public void onChannelQueryUserIsIn(String s, String s1, int i) {

        }

        @Override
        public void onChannelAttrUpdated(String s, String s1, String s2, String s3) {
            LogUtils.e(LogUtils.TAG, "频道属性发生变化回调" + s + s1 + s2);
            LogUtils.e(LogUtils.TAG, "频道属性状态" + s3);
            fastConShows = MessageUtils.getInstans().getChat();
            if (s3.equals("update")) {
                if (fastConShows != null) {
                    if (!StringUtils.isEmpty(s2)) {
                        for (ChatMessage chatMessage : fastConShows) {
                            if (chatMessage != null) {
                                chatMessage.setChannelAttrUpdated(s, s1, s2, s3);
                            }
                        }
                    }
                }
            }

        }

        @Override
        public void onInviteReceived(String s, String s1, int i, String s2) {

        }

        @Override
        public void onInviteReceivedByPeer(String s, String s1, int i) {

        }

        @Override
        public void onInviteAcceptedByPeer(String s, String s1, int i, String s2) {

        }

        @Override
        public void onInviteRefusedByPeer(String s, String s1, int i, String s2) {

        }

        @Override
        public void onInviteFailed(String s, String s1, int i, int i1, String s2) {

        }

        @Override
        public void onInviteEndByPeer(String s, String s1, int i, String s2) {

        }

        @Override
        public void onInviteEndByMyself(String s, String s1, int i) {

        }

        @Override
        public void onInviteMsg(String s, String s1, int i, String s2, String s3, String s4) {

        }

        // 设置消息发送失败回调
        @SuppressLint("CheckResult")
        @Override
        public void onMessageSendError(String s, int i) {
            LogUtils.d(TAG, "消息发送失败" + s + i);
            io.reactivex.Observable.just(s)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            android.widget.Toast.makeText(MyApplication.getInstance(), "消息发送失败" + s, Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        @Override
        public void onMessageSendProgress(String s, String s1, String s2, String s3) {

        }

        // 设置消息发送成功回调
        @Override
        public void onMessageSendSuccess(String s) {
            LogUtils.d(TAG, "消息发送成功" + s);
        }

        @Override
        public void onMessageAppReceived(String s) {

        }

        @Override
        public void onMessageInstantReceive(String s, int i, String s1) {

        }

        // 设置对端接收到频道消息回调
        @Override
        public void onMessageChannelReceive(String s, String s1, int i, String s2) {
            LogUtils.e(TAG, "对端接收到频道消息" + s + "  " + s1 + " " + i + " " + s2);
            fastConShows = MessageUtils.getInstans().getChat();
            if (fastConShows != null) {
                for (ChatMessage chatMessage : fastConShows) {
                    if (chatMessage != null) {
                        chatMessage.setMessageShow(s, s1, i, s2);
                    }
                }
            }
        }

        @Override
        public void onLog(String s) {

        }

        @Override
        public void onInvokeRet(String s, String s1, String s2) {

        }

        @Override
        public void onMsg(String s, String s1, String s2) {

        }

        @Override
        public void onUserAttrResult(String s, String s1, String s2) {

        }

        @Override
        public void onUserAttrAllResult(String s, String s1) {

        }

        @Override
        public void onError(String s, int i, String s1) {

        }

        @Override
        public void onQueryUserStatusResult(String s, String s1) {

        }

        @Override
        public void onDbg(String s, byte[] bytes) {

        }

        @Override
        public void onBCCall_result(String s, String s1, String s2) {

        }
    };

}
