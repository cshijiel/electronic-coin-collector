package com.roc.rest.service.impl;

import com.roc.rest.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author zoey
 * @Description:邮件发送的实现类
 * @date:2018年3月16日
 */
@Service
@Component
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${mail.fromMail.addr}")
    private String from;

    /**
     * @return
     * @Description:发送简单邮件(收件人，主题，内容都暂时写死)
     * @author:zoey
     * @date:2018年3月16日
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            log.info("简单邮件发送成功！");
        } catch (Exception e) {
            log.info("发送简单邮件时发生异常！" + e);
        }
    }
}