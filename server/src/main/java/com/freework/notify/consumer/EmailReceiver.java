package com.freework.notify.consumer;

import com.freework.common.loadon.cache.JedisUtil;
import com.freework.common.loadon.util.DateUtil;
import com.freework.notify.client.vo.EmailVo;
import com.freework.notify.util.EmailUtil;
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
public class EmailReceiver {
    public final static String MESSAGELOG_EMAIL_KEY = "notifyConsumerMessageLogEmail";
    @Autowired(required = false)
    private JedisUtil.Keys jedisKeys;
    @Autowired(required = false)
    private JedisUtil.Strings jedisStrings;

    @RabbitListener(queues = "email-queue")
    @RabbitHandler
    public void onSmsMessage(@Payload EmailVo emailVo, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        try {
            String key = MESSAGELOG_EMAIL_KEY + "_" + emailVo.getMessageId();
            if (!jedisKeys.exists(key)) {
                jedisStrings.setEx(key, 60 * 60 * 12, DateUtil.getCurrentTimeToString());
                EmailUtil.sendEmail(emailVo.getAddress(), emailVo.getHtmlText());
            }
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        } finally {
            channel.close();
        }
    }
}
