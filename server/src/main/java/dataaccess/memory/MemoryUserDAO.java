package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    private ArrayList<UserData> userDB;

    public MemoryUserDAO() {
        this.userDB = new ArrayList<>();
    }

    public void insertUser(UserData userData) throws DataAccessException {
        this.userDB.add(userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : this.userDB) {
            if (Objects.equals(username, user.username())) {
                return user;
            }
        }
        return null;
    }

    public void clear() throws DataAccessException {
        this.userDB = new ArrayList<>();
    }
}
