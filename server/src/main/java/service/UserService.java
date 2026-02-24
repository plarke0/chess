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

    public LoginResponse login(LoginRequest loginRequest) {
        throw new UnsupportedOperationException("Feature not implemented.");
    }

    public void logout(LogoutRequest logoutRequest) {
        throw new UnsupportedOperationException("Feature not implemented.");
    }
}
