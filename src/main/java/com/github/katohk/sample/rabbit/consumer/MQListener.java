package com.github.katohk.sample.rabbit.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component("mQListener")
public class MQListener implements ChannelAwareMessageListener {

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		String queueName = message.getMessageProperties().getConsumerQueue();
		String body = new String(message.getBody());
		System.out.println("queueName: " + queueName + " body: " + body);
		
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		//channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
	}

}
