package com.imennez.eventsourcespike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class EventSourceSpikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventSourceSpikeApplication.class, args);
	}

}
