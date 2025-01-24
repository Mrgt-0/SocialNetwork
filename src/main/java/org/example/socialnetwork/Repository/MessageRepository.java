package org.example.socialnetwork.Repository;

import org.example.socialnetwork.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender = :sender AND m.recipient = :recipient) OR (m.sender = :recipient AND m.recipient = :sender) ORDER BY m.created_at")
    List<Message> findMessagesBySenderAndRecipient(@Param("sender") User sender, @Param("recipient") User recipient);
}
