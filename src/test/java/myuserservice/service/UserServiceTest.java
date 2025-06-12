package myuserservice.service;


import myuserservice.dao.UserDao;
import myuserservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    public void addUser_validUser_callsDaoSave(){
        User user = new User();
        user.setName("Anna");
        user.setEmail("anna@example.com");
        user.setAge(25);

        userService.addUser(user);

        verify(userDao).save(user);
    }

    @Test
    void addUser_nullName_throwsException() {
        User user = new User();
        user.setEmail("test@example.com");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
        assertEquals("Имя и Email пользователя не могут быть null", ex.getMessage());
        verify(userDao, never()).save(any());
    }

    @Test
    void addUser_nullEmail_throwsException() {
        User user = new User();
        user.setName("Test");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
        assertEquals("Имя и Email пользователя не могут быть null", ex.getMessage());
        verify(userDao, never()).save(any());
    }

    @Test
    void getUser_delegatesToDao() {
        User user = new User();
        user.setId(1L);
        when(userDao.findById(1L)).thenReturn(user);

        User result = userService.getUser(1L);

        assertEquals(user, result);
        verify(userDao).findById(1L);
    }

    @Test
    void getAllUsers_delegatesToDao() {
        List<User> userList = List.of(new User(), new User());
        when(userDao.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userDao).findAll();
    }

    @Test
    void updateUser_delegatesToDao() {
        User user = new User();
        userService.updateUser(user);

        verify(userDao).update(user);
    }

    @Test
    void deleteUser_delegatesToDao() {
        userService.deleteUser(42L);

        verify(userDao).delete(42L);
    }

}

