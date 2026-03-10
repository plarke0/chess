package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.mysql.MySQLAuthDAO;
import dataaccess.mysql.MySQLGameDAO;
import dataaccess.mysql.MySQLUserDAO;

public class ClearService {

    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;

    public ClearService() {
        this.authDAO = new MySQLAuthDAO();
        this.userDAO = new MySQLUserDAO();
        this.gameDAO = new MySQLGameDAO();
    }

    public void clear() throws DataAccessException {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }
}
