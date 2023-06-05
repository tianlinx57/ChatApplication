package fr.utc.sr03.chat.dao;

import fr.utc.sr03.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByNom(String nom);
}
