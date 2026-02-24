package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.memory.database.UserDB;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    private final UserDB userDB;

    public MemoryUserDAO(UserDB userDB) {
        this.userDB = userDB;
    }

    public void insertUser(UserData userData) throws DataAccessException {
        this.userDB.userDBArray.add(userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : this.userDB.userDBArray) {
            if (Objects.equals(username, user.username())) {
                return user;
            }
        }
        return null;
    }

    public void clear() throws DataAccessException {
        this.userDB.userDBArray = new ArrayList<>();
    }
}
