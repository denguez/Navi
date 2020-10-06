package tech.zumaran.navi.security;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.zumaran.navi.user.User;
import tech.zumaran.navi.user.UserRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional(readOnly = true, noRollbackFor = UsernameNotFoundException.class)
	public User findByEmail(String email)  {
		final var user = userRepository.findByEmail(email);
		if (user.isPresent()) 
			return user.get();
		else 
			throw new UsernameNotFoundException("User not found " + email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return findByEmail(username);
	}
	
	@Transactional(noRollbackFor = UsernameNotFoundException.class)
	public User updateLastLogin(String email) {
		User user = findByEmail(email);
		user.setLastLogin(new Timestamp(System.currentTimeMillis()));
		return user;
	}

}
