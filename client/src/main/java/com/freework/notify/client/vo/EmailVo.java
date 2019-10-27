package com.freework.notify.client.vo;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author daihongru
 */
public class EmailVo implements Serializable {
    /**
     * 邮箱地址
     */
    private String address;

    /**
     * 邮件内容
     */
    private String htmlText;

    /**
     * 附件地址
     */
    private String enclosureUrl;

    /**
     * 在消息队列的消息ID，提供autoSetMessageId()方法自动生成
     */
    private String messageId;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
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

    public String getEnclosureUrl() {
        return enclosureUrl;
    }

    public void setEnclosureUrl(String enclosureUrl) {
        this.enclosureUrl = enclosureUrl;
    }
}
