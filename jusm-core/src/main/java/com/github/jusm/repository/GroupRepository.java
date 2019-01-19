package com.github.jusm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.jusm.entity.Group;


public interface GroupRepository extends JpaRepository<Group, String> {

	Group findByNumber(String number);

	@Query("select t from Group t where t.parent = null")
	List<Group> findRootReources();

	Group findByName(String name);

	@Query("select t from Group t where t.name = ?1 and t.id != ?2")
	Group findByName(String name, String id);

	@Query("select t from Group t where t.number = ?1 and t.id != ?2")
	Group findByNumber(String number, String id);

}
