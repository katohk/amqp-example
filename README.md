# Spring AMQP example

This repository provides sample code for implementing asynchronous messaging using Spring AMQP.

## Code Structure

The main components included in this repository are as follows:

- `MQListenerContainer.java`: A derived class of SimpleMessageListenerContainer responsible for listening to queues and distributing asynchronous processing. Bean definitions for this class are located in the `containers.xml` file.
- `MQListener.java`: A listener class that receives and processes messages.
- `MQProducer.java`: A producer class responsible for sending messages and handling acknowledgments.
- `ConsumerApp.java`: The main class for the consumer. Running it will start queue listening.
- `ProducerApp.java`: The main class for the producer. It is used to test message sending.
- `rabbit.xml`: Bean definitions for RabbitMQ connection settings. Configure these according to your environment.
