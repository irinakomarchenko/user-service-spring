package myuserservice.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            setDefaultIfAbsent("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres");
            setDefaultIfAbsent("hibernate.connection.username", "postgres");
            setDefaultIfAbsent("hibernate.connection.password", "postgres");

            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            log.error("Ошибка инициализации SessionFactory", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void setDefaultIfAbsent(String key, String value) {
        if (System.getProperty(key) == null) {
            System.setProperty(key, value);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
