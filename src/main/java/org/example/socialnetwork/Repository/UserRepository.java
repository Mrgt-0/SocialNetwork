package org.example.socialnetwork.Repository;

import org.example.socialnetwork.Model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
//    @PersistenceContext
//    private EntityManager entityManager;
//    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
//
//    public User findByUsername(String username){
//        try {
//            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.user_name = :user_name", User.class)
//                    .setParameter("user_name", username)
//                    .getSingleResult();
//            logger.info("Пользователь с именем '{}' найден.", username);
//            return user;
//        }catch (NoResultException e){
//            logger.warn("Пользователь с именем '{}' найден.", username);
//            return null;
//        }catch (Exception e){
//            logger.error("Ошибка запроса пользователя с именем '{}'.", username, e.getMessage());
//            return null;
//        }
//    }
//
//    public User findByEmail(@Email(message = "Некорректный формат email") String email) {
//        try {
//            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
//                    .setParameter("email", email)
//                    .getSingleResult();
//            logger.info("Пользователь с email '{}' найден.", email);
//            return user;
//        }catch (NoResultException e){
//            logger.warn("Пользователь с email '{}' найден.", email);
//            return null;
//        }catch (Exception e){
//            logger.error("Ошибка запроса пользователя с email '{}'.", email, e.getMessage());
//            return null;
//        }
//    }
}
