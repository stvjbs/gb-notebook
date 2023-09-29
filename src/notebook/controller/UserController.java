package notebook.controller;

import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class UserController {
    private final GBRepository repository;

    public UserController(GBRepository repository) {
        this.repository = repository;
    }

    public void saveUser(User user) {
        repository.create(user);
    }

    public User readUser(Long userId) throws Exception {
        List<User> users = repository.findAll();
        for (User user : users) {
            if (Objects.equals(user.getId(), userId)) {
                return user;
            }
        }

        throw new RuntimeException("User not found");
    }

    public void updateUser(String userId, User update) {
        update.setId(Long.parseLong(userId));
        repository.update(Long.parseLong(userId), update);
    }

    public void deleteUser(String id) {
        repository.delete(Long.parseLong(id));
    }

    public User createUser(String firstName, String lastName, String phone) {
        if (firstName.isEmpty()) {
            throw new RuntimeException("Имя не может быть пустым");
        }
        if (lastName.isEmpty()) {
            throw new RuntimeException("Фамилия не может быть пустой");
        }
        if (phone.isEmpty()) {
            throw new RuntimeException("Телефон не может быть пустым");
        }

        return new User(firstName.replaceAll(" ", ""), lastName.replaceAll(" ", ""), phone);
    }

    public User createsForUpdate(String firstName, String lastName, String phone) {
        if (!firstName.isEmpty()) {
            System.out.println("Firstname changed");
        }
        if (!lastName.isEmpty()) {
            System.out.println("Lastname changed");
        }
        if (!phone.isEmpty()) {
            System.out.println("Phone changed");
        }

        return new User(firstName.replaceAll(" ", ""), lastName.replaceAll(" ", ""), phone);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }
}


