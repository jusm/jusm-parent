package com.github.jusm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.jusm.entity.Group;
import com.github.jusm.exception.ReferenceException;
import com.github.jusm.exception.RepeatException;
import com.github.jusm.exception.ValidException;
import com.github.jusm.repository.GroupRepository;
import com.github.jusm.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupRepository groupRepository;

	@Override
	public List<Group> listAll() {
		return groupRepository.findAll();
	}

	@Override
	public List<Group> treeGroups() {
		return groupRepository.findRootReources();
	}

	@Override
	public Page<Group> findByPageable(Pageable pageable) {
		return groupRepository.findAll(pageable);
	}


	@Override
	@Transactional
	public void delete(String id, boolean subInclude) {
		Group group = groupRepository.findOne(id);
		List<Group> children = group.getChildren();
		if (children != null && children.isEmpty()) {
			groupRepository.delete(id);
		} else {
			if (subInclude) {
				groupRepository.delete(id);//这里会删除其子集
			}else {
				throw new ReferenceException("存在下级分组，请先删除子分组!");
			}
		}
	}

	 
	private void checkName(String name) {
		if (StringUtils.isNotBlank(name)) {
			Group g = groupRepository.findByName(name);
			if (g != null) {
				throw new RepeatException("已存在的分组名称");
			}
		} else {
			throw new ValidException("分组名称不能为空");
		}
	}

	private void checkNumber(String number) {
		if (StringUtils.isNotBlank(number)) {
			Group g = groupRepository.findByNumber(number);
			if (g != null) {
				throw new RepeatException("已存在的分组编码");
			}
		} else {
			throw new ValidException("分组编码不能为空");
		}
	}

	@Override
	public Group save(Group group) {
		return groupRepository.save(group);
	}

	@Override
	public void delete(String[] ids, boolean subInclude) {
		ArrayList<Group> arg0 = new ArrayList<>(ids.length);
		for (int i = 0; i < ids.length; i++) {
			Group group = new Group();
			group.setId(ids[i]);
			arg0.add(group);
		}
		groupRepository.deleteInBatch(arg0);
	}

	 

	private void checkNumber(String number, String id) {
		if (StringUtils.isNotBlank(number)) {
			Group g = groupRepository.findByNumber(number, id);
			if (g != null) {
				throw new RepeatException("已存在的分组编码");
			}
		} else {
			throw new ValidException("分组编码不能为空");
		}
	}

	private void checkName(String name, String id) {
		if (StringUtils.isNotBlank(name)) {
			Group g = groupRepository.findByName(name, id);
			if (g != null) {
				throw new RepeatException("已存在的分组名称");
			}
		} else {
			throw new ValidException("分组名称不能为空");
		}
	}

	@Override
	public Group findByNumber(String number) {
		return groupRepository.findByNumber(number);
	}
}
