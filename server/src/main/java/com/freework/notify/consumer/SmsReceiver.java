package com.freework.notify.consumer;

import com.freework.common.loadon.cache.JedisUtil;
import com.freework.common.loadon.util.DateUtil;
import com.freework.notify.client.vo.SmsVo;
import com.freework.notify.util.SmsUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author daihongru
 */
@Component
public class SmsReceiver {
    public final static String MESSAGELOG_SMS_KEY = "notifyConsumerMessageLogSms";
    @Autowired(required = false)
    private JedisUtil.Keys jedisKeys;
    @Autowired(required = false)
    private JedisUtil.Strings jedisStrings;

    @RabbitListener(queues = "sms-queue")
    @RabbitHandler
    public void onSmsMessage(@Payload SmsVo smsVo, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        try {
            String key = MESSAGELOG_SMS_KEY + "_" + smsVo.getMessageId();
            if (!jedisKeys.exists(key)) {
                jedisStrings.setEx(key, 60 * 60 * 12, DateUtil.getCurrentTimeToString());
                SmsUtil.sendVerificationSms(smsVo);
            }
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        } finally {
            channel.close();
        }
    }
}
