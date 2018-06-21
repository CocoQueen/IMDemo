package com.coco.imdemo.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coco.imdemo.R;
import com.tencent.qcloud.tlslibrary.activity.ImgCodeActivity;
import com.tencent.qcloud.tlslibrary.helper.Util;
import com.tencent.qcloud.tlslibrary.service.Constants;
import com.tencent.qcloud.tlslibrary.service.TLSService;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSUserInfo;

import static com.tencent.qalsdk.service.QalService.context;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etPassword;
    EditText etUsername;
    Button btLogin;
    TextView tvRegist;
    TextView tvCellphoneLogin;
    private String password;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
        tvRegist = findViewById(R.id.tv_regist);
        tvCellphoneLogin = findViewById(R.id.tv_cellphone_login);
        btLogin.setOnClickListener(this);
        tvCellphoneLogin.setOnClickListener(this);
        tvRegist.setOnClickListener(this);

    }


    private void regist() {
        Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
        startActivityForResult(intent, 0);
    }

    private void login() {
        password = etPassword.getText().toString().trim();
        username = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "用户名密码不能为空~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.length() < 4 || username.length() > 32) {
            Toast.makeText(this, "用户名需在4位到32位之间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(this, "密码需大于等于8位", Toast.LENGTH_SHORT).show();
        }
        //初始化TLSService
        TLSService tlsService = TLSService.getInstance();
        //调用自带注册方法 参数3是登录的监听
        tlsService.TLSPwdLogin(username, password, new PwdLoginListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_regist:
                regist();
                break;
            case R.id.tv_cellphone_login:
                break;
        }
    }

    class PwdLoginListener implements TLSPwdLoginListener {
        @Override
        public void OnPwdLoginSuccess(TLSUserInfo userInfo) {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            TLSService.getInstance().setLastErrno(0);
            //跳转
//            jumpToSuccActivity();
            startActivity(new Intent(LoginActivity.this,ChatActivity.class));
        }

        @Override
        public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {
            ImgCodeActivity.fillImageview(bytes);
        }

        @Override
        public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
            Intent intent = new Intent(context, ImgCodeActivity.class);
            intent.putExtra(Constants.EXTRA_IMG_CHECKCODE, bytes);
            intent.putExtra(Constants.EXTRA_LOGIN_WAY, Constants.USRPWD_LOGIN);
            context.startActivity(intent);
        }

        @Override
        public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
            TLSService.getInstance().setLastErrno(-1);
            Toast.makeText(LoginActivity.this, "登录失败" + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();
            Util.notOK(context, tlsErrInfo);
        }

        @Override
        public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
            TLSService.getInstance().setLastErrno(-1);
            Toast.makeText(LoginActivity.this, "登录超时" + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();

            Util.notOK(context, tlsErrInfo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == 1) {
                String username = data.getStringExtra("username");
                etUsername.setText(username);
            }
        }
    }
}
