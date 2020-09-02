package com.mine.product.czmtr.ram.flowable.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class RabbitMqUtil {
	public static final String NAME = "admin";
	public static final String PWD = "admin123";
	public static final String HOST = "10.0.41.21";
	public static final String QUEUE = "mhqueue";
	public static final int PORT = 5672;

	private static RabbitTemplate rabbitTemplate;
    private static Channel channel = null; 
	
	public static Channel getChannel() throws IOException, TimeoutException {
		if(channel == null ) {
			ConnectionFactory factory =new ConnectionFactory();
			factory.setUsername(NAME);
			factory.setPassword(PWD);
			factory.setHost(HOST);
			factory.setPort(PORT);
			Connection connection = factory.newConnection();
	        channel = connection.createChannel(); 
		    channel = getChannel() ;
		    //设置通道为confirm模式
		    channel.confirmSelect();
		}
		return channel;
	}
	
	public static boolean sendMqMessage(String message)   {
		boolean flag = true;
		try {

			channel = getChannel();
			channel.basicPublish("", QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
		    //确认消息是否发送成功
			if(!channel.waitForConfirms()){
				log.info("RabbitMQ Send Failed");
		    	flag = false;
			} 
		} catch (Exception e) {
			log.info("RabbitMQ Send Failed [{}]", e.getMessage());
			return false;
		}
		log.info("RabbitMQ Send Success");
		return flag;
	}

    private static RabbitTemplate getRabbitTemplate() {
		rabbitTemplate = new RabbitTemplate();
        CachingConnectionFactory cf = new CachingConnectionFactory();
        cf.setAddresses(HOST);
        cf.setPort(PORT);
        cf.setUsername(NAME);
        cf.setPassword(PWD);
        rabbitTemplate.setConnectionFactory(cf);
		return rabbitTemplate;
	}
	
	public static void sendMq(String message)   {
		RabbitTemplate rabbitTemplate = getRabbitTemplate();
        rabbitTemplate.convertAndSend("", QUEUE, message);
	}
		
		
	public static void main(String[] args) throws ParseException, InterruptedException, TimeoutException { 
		
	}
}
