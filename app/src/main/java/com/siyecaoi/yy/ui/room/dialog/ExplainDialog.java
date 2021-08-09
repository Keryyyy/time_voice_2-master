package com.siyecaoi.yy.ui.room.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.siyecaoi.yy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 玩法说明
 */
public class ExplainDialog extends Dialog {


    Context mContext;

    @BindView(R.id.tv_show)
    TextView tvShow;

    public ExplainDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        this.mContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_explain);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.CenterDialogAnimation);


    }


}
