package com.github.jusm.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jusm.entity.Role;


public interface RoleRepository  extends JpaRepository<Role,String>{

	Role findByName(String name);

	Role findByAuthority(String authority);

	List<Role> findByAuthorityIn(Collection<String> authority);
}
