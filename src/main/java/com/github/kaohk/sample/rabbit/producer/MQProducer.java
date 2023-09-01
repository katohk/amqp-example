package com.github.kaohk.sample.rabbit.producer;

import java.util.concurrent.ExecutionException;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MQProducer {
	private RabbitTemplate rabbitTemplate;
	private long publishWaitMillisecond = 5000L;

	public MQProducer(CachingConnectionFactory connectionFactory) {

		this.rabbitTemplate = new RabbitTemplate();
		this.rabbitTemplate.setConnectionFactory(connectionFactory);

		rabbitTemplate.setConfirmCallback( //ConfirmCallback#confirm
				(correlationData, ack, cause) -> {
					System.out.println(
							"confirm: id=" + correlationData.getId() + 
							" ack=" + ack + 
							" cause=" + cause);
				});

		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setReturnsCallback( //ReturnsCallback#returnedMessage
				(returned) -> {
					byte[] body = returned.getMessage().getBody();
					String sendData = new String(body);
					String queueName =  returned.getRoutingKey();
					String replyText = returned.getReplyText();
					int code = returned.getReplyCode();
					System.out.println("error: queueName=" + queueName + 
							" sendData=" + sendData +
							" replyText=" + replyText + " code=" + code);
				});
	}

	public void publish(String queueName, String sendData) throws MQException { 
		
		byte[] message = sendData.getBytes();

		try {
			CorrelationData correlationData = new CorrelationData();
			System.out.println("publish: queueName=" + queueName +
					" id=" + correlationData.getId() +
					" sendData=" + sendData);

			rabbitTemplate.invoke( //OperationsCallback#doInRabbit
					operations -> {
						operations.convertAndSend(queueName, message, correlationData);
						return operations.waitForConfirms(publishWaitMillisecond);
					});

			if (!correlationData.getFuture().get().isAck()) {
				throw new MQException("publish error");
			}

		} catch (AmqpException e) {
			throw new MQException(e.getMessage(), e);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		} catch (ExecutionException e) {
			throw new MQException(e.getMessage(), e);
		}
	}
	
	public void stop() {
		rabbitTemplate.destroy();
	}
}
