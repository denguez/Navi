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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.zumaran.navi.authority.AuthorityEntity;

@Entity
@ToString
public class User implements UserDetails, Serializable {
	
	private static final long serialVersionUID = -2687524538436467426L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter private long id;

	@Getter @Setter private String name;

	@NotBlank(message = "Email can't be empty")
	@Column(nullable = false, unique = true)
	@Getter @Setter private String email;
	
	@NotBlank(message = "Password can't be empty")
	@Size(min = 8, message = "Password must be 8 characters long")
	@Column(nullable = false, name = "authentication_string")
	@Setter private String password;

	@Transient
	@Setter private String passwordConfirm;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @Setter private Set<AuthorityEntity> authorities;
	
	@Getter @Setter private Timestamp lastLogin;

	@Getter @Setter private Timestamp lockedUntil;
	
	@Getter @Setter private boolean isAccountNonExpired = true;

	@Getter @Setter private boolean isAccountNonLocked = true;

	@Getter @Setter private boolean isCredentialsNonExpired = true;

	@Getter @Setter private boolean isEnabled = true;
	
	//=======================================================================================

	@JsonIgnore
	public long getId() {
		return id;
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
	
	@JsonIgnore
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities.stream()
				.map(a -> new SimpleGrantedAuthority(a.getAuthority().name()))
				.collect(Collectors.toList());
	}

	/*@JsonIgnore
	public boolean isUnlocked() {
		return lockedUntil.before(new Timestamp(System.currentTimeMillis()));
	}*/

	/*@JsonIgnore
	public long getRemainingLockTime() {
		long diff = Instant.now().until(Instant.ofEpochMilli(lockedUntil.getTime()), ChronoUnit.SECONDS);
		return diff < 0 ? 0: diff;
	}*/
}
