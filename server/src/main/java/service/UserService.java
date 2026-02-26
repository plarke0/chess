package service;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import dataaccess.memory.database.AuthDB;
import dataaccess.memory.database.UserDB;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.responses.LoginResponse;
import service.responses.RegisterResponse;
import service.responses.ResponseException;

import java.util.UUID;

public class UserService {

    private final MemoryUserDAO userDAO;
    private final MemoryAuthDAO authDAO;

    public UserService(UserDB userDB, AuthDB authDB) {
        this.userDAO = new MemoryUserDAO(userDB);
        this.authDAO = new MemoryAuthDAO(authDB);
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException, DataAccessException {
        if (registerRequest.username() == null ||
            registerRequest.password() == null ||
            registerRequest.email() == null
        ) {
            throw new ResponseException(400, "bad request");
        }
        UserData userData = new UserData(
                registerRequest.username(),
                registerRequest.password(),
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
            throw new ResponseException(400, "bad request");
        }

        if (!loginRequest.password().equals(userData.password())) {
            throw new ResponseException(401, "unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.insertAuth(authData);

        return new LoginResponse(userData.username(), authToken);
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException, DataAccessException {
        throw new UnsupportedOperationException("Feature not implemented.");
    }
}
