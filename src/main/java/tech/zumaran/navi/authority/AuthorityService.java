package tech.zumaran.navi.authority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorityService {
	
	@Autowired
	private AuthorityRepository repository;
	
	@Transactional
	public AuthorityEntity findByAuthority(Authority authority) {
		final var maybeAuthority = repository.findByAuthority(authority);
		if (maybeAuthority.isPresent())
			return maybeAuthority.get();
		else {
			return repository.saveAndFlush(new AuthorityEntity(authority));
		}
	}

}
