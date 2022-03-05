package com.ewallet.EWallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
@EnableEurekaClient
@SpringBootApplication
public class EWalletApplication {


	public static void main(String[] args) {
		SpringApplication.run(EWalletApplication.class, args);

	}
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(); }


}


