package com.lincopilot.springcopilotonl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan (basePackages = "com.lincopilot.springcopilotonl")
@EnableTransactionManagement 
public class SpringcopilotonlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcopilotonlApplication.class, args);
	}

}
