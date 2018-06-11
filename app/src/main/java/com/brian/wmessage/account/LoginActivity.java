package com.brian.wmessage.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brian.common.base.BaseActivity;
import com.brian.common.utils.ToastUtil;
import com.brian.wmessage.MainActivity;
import com.brian.wmessage.R;
import com.brian.wmessage.imservice.bmob.BmobHelper;
import com.brian.wmessage.entity.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录注册页面
 * @author huamm
 */
public class LoginActivity extends BaseActivity {


    @BindView(R.id.et_username)
    EditText mUserNameEt;
    @BindView(R.id.et_password)
    EditText mPasswordEt;
    @BindView(R.id.btn_login)
    Button mLoginBtn;
    @BindView(R.id.tv_register)
    TextView mRegisterBtn;
    @BindView(R.id.titlebar_title)
    TextView mTitleTv;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        mTitleTv.setText(R.string.app_name);

        initListeners();
    }

    private void initListeners() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserNameEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.showMsg("请填写用户名");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showMsg("请填写密码");
                    return;
                }

                password = UserInfo.encryptPassword(password);
                doLogin(username, password);
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startActivity(LoginActivity.this);
                finish();
            }
        });
    }

    private void doLogin(String username, String password) {
        BmobHelper.getInstance().login(username, password, new BmobHelper.OnLoginListener() {

            @Override
            public void onFinish(UserInfo userInfo) {
                //登录成功
                MainActivity.startActivity(LoginActivity.this);
                finish();
            }

            @Override
            public void onError(int errorCode, String msg) {
                if (errorCode == 101) {
                    ToastUtil.showMsg("用户名或密码不正确", true);
                    mPasswordEt.setText("");
                } else {
                    ToastUtil.showMsg(msg + "(" + errorCode + ")");
                }
            }
        });
    }
}
