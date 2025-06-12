package myuserservice.dao;

import myuserservice.entity.User;
import myuserservice.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            log.info("Сохранён пользователь: {}", user);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error("Ошибка при сохранении пользователя", e);

        }
    }

    @Override
    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                log.info("Найден пользователь по ID {}: {}", id, user);
            } else {
                log.warn("Пользователь с ID {} не найден", id);
            }
            return user;
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            log.info("Найдено {} пользователей", users.size());
            return users;
        }
    }

    @Override
    public void update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            log.info("Обновлён пользователь: {}", user);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error("Ошибка при обновлении пользователя", e);
        }
    }
    @Override
    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                log.info("Удалён пользователь: {}", id);
            } else {
                log.warn("Попытка удалить несуществующего пользователя с ID={}", id);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error("Ошибка при удалении пользователя", e);
        }
    }
}
