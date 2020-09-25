package tech.zumaran.navi.authority;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
public class AuthorityEntity implements Serializable {

	private static final long serialVersionUID = -8612704090861817831L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, unique = true)
	private Authority authority;
	
	public AuthorityEntity(Authority authority) {
		this.authority = authority;
	}
	
	public AuthorityEntity() {}
	
	public Authority getAuthority() {
		return authority;
	}
}
