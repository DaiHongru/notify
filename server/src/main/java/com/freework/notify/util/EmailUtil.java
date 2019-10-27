package com.freework.notify.util;

import com.freework.common.loadon.util.DesUtil;
import com.freework.notify.client.vo.EmailVo;
import com.freework.notify.exceptions.EmailOperationException;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLEncoder;

/**
 * @author daihongru
 */
@Component
public class EmailUtil {
    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);
    private static String hostName;
    private static String userName;
    private static String password;

    @Value("${smtp.hostName}")
    public void setHostName(String hostName) {
        EmailUtil.hostName = hostName;
    }

    @Value("${smtp.userName}")
    public void setUserName(String userName) {
        EmailUtil.userName = userName;
    }

    @Value("${smtp.password}")
    public void setPassword(String password) {
        EmailUtil.password = password;
    }

    /**
     * 发送邮件
     *
     * @param emailVo
     */
    public static void sendEmail(EmailVo emailVo) {
        String emailAddress = emailVo.getAddress();
        String htmlMsg = emailVo.getHtmlText();
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(hostName);
            email.setCharset("utf-8");
            email.setFrom("18959266963@163.com", "FreeWork");
            email.setAuthentication(DesUtil.getDecryptString(userName), DesUtil.getDecryptString(password));
            email.setSmtpPort(465);
            email.setSSLOnConnect(true);
            email.addTo(emailAddress);
            email.setSubject("Freework的邮件");
            email.setTextMsg("Freework的邮件");
            email.setHtmlMsg(htmlMsg);
            email.send();
            logger.info("发送邮件到：" + emailAddress);
        } catch (EmailException ee) {
            logger.error(ee.getMessage());
            try {
                throw new EmailOperationException(ee.getMessage());
            } catch (EmailOperationException eoe) {
                logger.error(eoe.getMessage());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param emailVo
     */
    public static void sendEmailEnclosure(EmailVo emailVo) {
        String emailAddress = emailVo.getAddress();
        String htmlMsg = emailVo.getHtmlText();
        String enclosureUrl = emailVo.getEnclosureUrl();
        String fileName = enclosureUrl.substring(enclosureUrl.lastIndexOf("/") + 1);
        String url = enclosureUrl.replace(fileName, "");
        try {
            String urlStr = URLEncoder.encode(fileName, "utf-8");
            HtmlEmail email = new HtmlEmail();
            EmailAttachment attachment = new EmailAttachment();
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setURL(new URL(url + urlStr));
            attachment.setName(fileName);
            email.setHostName(hostName);
            email.setCharset("utf-8");
            email.setFrom("18959266963@163.com", "FreeWork");
            email.setAuthentication(DesUtil.getDecryptString(userName), DesUtil.getDecryptString(password));
            email.setSmtpPort(465);
            email.setSSLOnConnect(true);
            email.addTo(emailAddress);
            email.setSubject("Freework的邮件");
            email.setTextMsg("Freework的邮件");
            email.setHtmlMsg(htmlMsg);
            email.attach(attachment);
            email.send();
            logger.info("发送邮件到：" + emailAddress);
        } catch (EmailException ee) {
            logger.error(ee.getMessage());
            try {
                throw new EmailOperationException(ee.getMessage());
            } catch (EmailOperationException eoe) {
                logger.error(eoe.getMessage());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
