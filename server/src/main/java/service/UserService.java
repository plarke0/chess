package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.mysql.MySQLAuthDAO;
import dataaccess.mysql.MySQLUserDAO;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.requests.RegisterRequest;
import service.responses.LoginResponse;
import service.responses.RegisterResponse;
import service.responses.ResponseException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService() {
        this.userDAO = new MySQLUserDAO();
        this.authDAO = new MySQLAuthDAO();
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException, DataAccessException {
        if (registerRequest.username() == null ||
            registerRequest.password() == null ||
            registerRequest.email() == null
        ) {
            throw new ResponseException(400, "bad request");
        }
        String encryptedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        UserData userData = new UserData(
                registerRequest.username(),
                encryptedPassword,
                registerRequest.email()
        );

        UserData existingUser = userDAO.getUser(userData.username());
        if (existingUser != null) {
            throw new ResponseException(403, "already taken");
        }

        userDAO.insertUser(userData);

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.insertAuth(authData);

        return new RegisterResponse(userData.username(), authToken);
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException, DataAccessException {
        if (loginRequest.username() == null ||
            loginRequest.password() == null
        ) {
            throw new ResponseException(400, "bad request");
        }

        UserData userData = userDAO.getUser(loginRequest.username());
        if (userData == null) {
            throw new ResponseException(401, "unauthorized");
        }

        if (!BCrypt.checkpw(loginRequest.password(), userData.password())) {
            throw new ResponseException(401, "unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.insertAuth(authData);

        return new LoginResponse(userData.username(), authToken);
    }

    public void logout(String authToken) throws ResponseException, DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new ResponseException(401, "unauthorized");
        }

        authDAO.deleteAuth(authData);
    }
}
