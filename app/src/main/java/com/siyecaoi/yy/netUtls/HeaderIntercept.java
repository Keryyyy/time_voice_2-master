package com.siyecaoi.yy.netUtls;

import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderIntercept implements Interceptor {
    String apptokenid = (String) SharedPreferenceUtils.get(MyApplication.getInstance(), Const.User.APP_TOKEN, "");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("apptokenid",apptokenid);
        return chain.proceed(builder.build());
    }
}
