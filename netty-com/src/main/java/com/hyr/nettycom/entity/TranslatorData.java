package com.hyr.nettycom.entity;

import java.io.Serializable;

/*
 * @author hyr
 * @date 19-11-10-下午8:14
 * */
public class TranslatorData implements Serializable {
//    private static final long serialVersionUID = 1L;


    private String id;
    private String name;
    //消息体
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
