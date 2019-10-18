package com.freework.notify.util;

import com.freework.common.loadon.util.DesUtil;
import com.freework.notify.exceptions.EmailOperationException;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
     * @param emailAddress,htmlMsg
     */
    public static void sendEmail(String emailAddress, String htmlMsg) {
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
            //todo 关闭邮件发送(y)
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
