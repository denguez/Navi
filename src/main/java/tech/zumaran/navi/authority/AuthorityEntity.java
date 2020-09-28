package tech.zumaran.navi.authority;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "authority")
public class AuthorityEntity implements Serializable {
	
	private static final long serialVersionUID = 2372488948350959814L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter private long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	@Getter @Setter private Authority authority;
	
	public AuthorityEntity(Authority authority) {
		this.authority = authority;
	}
	
}
