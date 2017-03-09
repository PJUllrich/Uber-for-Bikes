package controller;

import db.DBController;
import db.User;

public class AccountController {
    private static AccountController instance = new AccountController();

    private AccountController() {
        // Empty constructor
    }

    public static AccountController getInstance() {
        return instance;
    }

    public Boolean createUser(String username, String password) {
        return DBController.getInstance().insertUser(new User(username, password));
    }

    public Boolean validateLogin(String name, String password) {
        User user = DBController.getInstance().getUserByName(name);
        return user.checkPassword(password);
    }
}
