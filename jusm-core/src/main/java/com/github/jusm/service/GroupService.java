package com.github.jusm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.jusm.entity.Group;

public interface GroupService {
	
	String prex = "group_";
	
	List<Group> listAll();

	Page<Group> findByPageable(Pageable pageable);


	void delete(String id, boolean subInclude);


	Group save(Group group);

	List<Group> treeGroups();

	void delete(String[] ids, boolean subInclude);


	Group findByNumber(String string);
}
