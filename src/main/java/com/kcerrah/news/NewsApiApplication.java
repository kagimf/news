package com.kcerrah.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 
 * Spring Boot Application of News Api
 * 
 * @author kcerrah
 *
 */
@SpringBootApplication
@EnableCaching
public class NewsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsApiApplication.class, args);
	}
}
