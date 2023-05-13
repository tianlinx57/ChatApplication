package fr.utc.sr03.chat.dao;
import java.util.Optional;


import fr.utc.sr03.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
