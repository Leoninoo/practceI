package crud;

import models.User;

import java.util.List;

public interface UsersDao {
    User findById(long id);
    void save(User u);
    void update(User u);
    void delete(long id);

    List<User> findAll();
}
