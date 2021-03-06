package com.siyecaoi.yy.ui.room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.AllMsgSendBean;
import com.siyecaoi.yy.bean.AllmsgBean;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.control.ChatRoomMessage;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.model.AllMsgModel;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.PersonHomeActivity;
import com.siyecaoi.yy.ui.room.adapter.AllMsgAdapter;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * ????????????
 *
 * @author xha
 * @data 2019/11/14
 */
public class AllMsgActivity extends MyBaseActivity {
    @BindView(R.id.mRecyclerView_allmsg)
    RecyclerView mRecyclerViewAllmsg;
    @BindView(R.id.tv_newdata_allmsg)
    TextView tvNewdataAllmsg;
    @BindView(R.id.edt_input_allmsg)
    EditText edtInputAllmsg;
    @BindView(R.id.tv_gold_allmsg)
    TextView tvGoldAllmsg;
    @BindView(R.id.btn_send_allmsg)
    Button btnSendAllmsg;
    @BindView(R.id.ll_chat_radiodating)
    LinearLayout llChatRadiodating;

    @BindView(R.id.root_view)
    RelativeLayout root_view;


    AllMsgAdapter adapter;

    List<String> noShowString;//????????????

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_allmsg);
    }

    @Override
    public void initView() {
        root_view.setBackgroundColor(getResources().getColor(R.color.nav_noselct_color));
        setTitleText("????????????");
        setRecycler();
        addOnMessageCall();
        getCall();
        getGoldCall();
        try {
            noShowString = readFile02("ReservedWords-utf8.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = jsonObject.optJSONArray("data");
    }

    private void addOnMessageCall() {
//        TIMGroupManager.CreateGroupParam createGroupParam = new TIMGroupManager.CreateGroupParam("AVChatRoom", "????????????");
//        TIMGroupManager.getInstance().createGroup(createGroupParam, new TIMValueCallBack<String>() {
//            @Override
//            public void onError(int i, String s) {
//                LogUtils.e("????????????" + i + s);
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                LogUtils.e(s + "????????????");
//            }
//        });
        TIMGroupManager.getInstance().applyJoinGroup(Const.allMsgRoom, "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (!Objects.requireNonNull(isFinishing())) {
                    LogUtils.e(i + s);
                    //????????????
                    if (i == 6014 && StringUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
                        String userSig = (String) SharedPreferenceUtils.get(AllMsgActivity.this, Const.User.USER_SIG, "");
                        onRecvUserSig(userToken + "", userSig);
                    } else {
                        showToast("???????????????????????????????????????");
                    }
                }
            }

            @Override
            public void onSuccess() {
                LogUtils.e("?????????????????????");
                setMegReaded();
            }
        });
        MyApplication.getInstance().addChatRoomMessage(chatRoomMessage);
    }

    private void onRecvUserSig(String userId, String userSig) {
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                 */
                LogUtils.e(LogUtils.TAG, "?????????????????????");
                addOnMessageCall();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e(LogUtils.TAG, errCode + "?????????????????????" + errMsg);
            }
        });
    }


    private void setMegReaded() {
        TIMConversation timConversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group, Const.allMsgRoom);
        timConversation.setReadMessage(timConversation.getLastMsg(), null);
    }

    ChatRoomMessage chatRoomMessage = new ChatRoomMessage() {
        @Override
        public void onNewMessage(TIMMessage timMsg) {
//                msgs.add(timMsg);
            if (timMsg.getElement(0) instanceof TIMCustomElem) {
                JSONObject jsonObject = null;
                try {
                    AllMsgModel allMsgModel = new Gson().fromJson(new String(((TIMCustomElem) timMsg.getElement(0)).getData(),
                            "UTF-8"), AllMsgModel.class);
                    if (allMsgModel.getState() == 4) {
                        adapter.addData(allMsgModel.getData());

                        if (mRecyclerViewAllmsg != null && mRecyclerViewAllmsg.canScrollVertically(1)) {
                            tvNewdataAllmsg.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerViewAllmsg.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void getGoldCall() {
        showDialog();
        HttpManager.getInstance().post(Api.ScreenGold, HttpManager.getInstance().getMap(), new MyObserver(this) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                tvGoldAllmsg.setText("X" + getOneBean.getData().getGold());
            }
        });
    }

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

    private void setRecycler() {
        adapter = new AllMsgAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewAllmsg.setAdapter(adapter);
        mRecyclerViewAllmsg.setLayoutManager(layoutManager);

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            AllmsgBean.DataEntity dataEntity = (AllmsgBean.DataEntity) adapter.getItem(position);
            assert dataEntity != null;
            switch (view.getId()) {
                case R.id.iv_header_allmsg:
                    bundle.putInt(Const.ShowIntent.ID, dataEntity.getUid());
                    ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
                    break;
                case R.id.rl_show_allmsg:
                    if (dataEntity.getRid().equals(Const.RoomId)) {
                        showToast("??????????????????");
                        return;
                    }
                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    bundle.putString(Const.ShowIntent.ROOMID, dataEntity.getRid());
                    ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                    ActivityCollector.getActivityCollector().finishActivity(AllMsgActivity.class);
                    break;
            }
        });

        mRecyclerViewAllmsg.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;
                //?????????????????????????????????SCROLL_STATE_IDLE???
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                //??????????????????????????????item???position????????????itemCount??????-1?????????????????????item???position
                //?????????????????????????????????????????????
                if (lastPosition >= recyclerView.getLayoutManager().getItemCount() - 1) {
                    tvNewdataAllmsg.setVisibility(View.GONE);
                }
            }
        });


    }

    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.QBgetScreen, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                AllmsgBean allmsgBean = JSON.parseObject(responseString, AllmsgBean.class);
                if (adapter != null) {
                    adapter.setNewData(allmsgBean.getData());
                    if (mRecyclerViewAllmsg != null) {
                        mRecyclerViewAllmsg.scrollToPosition(adapter.getItemCount() - 1);
                    }
                }
            }
        });
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().delChatRoomMessage(chatRoomMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_newdata_allmsg, R.id.btn_send_allmsg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_newdata_allmsg:
                tvNewdataAllmsg.setVisibility(View.GONE);
                mRecyclerViewAllmsg.scrollToPosition(adapter.getItemCount() - 1);
                break;
            case R.id.btn_send_allmsg:
                String mesShow = edtInputAllmsg.getText().toString();
                if (StringUtils.isEmpty(mesShow)) {
                    showToast("?????????????????????");
                    return;
                }
                if (noShowString != null) {
                    for (String noShow : noShowString) {
                        if (noShow.equals(mesShow)) {
                            showToast("????????????????????????????????????");
                            return;
                        }
                    }
                }
                btnSendAllmsg.setClickable(false);
                getMegCall(mesShow);
                break;
        }
    }

    private void getMegCall(String mesShow) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("content", mesShow);
        map.put("rid", Const.RoomId);
        HttpManager.getInstance().post(Api.AddScreen, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                btnSendAllmsg.setClickable(true);
                AllMsgSendBean allMsgSendBean = new Gson().fromJson(responseString, AllMsgSendBean.class);
                if (allMsgSendBean.getCode() == Api.SUCCESS) {
                    setMesgSend(allMsgSendBean.getData());
                } else if (allMsgSendBean.getCode() == 2) {
                    showMyDialog("???????????????????????????????????????????????????", getString(R.string.tv_topup_packet));
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                btnSendAllmsg.setClickable(true);
            }
        });
    }

    private void setMesgSend(AllmsgBean.DataEntity dataEntity) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group,    //?????????????????????
                Const.allMsgRoom);

