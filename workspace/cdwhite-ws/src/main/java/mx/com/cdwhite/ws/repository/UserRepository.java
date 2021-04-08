package mx.com.cdwhite.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mx.com.cdwhite.ws.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);

}
