package tech.zumaran.navi.authority;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

	@Query("FROM AuthorityEntity p WHERE p.authority = :authority")
	Optional<AuthorityEntity> findByAuthority(@Param("authority") Authority authority);

}
