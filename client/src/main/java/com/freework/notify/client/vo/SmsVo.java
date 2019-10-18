package com.freework.notify.client.vo;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author daihongru
 */
public class SmsVo implements Serializable {
    /**
     * 发送短信的目的号码
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 在消息队列的消息ID，提供autoSetMessageId()方法自动生成
     */
    private String messageId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void autoSetMessageId() {
        this.messageId = System.currentTimeMillis() + "_" + UUID.randomUUID().toString();
    }
}
