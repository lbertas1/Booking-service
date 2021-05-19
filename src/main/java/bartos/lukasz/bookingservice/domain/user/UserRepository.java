package bartos.lukasz.bookingservice.domain.user;

import bartos.lukasz.bookingservice.domain.user.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);

    List<User> findAllByUsernameIsIn(List<String> username);
}
