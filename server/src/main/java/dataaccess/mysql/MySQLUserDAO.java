package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

public class MySQLUserDAO implements UserDAO {
    public void insertUser(UserData userData) throws DataAccessException {

    }

    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    public void clear() throws DataAccessException {

    }
}
