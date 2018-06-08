package com.brian.wmessage.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * 注册界面
 * @author huamm
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_register)
    Button btn_register;

    @BindView(R.id.et_password_again)
    EditText et_password_again;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobHelper.getInstance().register(et_username.getText().toString(), et_password.getText().toString(), et_password_again.getText().toString(), new LogInListener() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            MainActivity.startActivity(RegisterActivity.this);
                            finish();
                        } else {
                            if (e.getErrorCode() == BmobHelper.CODE_ERROR_MATCH) {
                                et_password_again.setText("");
                            }
                            LogUtil.d(e.getMessage() + "(" + e.getErrorCode() + ")");
                        }
                    }
                });
            }
        });

    }

}
