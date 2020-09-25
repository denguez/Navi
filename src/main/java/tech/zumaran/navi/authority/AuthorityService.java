package tech.zumaran.navi.authority;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
	
	@Autowired
	private AuthorityRepository repository;
	
	@Transactional
	public AuthorityEntity findByAuthority(Authority authority) {
		Optional<AuthorityEntity> maybeAuthority = repository.findByAuthority(authority);
		if (maybeAuthority.isPresent())
			return maybeAuthority.get();
		else {
			return repository.saveAndFlush(new AuthorityEntity(authority));
		}
	}

}
