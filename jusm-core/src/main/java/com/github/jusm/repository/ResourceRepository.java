package com.github.jusm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jusm.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

	Resource findByToken(String token);

	Resource findByFileName(String name);

}
