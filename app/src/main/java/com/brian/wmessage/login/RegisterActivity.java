package com.brian.wmessage.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brian.common.base.BaseActivity;
import com.brian.common.utils.TextDrawableUtil;
import com.brian.common.utils.ToastUtil;
import com.brian.common.views.NameLengthFilter;
import com.brian.wmessage.MainActivity;
import com.brian.wmessage.R;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.entity.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * 注册界面
 * @author huamm
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText mUserNameEt;
    @BindView(R.id.et_password)
    EditText mPasswordEt;
    @BindView(R.id.et_password_again)
    EditText mPasswordAgainEt;

    @BindView(R.id.btn_register)
    Button mRegisterBtn;

    @BindView(R.id.titlebar_left)
    TextView mTitleBarLeftTv;
    @BindView(R.id.titlebar_title)
    TextView mTitleTv;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);

        mTitleTv.setText("注册账号");
        mTitleBarLeftTv.setVisibility(View.VISIBLE);
        TextDrawableUtil.setLeftDrawable(mTitleBarLeftTv, R.drawable.ic_back);

        InputFilter[] filters = { new NameLengthFilter(30) };
        mUserNameEt.setFilters(filters);

        initListeners();
    }

    private void initListeners() {
        mTitleBarLeftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserNameEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                String pwdagain = mPasswordAgainEt.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.showMsg("请填写用户名");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showMsg("请填写密码");
                    return;
                }
                if (TextUtils.isEmpty(pwdagain)) {
                    ToastUtil.showMsg("请填写确认密码");
                    return;
                }
                if (!password.equals(pwdagain)) {
                    ToastUtil.showMsg("两次输入的密码不一致，请重新输入");
                    mPasswordEt.setText("");
                    mPasswordAgainEt.setText("");
                    return;
                }
                password = UserInfo.encryptPassword(password); // 不明文传输密码
                doRegister(username, password);
            }
        });
    }

    private void doRegister(String username, String password) {
        BmobHelper.getInstance().register(username, password, new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    MainActivity.startActivity(RegisterActivity.this);
                    finish();
                } else {
                    ToastUtil.showMsg(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack();
    }

    private void onBack() {
        LoginActivity.startActivity(this);
        finish();
    }
}
