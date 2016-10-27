package com.moliying.mly_bmob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moliying.mly_bmob.bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {


    private EditText editText_phone;
    private EditText editText2_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
        editText2_password = (EditText) findViewById(R.id.editText2_password);
    }

    public void loginClick(View view){
        String phone = editText_phone.getText().toString();
        String password = editText2_password.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(LoginActivity.this, "请输入手机号或用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.setUsername(phone);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
