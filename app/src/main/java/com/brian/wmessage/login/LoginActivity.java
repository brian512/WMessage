package com.brian.wmessage.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brian.common.base.BaseActivity;
import com.brian.common.utils.LogUtil;
import com.brian.wmessage.MainActivity;
import com.brian.wmessage.R;
import com.brian.wmessage.bmob.BmobHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * 登录注册页面
 * @author huamm
 */
public class LoginActivity extends BaseActivity {


    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.tv_register)
    TextView tv_register;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initListeners();
    }

    private void initListeners() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobHelper.getInstance().login(et_username.getText().toString(), et_password.getText().toString(), new LogInListener() {

                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            //登录成功
                            MainActivity.startActivity(LoginActivity.this);
                            finish();
                        } else {
                            LogUtil.d(e.getMessage() + "(" + e.getErrorCode() + ")");
                        }
                    }
                });
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startActivity(LoginActivity.this);
                finish();
            }
        });
    }
}
