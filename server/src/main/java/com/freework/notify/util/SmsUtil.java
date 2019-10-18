package com.freework.notify.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.freework.common.loadon.util.DesUtil;
import com.freework.notify.exceptions.SmsOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author daihongru
 */
@Component
public class SmsUtil {

    private static Logger logger = LoggerFactory.getLogger(SmsUtil.class);
    private static String signName;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String templateCode;

    @Value("${aliyun.sms.signName}")
    public void setSignName(String signName) {
        SmsUtil.signName = signName;
    }

    @Value("${aliyun.sms.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        SmsUtil.accessKeyId = accessKeyId;
    }

    @Value("${aliyun.sms.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        SmsUtil.accessKeySecret = accessKeySecret;
    }

    @Value("${aliyun.sms.templateCode}")
    public void setTemplateCode(String templateCode) {
        SmsUtil.templateCode = templateCode;
    }

    public static void sendVerificationSms(String phoneNumber, String code) {
        String templateParam = "{\"code\":\"" + code + "\"}";
        DefaultProfile profile = DefaultProfile.getProfile("default", DesUtil.getDecryptString(accessKeyId), DesUtil.getDecryptString(accessKeySecret));
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "default");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", templateParam);
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.info("发送验证码到：" + phoneNumber + "。【阿里云服务器返回消息：" + response.getData() + "】");
        } catch (ServerException e) {
            logger.error("发送验证码短信时阿里云服务器异常" + e.getMessage());
            throw new SmsOperationException(e.getMessage());
        } catch (ClientException e) {
            logger.error("发送验证码短信时本地服务器异常" + e.getMessage());
            throw new SmsOperationException(e.getMessage());
        }
    }
}
