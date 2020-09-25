package tech.zumaran.navi.user;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import tech.zumaran.navi.authority.AuthorityEntity;

@Entity
public class User implements UserDetails, Serializable {
	
	private static final long serialVersionUID = -2687524538436467426L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@NotBlank(message = "Email can't be empty")
	@Column(nullable = false, unique = true)
	private String email;
	
	@NotBlank(message = "Password can't be empty")
	@Size(min = 8, message = "Password must be 8 characters long")
	@Column(nullable = false, name = "authentication_string")
	private String password;

	@Transient
	private String passwordConfirm;
	
	@ManyToMany(fetch = FetchType.EAGER)
    private Set<AuthorityEntity> authorities;
	
	private Timestamp lastLogin;

	private Timestamp lockedUntil;
	
	private boolean isAccountNonExpired = true;

	private boolean isAccountNonLocked = true;

	private boolean isCredentialsNonExpired = true;

	private boolean isEnabled = true;
	
	//=======================================================================================
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	@JsonSetter(value = "password")
	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonIgnore
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	
	@JsonSetter(value = "passwordConfirm")
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities.stream()
				.map(a -> new SimpleGrantedAuthority(a.getAuthority().name()))
				.collect(Collectors.toList());
	}
	
	public void setAuthorities(Set<AuthorityEntity> authorities) {
		this.authorities = authorities;
	}
	
	public Timestamp getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	@JsonIgnore
	public Timestamp getLockedUntil() {
		return lockedUntil;
	}
	
	public void setLockedUntil(Timestamp lockedUntil) {
		this.lockedUntil = lockedUntil;
	}

	@JsonIgnore
	public boolean isUnlocked() {
		return lockedUntil.before(new Timestamp(System.currentTimeMillis()));
	}

	/*@JsonIgnore
	public long getRemainingLockTime() {
		long diff = Instant.now().until(Instant.ofEpochMilli(lockedUntil.getTime()), ChronoUnit.SECONDS);
		return diff < 0 ? 0: diff;
	}*/
	
	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}
	
	@Override
	public String toString() {
		return name + " email: " + email;
	}

}
