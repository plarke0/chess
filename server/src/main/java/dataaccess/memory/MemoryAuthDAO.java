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
        authDB.authDBArray.add(authData);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData auth : authDB.authDBArray) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        authDB.authDBArray.removeIf(auth -> auth.equals(authData));
    }

    public void clear() throws DataAccessException {
        this.authDB.authDBArray = new ArrayList<>();
    }
}
