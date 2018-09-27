package com.roc.rest.service;


/**
 * @author zoey
 * @Description:Service接口，定义邮件发送的方法
 * @date:2018年3月16日
 */

public interface MailService {
    void sendSimpleMail(String to, String subject, String content);
}

