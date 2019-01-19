package com.github.jusm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.github.jusm.listener.ShutdownApplicationListener;
import com.github.jusm.listener.StartupApplicationListener;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class JusmApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(JusmApplication.class);
	}

	public static void main(String[] args) {
		// SpringApplication.run(JusmApplication.class, args);

		/*
		 * 如果有启动监听器就使用以下代码
		 */
		SpringApplication sa = new SpringApplication(JusmApplication.class);
		sa.addListeners(new StartupApplicationListener(), new ShutdownApplicationListener());
		sa.run(args);
	}

}
