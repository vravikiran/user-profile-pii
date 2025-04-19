package com.encrypt.decrypt.kms.pii.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserInfoDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String name;
	private String password;
	private String email;
	private List<GrantedAuthority> authorities;

	public UserInfoDetails(UserProfile userProfile) {
		name = Long.toString(userProfile.getMobileno());
		email = userProfile.getEmail();
		authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(userProfile.getRole().getRole_name()));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
