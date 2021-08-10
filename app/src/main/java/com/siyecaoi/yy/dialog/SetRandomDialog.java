package com.siyecaoi.yy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.view.inputview.VerificationCodeInputView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 设置随机数
 * Created by Administrator on 2018/3/9.
 */

public class SetRandomDialog extends Dialog {

    @BindView(R.id.iv_number_ok)
    ImageView ivOk;
    @BindView(R.id.iv_number_cancel)
    ImageView ivCancel;
    @BindView(R.id.vciv_code)
    VerificationCodeInputView inputView;

    private CallBack mCallBack;
    private char[] inputNumber;
    private String mOldResult;
    private Activity activity;


    public SetRandomDialog(Context context,CallBack callBack) {
        super(context, R.style.CustomDialogStyle);
        mCallBack = callBack;
        activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
        setContentView(R.layout.dialog_number);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        initShow();
    }

    private void initShow() {
        inputView.setAttachedActivity(activity);

        inputView.setOnInputListener(new VerificationCodeInputView.OnInputListener() {
            @Override
            public void onComplete(String code) {
                mOldResult = code;
                inputNumber = code.toCharArray();
            }

            @Override
            public void onInput() {

            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });

        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                if (inputNumber!= null){
                    for (int i = 0; i<inputNumber.length; i++){
                        sb.append(inputNumber[i]);
                        if (i < 2){
                            sb.append(",");
                        }
                    }
                    mCallBack.setNumber(mOldResult,sb.toString());
                    dismiss();
                }else {
                    ToastUtils.showShort("请输入完整的数字");
                }
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                inputView.clearCode();
                inputView.clearFocus();
            }
        });
    }


    public interface CallBack{
        void setNumber(String originResult,String result);
    }

}