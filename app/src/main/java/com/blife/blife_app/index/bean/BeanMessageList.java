package com.blife.blife_app.index.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/8/25.
 */
public class BeanMessageList {

    private List<String> messages;

    public BeanMessageList() {
    }

    public BeanMessageList(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "BeanMessageList{" +
                "messages=" + messages +
                '}';
    }
}