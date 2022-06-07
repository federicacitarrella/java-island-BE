package com.bankIsland.account;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

	@Bean
	public Queue accountCreationQueue() {
		return new Queue("accountCreationQueue", false);
	}

	@Bean
	public Queue balanceQueue() {
		return new Queue("transferQueue", false);
	}

	@Bean
	public Queue transactionQueue() {
		return new Queue("transactionQueue", false);
	}

	@Bean
	public Queue closingAccountQueue() {
		return new Queue("closingAccountQueue", false);
	}

	@Bean
	public Queue openingTransactionQueue() {
		return new Queue("openingTransactionQueue", false);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}
