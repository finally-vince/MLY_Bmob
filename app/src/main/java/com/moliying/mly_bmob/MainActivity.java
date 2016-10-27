package com.moliying.mly_bmob;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.moliying.mly_bmob.bean.Person;
import com.moliying.mly_bmob.bean.User;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int GET_IMAGE_CODE = 0x1;
    private ImageView imageView_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView_icon = (ImageView) findViewById(R.id.imageView_icon);
        loadUserInfo();
    }

    private void loadUserInfo() {
        User currentUser = BmobUser.getCurrentUser(User.class);
        BmobFile icon = currentUser.getIcon();
        Log.i(TAG, "loadUserInfo: icon=" + icon.getFileUrl());
        VolleyUtils.getInstance(this).loadImage(icon.getFileUrl(), 100, 100, imageView_icon);
    }

    public void addClick(View v) {
        Person person = new Person();
        person.setName("J哥");
        person.setAddress("beijing");
        person.setAge(10);
        person.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "添加成功-" + objectId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "添加失败-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateClick(View v) {
        Person p = new Person();
        p.setName("高尔夫司机");
        p.setAddress("昌平");
        p.setAge(18);
        p.update("0c139396cf", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "更新失败-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void queryClick(View v) {
        BmobQuery<Person> query = new BmobQuery<>();
        //查询一个 对象
//        query.getObject("0c139396cf", new QueryListener<Person>() {
//            @Override
//            public void done(Person person, BmobException e) {
//                Toast.makeText(MainActivity.this, person.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
        //查询所有对象
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                Toast.makeText(MainActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //分页查询
        query.setSkip(5);
        query.setLimit(5);
        query.order("-createdAt").findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                for (Person p : list) {
                    Log.i(TAG, "done: " + p.toString());
                }
            }
        });
    }

    public void deleteClick(View v) {
        Person p = new Person();
        p.setObjectId("af674a2946");
        p.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "删除失败-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updatePasswordClick(View view) {
        //获取当前登录的用户
        User user = BmobUser.getCurrentUser(User.class);
        BmobUser.updateCurrentUserPassword("123456",
                "123456", new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "密码修改失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //上传头像
    public void uploadIconClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GET_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == GET_IMAGE_CODE) {
                    Uri uri = data.getData();
                    Log.i(TAG, "onActivityResult: " + uri);
                    imageView_icon.setImageURI(uri);
                    Cursor c = getContentResolver().query(uri,
                            new String[]{MediaStore.Images.Media.DATA},
                            null, null, null, null);
                    if (c != null && c.moveToFirst()) {
                        String imageUrl = c.getString(0);
                        Log.i(TAG, "onActivityResult: " + imageUrl);
                        uploadIcon(imageUrl);
                    }
                }
                break;
        }
    }

    //上传图片
    private void uploadIcon(String imageUrl) {
        final BmobFile file = new BmobFile(new File(imageUrl));
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    User currentUser = BmobUser.getCurrentUser(User.class);
                    currentUser.setIcon(file);
                    currentUser.update(currentUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "头像更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void exitClick(View v) {
        BmobUser.logOut();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
    //便签
    public void myNoteClick(View view){
        startActivity(new Intent(this,NotesActivity.class));
    }
}
