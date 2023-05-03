package org.academiadecodigo.thefellowshift.passionatepal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class PassionatepalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassionatepalApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		var template = new RestTemplate();

		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return template;
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("API_Call_Lookup-");
		executor.initialize();
		return executor;
	}
}
