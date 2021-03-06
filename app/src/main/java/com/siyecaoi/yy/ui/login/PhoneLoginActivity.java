package com.siyecaoi.yy.ui.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.control.VirifyCountDownTimer;
import com.siyecaoi.yy.ui.other.WebActivity;
import com.siyecaoi.yy.utils.LogUtils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.UserBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.MainActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.siyecaoi.yy.utils.spripaar.IsPhoneNumber;
import com.tencent.imsdk.TIMManager;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.sinata.xldutils.utils.Md5;
import cn.sinata.xldutils.utils.StringUtils;

import static com.mobsandgeeks.saripaar.annotation.Password.Scheme.ALPHA;

/**
 * Created by Administrator on 2018/12/20.
 */

public class PhoneLoginActivity extends MyBaseActivity {
    @BindView(R.id.edt_phone_login)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_phone_login)
    @IsPhoneNumber(sequence = 2)
    @Order(1)
    EditText edtPhoneLogin;
    @BindView(R.id.edt_psd_login)
    @NotEmpty(message = "???????????????")
    @Password(messageResId = R.string.hint_psdall_register, scheme = ALPHA)
    @Order(2)
    EditText edtPsdLogin;
    @BindView(R.id.lay_getcode_login)
    RelativeLayout layGetcodeLogin;
    @BindView(R.id.tv_getcode_login)
    TextView tvGetcodeLogin;
    @BindView(R.id.edt_getcode_login)
    @NotEmpty(message = "??????????????????")
    @Order(3)
    EditText edtGetcodeLogin;
    @BindView(R.id.tv_is_room_owner)
    TextView tvIsRoomOwner;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_login)
    TextView tvForgetLogin;

    @BindView(R.id.tv_agreement)
    TextView tv_agreement;

    Validator validator;
    String cityName;

    VirifyCountDownTimer virifyCountDownTimer;
    boolean isRoomOwner = false;

    //??????AMapLocationClient?????????
    AMapLocationClient mLocationClient = null;
    @Override
    public void initData() {
        cityName = getBundleString(Const.ShowIntent.NAME);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_phone_login);
    }

    @Override
    public void initView() {
//        setTitleText(R.string.title_login);

        validator = new Validator(this);
        validator.setValidationListener(validationListener);
        Validator.registerAnnotation(IsPhoneNumber.class);
        showTitle(false);
        showHeader(false);

//        initLocation(); //?????????????????????
    }

    Validator.ValidationListener validationListener = new Validator.ValidationListener() {
        @Override //??????
        public void onValidationSucceeded() {
            getCall();
        }

        @Override //??????
        public void onValidationFailed(List<ValidationError> errors) {
            for (ValidationError error : errors) {
                View view = error.getView();
                showToast(error.getFailedRules().get(0).getMessage(PhoneLoginActivity.this));
            }
        }
    };



    private void initLocation() {
        //???????????????
        mLocationClient = new AMapLocationClient(this);
        //????????????????????????
        mLocationClient.setLocationListener(mLocationListener);
        //??????AMapLocationClientOption??????
        AMapLocationClientOption mLocationOption = null;
        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        //?????????????????????AMapLocationMode.Hight_Accuracy?????????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //???????????????????????????
        //??????????????????false???
        mLocationOption.setOnceLocation(true);

        //????????????3s???????????????????????????????????????
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s???????????????
        // ??????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???
        // ????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);

        //??????????????????????????????????????????
        mLocationClient.setLocationOption(mLocationOption);
        //????????????
        mLocationClient.startLocation();

    }


    //???????????????????????????
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //??????????????????amapLocation?????????????????????
                    LogUtils.e("??????", aMapLocation.getPoiName() + "  " + aMapLocation.getCity());
                    String pointAddress = aMapLocation.getPoiName();
                    if (!StringUtils.isEmpty(pointAddress)) {
                        cityName = aMapLocation.getCity();//????????????
                        mLocationClient.stopLocation();//??????????????????????????????????????????????????????
                    }
                }
            }
        }
    };


    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.tv_is_room_owner, R.id.tv_getcode_login, R.id.btn_login, R.id.tv_register, R.id.tv_forget_login,R.id.img_qq_loging,R.id.img_wx_loging,R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_is_room_owner:
                if (isRoomOwner){
                    isRoomOwner = false;
                    tvIsRoomOwner.setSelected(false);
                    layGetcodeLogin.setVisibility(View.GONE);
                } else {
                    isRoomOwner = true;
                    tvIsRoomOwner.setSelected(true);
                    layGetcodeLogin.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_getcode_login:
                String phone = edtPhoneLogin.getText().toString();
                if (!StringUtils.isPhoneNumberValid(phone)) {
                    showToast(getString(R.string.hint_phone_login));
                    return;
                }
                virifyCountDownTimer =
                        new VirifyCountDownTimer(tvGetcodeLogin, 60000, 1000);
                getCodeCall(phone);
                break;
            case R.id.btn_login:
                validator.validate();
                break;
            case R.id.tv_register:
                Bundle bundle = new Bundle();
                bundle.putString(Const.ShowIntent.NAME,cityName);
                ActivityCollector.getActivityCollector().toOtherActivity(RegisterActivity.class,bundle);
                break;
            case R.id.tv_forget_login:
                ActivityCollector.getActivityCollector().toOtherActivity(ForgetPassActivity.class);
                break;

            case R.id.img_qq_loging:
                break;
            case R.id.img_wx_loging:
                break;
            case R.id.tv_agreement:
                Bundle bundle1 = new Bundle();
                bundle1.putInt(Const.ShowIntent.TYPE, 9);
                bundle1.putString(Const.ShowIntent.TITLE, getString(R.string.hint_checktwo_register));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle1);
                break;
        }
    }

    private void getCall() {
        showDialog();
        String phoneShow = edtPhoneLogin.getText().toString();
        String password = edtPsdLogin.getText().toString();
        String code = edtGetcodeLogin.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("phone", phoneShow);
        map.put("password", Md5.getMd5Value(password));
        map.put("smsCode", code);
        HttpManager.getInstance().post(Api.loginPhone, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    if (userBean.getData().getState() == 2) {
                        showToast("???????????????????????????????????????");
                        return;
                    }
                    initShared(userBean.getData());
                } else {
                    showToast(userBean.getMsg());
                }
            }
        });

    }

    private void getCodeCall(String phone) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("phone", phone);
        map.put("type", String.valueOf(51));
        HttpManager.getInstance().post(Api.getSmsCode, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = new Gson().fromJson(responseString, BaseBean.class);
                showToast(baseBean.getMsg());
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (virifyCountDownTimer != null) {
                        virifyCountDownTimer.start();
                    }
                }
            }
        });
    }

    private void toMain() {
        ActivityCollector.getActivityCollector().toOtherActivity(MainActivity.class);
        ActivityCollector.getActivityCollector().finishActivity();
    }

    private void initShared(UserBean.DataBean dataBean) {
        SharedPreferenceUtils.put(this, Const.User.USER_TOKEN, dataBean.getId());
        SharedPreferenceUtils.put(this, Const.User.APP_TOKEN, dataBean.getApptokenid());
        SharedPreferenceUtils.put(this, Const.User.AGE, dataBean.getAge());
        SharedPreferenceUtils.put(this, Const.User.IMG, dataBean.getImgTx());
        SharedPreferenceUtils.put(this, Const.User.SEX, dataBean.getSex());
        SharedPreferenceUtils.put(this, Const.User.NICKNAME, dataBean.getNickname());
        SharedPreferenceUtils.put(this, Const.User.ROOMID, dataBean.getUsercoding());
        SharedPreferenceUtils.put(this, Const.User.CharmGrade, dataBean.getCharmGrade());
        SharedPreferenceUtils.put(this, Const.User.DATEOFBIRTH, dataBean.getDateOfBirth());
        SharedPreferenceUtils.put(this, Const.User.FansNum, dataBean.getFansNum());
        SharedPreferenceUtils.put(this, Const.User.AttentionNum, dataBean.getAttentionNum());
        SharedPreferenceUtils.put(this, Const.User.GOLD, dataBean.getGold());
        SharedPreferenceUtils.put(this, Const.User.GoldNum, dataBean.getGoldNum());
        SharedPreferenceUtils.put(this, Const.User.GRADE_T, dataBean.getTreasureGrade());
        SharedPreferenceUtils.put(this, Const.User.PHONE, dataBean.getPhone());
        SharedPreferenceUtils.put(this, Const.User.QQSID, dataBean.getQqSid());
        SharedPreferenceUtils.put(this, Const.User.WECHATSID, dataBean.getWxSid());
        SharedPreferenceUtils.put(this, Const.User.Ynum, dataBean.getYnum());
        SharedPreferenceUtils.put(this, Const.User.Yuml, dataBean.getYuml());
        SharedPreferenceUtils.put(this, Const.User.USER_SIG, dataBean.getToken());
        SharedPreferenceUtils.put(this, Const.User.HEADWEAR_H, dataBean.getUserThfm());
        SharedPreferenceUtils.put(this, Const.User.CAR_H, dataBean.getUserZjfm());
        SharedPreferenceUtils.put(this, Const.User.HEADWEAR, dataBean.getUserTh());
        SharedPreferenceUtils.put(this, Const.User.CAR, dataBean.getUserZj());
        SharedPreferenceUtils.put(this, Const.User.SIGNER, dataBean.getIndividuation());
        SharedPreferenceUtils.put(this, Const.User.USER_LiANG, dataBean.getLiang());
        SharedPreferenceUtils.put(this, Const.User.PAY_PASS, dataBean.getPayPassword());
        //??????????????????????????????????????????????????????
        JPushInterface.setAlias(this, 0, String.valueOf(dataBean.getId()));

        onRecvUserSig(String.valueOf(dataBean.getId()), dataBean.getToken());
    }

    /**
     * ??????????????????????????? userSig ????????????IMSDK??? login ??????
     * userId ????????????
     * userSig ?????????????????????????????????????????? IMSDk ????????????
     */
    private void onRecvUserSig(String userId, String userSig) {
        showDialog();
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                 */
                LogUtils.e(LogUtils.TAG, "?????????????????????");
                showToast(getString(R.string.tv_login_success));
                LogUtils.e("getVersion", TIMManager.getInstance().getVersion());
                toMain();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                showToast("??????????????????????????????????????????????????????");
                LogUtils.e(LogUtils.TAG, errCode + "?????????????????????" + errMsg);
                toMain();
            }
        });
    }
}
