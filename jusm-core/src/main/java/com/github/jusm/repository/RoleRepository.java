package com.github.jusm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jusm.entities.Role;


public interface RoleRepository  extends JpaRepository<Role,String>{

	Role findByName(String name);

	Role findByAuthority(String authority);

}
