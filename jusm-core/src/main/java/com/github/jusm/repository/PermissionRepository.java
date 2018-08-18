package com.github.jusm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.jusm.entities.Permission;
import com.github.jusm.entities.Permission.Type;


public interface PermissionRepository extends JpaRepository<Permission, String> {
	
	@Query("select t from Permission t where t.name = ?1 and t.id != ?2")
	Permission findByName(String name, String id);

	@Query("select t from Permission t where t.number = ?1 and t.id != ?2")
	Permission findByNumber(String number, String id);

	@Query("select t from Permission t where t.parent = null order by position")
	public List<Permission> findFirstLevel();
	
	
	@Query("select t from Permission t where t.parent = null and type = ?1 order by position")
	public List<Permission> findFirstLevelByType(Type type);
	
	
	Permission findByName(String name);
	
	Permission findByNumber(String number);
}
