package com.github.kaohk.sample.rabbit.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:rabbit.xml"})
public class ProducerApp implements CommandLineRunner{
	
	@Autowired
	MQProducer producer;
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProducerApp.class);
		app.run(args);
	}

	public void run(String... args) throws MQException {

		try {
			for(int n=0; n < 5; n++) {
				producer.publish("my-queue1", "test data my-queue1:" + n);
				producer.publish("my-queue2", "test data my-queue2:" + n);
				producer.publish("my-queue3", "test data my-queue3:" + n);
			}

			producer.publish("unknown", "test data unknown");

		}finally {
			producer.stop();
		}
	}

}