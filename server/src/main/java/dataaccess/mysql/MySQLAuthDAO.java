package dataaccess.mysql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class MySQLAuthDAO implements AuthDAO {
    public void insertAuth(AuthData authData) throws DataAccessException {

    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }
}
