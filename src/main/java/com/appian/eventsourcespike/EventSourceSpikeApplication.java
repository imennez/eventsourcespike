package com.appian.eventsourcespike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@ServletComponentScan
@SpringBootApplication
public class EventSourceSpikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventSourceSpikeApplication.class, args);
	}

}
