package wavus.wavusproject.repository.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import wavus.wavusproject.entity.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserID(String userID);

    Optional<User> findByUserID(String userID);

}
