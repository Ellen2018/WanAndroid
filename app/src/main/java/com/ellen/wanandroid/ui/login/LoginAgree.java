package com.ellen.wanandroid.ui.login;

import com.ellen.baselibrary.mvp.activity.BaseModel;
import com.ellen.baselibrary.mvp.activity.BasePresenter;
import com.ellen.baselibrary.mvp.activity.BaseView;
import com.ellen.wanandroid.bean.UserLogin;
import com.ellen.wanandroid.bean.UserMessage;

import java.util.List;

import okhttp3.Callback;

public interface LoginAgree {

    interface LoginAgreeModel extends BaseModel{
        boolean saveUserLogin(UserLogin userLogin);
        List<UserLogin> getUserLogin();
        void startRemoteLogin(UserLogin userLogin, Callback callback);
    }

    interface LoginAgreeView extends BaseView{
        void loginBefore();
        void loginSuccess(UserLogin userLogin, UserMessage userMessage );
        void loginFailure(UserLogin userLogin,String errMessage);
        void loginAfter();
    }

    abstract class LoginAgreePresenter extends BasePresenter<LoginAgreeModel,LoginAgreeView>{

        abstract void startLogin(UserLogin userLogin);

    }

}
