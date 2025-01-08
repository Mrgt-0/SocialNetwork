package org.example.socialnetwork.Repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.socialnetwork.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class UserRepository implements JpaRepository<User, Integer> {
    @PersistenceContext
    private EntityManager entityManager;
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Override
    public void flush() {

    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public User getOne(Integer integer) {
        return null;
    }

    @Override
    public User getById(Integer integer) {
        return null;
    }

    @Override
    public User getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public User save(User user) {
        try {
            entityManager.persist(user);
            logger.info("Пользователь успешно добавлен.");
        } catch (Exception e) {
            logger.error("Не удалось сохранить пользователя.", e.getMessage());
        }
        return user;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public List<User> findAllById(Iterable<Integer> integers) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {
        User user=entityManager.find(User.class, id);
        if(user!=null){
            entityManager.remove(user);
            logger.info("Пользователь с ID {} успешно удален.", id);
        }else
            logger.warn("Не удалось удалить пользователя: пользователь с ID {} не найден.", id);
    }

    @Override
    public void delete(User user) {
        if(user!=null){
            entityManager.remove(user);
            logger.info("Пользователь с ID {} успешно удален.", user.getUserId());
        }else
            logger.warn("Не удалось удалить пользователя: пользователь с ID {} не найден.", user.getUserId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> users) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<User> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    //public User findByUsername(String username);
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public UserRepository() {
//        super(User.class);
//    }
//
//    @Transactional
//    public User save(User user) throws SystemException {
//        try {
//            entityManager.persist(user);
//        } catch (NullPointerException e) {
//            System.out.println(e.getMessage());
//            return null; // если произошла ошибка, лучше обработать ее
//        }
//        return user;
//    }
//
//    @Override
//    protected String getCreateSql() {
//        return "INSERT INTO User(user_name, first_name, last_name, password_hash, email, birthdate, profile_picture, user_role) " +
//                "VALUES (?, ?, ?, ?, ?)";
//    }
//
//    @Override
//    protected void delete(int bookId) throws HeuristicRollbackException, SystemException, HeuristicMixedException, RollbackException {
//        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
//        Transaction tx1 = (Transaction) session.beginTransaction();
//        session.delete(bookId);
//        tx1.commit();
//        session.close();
//    }
//
//    @Transactional
//    public User getById(int userId) {
//        return entityManager.find(User.class, userId);
//    }
//
//    @Override
//    protected void populateCreateStatement(PreparedStatement statement, User entity) throws SQLException {
//
//    }
//
//    @Override
//    protected void setId(User entity, Integer integer) {
//
//    }
//
//    @Override
//    protected String getSelectByIdSql() {
//        return "";
//    }
//
//    @Override
//    protected void setIdParameter(PreparedStatement statement, Integer integer) throws SQLException {
//
//    }
//
//    @Override
//    protected User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
//        return null;
//    }
//
//    @Override
//    protected String getSelectAllSql() {
//        return "SELECT * FROM Book";
//    }
//
//    @Override
//    protected String getUpdateSql() {
//        return "";
//    }
//
//    @Override
//    protected void populateUpdateStatement(PreparedStatement statement, User entity) throws SQLException {
//
//    }
//
//    @Override
//    protected String getDeleteSql() {
//        return "DELETE FROM User WHERE user_id = ?";
//    }
}
