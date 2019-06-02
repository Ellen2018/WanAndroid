package com.ellen.wanandroid.ui.login;

import com.ellen.baselibrary.net.okhttp.okhttpclient.AutoOkHttpClient;
import com.ellen.baselibrary.net.okhttp.request.AutoRequest;
import com.ellen.baselibrary.net.okhttp.request.RequestParams;
import com.ellen.wanandroid.bean.UserLogin;

import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LoginModel implements LoginAgree.LoginAgreeModel {

    private String loginUrl = "https://www.wanandroid.com/user/login";

    @Override
    public boolean saveUserLogin(UserLogin userLogin) {
        return false;
    }

    @Override
    public List<UserLogin> getUserLogin() {
        return null;
    }

    @Override
    public void startRemoteLogin(UserLogin userLogin, Callback callback) {
        OkHttpClient okHttpClient = new AutoOkHttpClient.Builder(null).build().getOkHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("username",userLogin.getAccount());
        requestParams.put("password",userLogin.getPassword());
        Request request = new AutoRequest.Builder().url(loginUrl).setRequestFormParams(requestParams).build().createPostRequest();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

}
