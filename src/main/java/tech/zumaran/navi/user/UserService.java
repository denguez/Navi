package tech.zumaran.navi.user;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tech.zumaran.navi.authority.Authority;
import tech.zumaran.navi.authority.AuthorityService;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
    private BCryptPasswordEncoder crypto;

	public User findByEmail(String email)  {
		Optional<User> user = repository.findByEmail(email);
		if (user.isPresent()) 
			return user.get();
		else 
			throw new UsernameNotFoundException(email);
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return findByEmail(email);
	}
	
	@Transactional
	public User signup(User user) {
		user.setPassword(crypto.encode(user.getPassword()));
		user.setAuthorities(Set.of(authorityService.findByAuthority(Authority.USER)));
		return repository.saveAndFlush(user);
	}

	/*@Override
	public void update(User old, User updated) {
		old.setName(updated.getName());
	}*/

	@Transactional
	public User updateLastLogin(User user) {
		user = findByEmail(user.getEmail());
		user.setLastLogin(new Timestamp(System.currentTimeMillis()));
		repository.flush();
		return user;
	}


	public User getAccountDetails() {
		return null;
	}
	
	/*public User verifyLock(String email)  {
		User user = findByEmail(email);
		if (!user.isAccountNonLocked()) {
			String msg = "User " + user.getEmail() + " account is locked until " 
					+ user.getLockEndTime() + ". Time remaining: " + user.getRemainingLockTime();
			//LOG.warn(msg);
			
			if (user.isLockEnded()) {
				user.setAccountNonLocked(true);
				repository.flush();
				//LOG.info("User " + user.getEmail() + " account is unlocked." );
			} else 
				throw new Transaction_Exception(msg);
		}
		return user;
	}*/
	
	/*@Transactional
	public User lockAccount(String email) throws NotFound_Exception {
		User user = findByEmail(email);
		user.setAccountNonLocked(false);
		user.setLockEndTime(10);
		repository.flush();

		LOG.warn("User account locked until " + user.getLockEndTime() 			
					+ ". Time remaining: " + user.getLockTimeRemaining());
		return user;
	}
	
	@BoostTransaction(message = "verifying lock account")
	*/


}

