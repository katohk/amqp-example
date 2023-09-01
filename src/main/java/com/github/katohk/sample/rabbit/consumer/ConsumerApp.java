package com.github.katohk.sample.rabbit.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:rabbit.xml","classpath:containers.xml"})
public class ConsumerApp implements CommandLineRunner{
	
	@Autowired
	List<MQListenerContainer> containers;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ConsumerApp.class);
		app.run(args);
	}

	public void run(String... args) throws Exception {
		for(MQListenerContainer container: containers) {
			container.start();
		}
	}

}
