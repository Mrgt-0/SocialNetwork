package org.example.socialnetwork.Repository;

import org.example.socialnetwork.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
