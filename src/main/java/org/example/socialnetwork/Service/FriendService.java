package org.example.socialnetwork.Service;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.Model.Friend;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.FriendRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

    @Transactional
    public void addFriend(User user, User friend) {
        if (friendRepository.findByUserAndFriend(user, friend) != null)
            throw new RuntimeException("Уже в друзьях");

        Friend friendship1 = new Friend();
        friendship1.setUser(user);
        friendship1.setFriend(friend);
        friendship1.setCreated_at(LocalDateTime.now());
        friendRepository.save(friendship1);  // Сохранение первой дружбы

        // Создание второй записи дружбы (для обратной дружбы)
        Friend friendship2 = new Friend();
        friendship2.setUser(friend);
        friendship2.setFriend(user);
        friendship2.setCreated_at(LocalDateTime.now());
        friendRepository.save(friendship2);
    }

    public List<Friend> getFriends(User user) {
        return friendRepository.findByUser(user);
    }

    @Transactional
    public void deleteFriend(Friend friend){
        if(friend!=null){
            friendRepository.delete(friend);
            logger.info("Пользователь {} успешно удален.", friend.getUser().getUserName());
        }else
            logger.error("Пользователь {} не найден.", friend.getUser().getUserName());
    }
}
