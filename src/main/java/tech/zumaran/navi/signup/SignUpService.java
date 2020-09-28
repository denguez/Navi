package tech.zumaran.navi.signup;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tech.zumaran.navi.authority.Authority;
import tech.zumaran.navi.authority.AuthorityService;
import tech.zumaran.navi.user.User;
import tech.zumaran.navi.user.UserRepository;

@Service
public class SignUpService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
    private BCryptPasswordEncoder crypto;
	
	@Transactional
	public User signup(User user) {
		user.setPassword(crypto.encode(user.getPassword()));
		user.setAuthorities(Set.of(authorityService.findByAuthority(Authority.USER)));
		return userRepository.saveAndFlush(user);
	}
}
