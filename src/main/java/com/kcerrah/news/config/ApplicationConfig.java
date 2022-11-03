package com.kcerrah.news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
/**
 * 
 * Configuration file of the application
 * 
 * @author kcerrah
 *
 */
@Configuration
public class ApplicationConfig{

	/**
	 * Bean for RestTemplate
	 * @return RestTemplate instance
	 */
   @Bean
   public RestTemplate restTemplate() {
       return new RestTemplate();
   }
}
