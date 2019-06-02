package com.ellen.wanandroid.ui.login;

import android.text.TextUtils;
import android.util.Log;

import com.ellen.wanandroid.bean.UserLogin;
import com.ellen.wanandroid.bean.UserMessage;
import com.ellen.wanandroid.util.GsonHelper;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginPresenter extends LoginAgree.LoginAgreePresenter {
    @Override
    void startLogin(final UserLogin userLogin) {

        mView.loginBefore();

        if(TextUtils.isEmpty(userLogin.getAccount()) || TextUtils.isEmpty(userLogin.getPassword())){
            mView.loginFailure(userLogin,"账号或者密码不能为空");
            return;
        }

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> emitter) throws Exception {

                mModel.startRemoteLogin(userLogin, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onNext(e.getMessage());
                        emitter.onComplete();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                String json = response.body().string();
                                UserMessage userMessage = GsonHelper.getInstance().fromJson(json,UserMessage.class);
                                if(userMessage.getErrorCode() == 0){
                                    //成功
                                   emitter.onNext(userMessage);
                                }else {
                                    //失败
                                  emitter.onNext(userMessage.getErrorMsg());
                                }
                            }else {
                                emitter.onNext(response.message());
                            }
                            emitter.onComplete();
                    }
                });

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object s) {
                          if(s instanceof UserMessage){
                              //成功
                              mView.loginSuccess(userLogin, (UserMessage) s);
                          }else {
                              mView.loginFailure(userLogin, (String) s);
                          }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
