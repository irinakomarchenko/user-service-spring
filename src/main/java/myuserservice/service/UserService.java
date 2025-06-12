package myuserservice.service;

import myuserservice.dao.UserDao;
import myuserservice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {
    private  static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(User user) {
        if (user.getName() == null || user.getEmail() == null) {
            log.error("Имя и Email пользователя не могут быть null");
            throw new IllegalArgumentException("Имя и Email пользователя не могут быть null");
        }
        userDao.save(user);
        log.info("Пользователь {} добавлен через сервис.", user);
    }

    public User getUser(Long id) {
        return  userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(User user) {
        userDao.update(user);
        log.info("Пользователь {} обновлён через сервис.", user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
        log.info("Пользователь с ID {} удалён через сервис.", id);
    }
}
