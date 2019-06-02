package com.ellen.wanandroid.ui.login;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ellen.baselibrary.base.BaseActivity;
import com.ellen.baselibrary.mvp.activity.BaseMvpActivity;
import com.ellen.wanandroid.R;
import com.ellen.wanandroid.bean.UserLogin;
import com.ellen.wanandroid.bean.UserMessage;
import com.ellen.wanandroid.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter>
        implements LoginAgree.LoginAgreeView, BaseActivity.ButterKnifeInterface {

    @BindView(R.id.et_user_account)
    EditText etAccount;
    @BindView(R.id.et_user_password)
    EditText etPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    @OnClick({R.id.tv_login,R.id.tv_register})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tv_login:
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();
                UserLogin userLogin = new UserLogin(account,password);
                mPresenter.startLogin(userLogin);
                break;
            case R.id.tv_register:
                ToastUtils.toast(LoginActivity.this,"注册");
                break;
        }
    }

    @Override
    public void initMvp() {
        mPresenter = new LoginPresenter();
        mPresenter.mModel = new LoginModel();
        mPresenter.mView = this;
    }

    @Override
    protected void setStatus() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void destory() {

    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return null;
    }

    @Override
    public void loginBefore() {
    }

    @Override
    public void loginSuccess(UserLogin userLogin, UserMessage userMessage) {
        loginAfter();
    }

    @Override
    public void loginFailure(UserLogin userLogin, String errMessage) {
        Toast.makeText(LoginActivity.this,errMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginAfter() {

    }

    @Override
    public void initButterKnife() {
        ButterKnife.bind(this);
    }
}
