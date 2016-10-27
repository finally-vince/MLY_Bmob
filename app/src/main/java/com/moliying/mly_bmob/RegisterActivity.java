package com.moliying.mly_bmob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moliying.mly_bmob.bean.User;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText editText_name;
    private EditText editText2_validCode;
    private EditText editText3_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText2_validCode = (EditText) findViewById(R.id.editText2_validCode);
        editText3_password = (EditText) findViewById(R.id.editText3_password);
    }

    //获取验证码
    public void getValidCodeClick(View view){
        String name = editText_name.getText().toString();
        if (TextUtils.isEmpty(name)){
            toast("手机号不能为空");
            return;
        }
        requestValidCode(name);
    }

    //发送请求验证码
    private void requestValidCode(String phone) {
        BmobSMS.requestSMSCode(phone, "mly_shop", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e!=null) {
                    toast("获取验证码失败:"+integer);
                }
            }
        });
    }

    //注册
    public void registerClick(View view){
        //先验证验证码是否正确
        String validCode = editText2_validCode.getText().toString();
        final String phone = editText_name.getText().toString();
        final String password = editText3_password.getText().toString();
        if(TextUtils.isEmpty(validCode)){
            toast("请输入验证码");
            return;
        }
        //验证短信验证码
        BmobSMS.verifySmsCode(phone, validCode, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //执行注册
                    User user = new User();
                    user.setUsername(phone);
                    user.setPassword(password);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e==null){
                                toast("注册成功");
                            }
                        }
                    });
                }
            }
        });
    }
    //登录
    public void loginClick(View view){
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser!=null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

    public void toast(String info){
        Toast.makeText(RegisterActivity.this, info, Toast.LENGTH_SHORT).show();
    }
}
