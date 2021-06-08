package common.utility;

import java.io.Serializable;
import java.util.Objects;
/**
 * Класс User.
 * Работа с пользователями.
 */
public class User implements Serializable {
    /** Поле логин */
    private String login;
    /** Поле пароль */
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Запрос логина пользователя.
     * @return login - логин пользователя.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Запрос пароля пользователя.
     * @return password - пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof User) {
            User userObj = (User) o;
            return login.equals(userObj.getLogin()) && password.equals(userObj.getPassword());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return login + ":" + password;
    }
}