//        /**
//         * ???????????????
//         */
//        int gradeShow = (int) SharedPreferenceUtils.get(getContext(), Const.User.GRADE_T, 0);
//        int charmGrade = (int) SharedPreferenceUtils.get(getContext(), Const.User.CharmGrade, 0);
//        int sex = (int) SharedPreferenceUtils.get(getContext(), Const.User.SEX, 0);
//        String name = (String) SharedPreferenceUtils.get(getContext(), Const.User.NICKNAME, "");
//        String header = (String) SharedPreferenceUtils.get(getContext(), Const.User.IMG, "");
//        ChatRoomMsgModel.DataBean dataBean = new ChatRoomMsgModel.DataBean();
//        dataBean.setGrade(gradeShow);
//        dataBean.setCharm(charmGrade);
//        dataBean.setUid(userToken);
//        dataBean.setMessageShow(mesShow);
//        dataBean.setName(name);
//        dataBean.setSex(sex);
//        dataBean.setHeader(header);
//        ChatRoomMsgModel chatRoomMsgModel = new ChatRoomMsgModel(113, dataBean, 3);

        AllMsgModel allMsgModel = new AllMsgModel(120, dataEntity, 4);

        //??????????????????
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("[????????????]");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        String msgShow = JSON.toJSONString(allMsgModel);
        //??? TIMMessage ????????????????????????
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgShow.getBytes());      //????????? byte[]
        elem.setDesc("[????????????]"); //?????????????????????
        //??? elem ???????????????
        if (msg.addElement(elem) != 0) {
            LogUtils.d(LogUtils.TAG, "addElement failed");
            return;
        }
//        MessageInfo messageInfo = new MessageInfo();
//        messageInfo.setTIMMessage(msg);
//        messageInfo.setMsgType(MessageInfo.MSG_TYPE_SHARE);
//        chatPanel.sendMessage(messageInfo);

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
                edtInputAllmsg.setText("");
                adapter.addData(dataEntity);
                mRecyclerViewAllmsg.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    /**
     * ?????????
     *
     * @param hintShow  ????????????
     * @param typeShow  ???????????? ????????????
     * @param rightShow ??????????????????????????????
     */
    MyDialog myDialog;

    private void showMyDialog(String hintShow, String rightShow) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(AllMsgActivity.this);
        myDialog.show();
        myDialog.setHintText(hintShow);
        if (!StringUtils.isEmpty(rightShow)) {
            myDialog.setRightText(rightShow);
        }
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
}
