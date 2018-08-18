package com.github.jusm.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * package org.springframework.security.core.userdetails; 参照这个类写自己的user
 */
@Entity
@Table(name = "usm_user")
public class User extends UuidEntity implements UserDetails/* , CredentialsContainer */ {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// ~ Instance fields
	// ================================================================================================
	@JsonIgnore
	private boolean accountNonExpired;
	@JsonIgnore
	private boolean accountNonLocked;
	@JsonIgnore
	private boolean credentialsNonExpired;

	private boolean enabled;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = true, unique = false)
	private String nickname;// 某些情况需要昵称

	@JsonIgnore
	@Column(nullable = false)
	private String password;

	/** 手机号码 */
	private String phonenumber;

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	/** 邮箱 */
	private String email;

	@Column(name = "last_password_reset_date")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonIgnore
	private Date lastPasswordResetDate = new Date();

	@Column(name = "last_login_time")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonIgnore
	private Date lastLoginTime;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usm_user_group", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "group_id", referencedColumnName = "ID") })
	private Set<Group> groups = new HashSet<Group>();

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usm_user_role", joinColumns = {
			@JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	private Set<Role> roles = new HashSet<Role>();

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "default_role_id")
	private Role defaultRole;
	// ~ Constructors
	// ===================================================================================================

	public User() {
	}

	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
	 *
	 * @param username
	 *            the username presented to the
	 *            <code>DaoAuthenticationProvider</code>
	 * @param password
	 *            the password that should be presented to the
	 *            <code>DaoAuthenticationProvider</code>
	 * @param enabled
	 *            set to <code>true</code> if the user is enabled
	 * @param accountNonExpired
	 *            set to <code>true</code> if the account has not expired
	 * @param credentialsNonExpired
	 *            set to <code>true</code> if the credentials have not expired
	 * @param accountNonLocked
	 *            set to <code>true</code> if the account is not locked
	 * @param authorities
	 *            the authorities that should be granted to the caller if they
	 *            presented the correct username and password and the user is
	 *            enabled. Not null.
	 *
	 * @throws IllegalArgumentException
	 *             if a <code>null</code> value was passed either as a parameter or
	 *             as an element in the <code>GrantedAuthority</code> collection
	 */
	User(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Set<Role> roles, String email, Role defaultRole, Set<Group> groups) {

		if (((username == null) || "".equals(username))) {
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.email = email;
		this.defaultRole = defaultRole;
		this.roles = roles;
		this.groups = groups;
		if (this.defaultRole == null && roles != null && !roles.isEmpty()) {
			this.defaultRole = sortAuthorities(roles).first();
		}
	}

	private static SortedSet<Role> sortAuthorities(Collection<Role> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		SortedSet<Role> sortedAuthorities = new TreeSet<Role>(new RoleComparator());
		for (Role role : authorities) {
			Assert.notNull(role, "Role list cannot contain any null elements");
			sortedAuthorities.add(role);
		}
		return sortedAuthorities;
	}

	private static class RoleComparator implements Comparator<Role>, Serializable {
		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

		public int compare(Role g1, Role g2) {
			return g1.getPriority().compareTo(g2.getPriority());
		}
	}

	/**
	 * 除了本身的角色还有所有分组的所属的角色
	 */
	public Collection<GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> emptySet = new HashSet<>(roles.size());
		for (GrantedAuthority grantedAuthority : roles) {
			emptySet.add(grantedAuthority);
		}
		Set<Group> groups = getGroups();
		for (Group group : groups) {
			emptySet.addAll(group.getRoles());
		}
		return emptySet;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	// public void eraseCredentials() {
	// password = null;
	// }

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Returns {@code true} if the supplied object is a {@code User} instance with
	 * the same {@code username} value.
	 * <p>
	 * In other words, the objects are equal if they have the same username,
	 * representing the same principal.
	 */
	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof User) {
			return username.equals(((User) rhs).username);
		}
		return false;
	}

	/**
	 * Returns the hashcode of the {@code username}.
	 */
	@Override
	public int hashCode() {
		return username.hashCode();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Set<Group> getGroups() {
		if (groups == null) {
			groups = new HashSet<>();
		}
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public Set<Role> getRoles() {
		if (roles == null) {
			roles = new HashSet<>();
		}
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
		if (defaultRole == null) {
			this.defaultRole = sortAuthorities(roles).first();
		}
	}

	public Role getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(Role defaultRole) {
		this.defaultRole = defaultRole;
		getRoles().add(getDefaultRole());
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(": ");
		sb.append("Username: ").append(this.username).append("; ");
		sb.append("Password: [PROTECTED]; ");
		sb.append("Enabled: ").append(this.enabled).append("; ");
		sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
		sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
		sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

		if (!roles.isEmpty()) {
			sb.append("Granted Authorities: ");

			boolean first = true;
			for (GrantedAuthority auth : roles) {
				if (!first) {
					sb.append(",");
				}
				first = false;

				sb.append(auth);
			}
		} else {
			sb.append("Not granted any authorities");
		}

		return sb.toString();
	}

	public static UserBuilder withUsername(String username) {
		return new UserBuilder().username(username);
	}

	/**
	 * Builds the user to be added. At minimum the username, password, and
	 * authorities should provided. The remaining attributes have reasonable
	 * defaults.
	 */
	public static class UserBuilder {
		private String username;
		private String password;
		private String email;
		private Set<Role> roles;
		private Set<Group> groups;
		private Role defaultRole;
		private boolean accountExpired;
		private boolean accountLocked;
		private boolean credentialsExpired;
		private boolean disabled;

		/**
		 * Creates a new instance
		 */
		private UserBuilder() {
		}

		/**
		 * Populates the username. This attribute is required.
		 *
		 * @param username
		 *            the username. Cannot be null.
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this user)
		 */
		private UserBuilder username(String username) {
			Assert.notNull(username, "username cannot be null");
			this.username = username;
			return this;
		}

		/**
		 * Populates the password. This attribute is required.
		 *
		 * @param password
		 *            the password. Cannot be null.
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this user)
		 */
		public UserBuilder password(String password) {
			Assert.notNull(password, "password cannot be null");
			this.password = password;
			return this;
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities
		 *            the authorities for this user. Cannot be null, or contain null
		 *            values
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this user)
		 * @see #roles(String...)
		 */
		public UserBuilder roles(Role... authorities) {
			return roles(Arrays.asList(authorities));
		}

		public UserBuilder groups(Group... groups) {
			return groups(Arrays.asList(groups));
		}

		public UserBuilder groups(List<Group> groups) {
			this.groups = new HashSet<Group>(groups);
			return this;
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities
		 *            the authorities for this user. Cannot be null, or contain null
		 *            values
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this user)
		 * @see #roles(String...)
		 */
		public UserBuilder roles(List<Role> authorities) {
			this.roles = new HashSet<Role>(authorities);
			return this;
		}

		public UserBuilder defaultRole(Role defaultRole) {
			this.defaultRole = defaultRole;
			roles(defaultRole);
			return this;
		}

		/**
		 * Defines if the account is expired or not. Default is false.
		 *
		 * @param accountExpired
		 *            true if the account is expired, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this user)
		 */
		public UserBuilder accountExpired(boolean accountExpired) {
			this.accountExpired = accountExpired;
			return this;
		}

		/**
		 * Defines if the account is locked or not. Default is false.
		 *
		 * @param accountLocked
		 *            true if the account is locked, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this user)
		 */
		public UserBuilder accountLocked(boolean accountLocked) {
			this.accountLocked = accountLocked;
			return this;
		}

		/**
		 * Defines if the credentials are expired or not. Default is false.
		 *
		 * @param credentialsExpired
		 *            true if the credentials are expired, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this user)
		 */
		public UserBuilder credentialsExpired(boolean credentialsExpired) {
			this.credentialsExpired = credentialsExpired;
			return this;
		}

		/**
		 * Defines if the account is disabled or not. Default is false.
		 *
		 * @param disabled
		 *            true if the account is disabled, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this user)
		 */
		public UserBuilder disabled(boolean disabled) {
			this.disabled = disabled;
			return this;
		}

		public User build() {
			return new User(username, password, !disabled, !accountExpired, !credentialsExpired, !accountLocked, roles,
					email, defaultRole, groups);
		}
	}
}
