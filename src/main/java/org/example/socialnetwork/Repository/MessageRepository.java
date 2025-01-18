package org.example.socialnetwork.Repository;

import org.example.socialnetwork.Model.Message;
import org.example.socialnetwork.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderAndRecipient(User sender, User recipient);
    List<Message> findMessagesBySenderOrRecipient(User user1, User user2);
}
