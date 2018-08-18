package com.github.jusm.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.jusm.entities.User;


public interface UserRepository extends JpaRepository<User,String> ,JpaSpecificationExecutor<User>{

	public User findByUsername(String username);
	
	Long countByUsername(String userName);
	
	@Query(value = "SELECT * FROM t_user u WHERE  u.default_role_id  IN (SELECT id FROM t_role t WHERE t.authority = :authority)", nativeQuery = true)
	public List<User> findByRoleAuthority(@Param(value = "authority")String authority);

	@Modifying
	@Query(value = "update User u set u.lastLoginTime = :lastLoginTime where u.id = :id",nativeQuery = false)
	public int  updateLastLoginTime(@Param(value = "lastLoginTime") Date lastLoginTime, @Param(value = "id")String id);

	public User findByPhonenumber(String phonenumber);

}
