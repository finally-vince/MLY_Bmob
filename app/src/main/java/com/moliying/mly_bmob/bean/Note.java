package com.moliying.mly_bmob.bean;

import cn.bmob.v3.BmobObject;

/**
 * company moliying.com
 * author vince
 * 2016/10/26
 */
public class Note extends BmobObject {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
