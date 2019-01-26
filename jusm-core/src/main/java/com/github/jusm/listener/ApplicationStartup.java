package com.github.jusm.listener;

import org.springframework.boot.CommandLineRunner;

/**
 * 程序启动完毕后你可以在这里做你想要做的事情
 *
 */
public class ApplicationStartup implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		System.out.println(args);
	}

}
