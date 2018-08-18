/**
 * 
 */
package com.github.jusm.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.jusm.entities.Permission;
import com.github.jusm.entities.Permission.Type;


@SpringBootTest
@RunWith(SpringRunner.class)
public class PermissionRepositoryTest {

	@Autowired
	private PermissionRepository permissionRepository;
	
	@Before
	public void clean() {
		//delete form tablename;
		permissionRepository.deleteAllInBatch();
		//一条一条删除
//		permissionRepository.deleteAll();
	}
	
//	@Test
	public void testNewadd() {
		Permission permission = new Permission();
		permission.setName("菜单一");
		permission.setDescription("菜单一测试");
		permission.setHttpMethod(HttpMethod.GET);
		permission.setNumber("001");
		permission.setPosition(2);
		permission.setType(Type.MENU);
		permission.setUri(null);
		Permission save = permissionRepository.save(permission);
		org.junit.Assert.assertEquals("菜单一", save.getName());
		
		Permission subPermission1_1 = new Permission();
		subPermission1_1.setName("菜单一-1");
		subPermission1_1.setDescription("菜单一-测试");
		subPermission1_1.setHttpMethod(HttpMethod.GET);
		subPermission1_1.setNumber("0011");
		subPermission1_1.setPosition(1);
		subPermission1_1.setParent(permission);
		subPermission1_1.setType(Type.MENU);
		subPermission1_1.setUri("/test");
		permissionRepository.save(subPermission1_1);
		org.junit.Assert.assertEquals("菜单一", save.getName());
		
		
		
		Permission permission2 = new Permission();
		permission2.setName("菜单二");
		permission2.setDescription("菜单二测试");
		permission2.setNumber("002");
		permission2.setPosition(1);
		permission2.setType(Type.MENU);
		permission2.setUri(null);
		Permission save2 = permissionRepository.save(permission2);
		org.junit.Assert.assertEquals("菜单二", save2.getName());
		
		Permission subPermission2_1 = new Permission();
		subPermission2_1.setName("菜单二-1");
		subPermission2_1.setDescription("菜单一-测试");
		subPermission2_1.setHttpMethod(HttpMethod.GET);
		subPermission2_1.setNumber("0021");
		subPermission2_1.setPosition(1);
		subPermission2_1.setParent(permission2);
		subPermission2_1.setType(Type.MENU);
		subPermission2_1.setUri("/test");
		permissionRepository.save(subPermission2_1);
		org.junit.Assert.assertEquals("菜单二", save2.getName());
		
		
		Permission subPermission2_2 = new Permission();
		subPermission2_2.setName("菜单二-2");
		subPermission2_2.setDescription("菜单一-测试");
		subPermission2_2.setHttpMethod(HttpMethod.GET);
		subPermission2_2.setNumber("0022");
		subPermission2_2.setPosition(21);
		subPermission2_2.setParent(permission2);
		subPermission2_2.setType(Type.MENU);
		subPermission2_2.setUri("/tes2t");
		permissionRepository.save(subPermission2_2);
		org.junit.Assert.assertEquals("菜单二", save2.getName());
		
		Permission permission3 = new Permission();
		permission3.setName("目录");
		permission3.setDescription("目录测试");
		permission3.setNumber("10022");
		permission3.setPosition(3);
		permission3.setParent(null);
		permission3.setType(Type.CATALOG);
		permissionRepository.save(subPermission2_2);
		org.junit.Assert.assertEquals("菜单二", save2.getName());
	}
	
	@Test
	public void testFind() {
		testNewadd();
		List<Permission> listAll = permissionRepository.findFirstLevelByType(Type.MENU);
		for (Permission permission : listAll) {
			System.out.print(permission.getPosition());
			System.out.print("\t" + permission.getNumber());
			System.out.println("\t" + permission.getName());
			for (Permission subpermission : permission.getChildren()) {
				System.out.print("\t" + subpermission.getPosition());
				System.out.print("\t" + subpermission.getNumber());
				System.out.println("\t" + subpermission.getName());
			}
		}
	}

}
