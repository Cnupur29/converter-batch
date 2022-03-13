package com.schaudha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class ConverterApplication {

	public static void main(String[] args) {

		log.info("Starting Batch");
		final ApplicationContext ctx = new SpringApplicationBuilder().sources(ConverterApplication.class).run(args);
		final int exitCode = SpringApplication.exit(ctx);

		log.info("Batch ended with exit code : {}" , exitCode);

		System.exit(exitCode);
	}

}
