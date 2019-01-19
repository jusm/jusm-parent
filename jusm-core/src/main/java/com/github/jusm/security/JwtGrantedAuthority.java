package com.github.jusm.security;

import org.springframework.security.core.GrantedAuthority;

public final class  JwtGrantedAuthority implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;

	private String name;

	private String authority;
	
	
	public JwtGrantedAuthority(String name, String authority) {
		this.name = name;
		this.authority = authority;
	}


	public String getName() {
		return name;
	}
 

	@Override
	public String getAuthority() {
		return authority;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JwtGrantedAuthority other = (JwtGrantedAuthority) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		return true;
	}
}
