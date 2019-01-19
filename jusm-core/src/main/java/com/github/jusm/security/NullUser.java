package com.github.jusm.security;

public final class NullUser extends JwtUser {

	public static NullUser getInstance() {
		return NullUserHolder.instance;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String NULL_ID = "00000000-0000-0000-0000-000000000000";
	public static final String NULL_USERNAME = "00000000000000";
	public static final String NULL_NICKNAME = "00000000000000";
	
	
	@Override
	public String getId() {
		return NULL_ID;
	}
	
	@Override
	public String getUsername() {
		return NULL_USERNAME;
	}
	
	@Override
	public String getNickname() {
		return NULL_NICKNAME;
	}
	
	private NullUser() {
	}
	
	private static class NullUserHolder{
		private static NullUser instance = new NullUser();
	}

	public static void main(String[] args) {
		NullUser instance = NullUser.getInstance();
		
		System.out.println(instance.getUsername());
	}
}
