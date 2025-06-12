package myuserservice;

import myuserservice.dao.UserDaoImpl;
import myuserservice.entity.User;
import myuserservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService(new UserDaoImpl());

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("""
                        === Меню ===
                        1. Добавить пользователя
                        2. Показать всех
                        3. Найти по ID
                        4. Обновить пользователя
                        5. Удалить пользователя
                        0. Выход
                        """);


                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> createUser();
                    case "2" -> listUsers();
                    case "3" -> findUser();
                    case "4" -> updateUser();
                    case "5" -> deleteUser();
                    case "0" -> {
                        log.info("Выход из приложения.");
                        return;
                    }
                    default -> System.out.println("Неверный ввод.");
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static void createUser() {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Возраст: ");
        int age = readInt("Возраст: ");

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        userService.addUser(user);
        log.info("Пользователь {} успешно добавлен.", user);
    }

    private static void listUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            log.info("Пользователей не найдено.");
        } else {
            users.forEach(user -> log.info(user.toString()));
        }
    }

    private static void findUser() {
        long id = readLong("ID: ");
        User user = userService.getUser(id);
        if (user != null) {
            log.info("Найден пользователь: {}", user);
        } else {
            log.warn("Пользователь с ID {} не найден.", id);
        }
    }


    private static void updateUser() {
        long id = readLong("ID: ");
        User user = userService.getUser(id);
        if (user == null) {
            log.warn("Пользователь с ID={} не найден", id);
            return;
        }

        System.out.print("Новое имя: ");
        user.setName(scanner.nextLine());
        System.out.print("Новый email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Новый возраст: ");
        user.setAge(Integer.parseInt(scanner.nextLine()));

        userService.updateUser(user);
        log.info("Пользователь обновлён: {}", user);
    }

    private static void deleteUser() {
        long id = readLong("ID: ");
        userService.deleteUser(id);
        log.info("Пользователь с ID {} удалён.", id);
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число.");
                log.warn("Ошибка при вводе целого числа", e);
            }
        }
    }

    private  static long readLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return  Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите корректный ID.");
                log.warn("Ошибка при вводе ID", e);
            }
        }
    }

}

