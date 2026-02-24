package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.memory.database.AuthDB;
import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {

    private final AuthDB authDB;

    public MemoryAuthDAO(AuthDB authDB) {
        this.authDB = authDB;
    }

    public void insertAuth(AuthData authData) throws DataAccessException {

    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {

    }

    public void clear() throws DataAccessException {
        this.authDB.authDBArray = new ArrayList<>();
    }
}
