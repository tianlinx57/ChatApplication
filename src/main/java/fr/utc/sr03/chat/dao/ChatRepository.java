package fr.utc.sr03.chat.dao;

import fr.utc.sr03.chat.model.Chat;
import fr.utc.sr03.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByNom(String nom);
    Optional<Chat> findById(long id);
}
