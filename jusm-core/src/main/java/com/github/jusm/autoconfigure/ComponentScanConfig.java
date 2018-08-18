package com.github.jusm.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfigureAfter({ UsmAutoConfiguration.class })
@ComponentScan(basePackages= {"com.github.jusm.component","com.github.jusm.service","com.github.jusm.controller","com.github.jusm.redis"})
@EntityScan(basePackages="com.github.jusm.entities")
@EnableJpaRepositories(basePackages = {"com.github.jusm.repository"})
public class ComponentScanConfig {

}
