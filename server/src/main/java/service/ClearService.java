package service;

import dataaccess.memory.database.AuthDB;
import dataaccess.memory.database.GameDB;
import dataaccess.memory.database.UserDB;

public class ClearService {

    private final AuthDB authDB;
    private final UserDB userDB;
    private final GameDB gameDB;

    public ClearService(AuthDB authDB, UserDB userDB, GameDB gameDB) {
        this.authDB = authDB;
        this.userDB = userDB;
        this.gameDB = gameDB;
    }

    public void clear() {
        throw new UnsupportedOperationException("Feature not implemented.");
    }
}
