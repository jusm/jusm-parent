package com.github.jusm.signature;

/**
 * 访问权限控制类
 *
 */
public class AccessControlLimit {
	
	private String kye;//组织机构代码
	
	private String accessKey;//密钥
	
	private String accessInterface;//接口控制

	public String getKye() {
		return kye;
	}

	public void setKye(String kye) {
		this.kye = kye;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getAccessInterface() {
		return accessInterface;
	}

	public void setAccessInterface(String accessInterface) {
		this.accessInterface = accessInterface;
	}
	
}