package service;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import dataaccess.memory.database.AuthDB;
import dataaccess.memory.database.GameDB;
import dataaccess.memory.database.UserDB;

public class ClearService {

    private final MemoryAuthDAO authDAO;
    private final MemoryUserDAO userDAO;
    private final MemoryGameDAO gameDAO;

    public ClearService(AuthDB authDB, UserDB userDB, GameDB gameDB) {
        this.authDAO = new MemoryAuthDAO(authDB);
        this.userDAO = new MemoryUserDAO(userDB);
        this.gameDAO = new MemoryGameDAO(gameDB);
    }

    public void clear() throws DataAccessException {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }
}
