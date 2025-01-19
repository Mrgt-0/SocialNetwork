package org.example.socialnetwork.Repository;

import org.example.socialnetwork.Model.Friend;
import org.example.socialnetwork.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUser(User user);
    Friend findByUserAndFriend(User user, User friend);
}