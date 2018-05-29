package com.coco.imdemo.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coco.imdemo.R;
import com.tencent.qcloud.tlslibrary.helper.Util;
import com.tencent.qcloud.tlslibrary.service.StrAccountLogin;
import com.tencent.qcloud.tlslibrary.service.TLSService;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

import static com.tencent.qalsdk.service.QalService.context;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername;
    EditText etPassword;
    EditText etRepassword;
    Button btRegist;
    private String password;
    private String repassword;
    private String username;
    private TLSService tlsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        etUsername=findViewById(R.id.et_username);
        etRepassword=findViewById(R.id.et_repassword);
        etPassword=findViewById(R.id.et_password);
        btRegist=findViewById(R.id.bt_regist);
        btRegist.setOnClickListener(this);
    }


    private void regist(String password, String repassword, String username) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)) {
            Toast.makeText(this, "输入项目不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.length() <= 4 || username.length() >= 32) {
            Toast.makeText(this, "用户名不能小于4位不能大于32位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(this, "密码需大于等于8位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(repassword)) {
            Toast.makeText(this, "两个密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        realRegist(username, repassword);
    }

    private void realRegist(String username, String repassword) {
        tlsService = TLSService.getInstance();
        int result = tlsService.TLSStrAccReg(username, password, new StrAccRegListener());
        if (result == TLSErrInfo.INPUT_INVALID) {
            Toast.makeText(this, "账号不合法", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        password = etPassword.getText().toString().trim();
        repassword = etRepassword.getText().toString().trim();
        username = etUsername.getText().toString().trim();

        regist(password, repassword, username);
    }

    class StrAccRegListener implements TLSStrAccRegListener {
        @Override
        public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
            Toast.makeText(RegistActivity.this, "成功注册 " + tlsUserInfo.identifier, Toast.LENGTH_SHORT).show();
            TLSService.getInstance().setLastErrno(0);
            StrAccountLogin login = new StrAccountLogin(context);
            //给登录页面返回数据
            Intent intent = new Intent();
            intent.putExtra("username", username);
            setResult(1, intent);
            finish();
        }

        @Override
        public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {
            Toast.makeText(RegistActivity.this, "注册失败 " + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();

            Util.notOK(context, tlsErrInfo);
        }

        @Override
        public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {
            Toast.makeText(RegistActivity.this, "注册失败 " + tlsErrInfo.Msg, Toast.LENGTH_SHORT).show();

            Util.notOK(context, tlsErrInfo);
        }
    }
}
