package tech.zumaran.navi.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("FROM User p WHERE p.email = :email")
	Optional<User> findByEmail(@Param("email") String email);
}
