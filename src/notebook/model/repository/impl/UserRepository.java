package notebook.model.repository.impl;

import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements GBRepository {
    private final UserMapper mapper;
    private final String fileName;
    public UserRepository(String fileName) {
        this.mapper = new UserMapper();
        this.fileName = fileName;
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        List<String> lines = readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id){
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(Long userId, User update) {
        if (userId == null) {
            throw  new RuntimeException("id cannot be empty");
        }
        List<User> users = findAll();
        User editUser = users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        if (!update.getFirstName().isEmpty()) {
            editUser.setFirstName(update.getFirstName());
        }
        if (!update.getLastName().isEmpty()) {
            editUser.setLastName(update.getLastName());
        }
        if (!update.getPhone().isEmpty()) {
            editUser.setPhone(update.getPhone());
        }

        write(users);
        return Optional.of(update);
    }

    @Override
    public Optional<User> delete(Long id) {
        if (id == null) {
            throw new RuntimeException("id cannot be empty");
        }
        List<User> users = findAll();
        User deleteUser = null;
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (!users.get(i).getId().equals(id)) {
                lines.add(mapper.toInput(users.get(i)));
            }
            deleteUser = users.get(i);
        }

        saveAll(lines);
        return Optional.of(deleteUser);

    }

    @Override
    public List<String> readAll() {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(fileName);
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            if (line != null) {
                lines.add(line);
            }
            while (line != null) {
                // считываем остальные строки в цикле
                line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public void saveAll(List<String> data) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (String line : data) {
                // запись всей строки
                writer.write(line);
                // запись по символам
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u: users) {
            lines.add(mapper.toInput(u));
        }
        saveAll(lines);
    }

}
