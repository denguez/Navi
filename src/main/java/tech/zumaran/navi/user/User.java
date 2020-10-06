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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.zumaran.navi.authority.AuthorityEntity;

@Entity
@ToString
@Getter@Setter
public class User implements UserDetails, Serializable {
	
	private static final long serialVersionUID = -2687524538436467426L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(onMethod_=@JsonIgnore)
	private long id;

	private String name;

	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, name = "authentication_string")
	@Getter(onMethod_=@JsonIgnore)
	@Setter(onMethod_=@JsonSetter(value = "password"))
	private String password;

	@Transient
	@Getter(onMethod_=@JsonIgnore)
	@Setter(onMethod_=@JsonSetter(value = "passwordConfirm"))
	private String passwordConfirm;
	
	@ManyToMany(fetch = FetchType.EAGER)
    private Set<AuthorityEntity> authorities;
	
	private Timestamp lastLogin;

	@Getter(onMethod_=@JsonIgnore)
	private Timestamp lockedUntil;
	
	private boolean isAccountNonExpired = true;

	private boolean isAccountNonLocked = true;

	private boolean isCredentialsNonExpired = true;

	private boolean isEnabled = true;
	
	//=======================================================================================

	@Override
	@JsonIgnore
	public String getUsername() {
		return email;
	}
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities.stream()
				.map(a -> new SimpleGrantedAuthority(a.getAuthority().name()))
				.collect(Collectors.toList());
	}
}
