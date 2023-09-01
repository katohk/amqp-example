package com.github.katohk.sample.rabbit.consumer;

import java.io.IOException;

import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;


public class MQListenerContainer extends SimpleMessageListenerContainer{
	
	private int consumerLimit = 0;
	
	public MQListenerContainer(CachingConnectionFactory connectionFactory, MQListener listener) {
		super();

		connectionFactory.addChannelListener(
				// ChannelListener#onCreate
				(channel,transactional)->{
					try {
						if (consumerLimit != 0) {
							channel.basicQos(consumerLimit,false); // Per consumer limit
						}
					} catch (IOException e) {
						throw new AmqpIOException(e);
					}
				}); 

		super.setConnectionFactory(connectionFactory);

		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
		super.setMessageListener(adapter);
		
		super.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		super.setDefaultRequeueRejected(true);
		super.setAutoStartup(false);
		super.setGlobalQos(true); // Per Channel limit
	}
	
	public void setQueuenames(String queuenames) {
		String queues[] = null;
		if ( queuenames != null && queuenames.length() != 0) {
			queues = queuenames.trim().split(",");
		}
		this.setQueueNames(queues);
	}
	
	public void setConsumerPrefetchCount(int count) {
		consumerLimit = count;
	}

}
