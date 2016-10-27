package com.moliying.mly_bmob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moliying.mly_bmob.bean.Note;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class NoteEditActivity extends AppCompatActivity {

    private EditText editText_content;
    private TextView textView_updateDate;
    private static final int SAVE_OP = 0X1;
    private static final int UPDATE_OP = 0X2;
    private int op;
    private Note updateNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        editText_content = (EditText) findViewById(R.id.editText_content);
        textView_updateDate = (TextView) findViewById(R.id.textView3_updateDate);
        initData();
    }

    private void initData() {
        Serializable data = getIntent().getSerializableExtra("note");
        if (data!=null){
            op = UPDATE_OP;
            updateNote = (Note) data;
            textView_updateDate.setText(updateNote.getUpdatedAt());
            editText_content.setText(updateNote.getContent());
        }else{
            op = SAVE_OP;
            textView_updateDate.setText(DateUtils.toDate(new Date()));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            switch (op){
                case SAVE_OP:
                    saveNote();
                    break;
                case UPDATE_OP:
                    updateNote();
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    //更新便签
    private void updateNote() {
        String content = editText_content.getText().toString();
        if (TextUtils.isEmpty(content)){
            return;
        }
        updateNote.setContent(content);
        updateNote.update(updateNote.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Toast.makeText(NoteEditActivity.this, "已更新", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //保存便签
    private void saveNote() {
        String content = editText_content.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        Note note = new Note();
        note.setContent(content);
        note.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Toast.makeText(NoteEditActivity.this, "已保存", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
