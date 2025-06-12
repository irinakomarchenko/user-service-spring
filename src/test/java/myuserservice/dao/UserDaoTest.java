package myuserservice.dao;

import myuserservice.entity.User;
import myuserservice.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private UserDao userDao;
    private SessionFactory sessionFactory;

    @BeforeAll
    void setUp() {
        postgres.start();

        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        sessionFactory = HibernateUtil.getSessionFactory();
        userDao = new UserDaoImpl();
    }

    @AfterAll
    void tearDown() {
        HibernateUtil.shutdown();
        postgres.stop();
    }

    @BeforeEach
    void cleanDb() {
        var session = sessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(22);

        userDao.save(user);

        assertNotNull(user.getId());

        User found = userDao.findById(user.getId());
        assertNotNull(found);
        assertEquals("Test User", found.getName());
        assertEquals("test@example.com", found.getEmail());
        assertEquals(22, found.getAge());
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("update@example.com");
        user.setAge(30);
        userDao.save(user);

        user.setName("Updated Name");
        user.setAge(35);
        userDao.update(user);

        User found = userDao.findById(user.getId());
        assertEquals("Updated Name", found.getName());
        assertEquals(35, found.getAge());
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setName("Del Name");
        user.setEmail("del@example.com");
        user.setAge(28);
        userDao.save(user);

        userDao.delete(user.getId());
        User delete = userDao.findById(user.getId());
        assertNull(delete);
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setName("U1");
        user1.setEmail("u1@example.com");
        user1.setAge(25);

        User user2 = new User();
        user2.setName("U2");
        user2.setEmail("u2@example.com");
        user2.setAge(30);

        userDao.save(user1);
        userDao.save(user2);

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testDeleteNotExisting() {
        assertDoesNotThrow(() -> userDao.delete(-999L));
    }

    @Test
    void testFindByIdNotFound() {
        assertNull(userDao.findById(-999L));
    }
}

